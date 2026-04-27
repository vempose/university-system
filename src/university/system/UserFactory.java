package university.system;

import java.util.Objects;
import java.util.UUID;
import university.domain.user.Admin;
import university.domain.user.GraduateStudent;
import university.domain.user.Manager;
import university.domain.user.Student;
import university.domain.user.Teacher;
import university.domain.user.TechSupportSpecialist;
import university.domain.user.User;
import university.enums.DegreeType;
import university.enums.Language;
import university.enums.ManagerType;
import university.enums.TeacherPosition;

public final class UserFactory {

    UserFactory() {}

    public User createUser(String role) {
        Objects.requireNonNull(role, "role must not be null");
        var id = UUID.randomUUID().toString();
        return switch (role.toUpperCase().trim()) {
            case "STUDENT" -> new Student(
                id,
                "",
                "",
                "",
                Language.EN,
                DegreeType.BACHELOR,
                null
            );
            case "GRADUATE_STUDENT" -> new GraduateStudent(
                id,
                "",
                "",
                "",
                Language.EN,
                DegreeType.MASTER,
                null
            );
            case "TEACHER" -> new Teacher(
                id,
                "",
                "",
                "",
                Language.EN,
                0.0,
                TeacherPosition.LECTOR
            );
            case "MANAGER" -> new Manager(
                id,
                "",
                "",
                "",
                Language.EN,
                0.0,
                ManagerType.TUTOR
            );
            case "ADMIN" -> new Admin(id, "", "", "", Language.EN, 0.0);
            case "TECH_SUPPORT" -> new TechSupportSpecialist(
                id,
                "",
                "",
                "",
                Language.EN,
                0.0
            );
            default -> throw new IllegalArgumentException(
                "Unknown role: \"" +
                    role +
                    "\". Expected one of: " +
                    "STUDENT, GRADUATE_STUDENT, TEACHER, MANAGER, ADMIN, TECH_SUPPORT"
            );
        };
    }
}
