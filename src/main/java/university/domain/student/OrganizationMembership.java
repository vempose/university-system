package university.domain.student;

import university.domain.user.Student;
import university.enums.OrganizationRole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class OrganizationMembership implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final StudentOrganization organization;
    private final LocalDate joinDate;
    private OrganizationRole role;

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

    public void setRole(OrganizationRole role) {
        this.role = role;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    @Override
    public String toString() {
        return "OrganizationMembership{student=%s, organization='%s', role=%s, joinDate=%s}".formatted(
                student,
                organization.getName(),
                role,
                joinDate
        );
    }
}
