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
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;

/**
 * Kontroler obsługujący formularz do dodawania klientów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddDiscountController {

    /**
     * Widok przewoźników
     */
    private final Resource discountsView;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Pola formularza
     */
    @FXML
    public TextField name, discount;

    /**
     * Etykiety do wyświetlania komunikatów o błędnie podanych danych w formularzu
     */
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

    /**
     * Metoda obsługująca dodawanie zniżkę po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została podstawowa obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
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

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param discountsView widok zniżek
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddDiscountController(@Value("classpath:/view/DiscountsView.fxml") Resource discountsView,
                                ApplicationContext applicationContext,
                                TicketDiscountService ticketDiscountService){
        this.discountsView = discountsView;
        this.applicationContext = applicationContext;
        this.discountService = ticketDiscountService;
    }

    /**
     * Metoda służąca do przejścia do widoku listy zniżek
     * @param actionEvent event emitowany przez przycisk
     */
    public void showDiscountsView(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(discountsView.getURL());

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
