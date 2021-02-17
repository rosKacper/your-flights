package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;

@Component
public class FlightDetailsController {

    private Flight flight;
    private final FlightService flightService;
    private final NavigationController navigationController;

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

    @FXML
    private Text numberOfReservations;
    @FXML
    private Text numberOfFreeSeats;
    @FXML
    private Text numberOfSeats;
    @FXML
    private Text numberOfTakenSeats;
    @FXML
    private Text flightTotalIncome;

    public FlightDetailsController(NavigationController navigationController,
                                   FlightService flightService) {
        this.navigationController = navigationController;
        this.flightService = flightService;
    }

    public void setData(Flight flight) {
        this.flight = flight;
        updateFields();
    }

    private void updateFields() {
        placeOfDeparture.setText(flight.getPlaceOfDeparture());
        placeOfDestination.setText(flight.getPlaceOfDestination());
        airline.setText(flight.getAirline().getName());
        departureTime.setText(flight.getDepartureDate() + " " + flight.getDepartureTime());
        arrivalTime.setText(flight.getArrivalDate() + " " + flight.getArrivalTime());

        if(flightTotalIncome != null) {
            flightTotalIncome.setText(flightService.getTotalIncomeFromFlight(flight).toString());
        }

        if(numberOfReservations != null) {
            numberOfReservations.setText(Integer.toString(flightService.getNumberOfReservationsForFlight(flight)));
        }

        if(numberOfSeats != null) {
            numberOfSeats.setText(Integer.toString(flightService.getNumberOfSeatsForFlight(flight)));
        }

        if(numberOfTakenSeats != null) {
            numberOfTakenSeats.setText(Integer.toString(flightService.getNumberOfTakenSeatsForFlight(flight)));
        }

        if(numberOfFreeSeats != null){
            numberOfFreeSeats.setText(Integer.toString(Integer.parseInt(numberOfSeats.getText()) - Integer.parseInt(numberOfTakenSeats.getText())));
        }
    }

    public void showFlightsView(ActionEvent actionEvent) {
        navigationController.showFlightsView(actionEvent);
    }
}
