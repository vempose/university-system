package university.comparator;

import university.domain.user.User;

public final class UserByNameComparator implements UserComparator {

    @Override
    public int compare(User first, User second) {
        return first.getName().compareToIgnoreCase(second.getName());
    }
}
