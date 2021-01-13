package pl.edu.agh.ki.lab.to.yourflights.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * Klasa definiuje model zniżki na bilet (czyli np. zniżka studencka, zniżka dla dzieci)
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class TicketDiscount {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

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
