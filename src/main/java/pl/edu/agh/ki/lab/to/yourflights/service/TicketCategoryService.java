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

import java.util.List;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class TicketCategoryService {

    /**
     * Repozytorium zamówień biletów
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
     * Metoda zwracająca wszystkie zamówienie biletów z bazy danych
     * @return lista wszystkich przewoźników
     */
    public List<TicketCategory> findAll() {
        return ticketCategoryRepository.findAll();
    }

    /**
     * Metoda zwracająca zamówienia biletów należące do jednej rezerwacji
     * @param id kategoria biletów
     * @return lista kategorii biletów
     */
    public List<TicketCategory> findById(Long id) {
        return ticketCategoryRepository.findById(id);
    }

    /**
     * Metoda usuwająca daną rezerwację z bazy danych
     * @param ticketCategory typ biletów do usunięcia
     */
    public void delete(TicketCategory ticketCategory) {
        ticketCategoryRepository.delete(ticketCategory);
    }

    /**
     * Metoda usuwająca danych przewoźników z bazy danych
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