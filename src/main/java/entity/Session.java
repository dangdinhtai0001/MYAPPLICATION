package entity;

public class Session {
    private String sessionName, password;
    private boolean isAdmin;

    public Session(String sessionName, boolean isAdmin) {
        this.sessionName = sessionName;
        this.isAdmin = isAdmin;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
