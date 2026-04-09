package project1;

// PatientRecord.java

import java.io.Serializable;

public class PatientRecord implements Serializable {
    private String name;
    private int age;
    private String medicalHistory;
    private String contactInformation;
    private int ID;

    public PatientRecord(int ID, String name, int age, String medicalHistory, String contactInformation) {
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.contactInformation = contactInformation;
        this.ID = ID;
    }
    
    public PatientRecord(String name, int age, String medicalHistory, String contactInformation) {
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.contactInformation = contactInformation;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public int getID() {
        return ID;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
    
    @Override
    public String toString() {
    	return "Name: " + name + ", Age: " + age + ", " + medicalHistory + ", " + contactInformation;
    }
}
