package pl.edu.agh.ki.lab.to.yourflights.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.utils.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/Flight")
    public List <Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping("/Flight/{id}")
    public ResponseEntity < Flight > getFlightByID(@PathVariable(value = "id") UUID flightID)
            throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightID)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + flightID));
        return ResponseEntity.ok().body(flight);
    }

    @PostMapping("/Flight")
    public Flight createFlight(@Valid @RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    @PutMapping("/Flight/{id}")
    public ResponseEntity < Flight > updateFlight(@PathVariable(value = "id") UUID flightID,
                                                      @Valid @RequestBody Flight flightDetails) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightID)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + flightID));

        flight.setPlaceOfDeparture(flightDetails.getPlaceOfDeparture());
        flight.setPlaceOfDestination(flightDetails.getPlaceOfDestination());
        flight.setAirline(flightDetails.getAirline());
        flight.setArrivalTime(flightDetails.getArrivalTime());
        flight.setDepartureTime(flightDetails.getDepartureTime());
        flight.setTicketCategories(flightDetails.getTicketCategories());
        final Flight updatedFlight = flightRepository.save(flight);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/Flight/{id}")
    public Map < String, Boolean > deleteFlight (@PathVariable(value = "id") UUID flightID)
            throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightID)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + flightID));

        flightRepository.delete(flight);
        Map < String, Boolean > response = new HashMap < > ();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}