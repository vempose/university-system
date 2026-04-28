package university.domain.academic;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Major implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private School school;

    public Major(String name, School school) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException(
            "Major name must not be blank."
        );
        this.name = name;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public School getSchool() {
        return school;
    }

    void setSchool(School school) {
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
        return "Major{name='%s', school='%s'}".formatted(
            name,
            school.getName()
        );
    }
}
