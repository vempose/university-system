package university.domain.student;

import university.domain.user.Student;
import university.domain.user.Teacher;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class TeacherRating implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Teacher teacher;
    private final int score;
    private final String comment;
    private final LocalDate createdDate;

    public TeacherRating(
            Student student,
            Teacher teacher,
            int score,
            String comment
    ) {
        if (score < 1 || score > 5) throw new IllegalArgumentException(
                "Score must be between 1 and 5, got: " + score
        );
        this.student = student;
        this.teacher = teacher;
        this.score = score;
        this.comment = comment;
        this.createdDate = LocalDate.now();
    }

    public Student getStudent() {
        return student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "TeacherRating{student=%s, teacher=%s, score=%d, comment='%s', createdDate=%s}".formatted(
                student,
                teacher,
                score,
                comment,
                createdDate
        );
    }
}
