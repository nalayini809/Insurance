package com.wipro.icms.entity;

public class Claim {

    private String claimId;
    private String holderId;
    private String incidentDate;
    private String description;
    private double claimAmount;
    private String status;

    public Claim(String claimId, String holderId, String incidentDate,
                 String description, double claimAmount, String status) {
        this.claimId = claimId;
        this.holderId = holderId;
        this.incidentDate = incidentDate;
        this.description = description;
        this.claimAmount = claimAmount;
        this.status = status;
    }

    public String getClaimId() {
        return claimId;
    }

    public String getHolderId() {
        return holderId;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getDescription() {
        return description;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
