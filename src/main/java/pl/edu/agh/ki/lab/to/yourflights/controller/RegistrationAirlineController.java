package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.User;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.UserRole;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

@Component
public class RegistrationAirlineController {

    private final NavigationController navigationController;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    @FXML
    public TextField firstName, country, description, emailAddress, username, password;

    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */
    @FXML
    public Label firstNameValidationLabel,
            countryValidationLabel, descriptionLabel
            , emailAddressValidationLabel,
            usernameValidationLabel, passwordValidationLabel;

    /**
     * Etykieta do wyświetlania komunikatu o sukcesie dodawania klienta
     */
    @FXML
    public Text actiontarget;

    private final CustomerService customerService;
    private final AirlineService airlineService;
    private final UserPrincipalService userPrincipalService;

    /**
     * Metoda obsługująca dodawanie użytkownika po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean firstNameValidation = Validator.validateNotEmpty(firstName, firstNameValidationLabel);
        boolean countryValidation = Validator.validateNotEmpty(country, countryValidationLabel);
        boolean emailAddressValidation = Validator.validateEmail(emailAddress, emailAddressValidationLabel);
        boolean usernameValidation = Validator.validateNotEmpty(username, usernameValidationLabel);
        boolean passwordValidation = Validator.validateNotEmpty(password, passwordValidationLabel);
        if(!firstNameValidation || !countryValidation || !emailAddressValidation
                || !usernameValidation || !passwordValidation) {
            return;
        }

        //Stworzenie nowego klienta i wyczyszczenie pól formularza
        User user = new User(username.getText(), emailAddress.getText(), password.getText(), UserRole.AIRLINE);
        Airline airline = new Airline(firstName.getText(),country.getText(),description.getText(), user);
        userPrincipalService.save(user);
        airline.setUser(user);
        airlineService.save(airline);

        firstName.clear();
        country.clear();
        description.clear();
        emailAddress.clear();
        username.clear();
        password.clear();

        //Po dodaniu klienta zakończonym sukcesem, następuje powrót do widoku listy klientów
        showLoginView(actionEvent);
    }

    public RegistrationAirlineController(ApplicationContext applicationContext,
                                         NavigationController navigationController,
                                         CustomerService customerService,
                                         UserPrincipalService userPrincipalService,
                                         AirlineService airlineService){
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.customerService = customerService;
        this.userPrincipalService = userPrincipalService;
        this.airlineService = airlineService;
    }

    public void showNavigationView(ActionEvent actionEvent) {
        navigationController.showNavigationView(actionEvent);

    }

    public void showLoginView(ActionEvent actionEvent){
        navigationController.showLoginView(actionEvent);
    }
}