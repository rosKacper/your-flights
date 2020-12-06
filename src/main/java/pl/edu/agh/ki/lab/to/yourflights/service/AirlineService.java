package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla przewoźników
 * Pozwala na pobieranie przewoźników
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class AirlineService {

    /**
     * Repozytorium przewoźników
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     */
    private final AirlineRepository airlineRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     * @param airlineRepository repozytorium przewoźników
     */
    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    /**
     * Tymczasowa lista przewoźników, dopóki nie ma zapisywania do bazy danych
     */
    public static ObservableList<Airline> airlines = FXCollections.observableArrayList();

    /**
     * Metoda zwracająca tymczasową listę przwoźników
     * @return lista przewoźników
     */
    public ObservableList<Airline> getMockData() {
        return airlines;
    }

    /**
     * Metoda dodająca przewoźnika do tymczasowej listy
     * @param airline przewoźnik do dodania
     */
    public static void addAirline(Airline airline){
        airlines.add(airline);
    }
}
