package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;

import java.io.IOException;

/**
 * Kontroler widoku tabeli rezerwacji
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class ReservationViewController {

    private ReservationService reservationService;
    private final Resource mainView;
    private final Resource customersView;
    private final Resource airlineView;
    private final ApplicationContext applicationContext;
    private final Resource flightView;
    private final Resource userFlightView;

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
    private TreeTableColumn<Customer, String> firstName;
    @FXML
    private TreeTableColumn<Customer, String> lastName;
    @FXML
    private TreeTableColumn<TicketCategory, String> flight_ID ;
    @FXML
    private TreeTableColumn<Flight, String> departure_time ;
    @FXML
    private TreeTableColumn<Flight, String> departure_place ;


    /**
     * Metoda która wczytuje dane do tabeli rezerwacji
     */
    public void setModel() {
        //Ustawienie kolumn
        reservationDate.setCellValueFactory(data -> data.getValue().getValue().getReservationDateProperty());
        firstName.setCellValueFactory(data -> data.getValue().getValue().getFirstNameProperty());
        //lastName.setCellValueFactory(data -> data.getValue().getValue().getSecondNameProperty());
        //flight_ID.setCellValueFactory(data -> data.getValue().getValue().getFlightIDProperty());
        //departure_place.setCellValueFactory(data -> data.getValue().getValue().getplaceOfDepartureProperty());
        //departure_time.setCellValueFactory(data -> data.getValue().getValue().getdepartureTimeProperty());

        //Pobranie rezerwacje z serwisu
        //ObservableList<Airline> airlines = airlineService.getMockData();

        //Przekazanie danych do tabeli
        //final TreeItem<Airline> root = new RecursiveTreeItem<Airline>(airlines, RecursiveTreeObject::getChildren);
        //airlinesTableView.setRoot(root);
        //airlinesTableView.setShowRoot(false);

    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param reservationService serwis do pobierania danych o rezerwacji
     * @param mainView główny widok aplikacji
     * @param AirlineView widok formularza do przewoźników
     * @param applicationContext kontekst aplikacji Springa
     */
    public ReservationViewController(ReservationService reservationService,
                                     @Value("classpath:/view/MainView/MainView.fxml") Resource mainView,
                                     @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                                     @Value("classpath:/view/AirlinesView.fxml") Resource AirlineView,
                                     @Value("classpath:/view/FlightView.fxml") Resource flightView,
                                     @Value("classpath:/view/UserView/UserFlightView.fxml") Resource userFlightView,
                                     ApplicationContext applicationContext) {
        this.reservationService = reservationService;
        this.mainView = mainView;
        this.airlineView = AirlineView;
        this.applicationContext = applicationContext;
        this.customersView = customersView;
        this.flightView = flightView;
        this.userFlightView = userFlightView;
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
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

    public void showFlightView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(flightView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userFlightView.getURL());
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


}