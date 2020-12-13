package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model rezerwacji
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Reservation extends RecursiveTreeObject<Reservation> {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    private String reservationDate;

    /**
     * Mapowanie relacji do zamówień na bilety przypisanych do danej rezerwacji
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "reservation"
    )
    private List<TicketOrder> ticketOrders;

    /**
     * Mapowanie relacji do klienta który składa daną rezerwację
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Reservation(String reservationDate, Customer customer) {
        this.reservationDate = reservationDate;
        this.customer = customer;
    }

    public Reservation() {}

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public StringProperty getReservationDateProperty(){
        return new SimpleStringProperty(reservationDate);
    }

}
