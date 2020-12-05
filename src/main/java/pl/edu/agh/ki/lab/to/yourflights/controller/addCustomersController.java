package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class addCustomersController {
    public PasswordField passwordField;

    private final Resource mainView;
    @FXML
    public Text actiontarget;
    public TextField firstName, lastName, country,city, street, postalCode, phoneNumber, emailAddress;
    private final ApplicationContext applicationContext;


    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        Customer customer = new Customer(firstName.getText(),lastName.getText(),country.getText(),city.getText(),street.getText(),postalCode.getText(),phoneNumber.getText(),emailAddress.getText());
        CustomerService.addCustomer(customer);
        actiontarget.setText("Done!");
        firstName.clear();
        lastName.clear();
        country.clear();
        city.clear();
        street.clear();
        postalCode.clear();
        phoneNumber.clear();
        emailAddress.clear();

    }

    public addCustomersController(@Value("classpath:/view/MainView.fxml") Resource mainView, ApplicationContext applicationContext){
        this.mainView=mainView;
        this.applicationContext=applicationContext;
    }

    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(mainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
