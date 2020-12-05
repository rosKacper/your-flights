package pl.edu.agh.ki.lab.to.yourflights.service;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public static ObservableList<Customer> customers = FXCollections.observableArrayList();


    /*
    //method returning example data for testing purposes
    public ObservableList<Customer> getMockData() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        Customer customer1 = new Customer("Adam", "Małysz", "Polska", "Wisła", "Wyzwolenia", "43-460", "102102102", "lecadam@lec.com");
        Customer customer2 = new Customer("Janne", "Ahonnen", "Finlandia", "Kuopio", "Suokatu", "70110", "103103103", "lecajanne@lec.com");

        customers.add(customer1);
        customers.add(customer2);

        return customers;
    }
    */
    //method adding customer to list
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }

    //method returning data
    public ObservableList<Customer> getMockData() {
        return customers;
    }
}
