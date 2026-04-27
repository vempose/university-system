package university.domain.academic;

import java.util.Objects;

public class Major {

    private final String name;
    private School school;

    public Major(String name, School school) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException(
            "Major name must not be blank."
        );
        Objects.requireNonNull(school, "School must not be null.");
        this.name = name;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public School getSchool() {
        return school;
    }

    // package-private — used by School when re-parenting a major
    void setSchool(School school) {
        Objects.requireNonNull(school, "School must not be null.");
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Major other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Major{name='" + name + "', school='" + school.getName() + "'}";
    }
}
