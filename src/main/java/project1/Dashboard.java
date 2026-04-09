package project1;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Dashboard {
	private MainApp mainApp;
	private Stage stage;
	private Scene scene;
	private ObservableList<String> appointmentListData;
	private ArrayList<Appointment> appointmentList;
	private ObservableList<String> patientListData;
	private ArrayList<PatientRecord> patientList;
	private Alert alert;		


	public Dashboard(MainApp mainApp) {
		this.mainApp = mainApp;
		this.appointmentList = new ArrayList<>();
		this.appointmentListData = FXCollections.observableArrayList();
		this.patientList = new ArrayList<>();
		this.patientListData = FXCollections.observableArrayList();
		alert = new Alert(AlertType.ERROR);
		
		// make use of static methods from the Appointment/Patient data manager classes
		// to load the data into the dashboard view. 
		loadAppointmentData();
		loadPatientData();
		createDashboardScene();
	}
	
	public void loadAppointmentData() {
		Runnable onComplete = () -> refreshAppointmentList();
    	Runnable onErr = () -> showAlert("Error", "Could not get appointment data");
    	
    	this.mainApp.exec.execute(new AppointmentDataManager.GetAllAppointmentsTask(appointmentList, onComplete, onErr));
	}
	
	public void refreshAppointmentList() {
		appointmentListData.clear();
		for (Appointment a : appointmentList) {
			appointmentListData.add(a.toString());
		}
	}
	
	public void loadPatientData() {
		
		Runnable onComplete = () -> refreshPatientList();
    	Runnable onErr = () -> showAlert("Error", "Could not get patient data");
    	
    	this.mainApp.exec.execute(new PatientDataManager.GetAllPatientsTask(patientList, onComplete, onErr));
	}
	
	public void refreshPatientList() {
		patientListData.clear();
		for (PatientRecord p : patientList) {
			patientListData.add(p.toString());
		}
	}


    public void createDashboardScene() {
    	
        ListView<String> appointmentList = new ListView<String>(appointmentListData);
        Button showAppointmentsButton = new Button("Manage Appointments");
        
        ListView<String> patientList = new ListView<String>(patientListData);
        Button showPatientsButton = new Button("Manage Patients");
        
        
        GridPane layout = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		
		col1.setPercentWidth(50);
		col2.setPercentWidth(50);
		
		layout.getColumnConstraints().addAll(col1, col2);
		

		layout.add(new Label("List of Appointments"), 0, 0);
		layout.add(appointmentList, 0, 1);
		
		layout.add(new Label("List of Patients"), 1, 0);
		layout.add(patientList, 1, 1);
		
		layout.add(showAppointmentsButton, 0, 2);
		layout.add(showPatientsButton, 1, 2);
		
        layout.setPadding(new Insets(15)); 
        layout.setVgap(10); 
        layout.setHgap(10); 
        
//      calling the scene-switching methods from the mainApp parent class. 
        showAppointmentsButton.setOnAction(e -> mainApp.showAppointments());
        showPatientsButton.setOnAction(e -> mainApp.showPatients());


        scene = new Scene(layout, 900, 600);        
        
    }
    
    private void showAlert(String title, String message) {
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show (Stage stage) {
    	this.stage = stage;
    	stage.setScene(scene);
    	stage.setTitle("Healthcare App - Dashboard");
    	stage.show();
    }
    
    public void close() {
    	if(stage != null) {
    		stage.close();
    	}
    }
}
