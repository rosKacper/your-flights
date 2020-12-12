package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model klienta
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Customer extends RecursiveTreeObject<Customer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customer_ID;

    @NotEmpty
    private String firstName, secondName, country, city, street, postalCode, phoneNumber, emailAddress;

    /**
     * Mapowanie relacji do użytkownika
     */
    @OneToOne
    @JoinColumn(name="account_id")
    private User user;

    /**
     * Mapowanie relacji do rezerwacji danego klienta
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "customer"
    )
    private List<Reservation> reservations;

    public Customer(String firstName, String secondName, String country,
                    String city, String street, String postalCode,
                    String phoneNumber, String emailAddress, User user) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.country = country;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.user = user;
    }

    public Customer(){}

    public StringProperty getFirstNameProperty(){
        return new SimpleStringProperty(firstName);
    }
    public StringProperty getSecondNameProperty(){
        return new SimpleStringProperty(secondName);
    }
    public StringProperty getCountryProperty(){
        return new SimpleStringProperty(country);
    }
    public StringProperty getCityProperty(){
        return new SimpleStringProperty(city);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
