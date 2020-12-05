package pl.edu.agh.ki.lab.to.yourflights.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID review_ID;

    @NotNull
    private double rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "airlineID")
    private Airline airline;

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