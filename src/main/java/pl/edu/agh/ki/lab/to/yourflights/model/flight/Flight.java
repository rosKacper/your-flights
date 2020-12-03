package pl.edu.agh.ki.lab.to.yourflights.model.flight;


import com.sun.istack.NotNull;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String placeOfDeparture;
    @NotEmpty
    private String placeOfDestination;

    @NotNull
    private Date departureTime;
    @NotNull
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
