package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
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

    /**
     * Metoda ustawiająca przewoźnika do edycji
     * @param airline przewoźnik do edycji, może być nullem
     */
    public void setData(Airline airline) {
        this.airline = airline;
        updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanego przewoźnika do edycji
     */
    private void updateControls() {
        name.textProperty().setValue(airline.getName());
        country.textProperty().setValue(airline.getCountry());
        description.textProperty().setValue(airline.getDescription());
    }

    /**
     * Metoda aktualizująca przewoźnika po edycji
     */
    private void updateModel() {
        airline.setName(name.textProperty().getValue());
        airline.setCountry(country.textProperty().getValue());
        airline.setDescription(description.textProperty().getValue());
    }

    /**
     * Metoda obsługująca dodawanie przewoźnika po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean countryValidation = Validator.validateNotEmpty(country, countryValidationLabel);
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        if(!countryValidation || !nameValidation) {
            return;
        }

        //Stworzenie nowego przewoźnika (jeśli to było dodawanie nowego przewoźnika), lub zaktualizowanie obecnego lotu
        if(airline == null) {
            //do usunięcia jest w ogóle to dodawanie linii lotniczych
//            airline = new Airline(name.getText(),country.getText(),description.getText());
        } else {
            updateModel();
        }
        airlineService.save(airline);

        //wyczyszczenie pól formularza
        country.clear();
        description.clear();
        name.clear();
        airline=null;

        //Po dodaniu/edycji przewoźnika zakończonym sukcesem, następuje powrót do widoku listy przewoźników
        showAirlinesView(actionEvent);
    }


    public AddAirlineController(NavigationController navigationController,
                                ApplicationContext applicationContext,
                                AirlineService airlineService){
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.airlineService = airlineService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAirlinesView(ActionEvent actionEvent) {
        navigationController.showAirlinesView(actionEvent);
    }
}
