package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

import java.util.List;

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
     * Metoda zwracająca wszystkie loty z bazy danych
     * @return lista wszystkich lotów
     */
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    /**
     * Metoda usuwająca dany lot z bazy danych
     * @param flight lot do usunięcia
     */
    public void delete(Flight flight) {
        flightRepository.delete(flight);
    }

    /**
     * Metoda usuwająca dane loty z bazy danych
     * @param flights lista lotów do usunięcia
     */
    public void deleteAll(ObservableList<Flight> flights) {
        flightRepository.deleteAll(flights);
    }

    /**
     * Metoda zapisująca lot w bazie danych
     * @param flight lot do zapisania w bazie danych
     */
    public void save(Flight flight) {
        if(flight != null) {
            flightRepository.save(flight);
        }
    }
}