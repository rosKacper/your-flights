package pl.edu.agh.ki.lab.to.yourflights.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;

@Component
public class FlightController {

    private final NavigationController navigationController;
    private final FlightService flightService;
    private final AirlineService airlineService;
    private final UserPrincipalService userPrincipalService;
    private final ApplicationContext applicationContext;


    @FXML
    private JFXTreeTableView<Flight> flightsTableView;

    @FXML
    private TreeTableColumn<Flight, String> departure;
    @FXML
    private TreeTableColumn<Flight, String> destination;
    @FXML
    private TreeTableColumn<Flight, String> departureDate;
    @FXML
    private TreeTableColumn<Flight, String> arrivalDate;
    @FXML
    private TreeTableColumn<Flight, String> airlineName;
    @FXML
    private TreeTableColumn<Flight, String> departureTime;
    @FXML
    private TreeTableColumn<Flight, String> arrivalTime;

    //Pola filtrów
    @FXML
    private JFXTextField departureInput;
    @FXML
    private JFXTextField destinationInput;
    @FXML
    private JFXDatePicker datePicker;

    //Lista zawierająca predykaty służące do filtrowania danych
    private final List<Predicate<Flight>> predicates = new LinkedList<>();

    @FXML
    private JFXButton buttonAddReservation;
    @FXML
    private JFXButton buttonDeleteFlight;
    @FXML
    private JFXButton buttonUpdateFlight;
    @FXML
    private JFXButton buttonShowFlightDetails;
    @FXML
    private JFXButton buttonShowTicketCategories;

