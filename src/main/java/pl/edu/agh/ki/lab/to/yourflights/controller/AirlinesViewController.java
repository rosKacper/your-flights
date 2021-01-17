package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.JavafxApplication;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;
import pl.edu.agh.ki.lab.to.yourflights.model.UserPrincipal;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler widoku tabeli przewoźników
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AirlinesViewController {

    /**
     * Serwis linii lotniczych
     */
    private AirlineService airlineService;
    private UserPrincipalService userPrincipalService;

    /**
     * Widoki
     */
    private final Resource mainView;
    private final Resource addAirlineView;
    private final Resource reservationListView;
    private final Resource reservationListViewCustomer;
    private final Resource anonymousMainView;
    private final Resource flightView;
    private final Resource anonymousFlightView;
    private final Resource userFlightView;
    private final Resource loginView;
    private final Resource customersView;
    private final Resource userCustomersView;


    /**
     * Kontekrs aplikacji Springa
     */
    private final ApplicationContext applicationContext;
    GenericFilter<Airline> airlineFilter;

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
    private ComboBox<String> countryPicker;

    /**
     * Przyciski
     */
    @FXML
    private JFXButton buttonDeleteAirline;
    @FXML
    private JFXButton buttonUpdateAirline;

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
        //Jeśli zalogowanym użytkownikiem jest linia lotnicza, wyświetli tylko dane o sobie

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList<Airline> airlines;

        if(role.equals("[AIRLINE]")){
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if(userDetails instanceof UserDetails){
                username = ((UserDetails)userDetails).getUsername();
            }
            List<Airline> airlineList = new ArrayList<>();
            airlineList.add(airlineService.findByUser(userPrincipalService.findByUsername(username)));
            airlines = FXCollections.observableList(airlineList);
        }
        else{
            airlines = FXCollections.observableList(airlineService.findAll());
        }




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
    public AirlinesViewController(AirlineService airlineService, UserPrincipalService userPrincipalService,
                                  @Value("classpath:/view/MainView/MainView.fxml") Resource mainView,
                                  @Value("classpath:/view/CustomersView.fxml") Resource customersView,
                                  @Value("classpath:/view/AddAirlineView.fxml") Resource addAirlineView,
                                  @Value("classpath:/view/ReservationListView.fxml") Resource reservationList,
                                  @Value("classpath:/view/MainView/AnonymousMainView.fxml") Resource anonymousMainView,
                                  @Value("classpath:/view/FlightView.fxml") Resource flightView,
                                  @Value("classpath:/view/AnonymousView/AnonymousFlightView.fxml") Resource anonymousFlightView,
                                  @Value("classpath:/view/UserView/UserFlightView.fxml") Resource userFlightView,
                                  @Value("classpath:/view/UserView/UserCustomersView.fxml") Resource userCustomersView,
                                  @Value("classpath:/view/AuthView/LoginView.fxml") Resource loginView,
                                  @Value("classpath:/view/ReservationListViewCustomer.fxml") Resource reservationListViewCustomer,
                                  ApplicationContext applicationContext) {
        this.airlineService = airlineService;
        this.mainView = mainView;
        this.addAirlineView = addAirlineView;
        this.applicationContext = applicationContext;
        this.customersView = customersView;
        this.anonymousMainView = anonymousMainView;
        this.reservationListView = reservationList;
        this.flightView = flightView;
        this.anonymousFlightView = anonymousFlightView;
        this.loginView = loginView;
        this.userCustomersView = userCustomersView;
        this.userFlightView = userFlightView;
        this.reservationListViewCustomer = reservationListViewCustomer;
        this.userPrincipalService = userPrincipalService;
    }

    /**
     * Metoda która inicjalizuje obsługę filtrowania
     */
    private void setPredicates() {
        // Generyczna klasa filtrów dla danego modelu
        airlineFilter = new GenericFilter<>(airlinesTableView);
        // Dodanie do listy predykatów testujących zawartość filtrów
        //filtrowanie na podstawie nazwy
        airlineFilter.addPredicate( testedValue -> testedValue.getName().toLowerCase().contains(nameInput.getText().toLowerCase()));
        //filtrowanie na podstawie kraju
        airlineFilter.addPredicate( testedValue ->
                countryPicker.getValue() == null ||
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
        this.setCountryPickerItems();
        setPredicates();
        airlinesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
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
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ADMIN]") || role.equals("[AIRLINE]")){
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

    /**
     * Metoda służąca do przejścia do widoku tabeli rezerwacji
     * @param actionEvent event emitowany przez przycisk
     */
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
     * Metoda służąca do przejścia do widoku przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showFlightView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousFlightView.getURL());
            }
            else if(role.equals("[ROLE_ADMIN]") || role.equals("[AIRLINE]")){
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
     * Metoda służąca do przejścia do widoku logowania
     * @param actionEvent event emitowany przez przycisk
     */
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
        this.setCountryPickerItems();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var airline = airlinesTableView.getSelectionModel().getSelectedItem();
        if(airline != null) {
            this.showAddAirline(event, airline.getValue());
        }
    }

    /**
     * Metoda obsługująca wylogowanie użytkownika
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

    /**
     * Metoda ustawiająca listę krajów w liście rozwijanej służącej do filtrowania linii lotniczych po kraju
     */
    private void setCountryPickerItems() {
        countryPicker.getItems().setAll(airlineService.getCountries());
    }

    /**
     * Metoda służąca do zresetowania filtrów
     */
    public void resetFilters() {
        nameInput.clear();
        countryPicker.setValue("");
    }

    /**
     * Metoda ustawiająca powiązanie atrybutu 'disabled' przycisków z zaznaczeniem w tabeli
     * Po to aby przyciski Delete i Update były nieaktywne w sytuacji gdy nic nie jest zaznaczone w tabeli
     */
    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteAirline != null) {
            buttonDeleteAirline.disableProperty().bind(
                    Bindings.isEmpty(airlinesTableView.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateAirline != null) {
            buttonUpdateAirline.disableProperty().bind(
                    Bindings.size(airlinesTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }
    }
}
