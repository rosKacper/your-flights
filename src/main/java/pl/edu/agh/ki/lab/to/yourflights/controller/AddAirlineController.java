package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

/**
 * Kontroler obsługujący formularz do dodawania klientów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddAirlineController {

    /**
     * Widok przewoźników
     */
    private final Resource airlinesView;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
    @FXML
    public TextField name, country, description;

    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */
    @FXML
    public Label nameValidationLabel,  countryValidationLabel;

    private AirlineService airlineService;
    private Airline airline;

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

        //Stworzenie nowego przewoźnika i wyczyszczenie pól formularza
        if(airline == null) {
            airline = new Airline(name.getText(),country.getText(),description.getText());
        } else {
            updateModel();
        }
        airlineService.save(airline);
        country.clear();
        description.clear();
        name.clear();

        //Po dodaniu/edycji przewoźnika zakończonym sukcesem, następuje powrót do widoku listy przewoźników
        showAirlinesView(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param airlinesView widok przewoźników
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddAirlineController(@Value("classpath:/view/AirlinesView.fxml") Resource airlinesView,
                                ApplicationContext applicationContext,
                                AirlineService airlineService){
        this.airlinesView = airlinesView;
        this.applicationContext = applicationContext;
        this.airlineService = airlineService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAirlinesView(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(airlinesView.getURL());

            //Spring wstrzykuje odpowiedni kontroler obsługujący dany plik .fxml na podstawie kontekstu aplikacji
            fxmlloader.setControllerFactory(applicationContext::getBean);

            //wczytanie sceny
            Parent parent = fxmlloader.load();

            //pobieramy stage z którego wywołany został actionEvent - bo nie chcemy tworzyć za każdym razem nowego Stage
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            //utworzenie i wyświetlenie sceny
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