    /**
     * Metoda która wczytuje dane do tabeli lotów
     */
    public void setModel() {
        //Ustawienie kolumn
        departure.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDepartureProperty());
        destination.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDestinationProperty());
        departureDate.setCellValueFactory(data -> data.getValue().getValue().getdepartureDateProperty());
        arrivalDate.setCellValueFactory(data -> data.getValue().getValue().getarrivalDateProperty());
        airlineName.setCellValueFactory(data-> data.getValue().getValue().getAirlineNameProperty());
        departureTime.setCellValueFactory(data-> data.getValue().getValue().getDepartureTimeProperty());
        arrivalTime.setCellValueFactory(data-> data.getValue().getValue().getArrivalTimeProperty());

        //Pobranie lotów z serwisu
        //Jeśli zalogowanym użytkownikiem jest linia lotnicza, to pobierze tylko loty danej linii
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList<Flight> flights;

        if(role.equals("[AIRLINE]")){
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if(userDetails instanceof UserDetails){
                username = ((UserDetails)userDetails).getUsername();
            }
           flights = FXCollections.observableList(flightService.findByAirline(airlineService.findByUser(userPrincipalService.findByUsername(username).get(0))));
        }
        else{
            flights = FXCollections.observableList(flightService.findAll());
        }

        //Przekazanie danych do tabeli
        final TreeItem<Flight> root = new RecursiveTreeItem<>(flights, RecursiveTreeObject::getChildren);
        flightsTableView.setRoot(root);
        flightsTableView.setShowRoot(false);
    }

    public FlightController(FlightService flightService, AirlineService airlineService,
                            UserPrincipalService userPrincipalService, ApplicationContext applicationContext,
                            NavigationController navigationController) {
        this.applicationContext = applicationContext;
        this.navigationController = navigationController;
        this.flightService = flightService;
        this.airlineService = airlineService;
        this.userPrincipalService = userPrincipalService;
    }

    /**
     * Metoda która inicjalizuje obsługę filtrowanie
     */
    private void setPredicates() {
        // Generyczna klasa filtrów dla danego modelu
        GenericFilter<Flight> airlineFilter = new GenericFilter<>(flightsTableView);
        // Dodanie do listy predykatów testujących zawartość filtrów
        //filtrowanie na podstawie lotniska źródłowego
        airlineFilter.addPredicate(testedValue -> testedValue.getPlaceOfDeparture().toLowerCase().contains(departureInput.getText().toLowerCase()));
        //filtrowanie na podstawie lotniska docelowego
        airlineFilter.addPredicate(testedValue -> testedValue.getPlaceOfDestination().toLowerCase().contains(destinationInput.getText().toLowerCase()));
        //filtrowanie na podstawie daty wylotu
        airlineFilter.addPredicate(testedValue -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    return datePicker.getValue() == null || datePicker.getValue().isEqual(LocalDate.parse(testedValue.getDepartureDate(), formatter));
        });
        // dodanie do filtrów obserwatorów zmiany wartości (sprawdzanie predykatów po zmianie wartości filtra)
        airlineFilter.setListener(departureInput.textProperty());
        airlineFilter.setListener(destinationInput.textProperty());
        airlineFilter.setListener(datePicker.valueProperty());
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
        setPredicates();
        flightsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setButtonsDisablePropertyBinding();
    }

    /**
     * Metoda służąca do przejścia do widoku tabeli rezerwacji
     * @param actionEvent event emitowany przez przycisk
     */
    public void showReservationsView(ActionEvent actionEvent) {
        navigationController.showReservationsView(actionEvent);
    }

    /**
     * Metoda służąca do przejścia do formularza dodawania/edycji rezerwacji
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddReservation(ActionEvent actionEvent, Flight flight) {
        navigationController.showAddReservation(actionEvent, flight, null);
    }

    /**
     * Metoda służąca do przejścia do widoku formularza do dodawania/edycji lotów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddFlight(ActionEvent actionEvent, Flight flight) {
        navigationController.showAddFlight(actionEvent, flight);
    }

    public void handleShowFlightDetailsView(ActionEvent event) {
        var flight = flightsTableView.getSelectionModel().getSelectedItem();
        if(flight != null) {
            this.showFlightDetailsView(event, flight.getValue());
        }
    }

    /**
     * Metoda służąca do przejścia do widoku szczegółów lotu
     * @param actionEvent event emitowany przez przycisk
     */
    public void showFlightDetailsView(ActionEvent actionEvent, Flight flight) {
        navigationController.showFlightDetailsView(actionEvent, flight);
    }

    /**
     * Metoda służąca do przejścia do widoku kategorii biletów dla danego lotu
     * @param actionEvent event emitowany przez przycisk
     * @param flight obiekt lotu
     */
    public void showTicketCategoriesView(ActionEvent actionEvent, Flight flight) {
        navigationController.showTicketCategoriesView(actionEvent, flight);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var flights = flightsTableView.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        flightService.deleteAll(FXCollections.observableList(flights));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var flight = flightsTableView.getSelectionModel().getSelectedItem();
        if(flight != null) {
            this.showAddFlight(event, flight.getValue());
        }
    }

    @FXML
    private void handleShowTicketCategories(ActionEvent event) {
        var flight = flightsTableView.getSelectionModel().getSelectedItem();
        if(flight != null) {
            this.showTicketCategoriesView(event, flight.getValue());
        }
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        this.showAddFlight(event, null);
    }

    @FXML
    private void handleAddReservationAction(ActionEvent event) {
        var flight = flightsTableView.getSelectionModel().getSelectedItem();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymous") && flight != null) {
            this.showAddReservation(event, flight.getValue());
        }
    }

    /**
     * Metoda resetująca filtry
     */
    public void resetFilters() {
        departureInput.clear();
        destinationInput.clear();
        datePicker.setValue(null);
    }

    /**
     * Metoda ustawiająca powiązanie atrybutu 'disabled' przycisków z zaznaczeniem w tabeli
     * Po to aby przyciski Delete, Update i AddReservation były nieaktywne w sytuacji gdy nic nie jest zaznaczone w tabeli
     */
    private void setButtonsDisablePropertyBinding() {
        if(buttonAddReservation != null) {
            buttonAddReservation.disableProperty().bind(
                    Bindings.size(flightsTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
        if(buttonDeleteFlight != null) {
            buttonDeleteFlight.disableProperty().bind(
                    Bindings.isEmpty(flightsTableView.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateFlight != null) {
            buttonUpdateFlight.disableProperty().bind(
                    Bindings.size(flightsTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
        if(buttonShowFlightDetails != null) {
            buttonShowFlightDetails.disableProperty().bind(
                    Bindings.size(flightsTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
        if(buttonShowTicketCategories != null) {
            buttonShowTicketCategories.disableProperty().bind(
                    Bindings.size(flightsTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
    }
}