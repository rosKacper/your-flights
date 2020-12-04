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
import pl.edu.agh.ki.lab.to.yourflights.model.customers.Customer;
import pl.edu.agh.ki.lab.to.yourflights.model.customers.CustomerService;

import java.io.IOException;

@Component
public class CustomerViewController {

    private CustomerService customerService;

    private final Resource mainView;
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


    public void setModel() {
        //set columns
        firstNameColumn.setCellValueFactory(data -> data.getValue().getValue().getFirstNameProperty());
        secondNameColumn.setCellValueFactory(data -> data.getValue().getValue().getSecondNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        cityColumn.setCellValueFactory(data -> data.getValue().getValue().getCityProperty());

        //get data from service
        ObservableList<Customer> customers = customerService.getMockData();

        //set table data
        final TreeItem<Customer> root = new RecursiveTreeItem<>(customers, RecursiveTreeObject::getChildren);
        customersTableView.setRoot(root);
        customersTableView.setShowRoot(false);

    }

    public CustomerViewController(CustomerService customerService, @Value("classpath:/view/MainView.fxml") Resource mainView, ApplicationContext applicationContext) {
        this.customerService = customerService;
        this.mainView = mainView;
        this.applicationContext = applicationContext;
    }

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
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
