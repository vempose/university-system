package university.domain.academic;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// A school or faculty within the university.
///
/// Has a name and a list of `Major` offerings under it.
public final class School implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Major> majors = new ArrayList<>();

    /// Creates a school. Name is trimmed and must not be blank.
    public School(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException(
                "School name must not be blank"
        );
        this.name = name.strip();
    }

    /// Adds a major to this school (skips duplicates).
    public void addMajor(Major major) {
        if (!majors.contains(major)) majors.add(major);
    }

    public String getName() {
        return name;
    }

    public List<Major> getMajors() {
        return List.copyOf(majors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "School{name='%s', majors=%d}".formatted(name, majors.size());
    }
}
