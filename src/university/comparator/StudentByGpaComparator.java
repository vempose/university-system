package university.comparator;

import university.domain.user.Student;
import university.domain.user.User;

public final class StudentByGpaComparator implements UserComparator {

    @Override
    public int compare(User u1, User u2) {
        return Double.compare(((Student) u2).getGpa(), ((Student) u1).getGpa());
    }
}
