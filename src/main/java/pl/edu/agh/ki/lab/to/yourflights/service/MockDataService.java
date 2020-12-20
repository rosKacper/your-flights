package pl.edu.agh.ki.lab.to.yourflights.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

/**
 * Serwis służący do tworzenia tymczasowych danych (zapełnienie bazy danych na starcie aplikacji, ponieważ wykorzystywana
 * jest wbudowana baza danych resetująca się przy każdym uruchomieniu aplikacji)
 */
@Service
public class MockDataService {

    private AirlineRepository airlineRepository;
    private FlightRepository flightRepository;

    public MockDataService(AirlineRepository airlineRepository,
                           FlightRepository flightRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
    }

    /**
     * Metoda dodająca do bazy przykładowy, startowy zestaw danych
     */
    public void createMockData() {
        airlineRepository.save(new Airline("LOT", "Poland", "Polskie Linie Lotnicze LOT S.A. is the flag carrier of Poland. Based in Warsaw and established on 29 December 1928, it is one of the world's oldest airlines in operation."));
        airlineRepository.save(new Airline("Lufthansa", "Germany", "Lufthansa is the largest German airline which, when combined with its subsidiaries, is the second largest airline in Europe in terms of passengers carried."));
        airlineRepository.save(new Airline("Ryanair", "Ireland", "Ryanair is an Irish Low-cost airline founded in 1984. It has headquartered in Swords, Dublin, with its primary operational bases at Dublin and London Stansted airports."));

//        flightRepository.save(new Flight("Warsaw", "Lisbon", "15/12/2020", "16/12/2020", airlineRepository.findByName("LOT")));
//        flightRepository.save(new Flight("London", "New York", "17/12/2020", "18/12/2020", airlineRepository.findByName("Lufthansa")));
//        flightRepository.save(new Flight("Miami", "New Jersey", "18/12/2020", "19/12/2020", airlineRepository.findByName("Ryanair")));
//        flightRepository.save(new Flight("Cracow", "Warsaw", "19/12/2020", "20/12/2020", airlineRepository.findByName("LOT")));
    }
}