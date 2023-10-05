package engine.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<String> usersSet;
    private boolean isAdminLoggedIn;

    public UserManager() {
        usersSet = new HashSet<>();
        isAdminLoggedIn = false;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }
    public synchronized void setAdminLoggedIn() { isAdminLoggedIn = true; }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }
    public void removeAdmin() { isAdminLoggedIn = false; }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
    public boolean isAdminLoggedIn(){ return isAdminLoggedIn; }
}
