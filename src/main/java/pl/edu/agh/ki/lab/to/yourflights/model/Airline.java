package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model przewoźnika
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Airline extends RecursiveTreeObject<Airline> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    public Long getId() {
        return id;
    }

    @NotEmpty
    private String name;
    @NotEmpty
    private String country;

    private String description;


    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;

    /**
     * Mapowanie relacji do lotów danego przewoźnika
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "airline"
    )
    private List<Flight> flights;

    /**
     * Mapowanie relacji do ocen danego przewoźnika
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY,
            mappedBy = "airline"
    )
    private List<Review> reviews;

    public Airline(String name, String country, String description, User user) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.user = user;
    }

    public Airline() {

    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty getCountryProperty() {
        return new SimpleStringProperty(country);
    }

    public StringProperty getDescriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
