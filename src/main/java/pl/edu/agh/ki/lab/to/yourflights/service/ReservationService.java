package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.Reservation;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketOrder;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.ReservationRepository;

import java.util.List;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla lotów
 * Pozwala na pobieranie lotów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class ReservationService {

    /**
     * Repozytorium rezerwacji
     */
    private final ReservationRepository reservationRepository;
    private final TicketOrderService ticketOrderService;


    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param reservationRepository repozytorium rezerwacji
     */
    public ReservationService(ReservationRepository reservationRepository, TicketOrderService ticketOrderService) {
        this.reservationRepository = reservationRepository;
        this.ticketOrderService = ticketOrderService;
    }

    /**
     * Metoda zwracająca wszystkich przewoźników z bazy danych
     * @return lista wszystkich przewoźników
     */
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    /**
     * Metoda zwracająca rezerwacje danego użytkownika
     * @param userName nazwa użytkownika
     * @return lista zamówień
     */
    public List<Reservation> findByUserName(String userName) {
        return reservationRepository.findByUserName(userName);
    }

    /**
     * Metoda usuwająca daną rezerwację z bazy danych
     * @param reservation rezerwacja do usunięcia
     */
    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    /**
     * Metoda usuwająca danych przewoźników z bazy danych
     * @param reservations lista rezerwacji do usunięcia
     */
    public void deleteAll(ObservableList<Reservation> reservations) {
        reservationRepository.deleteAll(reservations);
    }

    /**
     * Metoda zapisująca przewoźnika w bazie danych
     * @param reservation rezerwacja do zapisania w bazie danych
     */
    public boolean save(Reservation reservation) {
        if(reservation != null) {
            List<Reservation> reservations = findByUserName(reservation.getUserName());
            boolean hasNoCollidingReservations = reservations.stream()
                    .noneMatch(existingReservation -> existingReservation.getReservationDate().equals(reservation.getReservationDate()));
            if(hasNoCollidingReservations) {
                reservationRepository.save(reservation);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}