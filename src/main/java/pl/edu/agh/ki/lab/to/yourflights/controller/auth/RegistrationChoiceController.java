package pl.edu.agh.ki.lab.to.yourflights.controller.auth;

import javafx.event.ActionEvent;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;

@Component
public class RegistrationChoiceController {

    private final NavigationController navigationController;

    public RegistrationChoiceController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public void showMainView(ActionEvent actionEvent) {
        navigationController.showNavigationView(actionEvent);
    }

    public void showRegistrationView(ActionEvent actionEvent){
        navigationController.showRegistrationView(actionEvent);
    }

    public void showRegistrationAirlineView(ActionEvent actionEvent){
        navigationController.showRegistrationAirlineView(actionEvent);
    }
}