package pl.edu.agh.ki.lab.to.yourflights.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;

@Service
public class MockDataService {

    private AirlineRepository airlineRepository;

    public MockDataService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public void createMockData() {
        airlineRepository.save(new Airline("LOT", "Poland", "Polskie Linie Lotnicze LOT S.A. is the flag carrier of Poland. Based in Warsaw and established on 29 December 1928, it is one of the world's oldest airlines in operation."));
        airlineRepository.save(new Airline("Lufthansa", "Germany", "Lufthansa is the largest German airline which, when combined with its subsidiaries, is the second largest airline in Europe in terms of passengers carried."));
        airlineRepository.save(new Airline("Ryanair", "Ireland", "Ryanair is an Irish Low-cost airline founded in 1984. It has headquartered in Swords, Dublin, with its primary operational bases at Dublin and London Stansted airports."));
    }
}