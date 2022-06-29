package data;

import java.io.Serializable;


public class Results implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private double totalPayments;
    private double totalCredits;
    private double totalBalance;

    public Results(double totalPayments, double totalCredits, double totalBalance) {
        this.totalPayments = totalPayments;
        this.totalCredits = totalCredits;
        this.totalBalance = totalBalance;
    }

    public Results() {

    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    public double getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(double totalCredits) {
        this.totalCredits = totalCredits;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }
}
