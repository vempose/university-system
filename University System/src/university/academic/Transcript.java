package university.academic;

import university.courses.Enrollment;
import university.users.Student;

import java.io.Serializable;

public class Transcript implements Serializable {
    public String generate(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript of ").append(student.getName()).append("\n");
        sb.append("ID: ").append(student.getId()).append("\n");
        sb.append("Degree: ").append(student.getDegreeType()).append("\n");
        sb.append("Year: ").append(student.getYearOfStudy()).append("\n");
        sb.append("GPA: ").append(student.getGpa()).append("\n");
        for (Enrollment e : student.getEnrollments()) {
            if (e.getMark() != null) {
                sb.append(e.getCourse().getTitle())
                  .append(" - ").append(e.getMark().getLetterGrade())
                  .append(" (").append(e.getCourse().getCredits()).append(" cr)\n");
            }
        }
        return sb.toString();
    }
    public double calculateTotalGpa(Student student) {
        double total = 0;
        int count = 0;
        for (Enrollment e : student.getEnrollments()) {
            if (e.getMark() != null) {
                total += e.getMark().getDigitGrade();
                count++;
            }
        }
        return count > 0 ? total / count : 0.0;
    }

    @Override
    public String toString() {
        return "Transcript{}";
    }
}
