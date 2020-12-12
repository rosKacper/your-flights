package pl.edu.agh.ki.lab.to.yourflights.controller;

import java.io.IOException;

import java.util.stream.Collectors;

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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.repository.FlightRepository;
import pl.edu.agh.ki.lab.to.yourflights.service.FlightService;


@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final Resource mainView;
    private final Resource customersView;
    private final Resource airlinesView;
    private final Resource reservationListView;
    private final Resource addFlightView;

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
    private TreeTableColumn<Flight, String> departureTime;
    @FXML
    private TreeTableColumn<Flight, String> arrivalTime;

    /**
     * Metoda która wczytuje dane do tabeli lotów
     */
    public void setModel() {
        //Ustawienie kolumn
        departure.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDepartureProperty());
        destination.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDestinationProperty());
        departureTime.setCellValueFactory(data -> data.getValue().getValue().getdepartureTimeProperty());
        arrivalTime.setCellValueFactory(data -> data.getValue().getValue().getarrivalTimeProperty());

        //Pobranie lotów z serwisu
        ObservableList<Flight> flights = FXCollections.observableList(flightService.findAll());

        //Przekazanie danych do tabeli
        final TreeItem<Flight> root = new RecursiveTreeItem<Flight>(flights, RecursiveTreeObject::getChildren);
        flightsTableView.setRoot(root);
        flightsTableView.setShowRoot(false);
    }
    public FlightController(FlightService flightService, ApplicationContext applicationContext,
                            @Value("classpath:/view/AirlinesView.fxml") Resource airlinesView,
                            @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                            @Value("classpath:/view/MainView.fxml") Resource mainView,
                            @Value("classpath:/view/ReservationListView.fxml") Resource reservationListView,
                            @Value("classpath:/view/AddFlightView.fxml") Resource addFlightView) {
        this.applicationContext = applicationContext;
        this.airlinesView = airlinesView;
        this.customersView = customersView;
        this.mainView = mainView;
        this.flightService = flightService;
        this.reservationListView = reservationListView;
        this.addFlightView = addFlightView;
    }
    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
    }

    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(mainView.getURL());
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
            FXMLLoader fxmlloader = new FXMLLoader(airlinesView.getURL());
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
            FXMLLoader fxmlloader = new FXMLLoader(customersView.getURL());
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
            FXMLLoader fxmlloader = new FXMLLoader(reservationListView.getURL());
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

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var flights = flightsTableView.getSelectionModel().getSelectedItems().stream().map(item -> item.getValue()).collect(Collectors.toList());
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
}