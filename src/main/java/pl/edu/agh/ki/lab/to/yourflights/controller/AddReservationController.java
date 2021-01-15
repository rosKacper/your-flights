package pl.edu.agh.ki.lab.to.yourflights.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.model.*;
import pl.edu.agh.ki.lab.to.yourflights.service.ReservationService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketCategoryService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketDiscountService;
import pl.edu.agh.ki.lab.to.yourflights.service.TicketOrderService;
import pl.edu.agh.ki.lab.to.yourflights.utils.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Kontroler obsługujący formularz do dodawania rezerwacji
 * Oznaczenie @Component pozwala Springowi na wstrzykiwanie kontrolera tam gdzie jest potrzebny
 */
@Component
public class AddReservationController {

    /**
     * Widok rezerwacji
     */
    private final Resource reservationList;

    /**
     * Kontekst aplikacji Springowej
     */
    private final ApplicationContext applicationContext;

    /**
     * Serwis rezerwacji
     */
    private final ReservationService reservationService;

    /**
     * Serwis dla ticket order
     */
    private final TicketOrderService ticketOrderService;

    /**
     * Serwis dla ticket discount
     */
    private final TicketDiscountService ticketDiscountService;


    private final TicketCategoryService ticketCategoryService;

    /**
     * Pola formularza
     */
//    @FXML
//    public TextField placeOfDestination, placeOfDeparture, departureTime;

    @FXML
    public ComboBox<Integer> seatsCombo;

    @FXML
    public ComboBox<String> ticketCategoryCombo;

    @FXML
    public ComboBox<String> discountCombo;

    @FXML
    private JFXButton buttonDeleteTicketOrder, buttonAddTicketOrder, formTitle;

    /**
     * Tabela ticket Order
     */

    @FXML
    private JFXTreeTableView<TicketOrder> reservationOverviewTableView;

    /**
     * Kolumny tabeli
     */
    @FXML
    private TreeTableColumn<TicketOrder, String> category;
    @FXML
    private TreeTableColumn<TicketOrder, String> seats;
    @FXML
    private TreeTableColumn<TicketOrder, String> discount;
    @FXML
    private TreeTableColumn<TicketOrder, String> totalCost;

    private Flight flight;
    private Reservation reservation = null;
    private ObservableList<TicketOrder> ticketOrdersList = FXCollections.observableArrayList();

    /**
     * Formatuje date w postaci string do odpowiedniego formatu
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Metoda ustawiająca lot dla którego zostanie utworzona rezerwacja
     * I aktualizująca pola formularza, jeśli jest to edycja istniejącego lotu a nie dodawanie nowego
     * @param flight lot dla rezerwacji
     */
    public void setData(Flight flight) {
        this.flight = flight;
        this.updateControls();
    }

    /**
     * Metoda modyfikująca istniejącą rezerwację
     * @param flight lot dla rezerwacji
     * @param reservation lot dla rezerwacji
     */
    public void setData(Flight flight, Reservation reservation) {
        this.flight = flight;
        this.reservation = reservation;
        ticketOrdersList.addAll(reservation.getTicketOrders());
        this.updateControls();
    }

    /**
     * Metoda aktualizująca wartości pól tekstowych, w zależności od otrzymanej rezerwacji do edycji
     */
    private void updateControls() {

//        departureTime.textProperty().setValue(String.valueOf(LocalDate.parse( flight.getDepartureDate(),formatter)));
//        placeOfDeparture.textProperty().setValue(flight.getPlaceOfDeparture());
//        placeOfDestination.textProperty().setValue(flight.getPlaceOfDestination());

        ticketCategoryCombo.getItems().setAll(ticketCategoryService.findByFlight(flight).stream()
                .map(TicketCategory::getCategoryName)
                .collect(Collectors.toList())
        );

        seatsCombo.getItems().setAll(
                IntStream.rangeClosed(1, 50).boxed().collect(Collectors.toList())
        );

        discountCombo.getItems().setAll(ticketDiscountService.findAll().stream().map(TicketDiscount::getName)
                .collect(Collectors.toList())
        );
        discountCombo.getItems().add("Brak");
    }

