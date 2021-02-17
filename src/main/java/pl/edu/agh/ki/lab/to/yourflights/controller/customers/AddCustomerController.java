package pl.edu.agh.ki.lab.to.yourflights.controller.customers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

@Component
public class AddCustomerController {

    private final ApplicationContext applicationContext;
    private final NavigationController navigationController;
    private CustomerService customerService;
    private Customer customer;

    @FXML
    public TextField firstName, lastName, country,city, street, postalCode, phoneNumber, emailAddress;

    @FXML
    public Label firstNameValidationLabel, lastNameValidationLabel,
            countryValidationLabel, cityValidationLabel, streetValidationLabel,
            postalCodeValidationLabel, phoneNumberValidationLabel, emailAddressValidationLabel;

    @FXML
    public Text actiontarget;

    public void setData(Customer customer) {
        this.customer = customer;
        updateControls();
    }

    private void updateControls() {
        firstName.textProperty().setValue(customer.getFirstName());
        lastName.textProperty().setValue(customer.getSecondName());
        country.textProperty().setValue(customer.getCountry());
        city.textProperty().setValue(customer.getCity());
        street.textProperty().setValue(customer.getStreet());
        postalCode.textProperty().setValue(customer.getPostalCode());
        phoneNumber.textProperty().setValue(customer.getPhoneNumber());
        emailAddress.textProperty().setValue(customer.getEmailAddress());
    }

    private void updateModel() {
        customer.setFirstName(firstName.textProperty().getValue());
        customer.setSecondName(lastName.textProperty().getValue());
        customer.setCountry(country.textProperty().getValue());
        customer.setCity(city.textProperty().getValue());
        customer.setStreet(street.textProperty().getValue());
        customer.setPostalCode(postalCode.textProperty().getValue());
        customer.setPhoneNumber(phoneNumber.textProperty().getValue());
        customer.setEmailAddress(emailAddress.textProperty().getValue());
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
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

        if (customer == null) {
            customer = new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText());
        } else {
            updateModel();
        }

        customerService.save(customer);

        actiontarget.setText("Customer added successfully!");
        firstName.clear();
        lastName.clear();
        country.clear();
        city.clear();
        street.clear();
        postalCode.clear();
        phoneNumber.clear();
        emailAddress.clear();
        customer=null;

        showCustomersView(actionEvent);
    }

    public AddCustomerController(NavigationController navigationController,
                                 ApplicationContext applicationContext,
                                 CustomerService customerService){
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.customerService = customerService;
    }

    public void showCustomersView(ActionEvent actionEvent) {
        navigationController.showCustomersView(actionEvent);
    }
}
