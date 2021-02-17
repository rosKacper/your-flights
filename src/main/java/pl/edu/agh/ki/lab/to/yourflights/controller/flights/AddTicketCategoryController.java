package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketCategoryService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.math.BigDecimal;

@Component
public class AddTicketCategoryController {

    private final NavigationController navigationController;

    @FXML
    public TextField name, price, numberOfSeats;

    @FXML
    public Label nameValidationLabel, priceValidationLabel, numberOfSeatsValidationLabel;

    @FXML
    public Label nameValidation, priceValidation, numberOfSeatsValidation;

    private final TicketCategoryService ticketCategoryService;
    private TicketCategory ticketCategory;
    private Flight flight;

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

    private void updateModel() {
        ticketCategory.setCategoryName(name.textProperty().getValue());
        ticketCategory.setTotalNumberOfSeats(Integer.parseInt(numberOfSeats.textProperty().getValue()));
        ticketCategory.setCategoryPrice(new BigDecimal(price.textProperty().getValue()));
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        boolean numberOfSeatsValidation = Validator.validateNotEmpty(numberOfSeats, numberOfSeatsValidationLabel) && Validator.validatePositiveNumber(numberOfSeats, numberOfSeatsValidationLabel);
        boolean categoryPriceValidation = Validator.validateNotEmpty(price, priceValidationLabel) && Validator.validateMoneyFormat(price, priceValidationLabel);
        if(!nameValidation || !numberOfSeatsValidation || !categoryPriceValidation){
            return;
        }

        if (ticketCategory == null) {
            ticketCategory = new TicketCategory(name.textProperty().getValue(),
                    new BigDecimal(price.textProperty().getValue()),
                    Integer.parseInt(numberOfSeats.textProperty().getValue()),
                    flight);
        } else {
            updateModel();
        }

        ticketCategoryService.save(ticketCategory);

        name.clear();
        numberOfSeats.clear();
        price.clear();

        ticketCategory = null;
        name = null;
        price = null;
        numberOfSeats = null;

        showTicketCategoriesView(actionEvent);
    }

    public AddTicketCategoryController(NavigationController navigationController,
                                       TicketCategoryService ticketCategoryService){
        this.navigationController = navigationController;
        this.ticketCategoryService = ticketCategoryService;
    }

    public void showTicketCategoriesView(ActionEvent actionEvent) {
        navigationController.showTicketCategoriesView(actionEvent, flight);
    }

}
