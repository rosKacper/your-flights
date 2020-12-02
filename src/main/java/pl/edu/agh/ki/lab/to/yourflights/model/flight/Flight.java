package pl.edu.agh.ki.lab.to.yourflights.model.flight;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String placeOfDeparture;
    private String placeOfDestination;

    private Date departureTime;
    private Date arrivalTime;

    public Flight(String placeOfDeparture, String placeOfDestination, Date departureTime, Date arrivalTime) {
        this.placeOfDeparture = placeOfDeparture;
        this.placeOfDestination = placeOfDestination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Flight() {

    }
}
