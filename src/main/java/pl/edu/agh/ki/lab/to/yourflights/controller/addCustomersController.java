package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

@Component
public class addCustomersController {
    public PasswordField passwordField;

    @FXML
    public Text actiontarget;
    public TextField firstName, lastName, country, street, postalCode, phoneNumber, emailAddress;



    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        actiontarget.setText("Done!");
    }


}
