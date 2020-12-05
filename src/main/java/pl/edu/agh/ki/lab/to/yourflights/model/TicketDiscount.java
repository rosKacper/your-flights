package pl.edu.agh.ki.lab.to.yourflights.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class TicketDiscount {

    @Id
    @GeneratedValue
    private UUID id;

    @NotEmpty
    private double discount;

    @NotEmpty
    private String name;

    public TicketDiscount(double discount, String name) {
        this.discount = discount;
        this.name = name;
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY,
            mappedBy = "ticketDiscount"
    )
    private List<TicketOrder> ticketOrders;

    public TicketDiscount() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
