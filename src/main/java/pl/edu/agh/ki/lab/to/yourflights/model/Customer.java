package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
public class Customer extends RecursiveTreeObject<Customer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customer_ID;

    @NotEmpty
    private String firstName, secondName, country, city, street, postalCode, phoneNumber, emailAddress;

    public Customer(String firstName, String secondName, String country, String city, String street, String postalCode, String phoneNumber, String emailAddress) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.country = country;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
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

}
