package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;

import java.io.IOException;

/**
 * Kontroler widoku tabeli przewoźników
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class ReservationListController {

    private AirlineService reservationService;
    private final Resource mainView;
    private final Resource customersView;
    private final Resource airlineView;
    private final ApplicationContext applicationContext;

    /**
     * Tabela przewoźników
     */
    @FXML
    private JFXTreeTableView<Airline> airlinesTableView;

    /**
     * Kolumny tabeli
     */
    @FXML
    private TreeTableColumn<Airline, String> reservationDate;
    @FXML
    private TreeTableColumn<Airline, String> customer_ID;
    @FXML
    private TreeTableColumn<Airline, String> reservation_ID;


    /**
     * Metoda która wczytuje dane do tabeli przwoźników
     */
    public void setModel() {
        //Ustawienie kolumn
        reservationDate.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        customer_ID.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        reservation_ID.setCellValueFactory(data -> data.getValue().getValue().getDescriptionProperty());

        //Pobranie przewoźników z serwisu
        //ObservableList<Airline> airlines = airlineService.getMockData();

        //Przekazanie danych do tabeli
        //final TreeItem<Airline> root = new RecursiveTreeItem<Airline>(airlines, RecursiveTreeObject::getChildren);
        //airlinesTableView.setRoot(root);
        //airlinesTableView.setShowRoot(false);

    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param airlineService serwis do pobierania danych o przewoźnikach
     * @param mainView główny widok aplikacji
     * @param AirlineView widok formularza do przewoźników
     * @param applicationContext kontekst aplikacji Springa
     */
    public ReservationListController(AirlineService airlineService,
                                  @Value("classpath:/view/MainView.fxml") Resource mainView,
                                  @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                                  @Value("classpath:/view/AirlinesView.fxml") Resource AirlineView,

                                  ApplicationContext applicationContext) {
        this.reservationService = airlineService;
        this.mainView = mainView;
        this.airlineView = AirlineView;
        this.applicationContext = applicationContext;
        this.customersView = customersView;
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