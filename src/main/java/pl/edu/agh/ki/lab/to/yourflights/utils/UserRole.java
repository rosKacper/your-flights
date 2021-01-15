package pl.edu.agh.ki.lab.to.yourflights.utils;

/**
 * Enumerator definiujący role użytkownika
 */
public enum UserRole {
    USER("USER"),
    AIRLINE("AIRLINE"),
    ADMIN("ADMIN");

    public final String role;

    UserRole(String role) {
        this.role = role;
    }
}
