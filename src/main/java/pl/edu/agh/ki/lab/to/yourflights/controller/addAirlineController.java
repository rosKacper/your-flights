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

    private final Resource mainView;
    @FXML
    public TextField name, emailAddress, country, city, street, postalCode, description;
    @FXML
    public Button formTitle;
    private final ApplicationContext applicationContext;

    private TextField validateNotEmpty(TextField field) {
        return field.getText() == null || field.getText().isEmpty() ? field : null;
    }

    private boolean validateEmail(TextField field) {
        if(validateNotEmpty(field) != null) return false;
        String email = field.getText();
        return Pattern.matches("\\A\\s*[a-zA-Z0-9]+([-._]?[A-z0-9]+)*(\\+[A-z0-9]+([-._]?[A-z0-9]+)*)?@[A-z0-9]+([-.]?[a-z0-9]+)*\\.[A-z]+\\s*", email);
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        List<TextField> fields = Arrays.asList(name, emailAddress, country, city, street, postalCode, description);
        List<TextField> errors = fields.stream().map(this::validateNotEmpty).filter(Objects::nonNull).collect(Collectors.toList());
        if (errors.size() != 0) {
            formTitle.setText(errors.get(0).getId() + " cannot be empty");
            formTitle.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            formTitle.setTextFill(Color.WHITE);
            return;
        }

        if (!validateEmail(emailAddress)) {
            formTitle.setText("Email address is incorrect");
            formTitle.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            formTitle.setTextFill(Color.WHITE);
            return;
        }


        Airline airline = new Airline(name.getText(),country.getText(),description.getText(),
                city.getText(),street.getText(),postalCode.getText(),
                emailAddress.getText());
        AirlineService.addAirline(airline);
        fields.forEach(TextField::clear);

        formTitle.setText("Airline Added!");
        formTitle.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        formTitle.setTextFill(Color.WHITE);

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
