package university.domain.student;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentOrganization implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<OrganizationMembership> memberships = new ArrayList<>();
    private String description;

    public StudentOrganization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addMembership(OrganizationMembership membership) {
        memberships.add(membership);
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
        return "StudentOrganization{name='%s', description='%s', memberCount=%d}".formatted(
                name,
                description,
                memberships.size()
        );
    }
}
