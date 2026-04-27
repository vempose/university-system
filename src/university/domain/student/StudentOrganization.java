package university.domain.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentOrganization {

    private final String name;
    private String description;
    private final List<OrganizationMembership> memberships = new ArrayList<>();

    public StudentOrganization(String name, String description) {
        this.name = Objects.requireNonNull(
            name,
            "Organization name must not be null"
        );
        this.description = description;
    }

    public void addMembership(OrganizationMembership membership) {
        memberships.add(
            Objects.requireNonNull(membership, "Membership must not be null")
        );
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

    public void setDescription(String description) {
        this.description = description;
    }

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

    @Override
    public String toString() {
        return "StudentOrganization{name='%s', description='%s', memberCount=%d}".formatted(name, description, memberships.size());
    }
}
