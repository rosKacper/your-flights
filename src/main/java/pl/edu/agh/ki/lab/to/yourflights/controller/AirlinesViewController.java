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
public class AirlinesViewController {

    private AirlineService airlineService;
    private final Resource mainView;
    private final Resource addAirlineView;
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
    private TreeTableColumn<Airline, String> nameColumn;
    @FXML
    private TreeTableColumn<Airline, String> countryColumn;
    @FXML
    private TreeTableColumn<Airline, String> descriptionColumn;


    /**
     * Metoda która wczytuje dane do tabeli przwoźników
     */
    public void setModel() {
        //Ustawienie kolumn
        nameColumn.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().getValue().getDescriptionProperty());

        //Pobranie przewoźników z serwisu
        ObservableList<Airline> airlines = airlineService.getMockData();

        //Przekazanie danych do tabeli
        final TreeItem<Airline> root = new RecursiveTreeItem<Airline>(airlines, RecursiveTreeObject::getChildren);
        airlinesTableView.setRoot(root);
        airlinesTableView.setShowRoot(false);

    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param airlineService serwis do pobierania danych o przewoźnikach
     * @param mainView główny widok aplikacji
     * @param addAirlineView widok formularza do dodawania przewoźników
     * @param applicationContext kontekst aplikacji Springa
     */
    public AirlinesViewController(AirlineService airlineService,
                                  @Value("classpath:/view/MainView.fxml") Resource mainView,
                                  @Value("classpath:/view/addAirlineView.fxml") Resource addAirlineView,
                                  ApplicationContext applicationContext) {
        this.airlineService = airlineService;
        this.mainView = mainView;
        this.addAirlineView = addAirlineView;
        this.applicationContext = applicationContext;
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
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda służąca do przejścia do widoku formularza dodawania przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddAirline(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addAirlineView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
