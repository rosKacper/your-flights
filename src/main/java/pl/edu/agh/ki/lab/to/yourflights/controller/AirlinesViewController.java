package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Kontroler widoku tabeli przewoźników
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AirlinesViewController {

    private AirlineService airlineService;
    private final Resource mainView;
    private final Resource customersView;
    private final Resource addAirlineView;
    private final Resource reservationList;
    private final Resource anonymousMainView;
    private final Resource flightView;
    private final Resource anonymousFlightView;
    private final Resource loginView;
    private final ApplicationContext applicationContext;


    // Lista służąca do filtrowania kraju pochodzenia przewoźnika
    private static final List<String> COUNTRIES =
            Collections.unmodifiableList(Arrays.asList("POLSKA", "HISZPANIA", "NIEMCY"));


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
     * Pola służące do filtrowania linii lotniczych
     * nameInput - nazwa linii lotniczwej
     * countryPicker - combobox zawierający kraje
     */
    @FXML
    private JFXTextField nameInput;
    @FXML
    private JFXComboBox<String> countryPicker;



    /**
     * Metoda która wczytuje dane do tabeli przwoźników
     */
    public void setModel() {
        //Ustawienie kolumn
        nameColumn.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().getValue().getDescriptionProperty());

        //Ustawienie zawijania tekstu w kolumnie 'description'
        descriptionColumn.setCellFactory(data -> {
            TreeTableCell<Airline, String> cell = new TreeTableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            text.wrappingWidthProperty().setValue(descriptionColumn.widthProperty().getValue() - 10);
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        //Pobranie przewoźników z serwisu
        ObservableList<Airline> airlines = FXCollections.observableList(airlineService.findAll());

        //Przekazanie danych do tabeli
        final TreeItem<Airline> root = new RecursiveTreeItem<>(airlines, RecursiveTreeObject::getChildren);
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
                                  @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                                  @Value("classpath:/view/AddAirlineView.fxml") Resource addAirlineView,
                                  @Value("classpath:/view/ReservationListView.fxml") Resource reservationList,
                                  @Value("classpath:/view/AnonymousMainView.fxml") Resource anonymousMainView,
                                  @Value("classpath:/view/FlightView.fxml") Resource flightView,
                                  @Value("classpath:/view/AnonymousFlightView.fxml") Resource anonymousFlightView,
                                  @Value("classpath:/view/LoginView.fxml") Resource loginView,
                                  ApplicationContext applicationContext) {
        this.airlineService = airlineService;
        this.mainView = mainView;
        this.addAirlineView = addAirlineView;
        this.applicationContext = applicationContext;
        this.customersView = customersView;
        this.anonymousMainView = anonymousMainView;
        this.reservationList=reservationList;
        this.flightView = flightView;
        this.anonymousFlightView = anonymousFlightView;
        this.loginView = loginView;
    }

    /**
     * Metoda która inicjalizuje obsługę filtrowania
     */
    private void setPredicates() {
        // Generyczna klasa filtrów dla danego modelu
        GenericFilter<Airline> airlineFilter = new GenericFilter<>(airlinesTableView);
        // Dodanie do listy predykatów testujących zawartość filtrów
        //filtrowanie na podstawie nazwy
        airlineFilter.addPredicate( testedValue -> testedValue.getName().toLowerCase().contains(nameInput.getText().toLowerCase()));
        //filtrowanie na podstawie kraju
        airlineFilter.addPredicate( testedValue -> countryPicker.getValue() == null ||
                    countryPicker.getValue().length() == 0 ||
                    testedValue.getCountry().toLowerCase().equals(countryPicker.getValue().toLowerCase())
        );
        // dodanie do filtrów obserwatorów zmiany wartości (sprawdzanie predykatów po zmianie wartości filtra)
        airlineFilter.setListener(nameInput.textProperty());
        airlineFilter.setListener(countryPicker.valueProperty());
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
        countryPicker.getItems().addAll(COUNTRIES);
        setPredicates();
    }

    /**
     * Metoda służąca do przejścia do głównego widoku
     * @param actionEvent event emitowany przez przycisk
     */
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

    /**
     * Metoda służąca do przejścia do widoku formularza dodawania/edycji przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddAirline(ActionEvent actionEvent, Airline airline) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addAirlineView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            if(airline != null) {
                AddAirlineController controller = fxmlloader.getController();
                controller.setData(airline);
            }

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
            FXMLLoader fxmlloader = new FXMLLoader(reservationList.getURL());
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
            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousFlightView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(flightView.getURL());
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
        var airlines = airlinesTableView.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        airlineService.deleteAll(FXCollections.observableList(airlines));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var airline = airlinesTableView.getSelectionModel().getSelectedItem();
        if(airline != null) {
            this.showAddAirline(event, airline.getValue());
        }
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        this.showAddAirline(event, null);
    }


}
