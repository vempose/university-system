package university.domain.student;

import java.time.LocalDate;
import java.util.Objects;
import university.domain.user.Student;
import university.enums.OrganizationRole;

public class OrganizationMembership {

    private final Student student;
    private final StudentOrganization organization;
    private OrganizationRole role;
    private final LocalDate joinDate;

    public OrganizationMembership(
        Student student,
        StudentOrganization organization,
        OrganizationRole role
    ) {
        this.student = student;
        this.organization = organization;
        this.role = role;
        this.joinDate = LocalDate.now();
    }

    public Student getStudent() {
        return student;
    }

    public StudentOrganization getOrganization() {
        return organization;
    }

    public OrganizationRole getRole() {
        return role;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setRole(OrganizationRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "OrganizationMembership{student=%s, organization='%s', role=%s, joinDate=%s}".formatted(
                student, organization.getName(), role, joinDate
        );
    }
}
