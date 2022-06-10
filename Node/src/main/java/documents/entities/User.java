package documents.entities;

public class User {
    private final String name;
    private final String password;
    private final String authority;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", authority='" + authority + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthority() {
        return authority;
    }

    public User(String name, String password, String authority) {
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
