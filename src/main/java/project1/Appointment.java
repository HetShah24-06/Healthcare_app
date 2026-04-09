package project1;

import java.io.Serializable;

// Appointment class is serializable so it can be written directly to objects.
public class Appointment implements Serializable {
	String patientName;
	String AppointmentDateTime;
	String visitReason;
	int id;
	int patientId;
	
	public String getPatientName() {
		return patientName;
	}


	public String getAppointmentDateTime() {
		return AppointmentDateTime;
	}

	public String getVisitReason() {
		return visitReason;
	}


	public int getId() {
		return id;
	}


	public int getPatientId() {
		return patientId;
	}
	
	public Appointment(String patientName, String appointmentDateTime, String visitReason, int patientId)  {
		this.patientName = patientName;
		this.AppointmentDateTime = appointmentDateTime;
		this.visitReason = visitReason;
		this.patientId = patientId;
	}
	
	public Appointment(String patientName, String appointmentDateTime, String visitReason, int patientId, int id)  {
		this.patientName = patientName;
		this.AppointmentDateTime = appointmentDateTime;
		this.visitReason = visitReason;
		this.patientId = patientId;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.patientName + " | " + this.AppointmentDateTime.split(" ")[0] + " | " + this.visitReason;
	}
}