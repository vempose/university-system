package university.comparator;

import university.domain.user.User;

/// Compares users alphabetically by name (case-insensitive).
public final class UserByNameComparator implements UserComparator {

    @Override
    /// Delegates to {@link String#compareToIgnoreCase}.
    public int compare(User a, User b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
