package university.comparator;

import university.domain.user.Teacher;
import university.domain.user.User;

public final class TeacherByRatingComparator implements UserComparator {

    @Override
    public int compare(User a, User b) {
        return Double.compare(
                ((Teacher) b).getAverageRating(),
                ((Teacher) a).getAverageRating()
        );
    }
}
