package pl.edu.agh.ki.lab.to.yourflights.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    private Date reservationDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "reservation"
    )
    private List<TicketOrder> ticketOrders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID")
    private Customer customer;

    public Reservation(Date reservationDate, Customer customer) {
        this.reservationDate = reservationDate;
        this.customer = customer;
    }

    public Reservation() {}

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
