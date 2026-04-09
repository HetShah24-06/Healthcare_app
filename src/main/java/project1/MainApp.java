package project1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
	private Stage stage;
	private LoginScreen loginScreen;
	private Dashboard dashboard;
	private AppointmentForm appointmentForm;
	private PatientForm patientForm;
	public ExecutorService exec;

	@Override
	public void start(Stage stage) {

		// Initialize database and create tables if they don't exist
		DBConnector.init();

		// create stage and instantiate scene/screen objects.
		// Every scene takes the entire app object as its only constructor argument,
		// so that it can call the scene-switching methods from the parent class.
		this.exec = Executors.newFixedThreadPool(5);
		this.stage = stage;
		this.loginScreen = new LoginScreen(this);
		this.dashboard = new Dashboard(this);
		this.appointmentForm = new AppointmentForm(this);
		this.patientForm = new PatientForm(this);

		showLoginScreen();

	}

	@Override
	public void stop() throws Exception {
		exec.shutdown();
		super.stop();
	}

	public void closeStage() {
		if (stage != null) {
			stage.close();
		}
	}

	// Each of the methods below will close the current stage, create a new one
	// and then display the correct scene to the new stage.

	public void showLoginScreen() {
		closeStage();
		stage = new Stage();
		loginScreen.show(stage);
	}

	public void showDashboard() {
		closeStage();
		stage = new Stage();
		dashboard.loadAppointmentData();
		dashboard.loadPatientData();
		dashboard.show(stage);
	}

	public void showAppointments() {
		closeStage();
		stage = new Stage();
		appointmentForm.show(stage);
	}

	public void showPatients() {
		closeStage();
		stage = new Stage();
		patientForm.show(stage);
	}

	public static void main(String[] args) {
		DBConnector.init();
		launch(args);
	}
}
