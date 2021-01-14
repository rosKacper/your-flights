package pl.edu.agh.ki.lab.to.yourflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;

import java.util.List;

/**
 * Klasa definiująca repozytorium ze Springa5ea Data Jpa dla klientów
 * Na późniejszym etapie będzie służyć do pobierania danych z bazy danych
 */
@Repository
public interface TicketDiscountRepository extends JpaRepository<TicketDiscount, Long> {
    List<TicketDiscount> findAllByName(String name);
}
