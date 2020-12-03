package pl.edu.agh.ki.lab.to.yourflights.model.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {

}
