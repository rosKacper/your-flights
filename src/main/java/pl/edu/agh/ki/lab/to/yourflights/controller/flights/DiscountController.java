package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketDiscount;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import java.util.stream.Collectors;

@Component
public class DiscountController {

    private TicketDiscountService ticketDiscountService;
    private NavigationController navigationController;

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

    public void setModel() {
        nameColumn.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        percentageColumn.setCellValueFactory(data -> data.getValue().getValue().getDiscountProperty());

        ObservableList<TicketDiscount> ticketDiscounts = FXCollections.observableList(ticketDiscountService.findAll());

        final TreeItem<TicketDiscount> root = new RecursiveTreeItem<>(ticketDiscounts, RecursiveTreeObject::getChildren);
        discountsTableView.setRoot(root);
        discountsTableView.setShowRoot(false);
    }

    public DiscountController(TicketDiscountService ticketDiscountService,
                              NavigationController navigationController) {
        this.navigationController = navigationController;
        this.ticketDiscountService = ticketDiscountService;
    }

    @FXML
    public void initialize() {
        this.setModel();
        discountsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
    }

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
