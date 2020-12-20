package pl.edu.agh.ki.lab.to.yourflights.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Klasa definiuje model kategorii biletu
 * (czyli np. Kategoria biznesowa, o danej cenie biletu i danej liczbie miejsc w danym locie)
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class TicketCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String categoryName;
    @NotNull
    private BigDecimal categoryPrice;
    @NotNull
    private int totalNumberOfSeats;

    /**
     * Mapowanie relacji do lotu którego dotyczy dana kategoria biletu
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flightID")
    private Flight flight;

    /**
     * Mapowanie relacji do zamówień na bilety dotyczące danej kategorii biletu
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "ticketCategory"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TicketOrder> ticketOrders;

    public TicketCategory(){}

    public TicketCategory(String categoryName, BigDecimal categoryPrice, int totalNumberOfSeats, Flight flight){
        this.categoryName = categoryName;
        this.categoryPrice = categoryPrice;
        this.totalNumberOfSeats = totalNumberOfSeats;
        this.flight = flight;
    }


    //getters and setters

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(BigDecimal categoryPrice) {
        this.categoryPrice = categoryPrice;
    }

    public int getTotalNumberOfSeats() {
        return totalNumberOfSeats;
    }

    public void setTotalNumberOfSeats(int totalNumberOfSeats) {
        this.totalNumberOfSeats = totalNumberOfSeats;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public StringProperty getFlightIDProperty(){
        return new SimpleStringProperty(flight.getId().toString());
    }

    public List<TicketOrder> getTicketOrders() {
        return ticketOrders;
    }

    public void setTicketOrders(List<TicketOrder> ticketOrders) {
        this.ticketOrders = ticketOrders;
    }
}
