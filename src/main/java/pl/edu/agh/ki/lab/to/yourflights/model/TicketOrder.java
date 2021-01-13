package pl.edu.agh.ki.lab.to.yourflights.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Klasa definiuje model zamówienia na bilety (np. 3 bilety w kategorii ekonomicznej, ze zniżką studencką)
 * Pojedyncza rezerwacja może mieć kilka takich zamówień na bilety (np. 2 bilety dla dorosłych i 1 bilet dla dzieci)
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class TicketOrder {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    @NotNull
    private int numberOfSeats;

    /**
     * Mapowanie relacji do zniżki na bilet
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketDiscountID")
    private TicketDiscount ticketDiscount;

    /**
     * Mapowanie relacji do rezerwacji której dotyczy zamówienie na bilety
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservationId")
    private Reservation reservation;

    /**
     * Mapowanie relacji do kategorii biletów której dotyczy zamówienie na bilety
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketCategoryID")
    private TicketCategory ticketCategory;


    public TicketOrder(){}

    public TicketOrder(int numberOfSeats, TicketDiscount ticketDiscount, Reservation reservation, TicketCategory ticketCategory){
        this.numberOfSeats = numberOfSeats;
        this.ticketDiscount = ticketDiscount;
        this.reservation = reservation;
        this.ticketCategory = ticketCategory;
    }


    //getters and setters

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public TicketDiscount getTicketDiscount() {
        return ticketDiscount;
    }

    public void setTicketDiscount(TicketDiscount ticketDiscount) {
        this.ticketDiscount = ticketDiscount;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
    }
}
