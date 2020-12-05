package pl.edu.agh.ki.lab.to.yourflights.utils;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

public class Validator {
    public Validator(){}

    public static boolean validateNotEmpty(TextField field, Label label) {
        if(field.getText() == null || field.getText().isEmpty()) {
            label.setText(field.getId() + " cannot be empty!");
//            label.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//            label.setTextFill(Color.WHITE);
            label.setTextFill(Color.RED);
            return false;
        } else {
            label.setText("Correct!");
            label.setTextFill(Color.GREEN);
        }

//        label.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
//        label.setTextFill(Color.BLACK);

        return true;
    }

    public static boolean validateEmail(TextField field, Label label) {
        if (!Pattern.matches("\\A\\s*[a-zA-Z0-9]+([-._]?[A-z0-9]+)*(\\+[A-z0-9]+([-._]?[A-z0-9]+)*)?@[A-z0-9]+([-.]?[a-z0-9]+)*\\.[A-z]+\\s*", field.getText())) {
            label.setText("Email address incorrect!");
            label.setTextFill(Color.RED);
            return false;
        } else {
            label.setText("Correct!");
            label.setTextFill(Color.GREEN);
        }
        return true;
    }

}
