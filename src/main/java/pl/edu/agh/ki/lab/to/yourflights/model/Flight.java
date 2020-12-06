package pl.edu.agh.ki.lab.to.yourflights.model;


import com.sun.istack.NotNull;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model lotu
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
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

    /**
     * Mapowanie relacji do przewoźnika realizującego dany lot
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airlineID")
    private Airline airline;

    /**
     * Mapowanie relacji do dostępnych kategorii biletów w danym locie
     */
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
