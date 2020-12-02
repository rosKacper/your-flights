package pl.edu.agh.ki.lab.to.yourflights.model.airline;

import javax.persistence.*;

import java.util.UUID;

@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String country;
    private String description;

    public Airline(String name, String country, String description) {
        this.name = name;
        this.country = country;
        this.description = description;
    }

    public Airline() {

    }
}
