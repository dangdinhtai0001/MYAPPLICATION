package controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.IOException;

public class AboutMeController {

    @FXML
    private VBox root;
    //Phương thức load WebView lên
    private void loadURL(String url,String title) {
        try {
            FXMLLoader.load(getClass().getResource("/FXML/WebView.fxml"));
        } catch (IOException e) {
//            e.printStackTrace();
            Notification.showErors(e,"About Me Controller - Load URL");
        }
        Scene scene = new Scene(new Browser(url),Color.web("#666970"));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    @FXML
    private Button btnFacebook;

    @FXML
    private Button btnGitHub;

    @FXML
    private Button btnInstagram;

    @FXML
    private Button btnYouTube;


    @FXML
    void loadFacebook(ActionEvent event) {
        loadURL("https://www.facebook.com/dinhtai.dang.7","Facebool");
    }

    @FXML
    void loadGitHub(ActionEvent event) {
        loadURL("https://github.com/dangdinhtai0001","GitHub");
    }

    @FXML
    void loadInstagram(ActionEvent event) {
        loadURL("https://www.instagram.com/d.dinh.tai/","Instagram");
    }

    @FXML
    void loadYouTube(ActionEvent event) {
        loadURL("https://www.youtube.com/channel/UCFAkJhyZfnhYHHf1Pg9Hg2w/videos?view_as=subscriber","You Tube");
    }

}
//Class tạo browser riêng
class Browser extends StackPane {

    private final ProgressBar progress = new ProgressBar();

    Browser(String url) {
        // load the web page
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        //add the web view to the scene

        // updating progress bar using binding
        progress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == Worker.State.SUCCEEDED){
                // hide progress bar then page is ready
                progress.setVisible(false);
            }
        });
        // Tạo xong nhớ add :v
        getChildren().addAll(browser,progress);


    }
}

