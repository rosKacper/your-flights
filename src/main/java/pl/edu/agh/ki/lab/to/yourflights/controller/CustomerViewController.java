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
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;

import java.io.IOException;

/**
 * Kontroler widoku tabeli klientów
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class CustomerViewController {

    private final Resource mainView;
    private final Resource addCustomerView;

    /**
     * Serwis pozwalający na pobieranie i zapisywanie klientów
     */
    private CustomerService customerService;

    /**
     * Kontekst aplikacji Springa
     */
    private final ApplicationContext applicationContext;

    /**
     * Tabela klientów
     */
    @FXML
    private JFXTreeTableView<Customer> customersTableView;

    /**
     * Kolumny tabeli
     */
    @FXML
    private TreeTableColumn<Customer, String> firstNameColumn;
    @FXML
    private TreeTableColumn<Customer, String> secondNameColumn;
    @FXML
    private TreeTableColumn<Customer, String> countryColumn;
    @FXML
    private TreeTableColumn<Customer, String> cityColumn;


    /**
     * Metoda która wczytuje dane do tabeli przwoźników
     */
    public void setModel() {
        //Ustawienie kolumn
        firstNameColumn.setCellValueFactory(data -> data.getValue().getValue().getFirstNameProperty());
        secondNameColumn.setCellValueFactory(data -> data.getValue().getValue().getSecondNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        cityColumn.setCellValueFactory(data -> data.getValue().getValue().getCityProperty());

        //Pobranie klientów z serwisu
        ObservableList<Customer> customers = customerService.getMockData();

        //Przekazanie danych do tabeli
        final TreeItem<Customer> root = new RecursiveTreeItem<>(customers, RecursiveTreeObject::getChildren);
        customersTableView.setRoot(root);
        customersTableView.setShowRoot(false);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param customerService serwis pozwalający na pobieranie danych o klientach
     * @param mainView główny widok aplikacji
     * @param addCustomerView widok formularza do dodawania klientów
     * @param applicationContext kontekst aplikacji Springa
     */
    public CustomerViewController(CustomerService customerService,
                                  @Value("classpath:/view/MainView.fxml") Resource mainView,
                                  @Value("classpath:/view/addCustomers.fxml") Resource addCustomerView,
                                  ApplicationContext applicationContext) {
        this.customerService = customerService;
        this.mainView = mainView;
        this.addCustomerView = addCustomerView;
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
     * Metoda służąca do przejścia do widoku formularza do dodawania klientów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAddCustomer(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addCustomerView.getURL());
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
