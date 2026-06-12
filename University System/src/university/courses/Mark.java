package university.courses;
import java.io.Serializable;

public class Mark implements Serializable{
    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;
    public Mark(double firstAttestation, double secondAttestation,double finalExam){
        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }
    public double calculateTotal() {
        return firstAttestation * 0.30 + secondAttestation * 0.30 + finalExam * 0.40;
    }
    public boolean isPassed() {
        return calculateTotal() >= 50.0;
    }
    public double getDigitGrade() {
        double total = calculateTotal();
        if (total >= 95) return 4.0;
        if (total >= 90) return 3.67;
        if (total >= 85) return 3.33;
        if (total >= 80) return 3.0;
        if (total >= 75) return 2.67;
        if (total >= 70) return 2.33;
        if (total >= 65) return 2.0;
        if (total >= 60) return 1.67;
        if (total >= 55) return 1.33;
        if (total >= 50) return 1.0;
        return 0;
    }
    public String getLetterGrade() {
        double total = calculateTotal();
        if (total >= 95) return "A";
        if (total >= 90) return "A-";
        if (total >= 85) return "B+";
        if (total >= 80) return "B";
        if (total >= 75) return "B-";
        if (total >= 70) return "C+";
        if (total >= 65) return "C";
        if (total >= 60) return "C-";
        if (total >= 55) return "D+";
        if (total >= 50) return "D";
        return "F";
    }

    public double getFirstAttestation() { 
    	return firstAttestation; }
    public void setFirstAttestation(double v) { 
    	this.firstAttestation = v; }
    public double getSecondAttestation() { 
    	return secondAttestation; }
    public void setSecondAttestation(double v) { 
    	this.secondAttestation = v; }
    public double getFinalExam() { 
    	return finalExam; }
    public void setFinalExam(double v) { 
    	this.finalExam = v; }

    @Override
    public String toString() {
        return "Mark{first=" + firstAttestation + ", second=" + secondAttestation + ", final=" + finalExam + ", total=" + calculateTotal() + ", grade=" + getLetterGrade() + ", passed=" + isPassed() + "}";
    }
}