    private void setModel() {
        //Ustawienie kolumn
        seats.setCellValueFactory(data -> data.getValue().getValue().getNumberOfSeatsProperty());
        category.setCellValueFactory(data -> data.getValue().getValue().getTicketCategory().getNameProperty());
        discount.setCellValueFactory(data -> {
            TicketOrder order = data.getValue().getValue();
            return order.getTicketDiscount() != null ? order.getTicketDiscount().getNameProperty() : new SimpleStringProperty("Brak");
        });
        totalCost.setCellValueFactory(data -> {
            TicketCategory ticketCategory = data.getValue().getValue().getTicketCategory();
            if(ticketCategory == null) return null;
            return new SimpleStringProperty(ticketCategory.getCategoryPrice().multiply(new BigDecimal(data.getValue().getValue().getNumberOfSeats())).toString());
        });


        //Przekazanie danych do tabeli
        final TreeItem<TicketOrder> root = new RecursiveTreeItem<>(ticketOrdersList, RecursiveTreeObject::getChildren);
        reservationOverviewTableView.setRoot(root);
        reservationOverviewTableView.setShowRoot(false);
    }


    /**
     * Metoda obsługująca dodawanie rezerwacji po naciśnięciu przycisku "submit" w formularzu
     * Zaimplementowana została obsługa sprawdzania poprawności wpisanych wartości
     * @param actionEvent event emitowany przez przycisk
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //Obsługa poprawności danych w formularzu
        //Wykorzystuje klasę Validator, w której zaimplementowane są metody do sprawdzania poprawności danych
        if(seatsCombo.getValue() < 1 || seatsCombo.getValue() > 50 ){
            return;
        }

        if(ticketOrdersList.size() == 0) {
            formTitle.setText("You have a reservation in this time slot already");
            formTitle.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //Stworzenie nowej rezerwacji i wyczyszczenie pól formularza
        if(reservation == null) {
            // sprawdzamy czy nie istnieje już rezerwacja w tym czasie
            List<Reservation> reservationList = reservationService.findByUserName(userName).stream()
                    .filter(reservation1 -> {
                        Flight flightTmp = reservation1.getTicketOrders().get(0).getTicketCategory().getFlight();
                        try {
                            Date dateFromTmp = new SimpleDateFormat("dd/MM/yyyy").parse(flightTmp.getDepartureDate());
                            Date dateToTmp = new SimpleDateFormat("dd/MM/yyyy").parse(flightTmp.getArrivalDate());
                            Date dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(flight.getDepartureDate());
                            Date dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(flight.getArrivalDate());
                            return !dateFrom.after(dateToTmp) && !dateTo.before(dateFromTmp);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).collect(Collectors.toList());

            if (reservationList.size() != 0) {
                // informujemy użytkownika o błędzie
                formTitle.setText("You have a reservation in this time slot already");
                formTitle.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                return;
            }
//         Tworzymy rezerwację i zamówienie biletów dla niej
            reservation =
                    new Reservation(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    null, userName);
            // Zapisujemy w bazie odpowiednie relacje
        }
        List<TicketDiscount> ticketDiscounts = ticketDiscountService.findByName(discountCombo.getValue());

        TicketDiscount ticketDiscount = ticketDiscounts.size() > 0 ? ticketDiscounts.get(0) : null;
        ticketOrderService.deleteAll(FXCollections.observableList(reservation.getTicketOrders()));
        reservation.getTicketOrders().removeIf(ticketOrder -> 1==1);
        reservation.getTicketOrders().addAll(ticketOrdersList);
        ticketOrdersList.forEach(ticketOrder -> {
            ticketOrder.setReservation(reservation);
            ticketOrder.setTicketDiscount(ticketDiscount);
        });
        reservationService.save(reservation);

        ticketOrdersList = FXCollections.observableArrayList();

        //Po dodaniu lotu zakończonym sukcesem, następuje powrót do widoku listy lotów
        showReservationList(actionEvent);
    }

    /**
     * Konstruktor, Spring wstrzykuje odpowiednie zależności, jak np. kontekst aplikacji
     * @param reservationService serwis rezerwacji
     * @param applicationContext kontekst aplikacji Springa
     */
    public AddReservationController(@Value("classpath:/view/ReservationListView.fxml") Resource reservationList,
                                    ApplicationContext applicationContext,
                                    TicketOrderService ticketOrderService,
                                    TicketDiscountService ticketDiscountService,
                                    ReservationService reservationService,
                                    TicketCategoryService ticketCategoryService) {
        this.reservationList = reservationList;
        this.applicationContext = applicationContext;
        this.reservationService = reservationService;
        this.ticketOrderService = ticketOrderService;
        this.ticketDiscountService = ticketDiscountService;
        this.ticketCategoryService = ticketCategoryService;
    }

