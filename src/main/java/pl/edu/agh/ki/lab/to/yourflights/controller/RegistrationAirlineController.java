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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.User;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.UserRole;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;


/**
 * Kontroler obsługujący formularz do rejestracji
 * Nie jest to na razie wykorzystywane
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class RegistrationAirlineController {

    /**
     * Widoki
     */
    private final Resource mainView;
    private final Resource anonymousMainView;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
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

        //Stworzenie nowego przewoźnika i wyczyszczenie pól formularza
        User user = new User(username.getText(), emailAddress.getText(), password.getText(), UserRole.AIRLINE);
        Airline airline = new Airline(firstName.getText(),country.getText(),description.getText(), user);
        userPrincipalService.save(user);
        airline.setUser(user);
        airlineService.save(airline);

//        User user = new User(username.getText(), emailAddress.getText(), passwordEncoder.encode(password.getText()), UserRole.USER);
//        Airline airline = new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText(),username.getText(), user);
//        user.setCustomer(customer);
//        //        userPrincipalService.save(user);
//        customerService.save(customer);
//        userPrincipalService.findByUsername(username.getText()).setCustomer(customerService.findByUsername(username.getText()));



        firstName.clear();
        country.clear();
        description.clear();
        emailAddress.clear();
        username.clear();
        password.clear();



        //Zarejestrowanie konta - todo

//        UserDetails newUser = User.withUsername(username.toString())
//                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
//                .password(password.toString()).roles("USER").build();
//
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//        userDetailsManager.createUser(newUser);


        //Po dodaniu klienta zakończonym sukcesem, następuje powrót do widoku listy klientów
        showMainView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param mainView widok główny
     * @param applicationContext kontekst aplikacji Springa
     * @param userPrincipalService
     */
    public RegistrationAirlineController(@Value("classpath:/view/MainView/AnonymousMainView.fxml") Resource anonymousMainView,
                                  @Value("classpath:/view/MainView/MainView.fxml") Resource mainView,
                                  ApplicationContext applicationContext,
                                  CustomerService customerService, UserPrincipalService userPrincipalService,
                                         AirlineService airlineService){
        this.mainView = mainView;
        this.anonymousMainView = anonymousMainView;
        this.applicationContext = applicationContext;
        this.customerService = customerService;
        this.userPrincipalService = userPrincipalService;
        this.airlineService = airlineService;
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