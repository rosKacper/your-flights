package pl.edu.agh.ki.lab.to.yourflights.controller.flights;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

@Component
public class DestinationController {

    @FXML
    private Text placeOfDestination;

    public DestinationController() {
    }

    public void setData(String destination) {
        placeOfDestination.setText(destination);
    }
}
