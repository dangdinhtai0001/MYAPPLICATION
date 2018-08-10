package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import presentation.Notification;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainController {
    private TranslateTransition openMenu, closeMenu, openDashboard, closeDashboard, openSchedule, closeSchedule;
    private TranslateTransition openEmployee, closeEmployee, openCustomer, closeCustomer, openProvider, closeProvider;
    private TranslateTransition openWarehouse, closeWarehouse, openFinance, closeFinance, openDeal, closeDeal;
    private TranslateTransition openSetting, closeSetting, openSupport, closeSupport, openAboutMe, closeAboutMe, openLogOut, closeLogOut;
    private TranslateTransition openLine;
    private TranslateTransition closeLine;
    private TranslateTransition openItemOfFinance;
    private TranslateTransition closeItemOfFinance;

    private void loadScene(String fxmlUrl) {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource(fxmlUrl));
            scrollPane.setContent(parent);
        } catch (IOException e) {
            Notification.showErors(e, "MainController.loadScene");
        }
    }

    @FXML
    private StackPane mainPane;

    @FXML
    private ScrollPane menu;

    @FXML
    private ImageView timeIcon;

    @FXML
    private ImageView menuIcon;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label currentTime;

    @FXML
    private HBox itemDashboard;

    @FXML
    private HBox itemSchedule;

    @FXML
    private HBox itemEmployee;

    @FXML
    private HBox itemCustomer;

    @FXML
    private HBox itemProvider;

    @FXML
    private HBox itemWarehouse;

    @FXML
    private HBox itemFinance;

    @FXML
    private HBox itemDeal;

    @FXML
    private HBox itemSetting;

    @FXML
    private HBox itemSupport;

    @FXML
    private HBox itemAboutMe;

    @FXML
    private HBox itemLogOut;

    @FXML
    private Line itemLine;

    @FXML
    private VBox itemOfFinance;


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
        //tạo các đối tượng quản lí đi chuyển
        declareTranslateAnimation();
    }

    private void declareTranslateAnimation() {
        Duration duration = new Duration(350);
        openMenu = new TranslateTransition(duration, menu);
        openDashboard = new TranslateTransition(new Duration(400), itemDashboard);
        openSchedule = new TranslateTransition(new Duration(450), itemSchedule);
        openEmployee = new TranslateTransition(new Duration(500), itemEmployee);
        openCustomer = new TranslateTransition(new Duration(550), itemCustomer);
        openProvider = new TranslateTransition(new Duration(600), itemProvider);
        openWarehouse = new TranslateTransition(new Duration(650), itemWarehouse);
        openFinance = new TranslateTransition(new Duration(700), itemFinance);
        openDeal = new TranslateTransition(new Duration(750), itemDeal);
        openSetting = new TranslateTransition(new Duration(800), itemSetting);
        openSupport = new TranslateTransition(new Duration(850), itemSupport);
        openAboutMe = new TranslateTransition(new Duration(900), itemAboutMe);
        openLogOut = new TranslateTransition(new Duration(950), itemLogOut);
        openLine = new TranslateTransition(duration, itemLine);
        openItemOfFinance = new TranslateTransition(duration, itemOfFinance);

        closeMenu = new TranslateTransition(duration, menu);
        closeDashboard = new TranslateTransition(duration, itemDashboard);
        closeDashboard = new TranslateTransition(duration, itemDashboard);
        closeAboutMe = new TranslateTransition(duration, itemAboutMe);
        closeCustomer = new TranslateTransition(duration, itemCustomer);
        closeDeal = new TranslateTransition(duration, itemDeal);
        closeEmployee = new TranslateTransition(duration, itemEmployee);
        closeFinance = new TranslateTransition(duration, itemFinance);
        closeLogOut = new TranslateTransition(duration, itemLogOut);
        closeProvider = new TranslateTransition(duration, itemProvider);
        closeSchedule = new TranslateTransition(duration, itemSchedule);
        closeSetting = new TranslateTransition(duration, itemSetting);
        closeSupport = new TranslateTransition(duration, itemSupport);
        closeWarehouse = new TranslateTransition(duration, itemWarehouse);
        closeLine = new TranslateTransition(duration, itemLine);
        closeItemOfFinance = new TranslateTransition(duration, itemOfFinance);

    }

    private void openMenu() {
        openMenu.setToX(0);

        openDashboard.setToY(0);
        openAboutMe.setToY(0);
        openCustomer.setToY(0);
        openDeal.setToY(0);
        openEmployee.setToY(0);
        openFinance.setToY(0);
        openLogOut.setToY(0);
        openProvider.setToY(0);
        openSchedule.setToY(0);
        openSetting.setToY(0);
        openSupport.setToY(0);
        openWarehouse.setToY(0);
        openLine.setToX(0);


        openMenu.play();
        openDashboard.play();
        openAboutMe.play();
        openCustomer.play();
        openDeal.play();
        openEmployee.play();
        openFinance.play();
        openLogOut.play();
        openProvider.play();
        openSchedule.play();
        openSetting.play();
        openSupport.play();
        openWarehouse.play();
        openLine.setDelay(new Duration(800 - 350));
        openLine.play();

    }

    private void hideMenu() {
        closeMenu.setToX(-menu.getWidth());
        closeDashboard.setToY(menu.getHeight());
        closeAboutMe.setToY(menu.getHeight());
        closeCustomer.setToY(menu.getHeight());
        closeDeal.setToY(menu.getHeight());
        closeEmployee.setToY(menu.getHeight());
        closeFinance.setToY(menu.getHeight());
        closeLogOut.setToY(menu.getHeight());
        closeProvider.setToY(menu.getHeight());
        closeSchedule.setToY(menu.getHeight());
        closeSetting.setToY(menu.getHeight());
        closeSupport.setToY(menu.getHeight());
        closeWarehouse.setToY(menu.getHeight());
        closeLine.setToX(-221);
        closeItemOfFinance.setToX(-227);
//        openItemOfFinance.setToY(184.5);


        closeMenu.play();
        closeDashboard.play();
        closeDashboard.play();
        closeAboutMe.play();
        closeCustomer.play();
        closeDeal.play();
        closeEmployee.play();
        closeFinance.play();
        closeLogOut.play();
        closeProvider.play();
        closeSchedule.play();
        closeSetting.play();
        closeSupport.play();
        closeWarehouse.play();
        closeLine.play();
        closeItemOfFinance.play();
//        openItemOfFinance.play();
    }
    @FXML
    void loadProduct(MouseEvent event) {
        loadScene("/FXML/Product.fxml");
    }

    @FXML
    void loadProvider(MouseEvent event) {
        loadScene("/FXML/Provider.fxml");
    }

    @FXML
    void loadAboutMe(MouseEvent event) {
        loadScene("/FXML/AboutMe.fxml");
    }

    @FXML
    void loadEmployee(MouseEvent event) {
        loadScene("/FXML/EmployeeVer2.fxml");
    }

    @FXML
    void loadFinance(MouseEvent event) {
        if (itemOfFinance.getTranslateX() != 0) {
            closeItemOfFinance.setToX(0);
            closeItemOfFinance.play();
        } else {
            openItemOfFinance.setToX(230);
            openItemOfFinance.play();
        }

//        System.out.println(itemOfFinance.getTranslateX());

//        loadScene("/FXML/Finance.fxml");
    }

    @FXML
    void loadSalary(MouseEvent event) {
        loadScene("/FXML/Salary.fxml");
    }

    @FXML
    private ImageView sessionIcon;

    void setSessionIcon(Image image) {
        sessionIcon.setImage(image);
    }

    @FXML
    private Label session;

    void setSession(String session) {
        this.session.setText(session);
    }

    @FXML
    void menuClicked(MouseEvent event) {
        //Ban đầu cả 2 thằng này do đc set trước nên đều ở tọa đọ x = 0.0
        if (menu.getTranslateX() == 0) {
            //Set cho 2 thằng ni về vị trí vừa đủ để ẩn menu đi
            hideMenu();
        } else {
            //trở về dạng ban đầu
            openMenu();
        }
    }
}