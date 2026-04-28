package university.system;

import university.domain.academic.Major;
import university.domain.user.*;
import university.enums.DegreeType;
import university.enums.Language;
import university.enums.ManagerType;
import university.enums.TeacherPosition;

import java.util.UUID;

public final class UserFactory {

    UserFactory() {
    }

    public Student createStudent(
            String name,
            String email,
            String password,
            DegreeType degree,
            Major major
    ) {
        return new Student(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                degree,
                major
        );
    }

    public GraduateStudent createGraduateStudent(
            String name,
            String email,
            String password,
            DegreeType degree,
            Major major
    ) {
        return new GraduateStudent(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                degree,
                major
        );
    }

    public Teacher createTeacher(
            String name,
            String email,
            String password,
            TeacherPosition position
    ) {
        return new Teacher(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                0.0,
                position
        );
    }

    public Manager createManager(
            String name,
            String email,
            String password,
            ManagerType type
    ) {
        return new Manager(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                0.0,
                type
        );
    }

    public Admin createAdmin(String name, String email, String password) {
        return new Admin(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                0.0
        );
    }

    public TechSupportSpecialist createTechSupport(
            String name,
            String email,
            String password
    ) {
        return new TechSupportSpecialist(
                UUID.randomUUID().toString(),
                name,
                email,
                password,
                Language.EN,
                0.0
        );
    }
}
