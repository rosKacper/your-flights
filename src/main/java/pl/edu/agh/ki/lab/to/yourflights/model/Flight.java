package pl.edu.agh.ki.lab.to.yourflights.model;


import com.sun.istack.NotNull;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airlineID")
    private Airline airline;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "flight"
    )
    private List<TicketCategory> ticketCategories;

    public Flight(String placeOfDeparture, String placeOfDestination, Date departureTime, Date arrivalTime, Airline airline) {
        this.placeOfDeparture = placeOfDeparture;
        this.placeOfDestination = placeOfDestination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airline = airline;
    }

    public Flight() {

    }
}
