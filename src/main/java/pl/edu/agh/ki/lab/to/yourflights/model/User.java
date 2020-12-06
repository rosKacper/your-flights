package pl.edu.agh.ki.lab.to.yourflights.model;

import pl.edu.agh.ki.lab.to.yourflights.utils.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model użytkownika aplikacji
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @NotEmpty
    private String login;
    private String password;

    /**
     * Mapowanie relacji do ocen użytkownika
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private List<Review> reviews;

    public User(UserRole role, String login, String password) {
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public User() {}

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
