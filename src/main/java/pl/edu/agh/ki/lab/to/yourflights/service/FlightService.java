package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketOrder;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class FlightService {

    /**
     * Repozytorium lotów
     */
    private final FlightRepository flightRepository;

    private final TicketCategoryService ticketCategoryService;
    private final TicketOrderService ticketOrderService;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     * @param flightRepository repozytorium lotów
     * @param ticketCategoryService
     * @param ticketOrderService
     */
    public FlightService(FlightRepository flightRepository,
                         TicketCategoryService ticketCategoryService,
                         TicketOrderService ticketOrderService) {
        this.flightRepository = flightRepository;
        this.ticketCategoryService = ticketCategoryService;
        this.ticketOrderService = ticketOrderService;
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

    /**
     * Metoda znajdująca loty podanej linii lotniczej
     * @param airline linia lotnicza do wyszukania lotu w bazie danych
     */
    public List<Flight> findByAirline(Airline airline){
        return flightRepository.findByAirline(airline);
    }

    /**
     * Metoda obliczająca liczbę zajętych miejsc na dany lot
     * @param flight lot
     * @return
     */
    public int getNumberOfTakenSeatsForFlight(Flight flight) {
        int numberOfTakenSeats = 0;
        List<TicketCategory> ticketCategories = ticketCategoryService.findByFlight(flight);
        for(TicketCategory ticketCategory : ticketCategories) {
            List<TicketOrder> ticketOrders = ticketOrderService.findByTicketCategory(ticketCategory);
            for(TicketOrder ticketOrder : ticketOrders) {
                if(ticketOrder.getTicketCategory().getId() == ticketCategory.getId()){
                    numberOfTakenSeats += ticketOrder.getNumberOfSeats();
                }
            }
        }
        return numberOfTakenSeats;
    }

    /**
     * Metoda obliczająca liczbę wszystkich miejsc na dany lot
     * @param flight lot
     * @return
     */
    public int getNumberOfSeatsForFlight(Flight flight) {
        int numberOfSeats = 0;
        List<TicketCategory> ticketCategories = ticketCategoryService.findByFlight(flight);
        for(TicketCategory ticketCategory : ticketCategories) {
            numberOfSeats += ticketCategory.getTotalNumberOfSeats();
        }
        return numberOfSeats;
    }

    /**
     * Metoda obliczająca liczbę rezerwacji na dany lot
     * @param flight lot
     * @return
     */
    public int getNumberOfReservationsForFlight(Flight flight) {
        List<Long> reservations = new LinkedList<>();
        List<TicketCategory> ticketCategories = ticketCategoryService.findByFlight(flight);
        for(TicketCategory ticketCategory : ticketCategories) {
            List<TicketOrder> ticketOrders = ticketOrderService.findByTicketCategory(ticketCategory);
            for(TicketOrder ticketOrder : ticketOrders) {
                reservations.add(ticketOrder.getReservation().getId());
            }
        }
        return (int)reservations.stream().distinct().count();
    }


    /**
     * Metoda obliczająca sumaryczne zarobki przewoźnika za dany lot
     * @param flight lot
     * @return
     */
    public BigDecimal getTotalIncomeFromFlight(Flight flight) {
        BigDecimal totalIncome = new BigDecimal(0);
        List<TicketCategory> ticketCategories = ticketCategoryService.findByFlight(flight);
        for(TicketCategory ticketCategory : ticketCategories) {
            List<TicketOrder> ticketOrders = ticketOrderService.findByTicketCategory(ticketCategory);
            for(TicketOrder ticketOrder : ticketOrders) {
                totalIncome = totalIncome.add(ticketOrderService.getTicketOrderSummaryCost(ticketOrder));
            }
        }
        return totalIncome;
    }

    public List<Flight> getFlightsSortedDescendingBasedOnNumberOfReservations() {
        Map<Flight, Integer> map = new HashMap<>();
        findAll().stream().forEach(flight -> map.put(flight, getNumberOfReservationsForFlight(flight)));
        Map<Flight, Integer> result = map.entrySet()
                .stream()
                .sorted(Map.Entry.<Flight, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return new ArrayList<>(result.keySet());
    }

    public List<String> getFlightDestinationsSortedDescendingBasedOnNumberOfReservations() {
        List<Flight> flights = getFlightsSortedDescendingBasedOnNumberOfReservations();
        return flights.stream().map(flight -> flight.getPlaceOfDestination()).distinct().collect(Collectors.toList());
    }
}