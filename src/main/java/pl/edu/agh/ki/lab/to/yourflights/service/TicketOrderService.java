package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.ReservationRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.TicketOrderRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class TicketOrderService {

    /**
     * Repozytorium zamówień biletów
     */
    private final TicketOrderRepository ticketOrderRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param ticketOrderRepository repozytorium zamówień biletów
     */
    public TicketOrderService(TicketOrderRepository ticketOrderRepository) {
        this.ticketOrderRepository = ticketOrderRepository;
    }

    /**
     * Metoda zwracająca wszystkie zamówienie biletów z bazy danych
     * @return lista wszystkich przewoźników
     */
    public List<TicketOrder> findAll() {
        return ticketOrderRepository.findAll();
    }

    /**
     * Metoda zwracająca zamówienia biletów należące do jednej rezerwacji
     * @param reservation Rezerwacja
     * @return lista zamówień
     */
    public List<TicketOrder> findByReservation(Reservation reservation) {
        return ticketOrderRepository.findByReservation(reservation);
    }

    /**
     * Metoda zwracająca zamówienia biletów należące do danej kategorii biletów danego lotu
     * @param ticketCategory kategoria biletu
     * @return lista zamówień
     */
    public List<TicketOrder> findByTicketCategory(TicketCategory ticketCategory) {
        return ticketOrderRepository.findByTicketCategory(ticketCategory);
    }

    /**
     * Metoda usuwająca daną rezerwację z bazy danych
     * @param ticketOrder zamówienie biletów do usunięcia
     */
    public void delete(TicketOrder ticketOrder) {
        ticketOrderRepository.delete(ticketOrder);
    }

    /**
     * Metoda usuwająca danych przewoźników z bazy danych
     * @param ticketOrders lista zamówień biletów do usunięcia
     */
    public void deleteAll(ObservableList<TicketOrder> ticketOrders) {
        ticketOrderRepository.deleteAll(ticketOrders);
    }

    /**
     * Metoda zapisująca zamówienie w bazie danych
     * @param ticketOrder zamówienie do zapisania w bazie danych
     */
    public boolean save(TicketOrder ticketOrder) {
        if(ticketOrder != null) {
            ticketOrderRepository.save(ticketOrder);
        }
        return false;
    }

    public boolean saveAll(List<TicketOrder> ticketOrders) {
        if(ticketOrders.size() != 0) {
            ticketOrderRepository.saveAll(ticketOrders);
        }
        return false;
    }

    /**
     * Metoda zwracająca sumaryczny koszt danego zamówienia na bilety
     * @param ticketOrder zamówienie na bilety
     * @return
     */
    public BigDecimal getTicketOrderSummaryCost(TicketOrder ticketOrder) {
        TicketDiscount ticketDiscount = ticketOrder.getTicketDiscount();
        if(ticketDiscount == null) {
            return ticketOrder.getTicketCategory().getCategoryPrice()
                    .multiply(BigDecimal.valueOf(ticketOrder.getNumberOfSeats()))
                    .setScale(2); //totalPrice = categoryPrice * numberOfSeats
        } else {
            return ticketOrder.getTicketCategory().getCategoryPrice()
                    .multiply(BigDecimal.valueOf(ticketOrder.getNumberOfSeats())) //price = categoryPrice * numberOfSeats
                    .multiply(BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(ticketDiscount.getDiscount()).divide(new BigDecimal(100))))//totalPrice = price * ( 1 - discount/100)
                    .setScale(2);
        }
    }
}