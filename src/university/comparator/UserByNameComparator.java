package university.comparator;

import university.domain.user.User;

public final class UserByNameComparator implements UserComparator {

    @Override
    public int compare(User a, User b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
