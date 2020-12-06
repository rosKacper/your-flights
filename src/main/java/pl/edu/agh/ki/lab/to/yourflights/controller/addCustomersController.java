package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;


/**
 * Kontroler obsługujący formularz do dodawania klientów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class addCustomersController {

    /**
     * Widok klientów
     */
    private final Resource customerView;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
    @FXML
    public TextField firstName, lastName, country,city, street, postalCode, phoneNumber, emailAddress;

    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */
    @FXML
    public Label firstNameValidationLabel, lastNameValidationLabel,
            countryValidationLabel, cityValidationLabel, streetValidationLabel,
            postalCodeValidationLabel, phoneNumberValidationLabel, emailAddressValidationLabel;

    /**
     * Etykieta do wyświetlania komunikatu o sukcesie dodawania klienta
     */
    @FXML
    public Text actiontarget;


    /**
     * Metoda obsługująca dodawanie klienta po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean firstNameValidation = Validator.validateNotEmpty(firstName, firstNameValidationLabel);
        boolean lastNameValidation = Validator.validateNotEmpty(lastName, lastNameValidationLabel);
        boolean countryValidation = Validator.validateNotEmpty(country, countryValidationLabel);
        boolean cityValidation = Validator.validateNotEmpty(city, cityValidationLabel);
        boolean streetValidation = Validator.validateNotEmpty(street, streetValidationLabel);
        boolean postalCodeValidation = Validator.validateNotEmpty(postalCode, postalCodeValidationLabel);
        boolean phoneNumberValidation = Validator.validateNotEmpty(phoneNumber, phoneNumberValidationLabel);
        boolean emailAddressValidation = Validator.validateEmail(emailAddress, emailAddressValidationLabel);
        if(!firstNameValidation || !lastNameValidation || !countryValidation || !cityValidation || !streetValidation
                || !postalCodeValidation || !phoneNumberValidation || !emailAddressValidation) {
            return;
        }

        //Stworzenie nowego klienta i wyczyszczenie pól formularza
        Customer customer = new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText(), null);
        CustomerService.addCustomer(customer);
        actiontarget.setText("Customer added successfully!");
        firstName.clear();
        lastName.clear();
        country.clear();
        city.clear();
        street.clear();
        postalCode.clear();
        phoneNumber.clear();
        emailAddress.clear();

        //Po dodaniu klienta zakończonym sukcesem, następuje powrót do widoku listy klientów
        showCustomersView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param customerView widok tabeli klientów
     * @param applicationContext kontekst aplikacji Springa
     */
    public addCustomersController(@Value("classpath:/view/CustomerView.fxml") Resource customerView, ApplicationContext applicationContext){
        this.customerView = customerView;
        this.applicationContext = applicationContext;
    }

    /**
     * Metoda służąca do przejścia do widoku listy klientów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showCustomersView(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(customerView.getURL());

            //Spring wstrzykuje odpowiedni kontroler obsługujący dany plik .fxml na podstawie kontekstu aplikacji
            fxmlloader.setControllerFactory(applicationContext::getBean);

            //wczytanie sceny
            Parent parent = fxmlloader.load();

            //pobieramy stage z którego wywołany został actionEvent - bo nie chcemy tworzyć za każdym razem nowego Stage
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            //utworzenie i wyświetlenie sceny
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
