package steps;
public class RegisterRequest {

    public String name;
    public String username;
    public String email;
    public String password;

    public RegisterRequest(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String toJson() {
        return "{"
                + "\"name\":\"" + name + "\","
                + "\"username\":\"" + username + "\","
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\""
                + "}";
    }
}
