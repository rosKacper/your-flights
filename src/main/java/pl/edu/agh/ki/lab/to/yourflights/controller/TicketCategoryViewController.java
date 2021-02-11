package pl.edu.agh.ki.lab.to.yourflights.controller;

import java.util.stream.Collectors;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.TicketCategory;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketCategoryService;

@RestController
@RequestMapping("/api/v1")
public class TicketCategoryViewController {

    private NavigationController navigationController;
    private Flight flight;
    private final TicketCategoryService ticketCategoryService;
    private final FlightService flightService;

    @FXML
    private JFXTreeTableView<TicketCategory> ticketCategoryTableView;

    @FXML
    private TreeTableColumn<TicketCategory, String> name;
    @FXML
    private TreeTableColumn<TicketCategory, String> price;
    @FXML
    private TreeTableColumn<TicketCategory, String> numberOfSeats;

    @FXML
    private JFXButton buttonAddTicketCategory;
    @FXML
    private JFXButton buttonDeleteTicketCategory;
    @FXML
    private JFXButton buttonUpdateTicketCategory;

    @FXML
    private JFXTextField departureField, destinationField, departureDateField, airlineField;


    /**
     * Metoda która wczytuje dane do tabeli lotów
     */
    public void setModel() {
        //Ustawienie kolumn
        name.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        price.setCellValueFactory(data -> data.getValue().getValue().getPriceProperty());
        numberOfSeats.setCellValueFactory(data -> data.getValue().getValue().getNumberOfSeatsProperty());

        //Pobranie kategorii biletów z serwisu
        ObservableList<TicketCategory> ticketCategories = FXCollections.observableList(ticketCategoryService.findByFlight(flight));

        //Przekazanie danych do tabeli
        final TreeItem<TicketCategory> root = new RecursiveTreeItem<>(ticketCategories, RecursiveTreeObject::getChildren);
        ticketCategoryTableView.setRoot(root);
        ticketCategoryTableView.setShowRoot(false);
    }

    public TicketCategoryViewController(TicketCategoryService ticketCategoryService,
                                        NavigationController navigationController,
                                        FlightService flightService){
        this.navigationController = navigationController;
        this.ticketCategoryService = ticketCategoryService;
        this.flightService = flightService;
    }

    @FXML
    public void initialize() {
        this.setModel();
        ticketCategoryTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setButtonsDisablePropertyBinding();
    }

    /**
     * Metoda służąca do przejścia do widoku formularza do dodawania/edycji lotów
     * @param actionEvent event emitowany przez przycisk
     */
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

    /**
     * Metoda która ustawia informacje o locie
     */
    public void updateControls() {
        departureField.setText(flight.getPlaceOfDeparture());
        destinationField.setText(flight.getPlaceOfDestination());
        departureDateField.setText(flight.getDepartureDate());
        airlineField.setText(flight.getAirline().getName());
    }

    /**
     * Metoda która przekazuje do kontrolera dane
     * @param flight lot dla którego dodajemy kategorie biletów
     */
    public void setData(Flight flight) {
        this.flight = flight;
        this.updateControls();
        this.setModel();
    }

    /**
     * Metoda ustawiająca powiązanie atrybutu 'disabled' przycisków z zaznaczeniem w tabeli
     * Po to aby przyciski Delete, Update i AddReservation były nieaktywne w sytuacji gdy nic nie jest zaznaczone w tabeli
     */
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