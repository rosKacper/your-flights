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

@Component
public class AirlinesViewController {

    private AirlineService airlineService;
    private final Resource mainView;
    private final ApplicationContext applicationContext;

    @FXML
    private JFXTreeTableView<Airline> airlinesTableView;

    @FXML
    private TreeTableColumn<Airline, String> nameColumn;

    @FXML
    private TreeTableColumn<Airline, String> countryColumn;

    @FXML
    private TreeTableColumn<Airline, String> descriptionColumn;

    public void setModel() {
        //set columns
        nameColumn.setCellValueFactory(data -> data.getValue().getValue().getNameProperty());
        countryColumn.setCellValueFactory(data -> data.getValue().getValue().getCountryProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().getValue().getDescriptionProperty());

        //get data from service
        ObservableList<Airline> airlines = airlineService.getMockData();

        //set table data
        final TreeItem<Airline> root = new RecursiveTreeItem<Airline>(airlines, RecursiveTreeObject::getChildren);
        airlinesTableView.setRoot(root);
        airlinesTableView.setShowRoot(false);

    }

    public AirlinesViewController(AirlineService airlineService, @Value("classpath:/view/MainView.fxml") Resource mainView, ApplicationContext applicationContext) {
        this.airlineService = airlineService;
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
