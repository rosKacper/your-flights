package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    //method returning example data for testing purposes
    public ObservableList<Airline> getMockData() {
        ObservableList<Airline> airlines = FXCollections.observableArrayList();
        Airline airline1 = new Airline("Lufthansa", "Germany", "Niemieckie linie lotnicze Lufthansa.");
        Airline airline2 = new Airline("LOT", "Poland", "Polskie Linie Lotnicze LOT.");
        airlines.add(airline1);
        airlines.add(airline2);
        return airlines;
    }
}
