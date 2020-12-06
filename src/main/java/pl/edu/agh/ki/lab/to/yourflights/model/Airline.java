package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String country;
    @NotEmpty
    private String description;

    /**
     * Mapowanie relacji do użytkownika
     */
    @OneToOne
    @JoinColumn(name="accountID")
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

    public Airline(String name, String country, String description) {
        this.name = name;
        this.country = country;
        this.description = description;
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

}
