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

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY,
            mappedBy = "ticketCategory"
    )
    private List<TicketOrder> ticketOrders;

    public TicketCategory(){}


    //TODO
    //add flight to constructor
    public TicketCategory(String categoryName, BigDecimal categoryPrice, int totalNumberOfSeats){
        this.categoryName = categoryName;
        this.categoryPrice = categoryPrice;
        this.totalNumberOfSeats = totalNumberOfSeats;
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
}
