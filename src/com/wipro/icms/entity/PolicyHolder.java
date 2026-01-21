package com.wipro.icms.entity;

public class PolicyHolder {

    private String holderId;
    private String name;
    private String policyType;
    private double coverageAmount;

    public PolicyHolder(String holderId, String name, String policyType, double coverageAmount) {
        this.holderId = holderId;
        this.name = name;
        this.policyType = policyType;
        this.coverageAmount = coverageAmount;
    }

    public String getHolderId() {
        return holderId;
    }

    public String getName() {
        return name;
    }

    public String getPolicyType() {
        return policyType;
    }

    public double getCoverageAmount() {
        return coverageAmount;
    }
}
