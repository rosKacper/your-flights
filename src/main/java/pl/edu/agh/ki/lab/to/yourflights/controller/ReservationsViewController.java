package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketOrderService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class ReservationsViewController {

    private final ReservationService reservationService;
    private final TicketOrderService ticketOrderService;
    private final AirlineService airlineService;
    private final UserPrincipalService userPrincipalService;
    private final NavigationController navigationController;

    @FXML
    private JFXTreeTableView<Reservation> reservationListTable;

    @FXML
    private TreeTableColumn<Reservation, String> reservationDate;
    @FXML
    private TreeTableColumn<Reservation, String> userName;
    @FXML
    private TreeTableColumn<Reservation, String> departure;
    @FXML
    private TreeTableColumn<Reservation, String> departureDate;
    @FXML
    private TreeTableColumn<Reservation, String> destination;
    @FXML
    private TreeTableColumn<Reservation, String> destinationDate;

    /**
     * Pola do filtrowania
     */
    @FXML
    private JFXTextField userNameFilter;
    @FXML
    private JFXTextField departureFilter;
    @FXML
    private JFXTextField destinationFilter;
    @FXML
    private JFXDatePicker datePicker;

    /**
     * Przyciski
     */
    @FXML
    private JFXButton buttonDeleteReservation;
    @FXML
    private JFXButton buttonUpdateReservation;

    /**
     * Metoda która wczytuje dane do tabeli rezerwacji
     */
    public void setModel() {
        //Ustawienie kolumn
        reservationDate.setCellValueFactory(data -> data.getValue().getValue().getReservationDateProperty());
        userName.setCellValueFactory(data -> data.getValue().getValue().getUserNameProperty());
        departure.setCellValueFactory(data -> ticketOrderService.findByReservation(data.getValue().getValue()).get(0).getTicketCategory().getFlight().getplaceOfDepartureProperty());
        destination.setCellValueFactory(data -> ticketOrderService.findByReservation(data.getValue().getValue()).get(0).getTicketCategory().getFlight().getplaceOfDestinationProperty());
        departureDate.setCellValueFactory(data -> ticketOrderService.findByReservation(data.getValue().getValue()).get(0).getTicketCategory().getFlight().getdepartureDateProperty());
        destinationDate.setCellValueFactory(data -> ticketOrderService.findByReservation(data.getValue().getValue()).get(0).getTicketCategory().getFlight().getarrivalDateProperty());

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList<Reservation> reservationList;

        //Pobranie rezerwacje z serwisu
        if(role.equals("[AIRLINE]")){
            reservationList = FXCollections.observableList(airlineService.getReservationsForAirline(airlineService.findByUser(userPrincipalService.findByUsername(name).get(0))));
        }
        else{
             reservationList = FXCollections.observableList(reservationService.findAll().stream().filter(reservation -> ticketOrderService.findByReservation(reservation).size() > 0)
                    .filter(reservation -> reservation.getUserName().equals(name) || role.equals("[ROLE_ADMIN]"))
                    .collect(Collectors.toList()));
        }

        //Przekazanie danych do tabeli
        final TreeItem<Reservation> root = new RecursiveTreeItem<Reservation>(reservationList, RecursiveTreeObject::getChildren);
        reservationListTable.setRoot(root);
        reservationListTable.setShowRoot(false);
    }

    /**
     * Metoda służąca do inicjalizacji filtrowania
     */
    private void setPredicates() {
        // Generyczna klasa filtrów dla danego modelu
        GenericFilter<Reservation> reservationFilter = new GenericFilter<>(reservationListTable);
        // Dodanie do listy predykatów testujących zawartość filtrów
        //filtrowanie na podstawie nazwy użytkownika
        reservationFilter.addPredicate(testedValue -> testedValue.getUserName().toLowerCase().contains(userNameFilter.getText().toLowerCase()));
        //filtrowanie na podstawie lotniska docelowego
        reservationFilter.addPredicate(testedValue -> ticketOrderService.findByReservation(testedValue).get(0).getTicketCategory().getFlight().getPlaceOfDeparture().toLowerCase().contains(departureFilter.getText().toLowerCase()));
        //filtrowanie na podstawie lotniska źródłowego
        reservationFilter.addPredicate(testedValue -> ticketOrderService.findByReservation(testedValue).get(0).getTicketCategory().getFlight().getPlaceOfDestination().toLowerCase().contains(destinationFilter.getText().toLowerCase()));
        //filtrowanie na podstawie daty rezerwacji
        reservationFilter.addPredicate(testedValue -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            return datePicker.getValue() == null || datePicker.getValue().isEqual(LocalDate.parse(testedValue.getReservationDate(), formatter));
        });
        // dodanie do filtrów obserwatorów zmiany wartości (sprawdzanie predykatów po zmianie wartości filtra)
        reservationFilter.setListener(userNameFilter.textProperty());
        reservationFilter.setListener(departureFilter.textProperty());
        reservationFilter.setListener(destinationFilter.textProperty());
        reservationFilter.setListener(datePicker.valueProperty());
    }

    public ReservationsViewController(ReservationService reservationService,
                                      TicketOrderService ticketOrderService,
                                      AirlineService airlineService,
                                      UserPrincipalService userPrincipalService,
                                      NavigationController navigationController) {
        this.navigationController = navigationController;
        this.reservationService = reservationService;
        this.ticketOrderService = ticketOrderService;
        this.airlineService = airlineService;
        this.userPrincipalService = userPrincipalService;
    }

    @FXML
    public void initialize() {
        this.setModel();
        this.setPredicates();
        reservationListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
    }

    /**
     * Metoda służąca do przejścia do widoku do dodawania/edycji rezerwacji
     * @param actionEvent
     * @param flight
     * @param reservation - rezerwacja do edycji (może być nullem)
     */
    public void showAddReservation(ActionEvent actionEvent, Flight flight, Reservation reservation) {
        navigationController.showAddReservation(actionEvent, flight, reservation);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var reservations = reservationListTable.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        reservations.forEach(reservation -> ticketOrderService.deleteAll(FXCollections.observableList(ticketOrderService.findByReservation(reservation))));
        reservationService.deleteAll(FXCollections.observableList(reservations));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var reservation = reservationListTable.getSelectionModel().getSelectedItem();
        if(reservation != null) {
            this.showAddReservation(event, ticketOrderService.findByReservation(reservation.getValue()).get(0).getTicketCategory().getFlight(), reservation.getValue());
        }
    }

    /**
     * Metoda resetująca filtry
     */
    public void resetFilters() {
        destinationFilter.clear();
        departureFilter.clear();
        userNameFilter.clear();
        datePicker.setValue(null);
    }

    /**
     * Metoda ustawiająca powiązanie atrybutu 'disabled' przycisków z zaznaczeniem w tabeli
     * Po to aby przyciski Delete, Update i AddReservation były nieaktywne w sytuacji gdy nic nie jest zaznaczone w tabeli
     */
    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteReservation != null) {
            buttonDeleteReservation.disableProperty().bind(
                    Bindings.isEmpty(reservationListTable.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateReservation != null) {
            buttonUpdateReservation.disableProperty().bind(
                    Bindings.size(reservationListTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }

    }
}