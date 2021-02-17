package pl.edu.agh.ki.lab.to.yourflights.controller.navigation;

import com.jfoenix.controls.JFXButton;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.JavafxApplication;
import pl.edu.agh.ki.lab.to.yourflights.controller.airlines.AddAirlineController;
import pl.edu.agh.ki.lab.to.yourflights.controller.customers.AddCustomerController;
import pl.edu.agh.ki.lab.to.yourflights.controller.flights.*;
import pl.edu.agh.ki.lab.to.yourflights.controller.reservations.AddReservationController;
import pl.edu.agh.ki.lab.to.yourflights.model.*;

import java.io.IOException;
import java.util.Collections;

@Component
public class NavigationController {

    private final Resource navigationView;
    private final Resource mainView;
    private final Resource airlinesView;
    private final Resource addAirlineView;
    private final Resource flightsView;
    private final Resource addFlightView;
    private final Resource flightDetailsView;
    private final Resource reservationsView;
    private final Resource addReservationView;
    private final Resource discountsView;
    private final Resource addDiscountView;
    private final Resource customersView;
    private final Resource addCustomerView;
    private final Resource loginView;
    private final Resource registrationChoiceView;
    private final Resource airlineRegistrationView;
    private final Resource registrationView;
    private final Resource ticketCategoriesView;
    private final Resource addTicketCategoryView;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane componentPane;

    @FXML
    private JFXButton buttonLogin;

    @FXML
    private JFXButton buttonLogout;

    @FXML
    private JFXButton buttonRegister;

    @FXML
    private JFXButton customersButton;

    @FXML
    private JFXButton reservationsButton;

    @FXML
    private JFXButton discountButton;


    public NavigationController(@Value("classpath:/view/Navigation/NavigationView.fxml") Resource navigationView,
                                @Value("classpath:/view/Main/MainView.fxml") Resource mainView,
                                @Value("classpath:/view/Airlines/AirlinesView.fxml") Resource airlinesView,
                                @Value("classpath:/view/Airlines/AddAirlineView.fxml") Resource addAirlineView,
                                @Value("classpath:/view/Flights/FlightsView.fxml") Resource flightsView,
                                @Value("classpath:/view/Flights/AddFlightView.fxml") Resource addFlightView,
                                @Value("classpath:/view/Flights/FlightDetailsView.fxml") Resource flightDetailsView,
                                @Value("classpath:/view/Reservations/ReservationsView.fxml") Resource reservationsView,
                                @Value("classpath:/view/Reservations/AddReservationView.fxml") Resource addReservationView,
                                @Value("classpath:/view/Flights/DiscountsView.fxml") Resource discountsView,
                                @Value("classpath:/view/Flights/AddDiscountView.fxml") Resource addDiscountView,
                                @Value("classpath:/view/Customers/CustomersView.fxml") Resource customersView,
                                @Value("classpath:/view/Customers/AddCustomerView.fxml") Resource addCustomerView,
                                @Value("classpath:/view/Auth/LoginView.fxml") Resource loginView,
                                @Value("classpath:/view/Auth/RegistrationView.fxml") Resource registrationView,
                                @Value("classpath:/view/Auth/AirlineRegistrationView.fxml") Resource airlineRegistrationView,
                                @Value("classpath:/view/Auth/RegistrationChoiceView.fxml") Resource registrationChoiceView,
                                @Value("classpath:/view/Flights/TicketCategoriesView.fxml") Resource ticketCategoriesView,
                                @Value("classpath:/view/Flights/AddTicketCategoryView.fxml") Resource addTicketCategoryView,
                                ApplicationContext applicationContext) {
        this.navigationView = navigationView;
        this.mainView = mainView;
        this.airlinesView = airlinesView;
        this.addAirlineView = addAirlineView;
        this.flightsView = flightsView;
        this.addFlightView = addFlightView;
        this.flightDetailsView = flightDetailsView;
        this.reservationsView = reservationsView;
        this.addReservationView = addReservationView;
        this.discountsView = discountsView;
        this.addDiscountView = addDiscountView;
        this.customersView = customersView;
        this.addCustomerView = addCustomerView;
        this.loginView = loginView;
        this.registrationView = registrationView;
        this.airlineRegistrationView = airlineRegistrationView;
        this.registrationChoiceView = registrationChoiceView;
        this.ticketCategoriesView = ticketCategoriesView;
        this.addTicketCategoryView = addTicketCategoryView;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
        this.setupLoginBindings();
        this.setupNavigationBindings();
        this.showMainView();
    }

