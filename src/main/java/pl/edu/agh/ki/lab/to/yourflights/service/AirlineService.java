package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.ObservableList;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla przewoźników
 * Pozwala na pobieranie/usuwanie/zapisywanie przewoźników
 */
@Service
public class AirlineService {

    /**
     * Repozytorium przewoźników
     */
    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;
    private TicketCategoryService ticketCategoryService;
    private TicketOrderService ticketOrderService;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param airlineRepository repozytorium przewoźników
     */
    public AirlineService(AirlineRepository airlineRepository, FlightRepository flightRepository,
                            TicketCategoryService ticketCategoryService, TicketOrderService ticketOrderService) {

        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
        this.ticketCategoryService = ticketCategoryService;
        this.ticketOrderService = ticketOrderService;
    }

    /**
     * Metoda zwracająca wszystkich przewoźników z bazy danych
     * @return lista wszystkich przewoźników
     */
    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    /**
     *
     * @param value nazwa linii lotniczej
     * @return obiekt Airline o podanej nazwie
     */
    public Airline findByName(String value){ return airlineRepository.findByName(value);}

    /**
     *
     * @param user użytkownik powiązany z linią lotniczą
     * @return obiekt Airline o podanym użytkowniku
     */
    public Airline findByUser(User user){
        return airlineRepository.findByUser(user);
    }

    /**
     * Metoda usuwająca danego przewoźnika z bazy danych
     * @param airline przewoźnik do usunięcia
     */
    public void delete(Airline airline) {
        airlineRepository.delete(airline);
    }

    /**
     * Metoda usuwająca danych przewoźników z bazy danych
     * @param airlines lista przewoźników do usunięcia
     */
    public void deleteAll(ObservableList<Airline> airlines) {
        airlineRepository.deleteAll(airlines);
    }

    /**
     * Metoda zapisująca przewoźnika w bazie danych
     * @param airline przewoźnik do zapisania w bazie danych
     */
    public void save(Airline airline) {
        if(airline != null) {
            airlineRepository.save(airline);
        }
    }

    /**
     * Metoda służąca do pobrania listy wszystkich krajów
     * @return lista wszystkich krajów
     */
    public List<String> getCountries() {
        return this.findAll().stream().map(airline -> airline.getCountry()).distinct().collect(Collectors.toList());
    }


    public List<Reservation> getReservationsForAirline(Airline airline) {
        List<Reservation> reservations = new LinkedList<>();
        List<Flight> flights = flightRepository.findByAirline(airline);
        for(Flight flight: flights){
            List<TicketCategory> ticketCategories = ticketCategoryService.findByFlight(flight);
            for(TicketCategory ticketCategory : ticketCategories) {
                List<TicketOrder> ticketOrders = ticketOrderService.findByTicketCategory(ticketCategory);
                for(TicketOrder ticketOrder : ticketOrders) {
                    reservations.add(ticketOrder.getReservation());
                }
            }
        }
        return reservations.stream().distinct().collect(Collectors.toList());
    }

}
