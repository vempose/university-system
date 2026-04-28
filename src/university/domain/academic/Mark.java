package university.domain.academic;

import java.io.Serial;
import java.io.Serializable;

public class Mark implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final double MAX_FIRST_ATTESTATION = 30.0;
    public static final double MAX_SECOND_ATTESTATION = 30.0;
    public static final double MAX_FINAL_EXAM = 40.0;
    public static final double PASSING_THRESHOLD = 50.0;

    private double firstAttestation;
    private double secondAttestation;
    private double finalExam;

    public Mark(
        double firstAttestation,
        double secondAttestation,
        double finalExam
    ) {
        validateRange(
            "firstAttestation",
            firstAttestation,
            0,
            MAX_FIRST_ATTESTATION
        );
        validateRange(
            "secondAttestation",
            secondAttestation,
            0,
            MAX_SECOND_ATTESTATION
        );
        validateRange("finalExam", finalExam, 0, MAX_FINAL_EXAM);

        this.firstAttestation = firstAttestation;
        this.secondAttestation = secondAttestation;
        this.finalExam = finalExam;
    }

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public boolean isPassed() {
        return getTotal() >= PASSING_THRESHOLD;
    }

    public double getFirstAttestation() {
        return firstAttestation;
    }

    public double getSecondAttestation() {
        return secondAttestation;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public void setFirstAttestation(double firstAttestation) {
        validateRange(
            "firstAttestation",
            firstAttestation,
            0,
            MAX_FIRST_ATTESTATION
        );
        this.firstAttestation = firstAttestation;
    }

    public void setSecondAttestation(double secondAttestation) {
        validateRange(
            "secondAttestation",
            secondAttestation,
            0,
            MAX_SECOND_ATTESTATION
        );
        this.secondAttestation = secondAttestation;
    }

    public void setFinalExam(double finalExam) {
        validateRange("finalExam", finalExam, 0, MAX_FINAL_EXAM);
        this.finalExam = finalExam;
    }

    @Override
    public String toString() {
        return "Mark{first=%.1f, second=%.1f, final=%.1f, total=%.1f, passed=%b}".formatted(
            firstAttestation,
            secondAttestation,
            finalExam,
            getTotal(),
            isPassed()
        );
    }

    private static void validateRange(
        String field,
        double value,
        double min,
        double max
    ) {
        if (value < min || value > max) throw new IllegalArgumentException(
            String.format(
                "'%s' must be between %.1f and %.1f, but was %.1f.",
                field,
                min,
                max,
                value
            )
        );
    }
}
