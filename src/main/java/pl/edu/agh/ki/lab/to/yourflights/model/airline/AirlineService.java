package pl.edu.agh.ki.lab.to.yourflights.model.airline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    //method returning example data for testing purposes
    public ObservableList<Airline> getMockData() {
        ObservableList<Airline> airlines = FXCollections.observableArrayList();
        Airline airline1 = new Airline("Lufthansa", "Germany", "");
        Airline airline2 = new Airline("LOT", "Poland", "");
        airlines.add(airline1);
        airlines.add(airline2);
        return airlines;
    }
}
