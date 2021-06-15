package com.sushmita.jms.basic.message_filtering.model;

import java.io.Serializable;

public class Claim implements Serializable {
    private int hospitalId;
    private String doctorName;
    private String doctorType;
    private String insuranceProvider;
    private double claimAmount;

    public Claim(){};

    public Claim(int hospitalId, String doctorName, String doctorType, String insuranceProvider, double claimAmount) {
        this.hospitalId = hospitalId;
        this.doctorName = doctorName;
        this.doctorType = doctorType;
        this.insuranceProvider = insuranceProvider;
        this.claimAmount = claimAmount;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "hospitalId=" + hospitalId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorType='" + doctorType + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", claimAmount=" + claimAmount +
                '}';
    }
}
