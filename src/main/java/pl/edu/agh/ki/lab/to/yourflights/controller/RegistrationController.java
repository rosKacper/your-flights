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
import org.springframework.security.core.context.SecurityContextHolder;
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
public class RegistrationController {

    /**
     * Widok klientów
     */
    private final Resource mainView;
    private final Resource anonymousMainView;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
    @FXML
    public TextField firstName, lastName, country,city, street, postalCode, phoneNumber, emailAddress, username, password;

    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */
    @FXML
    public Label firstNameValidationLabel, lastNameValidationLabel,
            countryValidationLabel, cityValidationLabel, streetValidationLabel,
            postalCodeValidationLabel, phoneNumberValidationLabel, emailAddressValidationLabel,
            usernameValidationLabel, passwordValidationLabel;

    /**
     * Etykieta do wyświetlania komunikatu o sukcesie dodawania klienta
     */
    @FXML
    public Text actiontarget;

    private final CustomerService customerService;
//    private Customer customer;


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
        boolean usernameValidation = Validator.validateNotEmpty(username, usernameValidationLabel);
        boolean passwordValidation = Validator.validateNotEmpty(password, passwordValidationLabel);
        if(!firstNameValidation || !lastNameValidation || !countryValidation || !cityValidation || !streetValidation
                || !postalCodeValidation || !phoneNumberValidation || !emailAddressValidation
                || !usernameValidation || !passwordValidation) {
            return;
        }

        //Stworzenie nowego klienta i wyczyszczenie pól formularza
        System.out.println("HELLO");
        customerService.save(new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText(),username.getText()));
        System.out.println("HELLO2");
        firstName.clear();
        lastName.clear();
        country.clear();
        city.clear();
        street.clear();
        postalCode.clear();
        phoneNumber.clear();
        emailAddress.clear();
        username.clear();
        password.clear();

        //Po dodaniu klienta zakończonym sukcesem, następuje powrót do widoku listy klientów
        showMainView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param mainView widok główny
     * @param applicationContext kontekst aplikacji Springa
     */
    public RegistrationController(@Value("classpath:/view/AnonymousMainView.fxml") Resource anonymousMainView,
                                  @Value("classpath:/view/MainView.fxml") Resource mainView,
                                  ApplicationContext applicationContext,
                                  CustomerService customerService){
        this.mainView = mainView;
        this.anonymousMainView = anonymousMainView;
        this.applicationContext = applicationContext;
        this.customerService = customerService;
    }


    /**
     * Metoda służąca do przejścia do głównego widoku
     * @param actionEvent event emitowany przez przycisk
     */
    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(anonymousMainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
