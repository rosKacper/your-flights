package pl.edu.agh.ki.lab.to.yourflights.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.User;

import java.util.UUID;

/**
 * Klasa definiująca repozytorium ze Spring Data Jpa dla przewoźników
 * Na późniejszym etapie będzie służyć do pobierania danych z bazy danych
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    //Znajduje linie lotniczą po nazwie
    Airline findByName(String name);
    Airline findByUser(User user);
}