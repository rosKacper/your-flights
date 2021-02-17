package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Controller handling the form for adding and modifying flights
 */
@Component
public class AddFlightController {

    private final AirlineService airlineService;

    private final NavigationController navigationController;

    @FXML
    public TextField placeOfDestination,placeOfDeparture;
    @FXML
    public ComboBox<String> comboBox;
    @FXML
    public DatePicker departureDate, arrivalDate;
    @FXML
    public JFXTimePicker departureTime, arrivalTime;
    @FXML
    public Label placeOfDestinationValidationLabel, airlineNameValidationLabel, departureTimeValidationLabel, arrivalTimeValidationLabel;
    @FXML
    public Label departureDateValidationLabel;
    @FXML
    public Label arrivalDateValidationLabel;
    @FXML
    public Label placeOfDepartureValidationLabel;
    @FXML
    public Text actiontarget;
    @FXML
    public Label placeOfDestinationValidation, airlineNameValidation, departureTimeValidation, arrivalTimeValidation;
    @FXML
    public Label placeOfDepartureValidation;
    @FXML
    public Label departureDateValidation;
    @FXML
    public Label arrivalDateValidation;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    String date_blocker = "31/12/9000";
    String time_Blocker = "00:00";
    JFXTimePicker timeBlocker = new JFXTimePicker();
    DatePicker blocker = new DatePicker();

    private FlightService flightService;
    private Flight flight;
    private UserPrincipalService userPrincipalService;

    public void setData(Flight flight) {
        this.flight = flight;
        updateControls();
    }

    @FXML
    public void initialize() {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(role.equals("[AIRLINE]")){
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if(userDetails instanceof UserDetails){
                username = ((UserDetails)userDetails).getUsername();
            }
            Airline airline = airlineService.findByUser(userPrincipalService.findByUsername(username).get(0));
            this.comboBox.setItems(FXCollections.observableList(Arrays.asList(airline.getName())));
            this.comboBox.setValue(airline.getName());
            this.comboBox.setDisable(true);
        }
    }

    private void updateControls() {
        departureDate.setValue(LocalDate.parse( flight.getDepartureDate(),formatter));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        arrivalDate.setValue(LocalDate.parse( flight.getArrivalDate(),formatter));
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());
        departureTime.setValue(LocalTime.parse(flight.getDepartureTime(),timeFormatter));
        arrivalTime.setValue(LocalTime.parse(flight.getArrivalTime(),timeFormatter));
        comboBox.setValue(flight.getAirline().getName());
        comboBox.setPromptText(flight.getAirline().getName());
        comboBox.setPlaceholder(new Text(flight.getAirline().getName()));
    }

    private void updateModel() {
        flight.setPlaceOfDeparture(placeOfDeparture.textProperty().getValue());
        flight.setPlaceOfDestination(placeOfDestination.textProperty().getValue());
        flight.setDepartureTime(departureTime.getValue().toString());
        flight.setArrivalTime(arrivalTime.getValue().toString());
        flight.setArrivalDate(arrivalDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setDepartureDate(departureDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setAirline(airlineService.findByName(comboBox.getValue()));
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        blocker.setValue(LocalDate.parse( date_blocker,formatter));
        timeBlocker.setValue(LocalTime.parse(time_Blocker,timeFormatter));

        boolean placeOfDestinationValidation = Validator.validateNotEmpty(placeOfDestination, placeOfDestinationValidationLabel);
        boolean placeOfDepartureValidation = Validator.validateNotEmpty(placeOfDeparture, placeOfDepartureValidationLabel);
        boolean departureDateValidation = Validator.validateDate(departureDate, arrivalDate, departureDateValidationLabel);
        boolean arrivalDateValidation = Validator.validateDate(arrivalDate, blocker, arrivalDateValidationLabel);
        boolean departureTimeValidation = Validator.validateTime(departureTime, arrivalTime,departureDate,arrivalDate, departureTimeValidationLabel);
        boolean arrivalTimeValidation = Validator.validateTime(timeBlocker, arrivalTime,departureDate,arrivalDate,arrivalTimeValidationLabel);
        if(!placeOfDestinationValidation || !placeOfDepartureValidation || !departureDateValidation || !arrivalDateValidation ||
                !departureTimeValidation || !arrivalTimeValidation){
            return;
        }

        if (flight == null) {
            flight = new Flight(placeOfDeparture.getText(), placeOfDestination.getText(), departureDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), arrivalDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    airlineService.findByName(comboBox.getValue()), departureTime.getValue().toString(), arrivalTime.getValue().toString());
        } else {
            updateModel();
        }

        flightService.save(flight);

        placeOfDeparture.clear();
        placeOfDestination.clear();
        departureDate = null;
        arrivalDate = null;
        departureTime = null;
        arrivalTime = null;
        Flight tempFlight = flight;
        flight = null;

        showTicketCategoriesView(actionEvent, tempFlight);
    }

    public AddFlightController(FlightService flightService,
                               AirlineService airlineService,
                               NavigationController navigationController,
                               UserPrincipalService userPrincipalService){
        this.flightService = flightService;
        this.airlineService = airlineService;
        this.navigationController = navigationController;
        this.userPrincipalService = userPrincipalService;
    }

    public void showFlightsView(ActionEvent actionEvent) {
        navigationController.showFlightsView(actionEvent);
    }

    public void showTicketCategoriesView(ActionEvent actionEvent, Flight flight) {
        navigationController.showTicketCategoriesView(actionEvent, flight);
    }

    public void setComboBox(MouseEvent actionEvent) {
        ObservableList<Airline> airlines = FXCollections.observableList(airlineService.findAll());

        ObservableList<String> airlinesNames = FXCollections.observableList(airlines.stream()
                .map(airline -> airline.getName())
                .collect(Collectors.toList()));

        this.comboBox.setItems(airlinesNames);

        if(this.flight != null) {
            comboBox.setValue(flight.getAirline().getName());
            comboBox.setPromptText(flight.getAirline().getName());
            comboBox.setPlaceholder(new Text(flight.getAirline().getName()));
        }

    }
}
