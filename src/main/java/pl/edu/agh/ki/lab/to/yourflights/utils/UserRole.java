package pl.edu.agh.ki.lab.to.yourflights.utils;

public enum UserRole {
    CLIENT("CLIENT"),
    AIRLINE("AIRLINE"),
    ADMIN("ADMIN");

    public final String role;

    private UserRole(String role) {
        this.role = role;
    }
}
