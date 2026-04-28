package university.comparators;

import university.users.Student;
import java.util.Comparator;

public class StudentByGpaComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s2.getGpa(), s1.getGpa()); 
    }
}
