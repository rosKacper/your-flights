package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * Kontroler obsługujący formularz do dodawania klientów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddFlightController {

    /**
     * Widok klientów
     */
    private final Resource flightView;

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
    public DatePicker departureTime, arrivalTime ;
    //DatePicker
    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */

    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public Label firstNameValidationLabel, lastNameValidationLabel,
            countryValidationLabel, cityValidationLabel, streetValidationLabel,
            postalCodeValidationLabel, phoneNumberValidationLabel, emailAddressValidationLabel;

    /**
     * Etykieta do wyświetlania komunikatu o sukcesie dodawania klienta
     */
    @FXML
    public Text actiontarget;

    private FlightService flightService;
    private Flight flight;

    /**
     * Metoda ustawiająca klienta do edycji
     * @param flight lot do edycji, może być nullem
     */
    public void setData(Flight flight) {
        this.flight = flight;
        updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanego klienta do edycji
     */
    private void updateControls() {
        departureTime.setValue(LocalDate.parse( flight.getDepartureTime(),formatter));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        arrivalTime.setValue(LocalDate.parse( flight.getArrivalTime(),formatter));
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());

    }

    /**
     * Metoda aktualizująca klienta po edycji
     */
    private void updateModel() {
        flight.setPlaceOfDeparture(placeOfDeparture.textProperty().getValue());
        flight.setPlaceOfDestination(placeOfDestination.textProperty().getValue());
        flight.setArrivalTime(arrivalTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        flight.setDepartureTime(departureTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

    }

    /**
     * Metoda obsługująca dodawanie klienta po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {


        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        //boolean firstNameValidation = Validator.validateNotEmpty(firstName, firstNameValidationLabel);
        //boolean lastNameValidation = Validator.validateNotEmpty(lastName, lastNameValidationLabel);
        //boolean countryValidation = Validator.validateNotEmpty(country, countryValidationLabel);
        //boolean cityValidation = Validator.validateNotEmpty(city, cityValidationLabel);

        //if(!firstNameValidation || !lastNameValidation || !countryValidation || !cityValidation {
        //    return;
        //}

        //Stworzenie nowego klienta i wyczyszczenie pól formularza
        if (flight == null) {
            flight = new Flight(placeOfDeparture.getText(),placeOfDestination.getText(),departureTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),arrivalTime.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), null);
        } else {
            updateModel();
        }

        flightService.save(flight);
        placeOfDeparture.clear();
        placeOfDestination.clear();
        //departureTime=null;
        //arrivalTime=null;


        //Po dodaniu klienta zakończonym sukcesem, następuje powrót do widoku listy klientów
        showFlightView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param flightService widok tabeli lotów
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddFlightController(@Value("classpath:/view/FlightView.fxml") Resource flightView,
                                 ApplicationContext applicationContext,
                                 FlightService flightService){
        this.flightView = flightView;
        this.applicationContext = applicationContext;
        this.flightService = flightService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy klientów
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
}
