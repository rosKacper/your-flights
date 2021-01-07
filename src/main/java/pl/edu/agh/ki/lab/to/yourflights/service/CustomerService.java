package pl.edu.agh.ki.lab.to.yourflights.service;

import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.repository.CustomerRepository;

import java.util.List;

/**
 * Klasa definiująca serwis ze Spring Data Jpa dla klientów
 * Pozwala na pobieranie/usuwanie/zapisywanie klientów
 */
@Service
public class CustomerService {

    /**
     * Repozytorium klientów
     */
    private final CustomerRepository customerRepository;

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie repozytorium
     * @param customerRepository repozytorium klientów
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Metoda zwracająca wszystkich użytkowników z bazy danych
     * @return lista wszystkich użytkowników
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * Metoda usuwająca danego klienta z bazy danych
     * @param customer klient do usunięcia
     */
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    /**
     * Metoda usuwająca danych klientów z bazy danych
     * @param customers lista klientów do usunięcia
     */
    public void deleteAll(ObservableList<Customer> customers) {
        customerRepository.deleteAll(customers);
    }

    /**
     * Metoda zapisująca klienta w bazie danych
     * @param customer klient do zapisania w bazie danych
     */
    public void save(Customer customer) {
        if(customer != null) {
            customerRepository.save(customer);
        }
    }

    /**
     * Metoda służąca do znalezienia Klienta po jego username
     * @param username nazwa użytkownika
     * @return
     */
    public List<Customer> findByUsername(String username){
        return customerRepository.findByUsername(username);
    }

}
