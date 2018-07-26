package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import presentation.Notification;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainController {
    private double x, y;

    private void loadScene(String fxmlUrl) {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource(fxmlUrl));
//            borderPane.setCenter(parent);
            scrollPane.setContent(parent);
        } catch (IOException e) {
//            e.printStackTrace();
            Notification.showErors(e, "MainController.loadScene");
        }
    }


    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label currentTime;


    @FXML
    public void initialize() {
//        assert currentTime != null : "fx:id=\"currentTime\" was not injected: check your FXML file 'sample.fxml'.";
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //get current date time with Calendar()
            Calendar cal = Calendar.getInstance();
//            System.out.println(dateFormat.format(cal.getTime()));
            currentTime.setText("DATE:" + String.valueOf(dateFormat.format(cal.getTime())));

        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

//        System.out.println("OK");
    }


    @FXML
    void menuDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void menuPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void minWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void maxWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }


    @FXML
    void loadAboutMe(MouseEvent event) {
        loadScene("/FXML/AboutMe.fxml");
    }


    @FXML
    void loadEmployee(MouseEvent event) {
        loadScene("/FXML/Employee.fxml");
    }

    @FXML
    void loadFinance(MouseEvent event) {
        loadScene("/FXML/Finance.fxml");
    }

    @FXML
    private ImageView sessionIcon;

    public void setSessionIcon(Image image){
        sessionIcon.setImage(image);
    }

    @FXML
    private Label session;

    public void setSession(String session){
        this.session.setText(session);
    }
}