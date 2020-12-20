package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Kontroler głównego widoku aplikacji
 * Z niego możemy przechodzić m.in. do widoku klientów albo do widoku przewoźników
 */
@Component
public class MainViewController {

    public JFXButton loginButton = new JFXButton();
    public JFXButton boi = new JFXButton();

    private final Resource airlinesView;
    private final Resource customersView;
    private final Resource flightView;
    private final Resource reservationListView;
    private final Resource loginView;
    private final Resource registrationView;
    private final Resource mainView;
    private final Resource anonymousAirlinesView;
    private final Resource anonymousFlightView;
    private final Resource userFlightView;
    private final Resource userAirlineView;
    private final Resource userCustomersView;

    /**
     * Kontekst aplikacji Springa
     */
    private final ApplicationContext applicationContext;


    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności
     * @param applicationContext kontekst aplikacji Springa
     * @param airlinesView widok tabeli przewoźników
     * @param customersView widok tabeli klientów
     * @param reservationListView widok tabeli rezerwacji
     * @param loginView widok ekranu logowania
     */
    public MainViewController(ApplicationContext applicationContext,
                              @Value("classpath:/view/AdminView/AirlinesView.fxml") Resource airlinesView,
                              @Value("classpath:/view/AdminView/CustomersView.fxml") Resource customersView,
                              @Value("classpath:/view/AdminView/CustomersView.fxml") Resource mainView,
                              @Value("classpath:/view/AdminView/FlightView.fxml") Resource flightView,
                              @Value("classpath:/view/AdminView/ReservationListView.fxml") Resource reservationListView,
                              @Value("classpath:/view/AuthView/LoginView.fxml") Resource loginView,
                              @Value("classpath:/view/AuthView/RegistrationView.fxml") Resource registrationView,
                              @Value("classpath:/view/AnonymousView/AnonymousAirlinesView.fxml") Resource anonymousAirlinesView,
                              @Value("classpath:/view/AnonymousView/AnonymousFlightView.fxml") Resource anonymousFlightView,
                              @Value("classpath:/view/UserView/UserFlightView.fxml") Resource userFlightView,
                              @Value("classpath:/view/UserView/UserAirlinesView.fxml") Resource userAirlineView,
                              @Value("classpath:/view/UserView/UserCustomersView.fxml") Resource userCustomersView) {
        this.applicationContext = applicationContext;
        this.airlinesView = airlinesView;
        this.customersView = customersView;
        this.flightView=flightView;
        this.mainView = mainView;
        this.reservationListView = reservationListView;
        this.loginView = loginView;
        this.registrationView = registrationView;
        this.anonymousAirlinesView = anonymousAirlinesView;
        this.anonymousFlightView = anonymousFlightView;
        this.userAirlineView = userAirlineView;
        this.userFlightView = userFlightView;
        this.userCustomersView = userCustomersView;
    }


    /**
     * Metoda służąca do przejścia do widoku tabeli przewoźników
     * @param actionEvent event emitowany przez przycisk
     */
    public void showAirlinesView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousAirlinesView.getURL());
            }
            else if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(airlinesView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userAirlineView.getURL());
            }
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

    /**
     * Metoda służąca do przejścia do widoku tabeli klientów
     * @param actionEvent event emitowany przez przycisk
     */
    public void showCustomersView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(customersView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userCustomersView.getURL());
            }
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

    public void showReservation(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(reservationListView.getURL());
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

    public void showFlightView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            if(role.equals("[ROLE_ANONYMOUS]")){
                fxmlloader = new FXMLLoader(anonymousFlightView.getURL());
            }
            else if(role.equals("[ROLE_ADMIN]")){
                fxmlloader = new FXMLLoader(flightView.getURL());
            }
            else{
                fxmlloader = new FXMLLoader(userFlightView.getURL());
            }
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

    public void showLoginView(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(loginView.getURL());
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

    public void showRegistrationView(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(registrationView.getURL());
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
