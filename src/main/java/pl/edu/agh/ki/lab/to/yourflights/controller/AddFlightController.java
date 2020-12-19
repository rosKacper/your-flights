package pl.edu.agh.ki.lab.to.yourflights.controller;

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
import org.springframework.stereotype.Repository;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
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
    public ComboBox<String> airlineName;



    @FXML
    public DatePicker departureTime, arrivalTime ;
    @FXML
    public Label placeOfDestinationValidationLabel, airlineNameValidationLabel;
    @FXML
    public Label departureTimeValidationLabel;
    @FXML
    public Label arrivalTimeValidationLabel;
    @FXML
    public Label placeOfDepartureValidationLabel;
    @FXML
    public Text actiontarget;
    @FXML
    public Label placeOfDestinationValidation, airlineNameValidation;
    @FXML
    public Label placeOfDepartureValidation;
    @FXML
    public Label departureTimeValidation;
    @FXML
    public Label arrivalTimeValidation;
    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String date_blocker = "31/12/9000";
    DatePicker blocker=new DatePicker();


    private FlightService flightService;
    private Flight flight;



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
        departureTime.setValue(LocalDate.parse( flight.getDepartureTime(),formatter));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        arrivalTime.setValue(LocalDate.parse( flight.getArrivalTime(),formatter));
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());

    }

    /**
     * Metoda aktualizująca lotu po edycji
     */
    private void updateModel() {
        flight.setPlaceOfDeparture(placeOfDeparture.textProperty().getValue());
        flight.setPlaceOfDestination(placeOfDestination.textProperty().getValue());
        flight.setArrivalTime(arrivalTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setDepartureTime(departureTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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
        boolean departureTimeValidation = Validator.validateDate(departureTime,arrivalTime, departureTimeValidationLabel);
        boolean arrivalTimeValidation = Validator.validateDate(arrivalTime, blocker, arrivalTimeValidationLabel);


        if(!placeOfDestinationValidation || !placeOfDepartureValidation || !departureTimeValidation || !arrivalTimeValidation ){
            return;
        }

        //Stworzenie nowego lotu i wyczyszczenie pól formularza
        if (flight == null) {
            flight = new Flight(placeOfDeparture.getText(),placeOfDestination.getText(),departureTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),arrivalTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), null);
        } else {
            updateModel();
        }

        flightService.save(flight);
        actiontarget.setText("Flight added successfully!");
        placeOfDeparture.clear();
        placeOfDestination.clear();
        departureTime=null;
        arrivalTime=null;
        flight=null;


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
     * Uzupełnia ComboBox z nazwami linii lotniczych danymi
     */
    public void setComboBox(MouseEvent actionEvent) {
        //wczytanie przewoźników
        ObservableList<Airline> airlines = FXCollections.observableList(airlineService.findAll());

        //zmapowanie listy przewoźników na listę nazw przewoźników
        ObservableList<String> airlinesNames = FXCollections.observableList(airlines.stream()
                .map(airline -> airline.getName())
                .collect(Collectors.toList()));

        //ustawienie listy nazw przewoźników w ComboBox
        this.airlineName.setItems(airlinesNames);
    }
}
