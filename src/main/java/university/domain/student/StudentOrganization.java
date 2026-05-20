package university.domain.student;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// A student club or organization on campus.
///
/// Students can join and take on different roles.
public class StudentOrganization implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<OrganizationMembership> memberships = new ArrayList<>();
    private String description;

    /// Creates an organization with a name and description.
    public StudentOrganization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /// Adds a membership record to this org.
    public void addMembership(OrganizationMembership membership) {
        boolean alreadyMember = memberships
                .stream()
                .anyMatch(m -> m.getStudent().equals(membership.getStudent()));
        if (!alreadyMember) {
            memberships.add(membership);
        }
    }

    public List<OrganizationMembership> getMembers() {
        return List.copyOf(memberships);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /// Updates the organization description.
    public void setDescription(String description) {
        this.description = description;
    }

    /// Two orgs are equal if they have the same name.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentOrganization other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /// Returns a summary of the organization.
    @Override
    public String toString() {
        return "StudentOrganization{name='%s', description='%s', memberCount=%d}".formatted(
                name,
                description,
                memberships.size()
        );
    }
}
