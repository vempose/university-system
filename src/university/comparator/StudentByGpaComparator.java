package university.comparator;

import university.domain.user.Student;
import university.domain.user.User;

public final class StudentByGpaComparator implements UserComparator {

    @Override
    public int compare(User u1, User u2) {
        var s1 = (Student) u1;
        var s2 = (Student) u2;
        // descending: highest GPA first
        return Double.compare(s2.getGpa(), s1.getGpa());
    }
}
