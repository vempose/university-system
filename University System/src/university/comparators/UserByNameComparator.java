package university.comparators;

import university.users.User;
import java.util.Comparator;

public class UserByNameComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        return u1.getName().compareToIgnoreCase(u2.getName()); 
    }
}
