package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Component
public class FlightDetailsController {

    private final Resource flightsView;
    private final ApplicationContext applicationContext;


    public FlightDetailsController(ApplicationContext applicationContext,
                                   @Value("classpath:/view/FlightView.fxml") Resource flightsView) {
        this.applicationContext = applicationContext;
        this.flightsView = flightsView;
    }

    /**
     * Metoda służąca do przejścia do widoku lotów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showFlightsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(flightsView.getURL());
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
