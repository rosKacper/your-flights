package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.JavafxApplication;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginController {


    /**
     * Kontekst aplikacji Springa
     */
    private final ApplicationContext applicationContext;
    private final Resource mainView;

    /**
     * Pola potrzebne do autentykacji użytkownika
     */

    @Autowired
    private AuthenticationManager authManager;
    private ObservableList<String> userRoles = FXCollections.observableArrayList();

    /**
     * Pola tekstowe do formularza logowania
     */

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    public LoginController(ApplicationContext applicationContext, @Value("classpath:/view/MainView.fxml") Resource mainView) {
        this.applicationContext = applicationContext;
        this.mainView = mainView;
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

//            //TEST
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            boolean hasUserRole = authentication.getAuthorities().stream()
//                    .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
//            if(hasUserRole){
//                System.out.println("User is logged in!");
//            }
//            boolean hasAdminRole = authentication.getAuthorities().stream()
//                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
//            if(hasAdminRole){
//                System.out.println("Admin is logged in!");
//            }
            //

            usernameField.clear();
            passwordField.clear();
            showMainView(event);

        } catch (AuthenticationException e) {
            System.out.println("Incorrect username or password");
        }

    }

//    @FXML
//    void handleLoginAsAdmin(ActionEvent event) {
//        System.out.println("Login as admin");
//        setUserAndPassword("admin", "admin");
//    }
//
//    @FXML
//    void handleLoginAsUser(ActionEvent event) {
//        System.out.println("Login as user");
//        setUserAndPassword("user", "user");
//    }

//    private void setUserAndPassword(String username, String password){
//        usernameField.setText(username);
//        passwordField.setText(password);
//    }

    @FXML
    void handleLogout(ActionEvent event) {
        JavafxApplication.logout();
        updateUserInfo();
    }

    private void updateUserInfo(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> grantedAuthorities = auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList());
        userRoles.clear();
        userRoles.addAll(grantedAuthorities);
    }

    /**
     * Metoda służąca do przejścia do głównego widoku
     * @param actionEvent event emitowany przez przycisk
     */
    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(mainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
