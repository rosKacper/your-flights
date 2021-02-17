package pl.edu.agh.ki.lab.to.yourflights.controller.auth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.User;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.UserRole;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;
import javafx.scene.paint.Color;

@Component
public class RegistrationController {

    private final NavigationController navigationController;

    @FXML
    public TextField firstName, lastName, country,city, street, postalCode, phoneNumber, emailAddress, username, password;

    @FXML
    public Label firstNameValidationLabel, lastNameValidationLabel,
            countryValidationLabel, cityValidationLabel, streetValidationLabel,
            postalCodeValidationLabel, phoneNumberValidationLabel, emailAddressValidationLabel,
            usernameValidationLabel, passwordValidationLabel;

    @FXML
    public Text actiontarget;

    private final CustomerService customerService;
    private final UserPrincipalService userPrincipalService;


    public void handleSubmitButtonAction(ActionEvent actionEvent) {
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

        String name = username.getText();
        if(!userPrincipalService.findByUsername(name).isEmpty()) {
            usernameValidationLabel.setText("User with given username already exists");
            usernameValidationLabel.setTextFill(Color.RED);
            return;
        }

        User user = new User(username.getText(), emailAddress.getText(), password.getText(), UserRole.USER);
        Customer customer = new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText(),username.getText(), user);
        userPrincipalService.save(user);
        customer.setUser(user);
        customerService.save(customer);

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

        showLoginView(actionEvent);
    }

    public RegistrationController(NavigationController navigationController,
                                  CustomerService customerService,
                                  UserPrincipalService userPrincipalService){
        this.navigationController = navigationController;
        this.customerService = customerService;
        this.userPrincipalService = userPrincipalService;
    }


    public void showNavigationView(ActionEvent actionEvent) {
        navigationController.showNavigationView(actionEvent);
    }

    public void showLoginView(ActionEvent actionEvent){
        navigationController.showLoginView(actionEvent);
    }
}