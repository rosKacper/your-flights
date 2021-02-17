package pl.edu.agh.ki.lab.to.yourflights.controller.auth;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.controller.navigation.NavigationController;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginController {

    private final NavigationController navigationController;

    @Autowired
    private AuthenticationManager authManager;
    private ObservableList<String> userRoles = FXCollections.observableArrayList();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public Label usernameLabel;

    public LoginController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    @FXML
    void handleLogin(ActionEvent event) {

        final String userName = usernameField.getText().trim();
        final String userPassword = passwordField.getText().trim();

        try {
            Authentication request = new UsernamePasswordAuthenticationToken(userName, userPassword);
            Authentication result = authManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);

            updateUserInfo();

            usernameField.clear();
            passwordField.clear();
            showNavigationView(event);

        } catch (AuthenticationException e) {
            usernameLabel.setText("Incorrect username or password");
            usernameLabel.setTextFill(Color.RED);
        }
    }

    private void updateUserInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> grantedAuthorities = auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        userRoles.clear();
        userRoles.addAll(grantedAuthorities);
    }

    public void showNavigationView(ActionEvent actionEvent) {
        navigationController.showNavigationView(actionEvent);
    }
}