    /**
     * Metoda wywoływana po inicjalizacji widoku
     */
    @FXML
    public void initialize() {
        this.setModel();
        reservationOverviewTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setButtonsDisablePropertyBinding();
    }

    /**
     * Metoda służąca do przejścia do widoku listy rezerwacji
     * @param actionEvent event emitowany przez przycisk
     */
    public void showReservationList(ActionEvent actionEvent) {
        try {
            //ładujemy widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(reservationList.getURL());

            //Spring wstrzykuje odpowiedni kontroler obsługujący dany plik .fxml na podstawie kontekstu aplikacji
            fxmlloader.setControllerFactory(applicationContext::getBean);

            //wczytanie sceny
            Parent parent = fxmlloader.load();

            //pobieramy stage z którego wywołany został actionEvent - bo nie chcemy tworzyć za każdym razem nowego Stage
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            //utworzenie i wyświetlenie sceny
            Scene scene = new Scene(parent);
            stage.setScene(scene);

            ticketOrdersList = FXCollections.observableArrayList();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAddAction() {

        TicketCategory ticketCategory = ticketCategoryService.findByFlight(flight).stream()
                .filter(ticketCategory1 -> ticketCategory1.getCategoryName().equals(ticketCategoryCombo.getValue()))
                .findFirst().get();

        List<TicketDiscount> ticketDiscounts = ticketDiscountService.findByName(discountCombo.getValue());
        TicketDiscount ticketDiscount = ticketDiscounts.size() > 0 ? ticketDiscounts.get(0) : null;

        ticketOrdersList.removeIf(ticketOrder -> {
            if(ticketOrder.getTicketCategory().getCategoryName().equals(ticketCategory.getCategoryName())) {
                if(ticketOrder.getTicketDiscount() == null && discountCombo.getValue().equals("Brak")) {
                    return true;
                }
                if(ticketOrder.getTicketDiscount() != null && discountCombo.getValue().equals(ticketOrder.getTicketDiscount().getName())) {
                    return true;
                }
            }
            return false;
        });

        ticketOrdersList.addAll(new TicketOrder(seatsCombo.getValue(), ticketDiscount, reservation, ticketCategory));
        setModel();

    }

    public void handleDeleteAction() {
        var ticketOrders = reservationOverviewTableView.getSelectionModel().getSelectedItems().stream().map(TreeItem::getValue).collect(Collectors.toList());
        ticketOrdersList.removeAll(ticketOrders);
    }

    private void setButtonsDisablePropertyBinding() {
        if(buttonDeleteTicketOrder != null) {
            buttonDeleteTicketOrder.disableProperty().bind(
                    Bindings.isEmpty(reservationOverviewTableView.getSelectionModel().getSelectedItems())
            );
        }

    }
}