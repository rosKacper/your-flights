package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.repository.CustomerRepository;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla klientów
 * Pozwala na pobieranie klientów
 * Na późniejszym etapie będzie służyć do pośrednictwa pomiędzy modelem a repozytorium
 */
@Service
public class CustomerService {

    /**
     * Repozytorium klientów
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     */
    private final CustomerRepository customerRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * Nie jest na razie wykorzystywane, ponieważ nie ma połączenia z bazą danych
     * @param customerRepository repozytorium klientów
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Tymczasowa lista klientów, dopóki nie ma zapisywania do bazy danych
     */
    public static ObservableList<Customer> customers = FXCollections.observableArrayList();

    /**
     * Metoda dodająca klienta do tymczasowej listy
     */
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }

    /**
     * Metoda zwracająca klientów z tymczasowej listy
     */
    public ObservableList<Customer> getMockData() {
        return customers;
    }
}
