package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;

@Service
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }
    public static ObservableList<Airline> airlines = FXCollections.observableArrayList();
    
    //method returning example data for testing purposes
    public ObservableList<Airline> getMockData() {

        return airlines;
    }

    public static void addAirline(Airline airline){
        airlines.add(airline);
    }


}
