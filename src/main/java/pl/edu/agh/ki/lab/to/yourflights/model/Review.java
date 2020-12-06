package pl.edu.agh.ki.lab.to.yourflights.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.UUID;

/**
 * Klasa definiuje model oceny wystawianej przez użytkownika danej linii lotniczej po odbyciu lotu
 * Zawiera oznaczenia potrzebne do późniejszego wykorzystania jej w bazie danych z użyciem Spring Data JPA
 */
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID review_ID;

    @NotNull
    private double rating;
    private String comment;

    /**
     * Mapowanie relacji do przewoźnika którego dotyczy ocena
     */
    @ManyToOne
    @JoinColumn(name = "airlineID")
    private Airline airline;

    /**
     * Mapowanie relacji do użytkownika który dodaje ocenę
     */
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    public Review(double rating, String comment, User user, Airline airline){
        this.rating=rating;
        this.comment=comment;
        this.user=user;
        this.airline=airline;
    }

    public Review(){}

}