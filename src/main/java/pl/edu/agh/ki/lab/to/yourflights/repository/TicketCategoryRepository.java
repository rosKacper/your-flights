package pl.edu.agh.ki.lab.to.yourflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;

import java.util.Optional;

/**
 * Klasa definiująca repozytorium ze Springa5ea Data Jpa dla klientów
 * Na późniejszym etapie będzie służyć do pobierania danych z bazy danych
 */
@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    Optional<TicketCategory> findById(Long id);
}