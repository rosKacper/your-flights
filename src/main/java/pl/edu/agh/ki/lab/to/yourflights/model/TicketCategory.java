package pl.edu.agh.ki.lab.to.yourflights.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
public class TicketCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //TODO
    //flightID

    @NotEmpty
    private String categoryName;

    @NotNull
    private BigDecimal categoryPrice;

    @NotNull
    private int totalNumberOfSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightID")
    private Flight flight;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "ticketCategory"
    )
    private List<TicketOrder> ticketOrders;

    public TicketCategory(){}


    //TODO
    //add flight to constructor
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
}
