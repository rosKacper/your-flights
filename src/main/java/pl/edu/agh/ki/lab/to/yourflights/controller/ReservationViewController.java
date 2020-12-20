package pl.edu.agh.ki.lab.to.yourflights.controller;

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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Kontroler widoku tabeli rezerwacji
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class ReservationViewController {

    private final ReservationService reservationService;
    private final Resource mainView;
    private final Resource customersView;
    private final Resource airlineView;
    private final Resource addReservationView;
    private final ApplicationContext applicationContext;

    private GenericFilter<Reservation> reservationFilter;

    /**
     * Tabela rezerwacji
     */
    @FXML
    private JFXTreeTableView<Reservation> reservationListTable;

    /**
     * Kolumny tabeli
     */
    @FXML
    private TreeTableColumn<Reservation, String> reservationDate;
    @FXML
    private TreeTableColumn<Reservation, String> userName;
    @FXML
    private TreeTableColumn<Flight, String> flightID;
    @FXML
    private TreeTableColumn<Flight, String> departure;
    @FXML
    private TreeTableColumn<Flight, String> departureDate;
    @FXML
    private TreeTableColumn<Flight, String> destination;
    @FXML
    private TreeTableColumn<Flight, String> destinationDate;

    @FXML
    private JFXTextField userNameFilter;

    @FXML
    private JFXDatePicker datePicker;


    /**
     * Metoda która wczytuje dane do tabeli rezerwacji
     */
    public void setModel() {
        //Ustawienie kolumn
        reservationDate.setCellValueFactory(data -> data.getValue().getValue().getReservationDateProperty());
        userName.setCellValueFactory(data -> data.getValue().getValue().getUserNameProperty());
        //firstName.setCellValueFactory(data -> data.getValue().getValue().getFirstNameProperty());
        //lastName.setCellValueFactory(data -> data.getValue().getValue().getSecondNameProperty());
        //flight_ID.setCellValueFactory(data -> data.getValue().getValue().getFlightIDProperty());
        //departure_place.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDepartureProperty());
        //departure_time.setCellValueFactory(data -> data.getValue().getValue().getdepartureTimeProperty());

        //Pobranie rezerwacje z serwisu
        //ObservableList<Airline> airlines = airlineService.getMockData();
        ObservableList<Reservation> reservations = FXCollections.observableList(reservationService.findAll());


//        Przekazanie danych do tabeli
        final TreeItem<Reservation> root = new RecursiveTreeItem<Reservation>(reservations, RecursiveTreeObject::getChildren);
        reservationListTable.setRoot(root);
        reservationListTable.setShowRoot(false);

    }

    private void setPredicates() {
        // Generyczna klasa filtrów dla danego modelu
        GenericFilter<Reservation> reservationFilter = new GenericFilter<>(reservationListTable);
        // Dodanie do listy predykatów testujących zawartość filtrów
        //filtrowanie na podstawie nazwy użytkownika
        reservationFilter.addPredicate(testedValue -> testedValue.getUserName().toLowerCase().contains(userNameFilter.getText().toLowerCase()));
        //filtrowanie na podstawie lotniska docelowego
//        reservationFilter.addPredicate(testedValue -> testedValue.getPlaceOfDestination().toLowerCase().contains(destinationInput.getText().toLowerCase()));
        //filtrowanie na podstawie daty rezerwacji
        reservationFilter.addPredicate(testedValue -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            return datePicker.getValue() == null || datePicker.getValue().isEqual(LocalDate.parse(testedValue.getReservationDate(), formatter));
        });
        // dodanie do filtrów obserwatorów zmiany wartości (sprawdzanie predykatów po zmianie wartości filtra)
        reservationFilter.setListener(userNameFilter.textProperty());
//        reservationFilter.setListener(destinationInput.textProperty());
        reservationFilter.setListener(datePicker.valueProperty());
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param reservationService serwis do pobierania danych o rezerwacji
     * @param mainView główny widok aplikacji
     * @param AirlineView widok formularza do przewoźników
     * @param applicationContext kontekst aplikacji Springa
     */
    public ReservationViewController(ReservationService reservationService,
                                     @Value("classpath:/view/MainView.fxml") Resource mainView,
                                     @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                                     @Value("classpath:/view/AirlinesView.fxml") Resource AirlineView,
                                     @Value("classpath:/view/AddReservationView.fxml") Resource addReservationView,
                                     ApplicationContext applicationContext) {
        this.reservationService = reservationService;
        this.mainView = mainView;
        this.airlineView = AirlineView;
        this.applicationContext = applicationContext;
        this.customersView = customersView;
        this.addReservationView = addReservationView;
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
        this.setPredicates();
    }

    /**
     * Metoda służąca do przejścia do głównego widoku
     * @param actionEvent event emitowany przez przycisk
     */
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
            FXMLLoader fxmlloader = new FXMLLoader(airlineView.getURL());
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

    public void showAddReservation(ActionEvent actionEvent, Flight flight) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(addReservationView.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            AddReservationController controller = fxmlLoader.getController();
            controller.setData(flight);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var airlines = reservationListTable.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        reservationService.deleteAll(FXCollections.observableList(airlines));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var reservation = reservationListTable.getSelectionModel().getSelectedItem();
//        if(reservation != null) {
//            this.showAddReservation(event, reservation.getValue());
//        }
    }

}