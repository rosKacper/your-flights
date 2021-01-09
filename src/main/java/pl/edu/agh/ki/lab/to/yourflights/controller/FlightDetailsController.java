package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class FlightDetailsController {

    private final Resource flightsView;
    private final ApplicationContext applicationContext;

    private Flight flight;

    @FXML
    private Text placeOfDeparture;
    @FXML
    private Text placeOfDestination;
    @FXML
    private Text airline;
    @FXML
    private Text departureTime;
    @FXML
    private Text arrivalTime;


    public FlightDetailsController(ApplicationContext applicationContext,
                                   @Value("classpath:/view/FlightView.fxml") Resource flightsView) {
        this.applicationContext = applicationContext;
        this.flightsView = flightsView;
    }

    /**
     * Metoda ustawiająca lot
     * @param flight lot
     */
    public void setData(Flight flight) {
        this.flight = flight;
        updateFields();
    }

    /**
     * Metoda ustawiająca wartości pól tekstowych w formularzu, w zależności od otrzymanego lotu do edycji
     */
    private void updateFields() {
        placeOfDeparture.setText(flight.getPlaceOfDeparture());
        placeOfDestination.setText(flight.getPlaceOfDestination());
        airline.setText(flight.getAirline().getName());
        departureTime.setText(flight.getDepartureDate() + " " + flight.getDepartureTime());
        arrivalTime.setText(flight.getArrivalDate() + " " + flight.getArrivalTime());
    }

    /**
     * Metoda służąca do przejścia do widoku lotów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showFlightsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(flightsView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
