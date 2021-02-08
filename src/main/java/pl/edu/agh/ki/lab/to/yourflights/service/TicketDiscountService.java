package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class TicketDiscountService {

    /**
     * Repozytorium zniżek
     */
    private final TicketDiscountRepository ticketDiscountRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param ticketDiscountRepository repozytorium zniżek
     */
    public TicketDiscountService(TicketDiscountRepository ticketDiscountRepository) {
        this.ticketDiscountRepository = ticketDiscountRepository;
    }

    /**
     * Metoda zwracająca wszystkie zniżki z bazy danych
     * @return lista wszystkich zniżek
     */
    public List<TicketDiscount> findAll() {
        return ticketDiscountRepository.findAll();
    }

    public List<TicketDiscount> findByName(String name) {
        return ticketDiscountRepository.findAllByName(name);
    }


    /**
     * Metoda usuwająca daną zniżkę z  bazy danych
     * @param ticketDiscount zniżkę
     */
    public void delete(TicketDiscount ticketDiscount) {
        ticketDiscountRepository.delete(ticketDiscount);
    }

    /**
     * Metoda usuwająca dane zniżki z bazy danych
     * @param ticketDiscounts lista zniżek do usunięcia
     */
    public void deleteAll(ObservableList<TicketDiscount> ticketDiscounts) {
        ticketDiscountRepository.deleteAll(ticketDiscounts);
    }

    /**
     * Metoda zapisująca zniżkę w bazie danych
     * @param ticketDiscount zamówienie do zapisania w bazie danych
     */
    public boolean save(TicketDiscount ticketDiscount) {
        if(ticketDiscount != null) {
            ticketDiscountRepository.save(ticketDiscount);
        }
        return false;
    }

}