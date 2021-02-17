package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import java.util.stream.Collectors;
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
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketCategoryService;

@Component
public class TicketCategoryViewController {

    private NavigationController navigationController;
    private Flight flight;
    private final TicketCategoryService ticketCategoryService;

    @FXML
    private JFXTreeTableView<TicketCategory> ticketCategoryTableView;

    @FXML
    private TreeTableColumn<TicketCategory, String> name;
    @FXML
    private TreeTableColumn<TicketCategory, String> price;
    @FXML
    private TreeTableColumn<TicketCategory, String> numberOfSeats;

    @FXML
    private JFXButton buttonDeleteTicketCategory;
    @FXML
    private JFXButton buttonUpdateTicketCategory;

    @FXML
    private JFXTextField departureField, destinationField, departureDateField, airlineField;

    public void setModel() {
        name.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        price.setCellValueFactory(data -> data.getValue().getValue().getPriceProperty());
        numberOfSeats.setCellValueFactory(data -> data.getValue().getValue().getNumberOfSeatsProperty());

        ObservableList<TicketCategory> ticketCategories = FXCollections.observableList(ticketCategoryService.findByFlight(flight));

        final TreeItem<TicketCategory> root = new RecursiveTreeItem<>(ticketCategories, RecursiveTreeObject::getChildren);
        ticketCategoryTableView.setRoot(root);
        ticketCategoryTableView.setShowRoot(false);
    }

    public TicketCategoryViewController(TicketCategoryService ticketCategoryService,
                                        NavigationController navigationController){
        this.navigationController = navigationController;
        this.ticketCategoryService = ticketCategoryService;
    }

    @FXML
    public void initialize() {
        this.setModel();
        ticketCategoryTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setButtonsDisablePropertyBinding();
    }

    public void showAddTicketCategory(ActionEvent actionEvent, Flight flight, TicketCategory ticketCategory) {
        navigationController.showAddTicketCategory(actionEvent, flight, ticketCategory);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var ticketCategories = ticketCategoryTableView.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        ticketCategoryService.deleteAll(FXCollections.observableList(ticketCategories));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var ticketCategory = ticketCategoryTableView.getSelectionModel().getSelectedItem();
        this.showAddTicketCategory(event, flight, ticketCategory.getValue());
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        this.showAddTicketCategory(event, flight, null);
    }

    public void updateControls() {
        departureField.setText(flight.getPlaceOfDeparture());
        destinationField.setText(flight.getPlaceOfDestination());
        departureDateField.setText(flight.getDepartureDate());
        airlineField.setText(flight.getAirline().getName());
    }

    public void setData(Flight flight) {
        this.flight = flight;
        this.updateControls();
        this.setModel();
    }

    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteTicketCategory != null) {
            buttonDeleteTicketCategory.disableProperty().bind(
                    Bindings.isEmpty(ticketCategoryTableView.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateTicketCategory != null) {
            buttonUpdateTicketCategory.disableProperty().bind(
                    Bindings.size(ticketCategoryTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
    }
}