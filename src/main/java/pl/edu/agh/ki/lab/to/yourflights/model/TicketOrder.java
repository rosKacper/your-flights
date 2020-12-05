package pl.edu.agh.ki.lab.to.yourflights.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class TicketOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @NotNull
    private int numberOfSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketDiscountID")
    private TicketDiscount ticketDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservationID")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketCategoryID")
    private TicketCategory ticketCategory;


    public TicketOrder(){}


    //TODO
    //add fields used in mapping relations to constructor
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
