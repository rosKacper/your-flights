package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class addAirlineController {

    private Validator validator = new Validator();
    private final Resource airlineView;
    @FXML
    public TextField name, country, description;
    @FXML
    public Label nameLabel,  countryLabel, descriptionLabel;
    @FXML
    public Button formTitle;
    private final ApplicationContext applicationContext;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        boolean countryValidation = Validator.validateNotEmpty(country, countryLabel);
        boolean nameValidation = Validator.validateNotEmpty(name, nameLabel);
        boolean descriptionValidation = Validator.validateNotEmpty(description, descriptionLabel);

        if(!countryValidation || !nameValidation || !descriptionValidation) return;

        Airline airline = new Airline(name.getText(),country.getText(),description.getText());
        AirlineService.addAirline(airline);

        country.clear();
        description.clear();
        name.clear();

        formTitle.setText("Airline Added!");
        formTitle.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        formTitle.setTextFill(Color.WHITE);

    }

    public addAirlineController(@Value("classpath:/view/AirlinesView.fxml") Resource airlineView, ApplicationContext applicationContext){
        this.airlineView = airlineView;
        this.applicationContext = applicationContext;
    }


    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(airlineView.getURL());
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
