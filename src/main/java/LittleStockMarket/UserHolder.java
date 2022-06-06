package LittleStockMarket;

//Singleton-klasse
public class UserHolder {
    private User user;
    private final static UserHolder instance = new UserHolder();

    private UserHolder() {
    }

    public static UserHolder getInstance() {
        return instance;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public User getUser() {
        return this.user;
    }
}
