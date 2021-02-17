package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

@Component
public class AddDiscountController {

    private final NavigationController navigationController;

    @FXML
    public TextField name, discount;

    @FXML
    public Label nameValidationLabel,  discountValidationLabel;

    private TicketDiscountService discountService;
    private TicketDiscount currDiscount;

    public void setData(TicketDiscount discount) {
        this.currDiscount = discount;
        updateControls();
    }

    private void updateControls() {
        name.textProperty().setValue(currDiscount.getName());
        discount.textProperty().setValue(Double.toString(currDiscount.getDiscount()));
    }

    private void updateModel() {
        currDiscount.setName(name.textProperty().getValue());
        currDiscount.setDiscount(Double.parseDouble(discount.textProperty().getValue()));
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        boolean discountValidation = Validator.validatePositiveNumber(discount, discountValidationLabel);
        if(!nameValidation || !discountValidation) {
            return;
        }

        if(currDiscount == null) {
            currDiscount = new TicketDiscount(Double.parseDouble(discount.textProperty().getValue()), name.getText());
        } else {
            updateModel();
        }
        discountService.save(currDiscount);

        name.clear();
        discount.clear();
        discount = null;

        showDiscountsView(actionEvent);
    }

    public AddDiscountController(NavigationController navigationController,
                                 TicketDiscountService ticketDiscountService){
        this.navigationController = navigationController;
        this.discountService = ticketDiscountService;
    }

    public void showDiscountsView(ActionEvent actionEvent) {
        navigationController.showDiscountsView(actionEvent);
    }
}
