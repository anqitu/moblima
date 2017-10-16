package manager;

import manager.exception.InvalidRegisterEmailException;
import manager.exception.InvalidRegisterMobileException;
import manager.exception.InvalidRegisterUsernameException;
import manager.exception.InvalidReviewRatingException;
import model.cinema.Staff;
import model.commons.User;

public class UserManager extends EntityManager<User> {

    private static UserManager instance;

    private UserManager() {
        super();
    }

    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    public void registerUser(String firstName, String lastName, String mobile, String email)
            throws InvalidRegisterEmailException, InvalidRegisterMobileException {

        if (findByEmail(email) != null)
            throw new InvalidRegisterEmailException("Registered email already exists");

        if (findByMobile(mobile) != null)
            throw new InvalidRegisterMobileException("Registered phone number already exists");

        User user = new User(firstName, lastName, mobile, email);
        entities.put(user.getId(), user);
    }

    public void registerStaff(String firstName, String lastName, String mobile, String email,
                              String username, String password) throws InvalidRegisterEmailException,
                                InvalidRegisterMobileException, InvalidRegisterUsernameException {

        if (findByEmail(email) != null)
            throw new InvalidRegisterEmailException("Registered email already exists");

        if (findByMobile(mobile) != null)
            throw new InvalidRegisterMobileException("Registered phone number already exists");

        if (findByUsername(username) != null)
            throw new InvalidRegisterUsernameException("Registered username alreadye exists");

        Staff staff = new Staff(firstName, lastName, mobile, email, username, password);
        entities.put(staff.getId(), staff);
    }

    public User findByMobile(String mobile) {
        for(User user: entities.values()) {
            if (user.getMobile().equals(mobile))
                return user;
        }
        return null;
    }

    public User findByEmail(String email) {
        for(User user: entities.values()) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public Staff findByUsername(String username) {
        for(User registeredUser: entities.values()) {
            if (registeredUser instanceof Staff) {
                Staff staff = (Staff) registeredUser;
                if (staff.getUsername().equals(username))
                    return staff;
            }
        }
        return null;
    }

    public boolean login(String username, String password) {
        Staff staff = findByUsername(username);
        return staff == null || !staff.getPassword().equals(password);
    }
}
