package university.comparator;

import university.domain.user.Teacher;
import university.domain.user.User;

/// Compares teachers by their average rating (highest first).  Casts each {@link
/// university.domain.user.User} to {@link university.domain.user.Teacher}.
public final class TeacherByRatingComparator implements UserComparator {

    @Override
    /// Returns {@code teacherB.rating - teacherA.rating}.
    public int compare(User a, User b) {
        return Double.compare(
                ((Teacher) b).getAverageRating(),
                ((Teacher) a).getAverageRating()
        );
    }
}
