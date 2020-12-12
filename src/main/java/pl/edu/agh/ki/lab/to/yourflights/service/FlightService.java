package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class FlightService {

    /**
     * Repozytorium lotów
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     */
    private final FlightRepository flightRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     * @param flightRepository repozytorium lotów
     */
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Tymczasowa lista lotów, dopóki nie ma zapisywania do bazy danych
     */
    public static ObservableList<Flight> flights = FXCollections.observableArrayList();

    /**
     * Metoda dodająca loty do tymczasowej listy
     */
    public static void addFlight(Flight flight){
        flights.add(flight);
    }

    /**
     * Metoda zwracająca klientów z tymczasowej listy
     */
    public ObservableList<Flight> getMockData() {
        return flights;
    }
}