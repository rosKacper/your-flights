package pl.edu.agh.ki.lab.to.yourflights.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
 * Klasa definiuje model rezerwacji
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Reservation extends RecursiveTreeObject<Reservation> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    @NotEmpty
    private String reservationDate;

    @NotEmpty
    private String userName;

    /**
     * Mapowanie relacji do zamówień na bilety przypisanych do danej rezerwacji
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            mappedBy = "reservation"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TicketOrder> ticketOrders = new LinkedList<>();

    /**
     * Mapowanie relacji do klienta który składa daną rezerwację
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Reservation(String reservationDate, Customer customer, String userName) {
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TicketOrder> getTicketOrders() {
        return ticketOrders;
    }

    public void setTicketOrders(List<TicketOrder> ticketOrders) {
        this.ticketOrders = ticketOrders;
    }

    public StringProperty getUserNameProperty() {
        return new SimpleStringProperty(userName);
    }
}
