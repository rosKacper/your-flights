package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketCategoryService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.math.BigDecimal;

/**
 * Kontroler obsługujący formularz do dodawania lotów
 */
@Component
public class AddTicketCategoryController {

    private final Resource ticketCategoryView;

    private final ApplicationContext applicationContext;

    private final NavigationController navigationController;

    @FXML
    public TextField name,price, numberOfSeats;

    @FXML
    public Label nameValidationLabel, priceValidationLabel, numberOfSeatsValidationLabel;
    @FXML
    public Text actiontarget;
    @FXML
    public Label nameValidation, priceValidation, numberOfSeatsValidation;

    private final FlightService flightService;
    private final TicketCategoryService ticketCategoryService;
    private TicketCategory ticketCategory;
    private Flight flight;

    /**
     * Metoda ustawiająca lot do edycji
     * @param flight lot do edycji, może być nullem
     */
    public void setData(Flight flight, TicketCategory ticketCategory) {
        this.ticketCategory = ticketCategory;
        this.flight = flight;
        updateControls();
    }


    private void updateControls() {
        if(ticketCategory != null) {
            name.textProperty().setValue(ticketCategory.getCategoryName());
            price.textProperty().setValue(ticketCategory.getCategoryPrice().toString());
            numberOfSeats.textProperty().setValue(Integer.toString(ticketCategory.getTotalNumberOfSeats()));
        }
    }

    /**
     * Metoda aktualizująca kategorie biletu po edycji
     */
    private void updateModel() {
        ticketCategory.setCategoryName(name.textProperty().getValue());
        ticketCategory.setTotalNumberOfSeats(Integer.parseInt(numberOfSeats.textProperty().getValue()));
        ticketCategory.setCategoryPrice(new BigDecimal(price.textProperty().getValue()));
    }

    /**
     * Metoda obsługująca dodawanie/edycję lotu po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        boolean numberOfSeatsValidation = Validator.validateNotEmpty(numberOfSeats, numberOfSeatsValidationLabel) && Validator.validatePositiveNumber(numberOfSeats, numberOfSeatsValidationLabel);
        boolean categoryPriceValidation = Validator.validateNotEmpty(price, priceValidationLabel) && Validator.validateMoneyFormat(price, priceValidationLabel);
        if(!nameValidation || !numberOfSeatsValidation || !categoryPriceValidation){
            return;
        }

        //Stworzenie nowej kategorii biletu (jeśli to było dodawanie nowej kategorii biletów), lub zaktualizowanie obecnej
        if (ticketCategory == null) {
            ticketCategory = new TicketCategory(name.textProperty().getValue(),
                    new BigDecimal(price.textProperty().getValue()),
                    Integer.parseInt(numberOfSeats.textProperty().getValue()),
                    flight);
        } else {
            updateModel();
        }

        ticketCategoryService.save(ticketCategory);

        // wyczyszczenie pól formularza
        name.clear();
        numberOfSeats.clear();
        price.clear();

        ticketCategory = null;
        name = null;
        price = null;
        numberOfSeats = null;
        //Po dodaniu kategorii biletu zakończonym sukcesem, następuje powrót do widoku listy kategorii biletów
        showTicketCategoriesView(actionEvent);
    }

    public AddTicketCategoryController(@Value("classpath:/view/ToDelete/TicketCategoryView.fxml") Resource ticketCategoryView,
                               ApplicationContext applicationContext,
                               FlightService flightService,
                               NavigationController navigationController,
                               TicketCategoryService ticketCategoryService,
                               AirlineService airlineService){
        this.ticketCategoryView = ticketCategoryView;
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.flightService = flightService;
        this.ticketCategoryService = ticketCategoryService;
    }

    public void showTicketCategoriesView(ActionEvent actionEvent) {
        navigationController.showTicketCategoriesView(actionEvent, flight);
    }

}
