package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Airline;
import pl.edu.agh.ki.lab.to.yourflights.service.AirlineService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;
import pl.edu.agh.ki.lab.to.yourflights.utils.GenericFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler widoku tabeli przewoźników
 */
@Component
public class AirlinesViewController {

    private AirlineService airlineService;
    private UserPrincipalService userPrincipalService;
    private final NavigationController navigationController;

    private final ApplicationContext applicationContext;
    GenericFilter<Airline> airlineFilter;

    @FXML
    private JFXTreeTableView<Airline> airlinesTableView;

    @FXML
    private TreeTableColumn<Airline, String> nameColumn;
    @FXML
    private TreeTableColumn<Airline, String> countryColumn;
    @FXML
    private TreeTableColumn<Airline, String> descriptionColumn;

    @FXML
    private JFXTextField nameInput;
    @FXML
    private ComboBox<String> countryPicker;

    @FXML
    private JFXButton buttonDeleteAirline;
    @FXML
    private JFXButton buttonUpdateAirline;


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
            airlineList.add(airlineService.findByUser(userPrincipalService.findByUsername(username).get(0)));
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

    public AirlinesViewController(AirlineService airlineService, UserPrincipalService userPrincipalService,
                                  NavigationController navigationController,
                                  ApplicationContext applicationContext) {
        this.navigationController = navigationController;
        this.airlineService = airlineService;
        this.applicationContext = applicationContext;
        this.userPrincipalService = userPrincipalService;
    }


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

    @FXML
    public void initialize() {
        this.setModel();
        this.setCountryPickerItems();
        setPredicates();
        airlinesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
    }

    public void showAddAirline(ActionEvent actionEvent, Airline airline) {
        navigationController.showAddAirline(actionEvent, airline);
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

    private void setCountryPickerItems() {
        countryPicker.getItems().setAll(airlineService.getCountries());
    }

    public void resetFilters() {
        nameInput.clear();
        countryPicker.setValue("");
    }

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
