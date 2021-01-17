package pl.edu.agh.ki.lab.to.yourflights.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.repository.AirlineRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.CustomerRepository;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;

import java.math.BigDecimal;

/**
 * Serwis służący do tworzenia tymczasowych danych (zapełnienie bazy danych na starcie aplikacji, ponieważ wykorzystywana
 * jest wbudowana baza danych resetująca się przy każdym uruchomieniu aplikacji)
 */
@Service
public class MockDataService {

    private AirlineRepository airlineRepository;
    private FlightRepository flightRepository;
    private CustomerRepository customerRepository;

    public MockDataService(AirlineRepository airlineRepository,
                           FlightRepository flightRepository,
                            CustomerRepository customerRepository) {
        this.airlineRepository = airlineRepository;
        this.flightRepository = flightRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Metoda dodająca do bazy przykładowy, startowy zestaw danych
     */
    public void createMockData() {
//        customerRepository.save(new Customer("Adam", "Malysz", "Poland", "Wisla", "Warszawska", "43-460", "102102102", "lec@adamlec.pl", "user"));

//        airlineRepository.save(new Airline("LOT", "Poland", "Polskie Linie Lotnicze LOT S.A. is the flag carrier of Poland. Based in Warsaw and established on 29 December 1928, it is one of the world's oldest airlines in operation."));
//        airlineRepository.save(new Airline("Lufthansa", "Germany", "Lufthansa is the largest German airline which, when combined with its subsidiaries, is the second largest airline in Europe in terms of passengers carried."));
//        airlineRepository.save(new Airline("Ryanair", "Ireland", "Ryanair is an Irish Low-cost airline founded in 1984. It has headquartered in Swords, Dublin, with its primary operational bases at Dublin and London Stansted airports."));

        Flight flight1 = new Flight("Warsaw", "Lisbon", "25/12/2020", "25/12/2020", airlineRepository.findByName("LOT"), "12:30", "15:45");
        TicketCategory ticketCategory1 = new TicketCategory("normal", new BigDecimal(10), 80, flight1);
        flight1.getTicketCategories().add(ticketCategory1);
        flightRepository.save(flight1);

        Flight flight2 = new Flight("London", "New York", "26/12/2020", "27/12/2020", airlineRepository.findByName("Lufthansa"), "18:00", "02:00");
        TicketCategory ticketCategory2 = new TicketCategory("normal", new BigDecimal(10), 80, flight2);
        flight2.getTicketCategories().add(ticketCategory2);
        flightRepository.save(flight2);

        Flight flight3 = new Flight("Miami", "New Jersey", "27/12/2020", "27/12/2020", airlineRepository.findByName("Ryanair"), "11:00", "13:35");
        TicketCategory ticketCategory3 = new TicketCategory("normal", new BigDecimal(10), 80, flight3);
        flight3.getTicketCategories().add(ticketCategory3);
        flightRepository.save(flight3);

        Flight flight4 = new Flight("Cracow", "Warsaw", "28/12/2020", "28/12/2020", airlineRepository.findByName("LOT"), "17:30", "18:20");
        TicketCategory ticketCategory4 = new TicketCategory("normal", new BigDecimal(10), 80, flight4);
        flight4.getTicketCategories().add(ticketCategory4);
        flightRepository.save(flight4);

    }
}