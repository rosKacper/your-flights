package pl.edu.agh.ki.lab.to.yourflights.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class TicketDiscount {

    @Id
    @GeneratedValue
    private int UUID;

    @NotEmpty
    private double discount;

    @NotEmpty
    private String name;

    public TicketDiscount(double discount, String name) {
        this.discount = discount;
        this.name = name;
    }

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
