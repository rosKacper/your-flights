package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.flights.DestinationController;
import pl.edu.agh.ki.lab.to.yourflights.controller.flights.FlightDetailsController;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainViewController {

    private final Resource destinationView;
    private FlightService flightService;

    @FXML
    private VBox flightsList;

    @FXML
    private HBox destinationsList;

    @FXML
    private Resource flightDetailsBrief;

    private List<Flight> flights;
    private List<String> destinations;

    private final ApplicationContext applicationContext;

    public MainViewController(ApplicationContext applicationContext,
                              FlightService flightService,
                              @Value("classpath:/view/Main/FlightDetailsBrief.fxml") Resource flightDetailsBrief,
                              @Value("classpath:/view/Main/Destination.fxml") Resource destinationView) {
        this.applicationContext = applicationContext;
        this.flightDetailsBrief = flightDetailsBrief;
        this.flightService = flightService;
        this.destinationView = destinationView;
    }

    @FXML
    public void initialize() {
        this.setFlights();
        this.setFlightsList();
        this.setDestinations();
    }

    private void setFlights() {
        flights = flightService.getFlightsSortedDescendingBasedOnNumberOfReservations().stream().limit(3).collect(Collectors.toList());
        this.showFlightDetailsView(flights);
    }
    private void setDestinations() {
        destinations = flightService.getFlightDestinationsSortedDescendingBasedOnNumberOfReservations().stream().limit(5).collect(Collectors.toList());
        this.showMostPopularDestinations(destinations);
    }

    private void setFlightsList() {

    }

    public void showMostPopularDestinations(List<String> destinations) {
        try {
            for(String destination : destinations) {
                FXMLLoader fxmlloader;
                fxmlloader = new FXMLLoader(destinationView.getURL());
                fxmlloader.setControllerFactory(applicationContext::getBean);
                Parent parent = fxmlloader.load();
                if(destination != null) {
                    DestinationController controller = fxmlloader.getController();
                    controller.setData(destination);
                }
                this.destinationsList.getChildren().add(parent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFlightDetailsView(List<Flight> flights) {
        try {
            for(Flight flight : flights) {
                FXMLLoader fxmlloader;
                fxmlloader = new FXMLLoader(flightDetailsBrief.getURL());
                fxmlloader.setControllerFactory(applicationContext::getBean);
                Parent parent = fxmlloader.load();
                if(flight != null) {
                    FlightDetailsController controller = fxmlloader.getController();
                    controller.setData(flight);
                }
                this.flightsList.getChildren().add(parent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}