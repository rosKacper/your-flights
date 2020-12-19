package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


/**
 * Kontroler obsługujący formularz do dodawania lotów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddFlightController {

    /**
     * Widok lotów
     */
    private final Resource flightView;
    private AirlineService airlineService;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
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
    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("hh/mm");
    String date_blocker = "31/12/9000";
    DatePicker blocker=new DatePicker();


    private FlightService flightService;
    private Flight flight;
    private String airlineName;



    /**
     * Metoda ustawiająca lot do edycji
     * @param flight lot do edycji, może być nullem
     */
    public void setData(Flight flight) {
        this.flight = flight;
        updateControls();

    }



    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanego lotu do edycji
     */
    private void updateControls() {
        departureDate.setValue(LocalDate.parse( flight.getDepartureDate(),formatter));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        arrivalDate.setValue(LocalDate.parse( flight.getArrivalDate(),formatter));
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());
        departureTime.setValue(LocalTime.parse(flight.getDepartureTime(),timeFormatter));
        arrivalTime.setValue(LocalTime.parse(flight.getArrivalTime(),timeFormatter));

    }

    /**
     * Metoda aktualizująca lot po edycji
     */
    private void updateModel() {
        flight.setPlaceOfDeparture(placeOfDeparture.textProperty().getValue());
        flight.setPlaceOfDestination(placeOfDestination.textProperty().getValue());
        flight.setDepartureTime(departureTime.getValue().toString());
        flight.setArrivalTime(arrivalTime.getValue().toString());
        flight.setArrivalDate(arrivalDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setDepartureDate(departureDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setAirline(airlineService.findByName(comboBox.getValue()));

    }

    /**
     * Metoda obsługująca dodawanie lotu po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //blocker to nieosiągalna data, która sprawi, że walidacja daty przylotu pominie warunek data odlotu później
        //od daty przylotu
        blocker.setValue(LocalDate.parse( date_blocker,formatter));
        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean placeOfDestinationValidation = Validator.validateNotEmpty(placeOfDestination, placeOfDestinationValidationLabel);
        boolean placeOfDepartureValidation = Validator.validateNotEmpty(placeOfDeparture, placeOfDepartureValidationLabel);
        boolean departureTimeValidation = Validator.validateDate(departureDate, arrivalDate, departureDateValidationLabel);
        boolean arrivalTimeValidation = Validator.validateDate(arrivalDate, blocker, arrivalDateValidationLabel);


        if(!placeOfDestinationValidation || !placeOfDepartureValidation || !departureTimeValidation || !arrivalTimeValidation ){
            return;
        }

        //Stworzenie nowego lotu i wyczyszczenie pól formularza
        if (flight == null) {
            flight = new Flight(placeOfDeparture.getText(),placeOfDestination.getText(), departureDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), arrivalDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), airlineService.findByName(comboBox.getValue())
            ,departureTime.getValue().toString(),arrivalTime.getValue().toString());
        } else {
            updateModel();
        }

        flightService.save(flight);
        actiontarget.setText("Flight added successfully!");
        placeOfDeparture.clear();
        placeOfDestination.clear();
        departureDate =null;
        arrivalDate =null;
        flight=null;
        airlineName=null;
        departureTime=null;
        arrivalTime=null;


        //Po dodaniu lotu zakończonym sukcesem, następuje powrót do widoku listy lotów
        showFlightView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param flightService widok tabeli lotów
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddFlightController(@Value("classpath:/view/FlightView.fxml") Resource flightView,
                                 ApplicationContext applicationContext,
                                 FlightService flightService,
                               AirlineService airlineService){
        this.flightView = flightView;
        this.applicationContext = applicationContext;
        this.flightService = flightService;
        this.airlineService = airlineService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy lotów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showFlightView(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(flightView.getURL());

            //Spring wstrzykuje odpowiedni kontroler obsługujący dany plik .fxml na podstawie kontekstu aplikacji
            fxmlloader.setControllerFactory(applicationContext::getBean);

            //wczytanie sceny
            Parent parent = fxmlloader.load();

            //pobieramy stage z którego wywołany został actionEvent - bo nie chcemy tworzyć za każdym razem nowego Stage
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            //utworzenie i wyświetlenie sceny
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent wywołanie po nacisnięciu myszką
     * Uzupełnia ComboBox nazwami linii lotniczych
     */
    public void setComboBox(MouseEvent actionEvent) {
        //wczytanie przewoźników
        ObservableList<Airline> airlines = FXCollections.observableList(airlineService.findAll());

        //zmapowanie listy przewoźników na listę nazw przewoźników
        ObservableList<String> airlinesNames = FXCollections.observableList(airlines.stream()
                .map(airline -> airline.getName())
                .collect(Collectors.toList()));

        //ustawienie listy nazw przewoźników w ComboBox
        this.comboBox.setItems(airlinesNames);

    }
    public void getComboBoxValue(){
        this.airlineName=comboBox.getValue();
    }

}
