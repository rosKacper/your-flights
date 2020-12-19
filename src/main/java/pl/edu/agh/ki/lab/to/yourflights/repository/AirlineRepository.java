package pl.edu.agh.ki.lab.to.yourflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;

import java.util.UUID;

/**
 * Klasa definiująca repozytorium ze Spring Data Jpa dla przewoźników
 * Służy do pobierania danych z bazy danych
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {
    Airline findByName(String name);
}
