package project1;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientForm {
    private MainApp mainApp;
    private Scene scene;
    private ArrayList<PatientRecord> patientRecordList;
    private int selectedIndex = -1;
    private boolean isUpdating;
    private int selectedPatientId = -1;

    private static int sceneWidth = 600;
    private static int sceneHeight = 600;

    private ListView<String> patientListView;
    private ObservableList<String> listViewData;

    private TextField nameField;
    private TextField ageField;
    private TextArea medicalHistoryArea;
    private TextField contactField;
    private Button saveButton;
    private Button cancelButton;
    private Button backButton;
    private Button exportButton;
    private Button deleteButton;

    public PatientForm(MainApp mainApp) {
        this.mainApp = mainApp;
        this.patientRecordList = new ArrayList<>();
    }

    public void show(Stage stage) {
        initialize();
        stage.setScene(scene);
        stage.setTitle("Healthcare App - Manage Patient Records");
        stage.show();
    }

    public void initialize() {

        // set up layout
        GridPane grid = new GridPane();

        nameField = new TextField();
        ageField = new TextField();
        medicalHistoryArea = new TextArea();
        contactField = new TextField();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        backButton = new Button("Back");
        exportButton = new Button("Export");
        exportButton.setDisable(true);
        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        listViewData = FXCollections.observableArrayList();
        patientListView = new ListView<String>(listViewData);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(ageField, 1, 1);
        grid.add(new Label("Medical History:"), 0, 2);
        grid.add(medicalHistoryArea, 1, 2);
        grid.add(new Label("Contact Info:"), 0, 3);
        grid.add(contactField, 1, 3);

        HBox topBox = new HBox(10);
        HBox.setHgrow(patientListView, Priority.ALWAYS);
        topBox.setPadding(new Insets(10));
        topBox.getChildren().add(patientListView);
        topBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.getChildren().addAll(saveButton, cancelButton, deleteButton, exportButton, backButton);
        bottomBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10);
        VBox.setVgrow(grid, javafx.scene.layout.Priority.ALWAYS);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(topBox, grid, bottomBox);

        // add event listeners
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        backButton.setOnAction(e -> mainApp.showDashboard());
        exportButton.setOnAction(e -> handleExport());
        deleteButton.setOnAction(e -> handleDelete());
        patientListView.setOnMouseClicked(event -> handleListViewClick());

        scene = new Scene(root, sceneWidth, sceneHeight);

        // load patient data
        getPatientsData();
    }

    // this method will update the patient record list directly. so we only need to
    // refresh the list view afterwards.
    private void getPatientsData() {
        Runnable onComplete = () -> resetUI();
        Runnable onErr = () -> showAlert("Error", "Could not get patient data");

        this.mainApp.exec.execute(new PatientDataManager.GetAllPatientsTask(patientRecordList, onComplete, onErr));
    }

    private void clearInputs() {
        nameField.clear();
        ageField.clear();
        medicalHistoryArea.clear();
        contactField.clear();
    }

    private void refreshList() {
        listViewData.clear();
        for (PatientRecord rec : patientRecordList) {
            listViewData.add(rec.toString());
        }
    }

    private boolean validateInput() {

        if (nameField.getText().isEmpty() || ageField.getText().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private void resetUI() {
        clearInputs();
        isUpdating = false;
        exportButton.setDisable(patientRecordList.size() < 1);
        deleteButton.setDisable(true);
        selectedIndex = -1;
        selectedPatientId = -1;
        refreshList();
    }

    private void handleSave() {
        if (!validateInput()) {
            showAlert("Error", "Invalid input, name and age (number) are required");
            return;
        }

        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String medicalHistory = medicalHistoryArea.getText();
        String contact = contactField.getText();

        if (medicalHistory.isEmpty()) {
            medicalHistory = "N/A";
        }

        if (contact.isEmpty()) {
            contact = "N/A";
        }

        PatientRecord record;

        if (isUpdating == true) {
            record = new PatientRecord(selectedPatientId, name, age, medicalHistory, contact);
            updateRecord(record);
        } else {
            record = new PatientRecord(name, age, medicalHistory, contact);
            addRecord(record);
        }
        getPatientsData();
        resetUI();
    }

    private void handleDelete() {
        if (selectedIndex >= 0 && selectedIndex <= patientRecordList.size() && selectedPatientId >= 0) {
            deleteRecord(selectedPatientId);
            getPatientsData();
            resetUI();
        }
    }

    private void handleCancel() {
        resetUI();
    }

    private void handleListViewClick() {
        selectedIndex = patientListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            PatientRecord rec = patientRecordList.get(selectedIndex);
            nameField.setText(rec.getName());
            ageField.setText(String.valueOf(rec.getAge()));
            medicalHistoryArea.setText(rec.getMedicalHistory());
            contactField.setText(rec.getContactInformation());
            isUpdating = true;
            deleteButton.setDisable(false);
            selectedPatientId = rec.getID();
        }
    }

    private void handleExport() {
        Runnable onComplete = () -> showInfo("Success", "Exported patient data");
        Runnable onErr = () -> showAlert("Error", "Failed to export patient data ");
        this.mainApp.exec.execute(new PatientDataManager.ExportRecordsTask(patientRecordList, onComplete, onErr));
    }

    private void updateRecord(PatientRecord rec) {
        Runnable onComplete = () -> getPatientsData();// refresh patient data from source.
        Runnable onErr = () -> showAlert("Error", "Failed to update patient record");
        this.mainApp.exec.execute(new PatientDataManager.UpdatePatientTask(rec, onComplete, onErr));
    }

    private void addRecord(PatientRecord rec) {
        Runnable onComplete = () -> getPatientsData(); // refresh patient data from source.
        Runnable onErr = () -> showAlert("Error", "Failed to create patient record");

        this.mainApp.exec.execute(new PatientDataManager.CreatePatientTask(rec, onComplete, onErr));
    }

    private void deleteRecord(int patientId) {
        Runnable onComplete = () -> getPatientsData(); // refresh patient data from source.
        Runnable onErr = () -> showAlert("Error", "Failed to delete patient record");

        this.mainApp.exec.execute(new PatientDataManager.DeletePatientTask(patientId, onComplete, onErr));
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
