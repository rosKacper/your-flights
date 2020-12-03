package pl.edu.agh.ki.lab.to.yourflights.model.airline;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.UUID;

@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String country;
    @NotEmpty
    private String description;

    public Airline(String name, String country, String description) {
        this.name = name;
        this.country = country;
        this.description = description;
    }

    public Airline() {

    }
}
