package pl.edu.agh.ki.lab.to.yourflights.model;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.istack.NotNull;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model lotu
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Flight extends RecursiveTreeObject<Flight> {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String placeOfDeparture;
    @NotEmpty
    private String placeOfDestination;

    @NotNull
    private String departureDate;
    @NotNull
    private String arrivalDate;

    @NotNull
    private String departureTime;
    @NotNull
    private String arrivalTime;
    /**
     * Mapowanie relacji do przewoźnika realizującego dany lot
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airlineID")
    private Airline airline;

    /**
     * Mapowanie relacji do dostępnych kategorii biletów w danym locie
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            mappedBy = "flight"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TicketCategory> ticketCategories = new LinkedList<>();

    public Flight(String placeOfDeparture, String placeOfDestination, String departureDate, String arrivalDate, Airline airline, String departureTime, String arrivalTime) {
        this.placeOfDeparture = placeOfDeparture;
        this.placeOfDestination = placeOfDestination;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.airline = airline;
        this.departureTime=departureTime;
        this.arrivalTime=arrivalTime;
    }

    public Flight() {

    }

    public void setPlaceOfDeparture(String placeOfDeparture) {
        this.placeOfDeparture = placeOfDeparture;
    }

    public void setPlaceOfDestination(String placeOfDestination) {
        this.placeOfDestination = placeOfDestination;
    }

    public void setDepartureDate(String departureTime) {
        this.departureDate = departureTime;
    }

    public void setArrivalDate(String arrivalTime) {
        this.arrivalDate = arrivalTime;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }

    public String getPlaceOfDeparture() {
        return placeOfDeparture;
    }

    public String getPlaceOfDestination() {
        return placeOfDestination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public Airline getAirline() {
        return airline;
    }

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public String getDepartureTime() { return departureTime; }

    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StringProperty getplaceOfDepartureProperty(){
        return new SimpleStringProperty(placeOfDeparture);
    }
    public StringProperty getplaceOfDestinationProperty(){
        return new SimpleStringProperty(placeOfDestination);
    }
    public StringProperty getdepartureDateProperty(){
        return new SimpleStringProperty(departureDate);
    }
    public StringProperty getarrivalDateProperty(){
        return new SimpleStringProperty(arrivalDate);
    }
    public StringProperty getAirlineNameProperty(){return new SimpleStringProperty(airline.getName());}
    public StringProperty getDepartureTimeProperty(){
        return new SimpleStringProperty(departureTime);
    }
    public StringProperty getArrivalTimeProperty(){
        return new SimpleStringProperty(arrivalTime);
    }
}
