package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import java.util.stream.Collectors;

/**
 * Kontroler widoku tabeli przewoźników
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class DiscountController {

    private TicketDiscountService ticketDiscountService;
    private NavigationController navigationController;
    private final ApplicationContext applicationContext;

    @FXML
    private JFXTreeTableView<TicketDiscount> discountsTableView;

    @FXML
    private TreeTableColumn<TicketDiscount, String> nameColumn;
    @FXML
    private TreeTableColumn<TicketDiscount, String> percentageColumn;

    @FXML
    private JFXButton buttonDeleteDiscount;
    @FXML
    private JFXButton buttonUpdateDiscount;

    /**
     * Metoda która wczytuje dane do tabeli przwoźników
     */
    public void setModel() {
        //Ustawienie kolumn
        nameColumn.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        percentageColumn.setCellValueFactory(data -> data.getValue().getValue().getDiscountProperty());

        //Pobranie przewoźników z serwisu
        ObservableList<TicketDiscount> ticketDiscounts = FXCollections.observableList(ticketDiscountService.findAll());

        //Przekazanie danych do tabeli
        final TreeItem<TicketDiscount> root = new RecursiveTreeItem<>(ticketDiscounts, RecursiveTreeObject::getChildren);
        discountsTableView.setRoot(root);
        discountsTableView.setShowRoot(false);
    }

    public DiscountController(TicketDiscountService ticketDiscountService,
                              ApplicationContext applicationContext,
                              NavigationController navigationController) {
        this.navigationController = navigationController;
        this.ticketDiscountService = ticketDiscountService;
        this.applicationContext = applicationContext;
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
        discountsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
    }

    /**
     * Metoda służąca do przejścia do widoku formularza dodawania/edycji przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddDiscountView(ActionEvent actionEvent, TicketDiscount discount) {
        navigationController.showAddDiscountView(actionEvent, discount);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var discounts = discountsTableView.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        ticketDiscountService.deleteAll(FXCollections.observableList(discounts));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var discount = discountsTableView.getSelectionModel().getSelectedItem();
        if(discount != null) {
            this.showAddDiscountView(event, discount.getValue());
        }
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        this.showAddDiscountView(event, null);
    }

    /**
     * Metoda ustawiająca powiązanie atrybutu 'disabled' przycisków z zaznaczeniem w tabeli
     * Po to aby przyciski Delete i Update były nieaktywne w sytuacji gdy nic nie jest zaznaczone w tabeli
     */
    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteDiscount != null) {
            buttonDeleteDiscount.disableProperty().bind(
                    Bindings.isEmpty(discountsTableView.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateDiscount != null) {
            buttonUpdateDiscount.disableProperty().bind(
                    Bindings.size(discountsTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
    }
}
