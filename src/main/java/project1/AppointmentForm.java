package project1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppointmentForm {

	private MainApp mainApp;
	private Scene scene;

	// scene parameters
	private static int sceneWidth = 500;
	private static int sceneHeight = 600;

	// UI state
	private int selectedIndex = -1;
	private int selectedPatientIndex = -1;
	boolean isUpdating = false;

	// Data sources
	private ArrayList<Appointment> apptList;
	private ObservableList<String> listViewAppData;
	private ArrayList<PatientRecord> patientList;
	private ObservableList<String> patientNameList;

	// UI Controls
	private ListView<String> appointmentListView;
	private TextField visitReasonInput;
	private DatePicker dateInput;
	private Button addBtn, cancelBtn, deleteBtn, backBtn, exportBtn;
	private Alert alert;
	private ComboBox<String> patientComboBox;

	public AppointmentForm(MainApp mainApp) {
		this.mainApp = mainApp;
		this.apptList = new ArrayList<>();
	}

	public void show(Stage stage) {
		createAppointmentScene();
		stage.setScene(scene);
		stage.setTitle("Healthcare App - Manage Appointments");
		stage.show();
	}

	// set-up function
	private void createAppointmentScene() {
		initializeUI();
		registerEventListeners();
		loadPatientsData();
		loadAppointmentData();
		refreshListView();
	}

	private void initializeUI() {
		// create control elements

		alert = new Alert(AlertType.ERROR);

		apptList = new ArrayList<Appointment>();
		// ListView requires this ObservableList object.

		listViewAppData = FXCollections.observableArrayList();
		appointmentListView = new ListView<String>(listViewAppData);

		// Patients Combo Box
		patientList = new ArrayList<PatientRecord>();
		patientNameList = FXCollections.observableArrayList();
		patientComboBox = new ComboBox<String>(patientNameList);
		patientComboBox.setPromptText("Select a patient");

		dateInput = new DatePicker();
		dateInput.setValue(LocalDate.now());
		visitReasonInput = new TextField();

		addBtn = new Button("Add Appointment");
		addBtn.setDisable(true);
		addBtn.setPadding(new Insets(10));

		cancelBtn = new Button("Cancel");
		cancelBtn.setPadding(new Insets(10));
		cancelBtn.setDisable(true);

		deleteBtn = new Button("Delete Appointment");
		deleteBtn.setPadding(new Insets(10));
		deleteBtn.setDisable(true);

		backBtn = new Button("Back to Dashboard");
		backBtn.setPadding(new Insets(5));

		exportBtn = new Button("Export data");
		exportBtn.setPadding(new Insets(5));
		deleteBtn.setDisable(true);

		// create layout
		GridPane pane = new GridPane();

		pane.setAlignment(Pos.CENTER);
		pane.setVgap(10);
		pane.setHgap(5);

		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		col1.setPercentWidth(33.33);
		col2.setPercentWidth(33.33);
		col3.setPercentWidth(33.33);
		pane.getColumnConstraints().addAll(col1, col2, col3);

		pane.add(new Label("List of Appointments \n(click an appointment to update it)"), 0, 0, 3, 1);
		pane.add(appointmentListView, 0, 1, 3, 1);

		pane.add(new Label("Add New Appointment"), 0, 2, 3, 1);

		pane.add(new Label("Patient"), 0, 3);
		pane.add(patientComboBox, 1, 3, 2, 1);

		pane.add(new Label("Appointment Date: "), 0, 4);
		pane.add(dateInput, 1, 4, 2, 1);

		pane.add(new Label("Visit Reason: "), 0, 5);
		pane.add(visitReasonInput, 1, 5, 2, 1);

		pane.add(addBtn, 0, 6);
		pane.add(cancelBtn, 2, 6);
		pane.add(deleteBtn, 1, 6);

		HBox topBox = new HBox(10);
		topBox.setPadding(new Insets(10));
		topBox.getChildren().addAll(backBtn, exportBtn);
		topBox.setAlignment(Pos.CENTER_LEFT);

		VBox root = new VBox(10);
		VBox.setVgrow(pane, javafx.scene.layout.Priority.ALWAYS);
		root.setPadding(new Insets(10));
		root.getChildren().addAll(topBox, pane);

		scene = new Scene(root, sceneWidth, sceneHeight);
	}

	private void registerEventListeners() {
		cancelBtn.setOnAction(event -> handleCancelButtonClick());
		addBtn.setOnAction(event -> handleAddButtonClick());
		deleteBtn.setOnAction(event -> handleDeleteButtonClick());
		appointmentListView.setOnMouseClicked(event -> handleListViewClick());
		backBtn.setOnAction(event -> mainApp.showDashboard());
		exportBtn.setOnAction(event -> handleExportButtonClick());
		patientComboBox.setOnAction(event -> handlePatientSelect());
	}

	private synchronized void loadAppointmentData() {
		Runnable onComplete = () -> resetUI();
		Runnable onErr = () -> showAlert("Error", "Failed to retrieve appointment records");

		this.mainApp.exec.execute(new AppointmentDataManager.GetAllAppointmentsTask(apptList, onComplete, onErr));
	}

	private synchronized void loadPatientsData() {
		Runnable onComplete = () -> refreshPatientList();
		Runnable onErr = () -> showAlert("Error", "Failed to retrieve patient records");

		this.mainApp.exec.execute(new PatientDataManager.GetAllPatientsTask(patientList, onComplete, onErr));
	}

	private void refreshPatientList() {
		patientNameList.clear();
		for (PatientRecord p : patientList) {
			patientNameList.add(p.getName());
		}
	}

	private void clearInputFields() {
		patientComboBox.getSelectionModel().clearSelection();
		dateInput.setValue(LocalDate.now());
		visitReasonInput.clear();
	}

	private void handleExportButtonClick() {
		Runnable onComplete = () -> showInfo("Success", "Exported appointment data");
		Runnable onErr = () -> showAlert("Error", "Failed to export appointment data ");
		this.mainApp.exec.execute(new AppointmentDataManager.ExportRecordsTask(apptList, onComplete, onErr));
	}

	private void handleCancelButtonClick() {
		resetUI();
	}

	private void handleAddButtonClick() {
		if (!validateInputFields()) {
			showAlert("Validation Error", "All input fields must be provided");
			return;
		}

		PatientRecord selectedPatient = patientList.get(selectedPatientIndex);

		Appointment appt;

		if (isUpdating) {
			Appointment selectedAppointment = apptList.get(selectedIndex);
			appt = new Appointment(
					selectedPatient.getName(), // patient name
					dateInput.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), // appointment date
					visitReasonInput.getText(), // reason
					selectedPatient.getID(),
					selectedAppointment.getId());
			updateAppointment(appt);
		} else {
			appt = new Appointment(
					selectedPatient.getName(), // patient name
					dateInput.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), // appointment date
					visitReasonInput.getText(), // reason
					selectedPatient.getID());
			createAppointment(appt);
		}

		resetUI();

	}

	private void handlePatientSelect() {
		selectedPatientIndex = patientComboBox.getSelectionModel().getSelectedIndex();
		addBtn.setDisable(false);
	}

	private void handleDeleteButtonClick() {
		if (selectedIndex >= 0 && selectedIndex <= apptList.size()) {
			deleteAppointment();
			resetUI();
		}
	}

	private void handleListViewClick() {

		selectedIndex = appointmentListView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			Appointment selectedAppointment = apptList.get(selectedIndex);

			// enable necessary controls
			cancelBtn.setDisable(false);
			deleteBtn.setDisable(false);

			// update combobox to have correct patient name
			String patientName = selectedAppointment.patientName;
			int patientIndex = patientNameList.indexOf(patientName);
			patientComboBox.getSelectionModel().select(patientIndex);

			dateInput.setValue(LocalDate.parse(selectedAppointment.AppointmentDateTime,
					DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			visitReasonInput.setText(selectedAppointment.visitReason);

			this.isUpdating = true;

			// update the button label
			addBtn.setText("Update Appointment");
		}
	}

	private void deleteAppointment() {
		Runnable onComplete = () -> loadAppointmentData(); // refresh appointment data from source.
		Runnable onErr = () -> showAlert("Error", "Failed to delete appointment record");
		int selectedAppointmentID = apptList.get(selectedIndex).getId();
		this.mainApp.exec
				.execute(new AppointmentDataManager.DeleteAppointmentTask(selectedAppointmentID, onComplete, onErr));
	}

	private void updateAppointment(Appointment newAppointment) {
		Runnable onComplete = () -> loadAppointmentData(); // refresh appointment data from source.
		Runnable onErr = () -> showAlert("Error", "Failed to update appointment record");

		this.mainApp.exec.execute(new AppointmentDataManager.UpdateAppointmentTask(newAppointment, onComplete, onErr));
	}

	private void createAppointment(Appointment newAppointment) {
		Runnable onComplete = () -> loadAppointmentData(); // refresh appointment data from source.
		Runnable onErr = () -> showAlert("Error", "Failed to create appointment record");

		this.mainApp.exec.execute(new AppointmentDataManager.CreateAppointmentTask(newAppointment, onComplete, onErr));
	}

	// helper method to reset and cleanup all UI state after most actions
	private void resetUI() {
		clearInputFields();
		addBtn.setText("Add Appointment");
		isUpdating = false;
		selectedPatientIndex = -1;
		selectedIndex = -1;
		addBtn.setDisable(true);
		;
		cancelBtn.setDisable(true);
		deleteBtn.setDisable(true);
		exportBtn.setDisable(apptList.size() < 1);
		appointmentListView.getSelectionModel().clearSelection();
		refreshListView();

	}

	private void refreshListView() {
		listViewAppData.clear();
		for (Appointment appt : apptList) {
			listViewAppData.add(appt.toString());
		}
	}

	private boolean validateInputFields() {
		return !(visitReasonInput.getText().isEmpty());
	}

	private void showInfo(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert(String title, String message) {

		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
