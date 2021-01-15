package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketOrder;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.ReservationRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.TicketCategoryRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.TicketOrderRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class TicketCategoryService {

    /**
     * Repozytorium kategorii biletów
     */
    private final TicketCategoryRepository ticketCategoryRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param ticketCategoryRepository repozytorium typu biletów
     */
    public TicketCategoryService(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    /**
     * Metoda zwracająca wszystkie kategorie biletów z bazy danych
     * @return lista wszystkich kategorii biletów
     */
    public List<TicketCategory> findAll() {
        return ticketCategoryRepository.findAll();
    }

    public List<TicketCategory> findByFlight(Flight flight) {
        if(flight == null) return new LinkedList<>();
        return ticketCategoryRepository.findAllByFlight(flight);
    }


    /**
     * Metoda usuwająca daną kategorię z bazy danych
     * @param ticketCategory kategoria do usunięcia
     */
    public void delete(TicketCategory ticketCategory) {
        ticketCategoryRepository.delete(ticketCategory);
    }

    /**
     * Metoda usuwająca dane kategorie z bazy danych
     * @param ticketCategories lista typów biletów do usunięcia
     */
    public void deleteAll(ObservableList<TicketCategory> ticketCategories) {
        ticketCategoryRepository.deleteAll(ticketCategories);
    }

    /**
     * Metoda zapisująca zamówienie w bazie danych
     * @param ticketCategory zamówienie do zapisania w bazie danych
     */
    public boolean save(TicketCategory ticketCategory) {
        if(ticketCategory != null) {
            ticketCategoryRepository.save(ticketCategory);
        }
        return false;
    }

}