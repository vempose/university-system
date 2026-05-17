package university.comparator;

import university.domain.user.Student;
import university.domain.user.User;

/// Compares students by their GPA (highest first).  Casts each {@link
/// university.domain.user.User} to {@link university.domain.user.Student}.
public final class StudentByGpaComparator implements UserComparator {

    @Override
    /// Returns {@code student2.gpa - student1.gpa}.
    public int compare(User u1, User u2) {
        return Double.compare(((Student) u2).getGpa(), ((Student) u1).getGpa());
    }
}
