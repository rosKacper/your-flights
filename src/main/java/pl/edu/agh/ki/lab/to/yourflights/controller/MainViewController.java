package pl.edu.agh.ki.lab.to.yourflights.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainViewController {

    private final ApplicationContext applicationContext;
    private final Resource airlinesView;

    public MainViewController(ApplicationContext applicationContext,
                              @Value("classpath:/view/AirlinesView.fxml") Resource airlinesView) {
        this.applicationContext = applicationContext;
        this.airlinesView = airlinesView;
    }

    public void showAirlinesView(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlloader = new FXMLLoader(airlinesView.getURL());
            fxmlloader.setControllerFactory(applicationContext::getBean);
            Parent parent = fxmlloader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(parent, 800, 600);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
