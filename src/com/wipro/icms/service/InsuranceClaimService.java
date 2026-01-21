package com.wipro.icms.service;

import java.util.ArrayList;

import com.wipro.icms.entity.*;
import com.wipro.icms.util.*;

public class InsuranceClaimService {

    private ArrayList<PolicyHolder> holders;
    private ArrayList<Claim> claims;
    private ArrayList<ClaimUpdate> updates;

    public InsuranceClaimService(ArrayList<PolicyHolder> holders,
                                 ArrayList<Claim> claims,
                                 ArrayList<ClaimUpdate> updates) {
        this.holders = holders;
        this.claims = claims;
        this.updates = updates;
    }

    public void addPolicyHolder(PolicyHolder p) {
        holders.add(p);
    }

    public PolicyHolder findPolicyHolder(String holderId)
            throws PolicyHolderNotFoundException {

        for (PolicyHolder p : holders) {
            if (p.getHolderId().equals(holderId)) {
                return p;
            }
        }
        throw new PolicyHolderNotFoundException("Policy Holder not found");
    }

    public void fileClaim(Claim c)
            throws PolicyHolderNotFoundException, InvalidClaimOperationException {

        findPolicyHolder(c.getHolderId());

        if (c.getClaimAmount() <= 0) {
            throw new InvalidClaimOperationException("Claim amount must be positive");
        }

        if (c.getDescription() == null || c.getDescription().isEmpty()) {
            throw new InvalidClaimOperationException("Description cannot be empty");
        }

        claims.add(c);
    }

    public Claim findClaim(String claimId)
            throws ClaimNotFoundException {

        for (Claim c : claims) {
            if (c.getClaimId().equals(claimId)) {
                return c;
            }
        }
        throw new ClaimNotFoundException("Claim not found");
    }

    public void updateClaimStatus(String claimId, String newStatus)
            throws ClaimNotFoundException, InvalidClaimOperationException {

        Claim claim = findClaim(claimId);
        String current = claim.getStatus();

        if (current.equals("FILED") && newStatus.equals("UNDER REVIEW") ||
            current.equals("UNDER REVIEW") &&
                    (newStatus.equals("APPROVED") || newStatus.equals("REJECTED")) ||
            current.equals("APPROVED") && newStatus.equals("SETTLED")) {

            claim.setStatus(newStatus);
        } else {
            throw new InvalidClaimOperationException("Invalid status change");
        }
    }

    public void addClaimUpdate(ClaimUpdate update)
            throws ClaimNotFoundException, InvalidClaimOperationException {

        findClaim(update.getClaimId());

        if (update.getNotes() == null || update.getNotes().isEmpty()) {
            throw new InvalidClaimOperationException("Update notes cannot be empty");
        }

        updates.add(update);
    }

    public ArrayList<ClaimUpdate> getClaimHistory(String claimId)
            throws ClaimNotFoundException {

        findClaim(claimId);
        ArrayList<ClaimUpdate> history = new ArrayList<>();

        for (ClaimUpdate u : updates) {
            if (u.getClaimId().equals(claimId)) {
                history.add(u);
            }
        }
        return history;
    }

    public double computeSettlementAmount(String claimId)
            throws ClaimNotFoundException,
                   InvalidClaimOperationException,
                   PolicyHolderNotFoundException {


        Claim claim = findClaim(claimId);

        if (!claim.getStatus().equals("APPROVED")) {
            throw new InvalidClaimOperationException("Claim not approved");
        }

        PolicyHolder holder = findPolicyHolder(claim.getHolderId());
        return Math.min(claim.getClaimAmount(), holder.getCoverageAmount());
    }

    public String generateClaimSummary(String claimId) {

        try {
            Claim claim = findClaim(claimId);
            PolicyHolder holder = findPolicyHolder(claim.getHolderId());

            StringBuilder sb = new StringBuilder();
            sb.append("Policy Holder: ").append(holder.getName()).append("\n");
            sb.append("Policy Type: ").append(holder.getPolicyType()).append("\n");
            sb.append("Claim Amount: ").append(claim.getClaimAmount()).append("\n");
            sb.append("Coverage Limit: ").append(holder.getCoverageAmount()).append("\n");
            sb.append("Status: ").append(claim.getStatus()).append("\n");

            sb.append("\nUpdates:\n");
            for (ClaimUpdate u : getClaimHistory(claimId)) {
                sb.append(u.getDate()).append(" - ").append(u.getNotes()).append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
