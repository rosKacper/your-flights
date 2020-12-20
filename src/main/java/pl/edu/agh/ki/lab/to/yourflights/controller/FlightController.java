package pl.edu.agh.ki.lab.to.yourflights.controller;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import pl.edu.agh.ki.lab.to.yourflights.JavafxApplication;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;


@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final Resource mainView;
    private final Resource customersView;
    private final Resource airlinesView;
    private final Resource reservationListView;
    private final Resource addFlightView;
    private final Resource anonymousMainView;
    private final Resource anonymousAirlineView;
    private final Resource loginView;
    private final Resource userAirlineView;
    private final Resource userReservationView;
    private final Resource userCustomersView;


    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    /**
     * Kontekst aplikacji Springa
     */
    private final ApplicationContext applicationContext;

    @FXML
    private JFXTreeTableView<Flight> flightsTableView;
    /**
     * Kolumny tabeli
     */
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
        ObservableList<Flight> flights = FXCollections.observableList(flightService.findAll());

        //Przekazanie danych do tabeli
        final TreeItem<Flight> root = new RecursiveTreeItem<>(flights, RecursiveTreeObject::getChildren);
        flightsTableView.setRoot(root);
        flightsTableView.setShowRoot(false);
    }
    public FlightController(FlightService flightService, ApplicationContext applicationContext,
                            @Value("classpath:/view/AirlinesView.fxml") Resource airlinesView,
                            @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                            @Value("classpath:/view/MainView/MainView.fxml") Resource mainView,
                            @Value("classpath:/view/ReservationListView.fxml") Resource reservationListView,
                            @Value("classpath:/view/AddFlightView.fxml") Resource addFlightView,
                            @Value("classpath:/view/AuthView/LoginView.fxml") Resource loginView,
                            @Value("classpath:/view/MainView/AnonymousMainView.fxml") Resource anonymousMainView,
                            @Value("classpath:/view/UserView/UserAirlinesView.fxml") Resource userAirlineView,
                            @Value("classpath:/view/UserView/UserReservationView.fxml") Resource userReservationView,
                            @Value("classpath:/view/AnonymousView/AnonymousAirlinesView.fxml") Resource anonymousAirlineView,
                            @Value("classpath:/view/UserView/UserCustomersView.fxml") Resource userCustomersView
                            ) {
        this.applicationContext = applicationContext;
        this.airlinesView = airlinesView;
        this.customersView = customersView;
        this.mainView = mainView;
        this.flightService = flightService;
        this.reservationListView = reservationListView;
        this.addFlightView = addFlightView;
        this.loginView = loginView;
        this.anonymousMainView = anonymousMainView;
        this.anonymousAirlineView = anonymousAirlineView;
        this.userAirlineView = userAirlineView;
        this.userReservationView = userReservationView;
        this.userCustomersView = userCustomersView;
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
    }

    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousMainView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(mainView.getURL());
            }
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAirlinesView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousAirlineView.getURL());
            }
            else if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(airlinesView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userAirlineView.getURL());
            }
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metoda służąca do przejścia do widoku tabeli klientów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showCustomersView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(customersView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userCustomersView.getURL());
            }
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservation(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(reservationListView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userReservationView.getURL());
            }
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda służąca do przejścia do widoku formularza do dodawania/edycji lotów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddFlight(ActionEvent actionEvent, Flight flight) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addFlightView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            if(flight != null) {
                AddFlightController controller = fxmlloader.getController();
                controller.setData(flight);
            }

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLoginView(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(loginView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void handleAddAction(ActionEvent event) {
        this.showAddFlight(event, null);
    }

    /**
     * Metoda zapewniająca możliwość wylogowania użytkownika
     * @param event event emitowany przez przycisk
     */
    @FXML
    void handleLogout(ActionEvent event) {
        JavafxApplication.logout();
        try {
            FXMLLoader fxmlloader = new FXMLLoader(anonymousMainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}