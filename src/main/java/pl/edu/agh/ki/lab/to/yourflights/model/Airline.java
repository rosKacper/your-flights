package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

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
    @NotEmpty
    private String city;
    @NotEmpty
    private String street;
    @NotEmpty
    private String postalCode;
    @NotEmpty
    private String emailAddress;

    @OneToOne
    @JoinColumn(name="accountID")
    private User user;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "airline"
    )
    private List<Flight> flights;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY,
            mappedBy = "airline"
    )
    private List<Review> reviews;

    public Airline(String name, String country,String description,  String city,
                   String street, String postalCode, String emailAdress) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.emailAddress = emailAdress;
    }

    public Airline() {

    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty getCountryProperty() {
        return new SimpleStringProperty(country);
    }

    public StringProperty getDescriptionProperty() { return new SimpleStringProperty(description); }

    public StringProperty getCityProperty() {
        return new SimpleStringProperty(city);
    }

    public StringProperty getStreetProperty() {
        return new SimpleStringProperty(street);
    }

    public StringProperty getPostalCodeProperty() { return new SimpleStringProperty(postalCode); }

    public StringProperty getEmailAddressProperty() {
        return new SimpleStringProperty(emailAddress);
    }

}
