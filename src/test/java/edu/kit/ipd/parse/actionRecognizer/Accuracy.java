package edu.kit.ipd.parse.actionRecognizer;

import java.text.DecimalFormat;

public class Accuracy {

    private double roleAccuracy;
    private double arcPrecision;
    private double arcRecall;

    public Accuracy(double roleAccuracy, double arcPrecision, double arcRecall) {
        this.roleAccuracy = roleAccuracy;
        this.arcPrecision = arcPrecision;
        this.arcRecall = arcRecall;
    }

    public double getRoleAccuracy() {
        return roleAccuracy;
    }

    public void setRoleAccuracy(double roleAccuracy) {
        this.roleAccuracy = roleAccuracy;
    }

    public double getArcPrecision() {
        return arcPrecision;
    }

    public void setArcPrecision(double arcPrecision) {
        this.arcPrecision = arcPrecision;
    }

    public double getArcRecall() {
        return arcRecall;
    }

    public void setArcRecall(double arcRecall) {
        this.arcRecall = arcRecall;
    }

    public String toString() {
        return new DecimalFormat("#0.0000").format(this.roleAccuracy) + "            "
                + new DecimalFormat("#0.0000").format(this.arcPrecision) + "          "
                + new DecimalFormat("#0.0000").format(this.arcRecall);
    }

}
