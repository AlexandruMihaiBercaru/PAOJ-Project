package models;

import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String role;
    private String username;
    private String encryptedPassword;


    public User(String userId, String firstName, String lastName, String username, String role, String password) {
        this.userId = UUID.fromString(userId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.encryptedPassword = password;
    }

    public User(String firstName, String lastName, String username, String role, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.encryptedPassword = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.encryptedPassword = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getUserId() {
        return userId.toString();
    }

    public void setUserId() {
        this.userId = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
