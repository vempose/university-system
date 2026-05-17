package university.system;

import university.domain.academic.Major;
import university.domain.user.*;
import university.enums.DegreeType;
import university.enums.Language;
import university.enums.ManagerType;
import university.enums.TeacherPosition;

import java.util.UUID;

/// Creates users from login credentials and profile info.
///
/// Has methods for each user type — student, teacher, manager, etc.
/// Each one generates a random ID so you don't have to.
public final class UserFactory {

    /// Package-private constructor — only UniversitySystem creates this.
    UserFactory() {
    }

    /// Creates a new Student with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @param degree   degree type (bachelor, etc.)
    /// @param major    the student's major
    /// @return the new Student
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

    /// Creates a new GraduateStudent with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @param degree   degree type (master, phd, etc.)
    /// @param major    the student's major
    /// @return the new GraduateStudent
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

    /// Creates a new Teacher with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @param position teacher's position (lector, professor, etc.)
    /// @return the new Teacher
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

    /// Creates a new Manager with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @param type     manager type (OR, department, dean)
    /// @return the new Manager
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

    /// Creates a new Admin with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @return the new Admin
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

    /// Creates a new TechSupportSpecialist with a random ID.
    ///
    /// @param name     full name
    /// @param email    email address
    /// @param password login password
    /// @return the new TechSupportSpecialist
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
