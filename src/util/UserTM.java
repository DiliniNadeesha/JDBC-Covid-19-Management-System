package util;

import javafx.scene.control.Button;

public class UserTM {

    private String username;
    private String name;
    private String role;
    private Button delete;

    public UserTM() {
    }

    public UserTM(String username, String name, String role, Button delete) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.delete = delete;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", delete=" + delete +
                '}';
    }
}
