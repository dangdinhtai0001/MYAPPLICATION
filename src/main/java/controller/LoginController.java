package controller;

import businessLogicLayer.LoginB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    private static LoginB loginB;
    private double x, y;
    private String username, password;
    private boolean isAdmin;

    @FXML
    private ImageView loginIcon;
    @FXML
    private VBox mainVbox;
    @FXML
    private HBox menuBar;

    public LoginController() {
        if (loginB == null) {
            try {
                loginB = new LoginB();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void login(ActionEvent event) {
        try {
            if (loginB.checkLogin(txtUsername.getText(), txtPassword.getText())) {
//                Notification.informationAlert("", "Login complete ");
                //Load main lên
                this.username = loginB.getSession().getSessionName();
                this.password = loginB.getSession().getPassword();
                this.isAdmin = loginB.getSession().isAdmin();
                loadMain();
                //Ẩn login đi
                ((Node) event.getSource()).getScene().getWindow().hide();
            } else {
                Notification.informationAlert("", "Login failed ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadMain() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainVer2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("Icon/Logo.png"));
            stage.setTitle("MY APPLICATION");
            Scene scene = new Scene(root);
            stage.setScene(scene);

            MainController mainController = loader.getController();
            mainController.setSession(username);
            Image image;
            if (isAdmin) {
                image = new Image("/Icon/ADMIN.png");

            } else {
                image = new Image("/Icon/user.png");

            }
            mainController.setSessionIcon(image);
            stage.show();
        } catch (IOException e) {
//            e.printStackTrace();
            Notification.showErors(e, "Login Controller - Load URL");
        }

    }

    @FXML
    void keyBoardPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            try {
                if (loginB.checkLogin(txtUsername.getText(), txtPassword.getText())) {
//                Notification.informationAlert("", "Login complete ");
                    //Load main lên
                    this.username = loginB.getSession().getSessionName();
                    this.password = loginB.getSession().getPassword();
                    this.isAdmin = loginB.getSession().isAdmin();
                    loadMain();
                    //Ẩn login đi
                    ((Node) event.getSource()).getScene().getWindow().hide();
                } else {
                    Notification.informationAlert("", "Login failed ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

}
