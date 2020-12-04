package pl.edu.agh.ki.lab.to.yourflights.model.customers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.airline.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.airline.AirlineRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //method returning example data for testing purposes
    public ObservableList<Customer> getMockData() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        Customer customer1 = new Customer("Adam", "Małysz", "Polska", "Wisła", "Wyzwolenia", "43-460", "102102102", "lecadam@lec.com");
        Customer customer2 = new Customer("Janne", "Ahonnen", "Finlandia", "Kuopio", "Suokatu", "70110", "103103103", "lecajanne@lec.com");

        customers.add(customer1);
        customers.add(customer2);

        return customers;
    }

}
