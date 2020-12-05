package pl.edu.agh.ki.lab.to.yourflights.utils;


import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class Validator {
    public Validator(){}

    public static boolean validateNotEmpty(TextField field, Label label) {
        if(field.getText() == null || field.getText().isEmpty()) {
            label.setText(field.getId() + " cannot be empty!");
            return false;
        }
        label.setText(field.getId() + ":");
        return true;
    }

    public static boolean validateEmail(TextField field, Label label) {
        if (!Pattern.matches("\\A\\s*[a-zA-Z0-9]+([-._]?[A-z0-9]+)*(\\+[A-z0-9]+([-._]?[A-z0-9]+)*)?@[A-z0-9]+([-.]?[a-z0-9]+)*\\.[A-z]+\\s*", field.getText())) {
            label.setText("Email incorrect!");
            return false;
        }
        return true;
    }

}