    public void showNavigationView(ActionEvent event) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(navigationView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(mainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(mainView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAirlinesView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(airlinesView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddAirline(ActionEvent actionEvent, Airline airline) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addAirlineView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            if(airline != null) {
                AddAirlineController controller = fxmlloader.getController();
                controller.setData(airline);
            }
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFlightsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(flightsView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddFlight(ActionEvent actionEvent, Flight flight) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addFlightView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            if(flight != null) {
                AddFlightController controller = fxmlloader.getController();
                controller.setData(flight);
            }
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFlightDetailsView(ActionEvent actionEvent, Flight flight) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(flightDetailsView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            if(flight != null) {
                FlightDetailsController controller = fxmlloader.getController();
                controller.setData(flight);
            }
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReservationsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(reservationsView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddReservation(ActionEvent actionEvent, Flight flight, Reservation reservation) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(addReservationView.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            AddReservationController controller = fxmlLoader.getController();
            if(reservation != null){
                controller.setData(flight, reservation);
            } else {
                controller.setData(flight);
            }

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDiscountsView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(discountsView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddDiscountView(ActionEvent actionEvent, TicketDiscount discount) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addDiscountView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            if(discount != null) {
                AddDiscountController controller = fxmlloader.getController();
                controller.setData(discount);
            }

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomersView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader;
            fxmlloader = new FXMLLoader(customersView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddCustomerView(ActionEvent actionEvent, Customer customer) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addCustomerView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            if(customer != null) {
                AddCustomerController controller = fxmlloader.getController();
                controller.setData(customer);
            }

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

    public void showRegistrationChoiceView(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(registrationChoiceView.getURL());
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

    public void showRegistrationAirlineView(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlloader = new FXMLLoader(airlineRegistrationView.getURL());
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

    @FXML
    public void handleLogout(ActionEvent event) {
        JavafxApplication.logout();
        showNavigationView(event);
    }

    public void showTicketCategoriesView(ActionEvent actionEvent, Flight flight) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(ticketCategoriesView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();

            TicketCategoryViewController controller = fxmlloader.getController();
            controller.setData(flight);

            showNavigationView(actionEvent);
            this.componentPane.getChildren().clear();
            this.componentPane.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddTicketCategory(ActionEvent actionEvent, Flight flight, TicketCategory ticketCategory) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(addTicketCategoryView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            AddTicketCategoryController controller = fxmlloader.getController();
            controller.setData(flight, ticketCategory);

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupLoginBindings() {
        String r = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList role = FXCollections.observableArrayList(Collections.singletonList(r));

        buttonLogin.visibleProperty().bind(
                Bindings.valueAt(role, 0)
                .isEqualTo("[ROLE_ANONYMOUS]")
        );

        buttonLogout.visibleProperty().bind(
                Bindings.valueAt(role, 0)
                        .isNotEqualTo("[ROLE_ANONYMOUS]")
        );

        buttonRegister.visibleProperty().bind(
                Bindings.valueAt(role, 0)
                        .isEqualTo("[ROLE_ANONYMOUS]")
        );
    }

    private void setupNavigationBindings(){
        String r = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        ObservableList role = FXCollections.observableArrayList(Collections.singletonList(r));

        customersButton.visibleProperty().bind(Bindings.valueAt(role, 0)
                .isNotEqualTo("[ROLE_ANONYMOUS]"));

        reservationsButton.visibleProperty().bind(Bindings.valueAt(role, 0)
                .isNotEqualTo("[ROLE_ANONYMOUS]"));

        discountButton.visibleProperty().bind(Bindings.valueAt(role, 0)
                .isNotEqualTo("[ROLE_ANONYMOUS]").and(Bindings.valueAt(role, 0)
                        .isNotEqualTo("[USER]")));
    }

}
