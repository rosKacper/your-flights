package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.airline.Airline;
import pl.edu.agh.ki.lab.to.yourflights.model.airline.AirlineService;

@Component
public class AirlinesViewController {

    private AirlineService airlineService;

    @FXML
    private JFXListView<Airline> airlinesListView;

    public void setModel() {
        ObservableList<Airline> airlines = airlineService.getMockData();
        airlinesListView.setItems(airlines);
    }
}
