package pl.edu.agh.ki.lab.to.yourflights.model;


import org.springframework.security.core.GrantedAuthority;
import pl.edu.agh.ki.lab.to.yourflights.utils.UserRole;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    private String username;

    private String email;

    private String password;

    private UserRole userRole;

    @OneToOne
    private Customer customer;

    public User(){}

    public User(String username, String email, String password, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
