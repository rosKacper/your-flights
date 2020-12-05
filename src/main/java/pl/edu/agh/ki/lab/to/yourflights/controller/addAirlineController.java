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
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class addAirlineController {

    private final Resource mainView;
    @FXML
    public TextField name, emailAddress, country, city, street, postalCode, description;
    private final ApplicationContext applicationContext;


    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        Airline airline = new Airline(name.getText(),country.getText(),description.getText(),
                city.getText(),street.getText(),postalCode.getText(),
                emailAddress.getText());
        AirlineService.addAirline(airline);
        name.clear();
        description.clear();
        country.clear();
        city.clear();
        street.clear();
        postalCode.clear();
        emailAddress.clear();
    }

    public addAirlineController(@Value("classpath:/view/MainView.fxml") Resource mainView, ApplicationContext applicationContext){
        this.mainView = mainView;
        this.applicationContext = applicationContext;
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
