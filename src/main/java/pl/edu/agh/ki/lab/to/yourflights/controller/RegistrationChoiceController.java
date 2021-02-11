package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import org.springframework.stereotype.Component;

@Component
public class RegistrationChoiceController {

    private final NavigationController navigationController;

    public RegistrationChoiceController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    /**
     * Metoda służąca do przejścia do głównego widoku
     * @param actionEvent event emitowany przez przycisk
     */
    public void showMainView(ActionEvent actionEvent) {
        navigationController.showNavigationView(actionEvent);
    }

    /**
     * Metoda służąca do przejścia do widoku rejestracji
     * @param actionEvent
     */
    public void showRegistrationView(ActionEvent actionEvent){
        navigationController.showRegistrationView(actionEvent);
    }

    public void showRegistrationAirlineView(ActionEvent actionEvent){
        navigationController.showRegistrationAirlineView(actionEvent);
    }
}