package pl.edu.agh.ki.lab.to.yourflights.controller.airlines;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

@Component
public class AddAirlineController {

    private final ApplicationContext applicationContext;
    private final NavigationController navigationController;
    private AirlineService airlineService;
    private Airline airline;

    @FXML
    public TextField name, country, description;

    @FXML
    public Label nameValidationLabel,  countryValidationLabel;

    public void setData(Airline airline) {
        this.airline = airline;
        updateControls();
    }

    private void updateControls() {
        name.textProperty().setValue(airline.getName());
        country.textProperty().setValue(airline.getCountry());
        description.textProperty().setValue(airline.getDescription());
    }

    private void updateModel() {
        airline.setName(name.textProperty().getValue());
        airline.setCountry(country.textProperty().getValue());
        airline.setDescription(description.textProperty().getValue());
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        boolean countryValidation = Validator.validateNotEmpty(country, countryValidationLabel);
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        if(!countryValidation || !nameValidation) {
            return;
        }

        updateModel();

        airlineService.save(airline);

        country.clear();
        description.clear();
        name.clear();
        airline=null;

        showAirlinesView(actionEvent);
    }

    public AddAirlineController(NavigationController navigationController,
                                ApplicationContext applicationContext,
                                AirlineService airlineService){
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.airlineService = airlineService;
    }

    public void showAirlinesView(ActionEvent actionEvent) {
        navigationController.showAirlinesView(actionEvent);
    }
}
