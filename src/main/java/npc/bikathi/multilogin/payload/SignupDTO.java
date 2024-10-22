package npc.bikathi.multilogin.payload;

public class SignupDTO {
    private String username;
    private String password;

    public SignupDTO(String username) {
        this.username = username;
    }

    public SignupDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
