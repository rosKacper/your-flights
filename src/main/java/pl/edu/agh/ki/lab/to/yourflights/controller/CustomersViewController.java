package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.Customer;
import pl.edu.agh.ki.lab.to.yourflights.service.CustomerService;
import pl.edu.agh.ki.lab.to.yourflights.service.UserPrincipalService;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Kontroler widoku tabeli klientów
 */
@Component
public class CustomersViewController {

    private final NavigationController navigationController;
    private CustomerService customerService;
    private UserPrincipalService userPrincipalService;
    private final ApplicationContext applicationContext;

    @FXML
    private JFXTreeTableView<Customer> customersTableView;

    @FXML
    private TreeTableColumn<Customer, String> firstNameColumn;
    @FXML
    private TreeTableColumn<Customer, String> secondNameColumn;
    @FXML
    private TreeTableColumn<Customer, String> countryColumn;
    @FXML
    private TreeTableColumn<Customer, String> cityColumn;

    @FXML
    private JFXButton buttonDeleteCustomer;
    @FXML
    private JFXButton buttonUpdateCustomer;



    public void setModel() {
        //Ustawienie kolumn
        firstNameColumn.setCellValueFactory(data -> data.getValue().getValue().getFirstNameProperty());
        secondNameColumn.setCellValueFactory(data -> data.getValue().getValue().getSecondNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        cityColumn.setCellValueFactory(data -> data.getValue().getValue().getCityProperty());

        //Pobranie klientów z serwisu
        ObservableList<Customer> customers;// = FXCollections.observableList(customerService.findAll());

        FXMLLoader fxmlloader;
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if(role.equals("[ROLE_ADMIN]")){
            customers = FXCollections.observableList(customerService.findAll());
        }
        else{
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if(userDetails instanceof UserDetails){
                username = ((UserDetails)userDetails).getUsername();
            }
            customers = FXCollections.observableList(customerService.findByUsername(username));
        }

        //Przekazanie danych do tabeli
        final TreeItem<Customer> root = new RecursiveTreeItem<>(customers, RecursiveTreeObject::getChildren);
        customersTableView.setRoot(root);
        customersTableView.setShowRoot(false);
    }


    public CustomersViewController(CustomerService customerService,
                                   NavigationController navigationController,
                                   UserPrincipalService userPrincipalService,
                                   ApplicationContext applicationContext) {
        this.customerService = customerService;
        this.navigationController = navigationController;
        this.applicationContext = applicationContext;
        this.userPrincipalService = userPrincipalService;
    }


    @FXML
    public void initialize() {
        this.setModel();
        customersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.setButtonsDisablePropertyBinding();
    }

    public void showAddCustomerView(ActionEvent actionEvent, Customer customer) {
        navigationController.showAddCustomerView(actionEvent, customer);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        var customers = customersTableView.getSelectionModel().getSelectedItems().stream().map(item -> item.getValue()).collect(Collectors.toList());
        customerService.deleteAll(FXCollections.observableList(customers));
        this.setModel();
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {
        var customer = customersTableView.getSelectionModel().getSelectedItem();
        if(customer != null) {
            this.showAddCustomerView(event, customer.getValue());
        }
    }

    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteCustomer != null) {
            buttonDeleteCustomer.disableProperty().bind(
                    Bindings.isEmpty(customersTableView.getSelectionModel().getSelectedItems())
            );
        }
        if(buttonUpdateCustomer != null) {
            buttonUpdateCustomer.disableProperty().bind(
                    Bindings.size(customersTableView.getSelectionModel().getSelectedItems()).isNotEqualTo(1)
            );
        }

        String r = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList role = FXCollections.observableArrayList(Collections.singletonList(r));

        buttonDeleteCustomer.visibleProperty().bind(
                Bindings.valueAt(role, 0)
                        .isEqualTo("[ROLE_ADMIN]")
        );

        buttonUpdateCustomer.visibleProperty().bind(
                Bindings.valueAt(role, 0)
                        .isNotEqualTo("[ROLE_USER]")
        );
    }
}