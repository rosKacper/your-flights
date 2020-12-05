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

    //TODO
    //reservationID, ticketCategoryID, ticketDiscountID
    //used in mapping relations in database
    @ManyToOne
    @JoinColumn(name = "ticketDiscountID", referencedColumnName = "ID")
    private TicketDiscount ticketDiscount;

    @ManyToOne
    @JoinColumn(name = "reservationID", referencedColumnName = "ID")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "ticketCategoryID", referencedColumnName = "ID")
    private TicketCategory ticketCategory;


    public TicketOrder(){}


    //TODO
    //add fields used in mapping relations to constructor
    public TicketOrder(int numberOfSeats){
        this.numberOfSeats = numberOfSeats;
    }



    //getters and setters

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

}
