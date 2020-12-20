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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketOrder;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketOrderService;
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
     * Widok rezerwacji
     */
    private final Resource reservationList;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    private final ReservationService reservationService;

    private final TicketOrderService ticketOrderService;

    /**
     * Pola formularza
     */
    @FXML
    public TextField placeOfDestination, placeOfDeparture, departureTime;

    @FXML
    public ComboBox<Integer> seats;

    private Flight flight;

    private Reservation reservation = null;

    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Metoda ustawiająca lotów dla którego zostanie utworzona rezerwacja
     * @param flight lot dla rezerwacji
     */
    public void setData(Flight flight) {
        this.flight = flight;
        this.updateControls();
    }

    /**
     * Metoda modyfikująca istniejącą rezerwację
     * @param flight lot dla rezerwacji
     * @param reservation lot dla rezerwacji
     */
    public void setData(Flight flight, Reservation reservation) {
        this.flight = flight;
        this.reservation = reservation;
        this.updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanego lotu
     */
    private void updateControls() {

        departureTime.textProperty().setValue(String.valueOf(LocalDate.parse( flight.getDepartureDate(),formatter)));
        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());

        if(reservation != null) {
            seats.setValue(reservation.getTicketOrders().get(0).getNumberOfSeats());
        }
        seats.getItems().setAll(
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

        if(seats.getValue() < 1 || seats.getValue() > 50 ){
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //Stworzenie nowego lotu i wyczyszczenie pól formularza
        if(reservation == null) {
            // Tworzymy rezerwację i zamówienie biletów dla niej
            Reservation reservation = new Reservation(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    null,

                    userName);
            TicketOrder ticketOrder = new TicketOrder(seats.getValue(), null, reservation, flight.getTicketCategories().get(0));
            reservation.getTicketOrders().add(ticketOrder);

            // Zapisujemy w bazie odpowiednie relacje

            reservationService.save(reservation);
        }
        else {
            reservation.getTicketOrders().get(0).setNumberOfSeats(seats.getValue());
        }

        //Po dodaniu lotu zakończonym sukcesem, następuje powrót do widoku listy lotów
        showReservationList(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param reservationService serwis zapisujący rezerwacje
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddReservationController(@Value("classpath:/view/ReservationListView.fxml") Resource reservationList,
                               ApplicationContext applicationContext,
                               TicketOrderService ticketOrderService,
                               ReservationService reservationService)
    {
        this.reservationList = reservationList;
        this.applicationContext = applicationContext;
        this.reservationService = reservationService;
        this.ticketOrderService = ticketOrderService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy rezerwacji
     * @param actionEvent event emitowany przez przycisk
     */
    public void showReservationList(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(reservationList.getURL());

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

