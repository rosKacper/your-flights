package pl.edu.agh.ki.lab.to.yourflights;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.ki.lab.to.yourflights.service.MockDataService;

import java.io.IOException;

/**
 * Klasa służąca do inicjalizacji aplikacji
 * Nasłuchuje na StageReadyEvent emitowany przez JavafxApplication i inicjalizuje aplikację
 */
@Component
public class StageInitializer implements ApplicationListener<JavafxApplication.StageReadyEvent> {

    private final Resource mainView;
    private final Resource loginView;
    private final String applicationTitle;
    private final MockDataService mockDataService;

    /**
     * Kontekst aplikacji Springa
     */
    private final ApplicationContext applicationContext;

    /**
     * Konstruktor, zależności są automatycznie wstrzykiwane przez Springa
     * @param applicationTitle tytuł aplikacji
     * @param mainView główny widok aplikacji
     * @param applicationContext kontekst aplikacji Springa
     */
    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle,
                            @Value("classpath:/view/MainView.fxml") Resource mainView,
                            @Value("classpath:/view/LoginView.fxml") Resource loginView,
                            ApplicationContext applicationContext,
                            MockDataService mockDataService) {
        this.applicationTitle = applicationTitle;
        this.mainView = mainView;
        this.loginView = loginView;
        this.applicationContext = applicationContext;
        this.mockDataService = mockDataService;
    }

    /**
     * Metoda reagująca na StageReadyEvent i inicjalizująca aplikację
     */
    @Override
    public void onApplicationEvent(JavafxApplication.StageReadyEvent event) {
        try {
            //ładujemy główny widok z pliku .fxml
            FXMLLoader fxmlloader = new FXMLLoader(mainView.getURL());

            //Spring wstrzykuje odpowiedni kontroler obsługujący dany plik .fxml na podstawie kontekstu aplikacji
            fxmlloader.setControllerFactory(applicationContext::getBean);

            //wczytanie sceny
            Parent parent = fxmlloader.load();

            //pobieramy stage z którego wywołany został event
            Stage stage = event.getStage();

            //wypełnienie wbudowanej bazy danych przykładowymi danymi startowymi
            mockDataService.createMockData();

            //utworzenie i wyświetlenie sceny
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
