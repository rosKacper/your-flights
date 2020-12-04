package pl.edu.agh.ki.lab.to.yourflights.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    private Date reservationDate;

    public Reservation(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Reservation() {}

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
}
