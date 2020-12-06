package pl.edu.agh.ki.lab.to.yourflights.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

/**
 * Klasa implementująca metody, służące do sprawdzania poprawności
 */
public class Validator {
    public Validator(){}

    /**
     * Metoda sprawdzająca czy pole formularza jest niepuste
     * @param field pole formularza
     * @param label etykieta do wpisania komunikatu błędu
     * @return true jeśli pole jest niepuste, false w przeciwnym razie
     */
    public static boolean validateNotEmpty(TextField field, Label label) {
        if(field.getText() == null || field.getText().isEmpty()) {
            label.setText(field.getId() + " cannot be empty!");
            label.setTextFill(Color.RED);
            return false;
        } else {
            label.setText("Correct!");
            label.setTextFill(Color.GREEN);
        }
        return true;
    }

    /**
     * Metoda sprawdzająca poprawność podanego adresu email w formularzu
     * @param field pole formularza
     * @param label etykieta do wpisania komunikatu błędu
     * @return true jeśli email jest poprawny, false w przeciwnym razie
     */
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
