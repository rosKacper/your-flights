package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.ReservationRepository;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class ReservationService {

    /**
     * Repozytorium lotów
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     */
    private final ReservationRepository reservationRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     * @param ReservationRepository repozytorium lotów
     */
    public ReservationService(ReservationRepository ReservationRepository) {
        this.reservationRepository = ReservationRepository;
    }

    /**
     * Tymczasowa lista lotów, dopóki nie ma zapisywania do bazy danych
     */
    public static ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    /**
     * Metoda dodająca loty do tymczasowej listy
     */
    public static void addFlight(Reservation reservation){
        reservations.add(reservation);
    }

    /**
     * Metoda zwracająca klientów z tymczasowej listy
     */
    public ObservableList<Reservation> getMockData() {
        return reservations;
    }
}