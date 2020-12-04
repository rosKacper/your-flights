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

    public Review(double rating, String comment){
        this.rating=rating;
        this.comment=comment;
    }

    public Review(){}

}