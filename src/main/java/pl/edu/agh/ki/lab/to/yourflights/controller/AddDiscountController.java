package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

/**
 * Kontroler obsługujący formularz do dodawania klientów
 */
@Component
public class AddDiscountController {

    private final Resource discountsView;

    private final ApplicationContext applicationContext;

    private final NavigationController navigationController;

    @FXML
    public TextField name, discount;

    @FXML
    public Label nameValidationLabel,  discountValidationLabel;

    private TicketDiscountService discountService;
    private TicketDiscount currDiscount;

    /**
     * Metoda ustawiająca zniżkę do edycji
     * @param discount zniżke, może być nullem
     */
    public void setData(TicketDiscount discount) {
        this.currDiscount = discount;
        updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanej zniżki do edycji
     */
    private void updateControls() {
        name.textProperty().setValue(currDiscount.getName());
        discount.textProperty().setValue(Double.toString(currDiscount.getDiscount()));
    }

    /**
     * Metoda aktualizująca zniżkę
     */
    private void updateModel() {
        currDiscount.setName(name.textProperty().getValue());
        currDiscount.setDiscount(Double.parseDouble(discount.textProperty().getValue()));
    }


    public void handleSubmitButtonAction(ActionEvent actionEvent) {

        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        boolean nameValidation = Validator.validateNotEmpty(name, nameValidationLabel);
        boolean discountValidation = Validator.validatePositiveNumber(discount, discountValidationLabel);
        if(!nameValidation || !discountValidation) {
            return;
        }

        //Stworzenie nowego przewoźnika (jeśli to było dodawanie nowego przewoźnika), lub zaktualizowanie obecnego lotu
        if(currDiscount == null) {
            currDiscount = new TicketDiscount(Double.parseDouble(discount.textProperty().getValue()), name.getText());
        } else {
            updateModel();
        }
        discountService.save(currDiscount);

        //wyczyszczenie pól formularza
        name.clear();
        discount.clear();
        discount = null;

        //Po dodaniu/edycji przewoźnika zakończonym sukcesem, następuje powrót do widoku listy przewoźników
        showDiscountsView(actionEvent);
    }

    public AddDiscountController(@Value("classpath:/view/ToDelete/DiscountsView.fxml") Resource discountsView,
                                NavigationController navigationController,
                                ApplicationContext applicationContext,
                                TicketDiscountService ticketDiscountService){
        this.navigationController = navigationController;
        this.discountsView = discountsView;
        this.applicationContext = applicationContext;
        this.discountService = ticketDiscountService;
    }

    public void showDiscountsView(ActionEvent actionEvent) {
        navigationController.showDiscountsView(actionEvent);
    }
}
