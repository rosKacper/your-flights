package pl.edu.agh.ki.lab.to.yourflights.controller;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Kontroler obsługujący formularz do dodawania rezerwacji
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddReservationController {

    /**
     * Widok lotów
     */
    private final Resource flightView;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    private final ReservationService reservationService;

    /**
     * Pola formularza
     */
    @FXML
    public TextField placeOfDestination,placeOfDeparture, departureTime,arrivalTime;

    @FXML
    public ComboBox<Integer> numberOfSeats;

    private Flight flight;

    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Metoda ustawiająca lotów dla którego zostanie utworzona rezerwacja
     * @param flight lot dla rezerwacji
     */
    public void setData(Flight flight) {
        this.flight = flight;
        updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanego lotu
     */
    private void updateControls() {
        departureTime.textProperty().setValue(String.valueOf(LocalDate.parse( flight.getDepartureTime(),formatter)));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        arrivalTime.textProperty().setValue(String.valueOf(LocalDate.parse( flight.getArrivalTime(),formatter)));
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());
        numberOfSeats.getItems().setAll(
                IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList())
        );

    }

    /**
     * Metoda obsługująca dodawanie lotu po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych

        if(numberOfSeats.getValue() < 1 || numberOfSeats.getValue() > 50 ){
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //Stworzenie nowego lotu i wyczyszczenie pól formularza
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Reservation reservation = new Reservation(LocalDate.parse(new Date().toString(), formatter).toString()
        , null, );
        reservationService.save(reservation);

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
     * @param reservationService serwis zapisujący rezerwacje
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddReservationController(@Value("classpath:/view/FlightView.fxml") Resource flightView,
                               ApplicationContext applicationContext,
                               ReservationService reservationService)
    {
        this.flightView = flightView;
        this.applicationContext = applicationContext;
        this.reservationService = reservationService;
    }





}

/**
 * Kontroler obsługujący formularz do dodawania lotów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddFlightController {

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
}
