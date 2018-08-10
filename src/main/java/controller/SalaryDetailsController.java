package controller;

import businessLogicLayer.LoginB;
import businessLogicLayer.SalaryB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import presentation.MyToolTip;
import presentation.Notification;

import java.sql.SQLException;


public class SalaryDetailsController {
    private double x, y;
    private boolean checkValidateName, checkValidateMoney;
    private SalaryB salaryB;
    private LoginB loginB;
    private boolean isUpdate = false;
    private int salaryId;
    @FXML
    private Label title;
    @FXML
    private TextField txtName;
    @FXML
    private HBox hBoxName;
    @FXML
    private ImageView validateName;
    @FXML
    private TextField txtMoney;
    @FXML
    private HBox hBoxMoney;
    @FXML
    private ImageView validateMoney;
    @FXML
    private TextArea txtDescription;
    @FXML
    private HBox hBoxDescription;
    @FXML
    private ImageView validateDescription;

    public SalaryDetailsController() {
        try {
            salaryB = new SalaryB();
            loginB = new LoginB();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        checkValidateName = true;
        checkValidateMoney = true;
        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && isNumber(newValue)) {
                showWarning(validateName, hBoxName);
                Tooltip.install(hBoxName, new MyToolTip("Tên không được toàn bộ là chữ số"));
            } else {
                validateName.setVisible(false);
                hBoxName.setStyle("-fx-border-color: transparent");
                checkValidateName = true;
            }
            if (newValue.length() > 30) {
                showErrors(validateName, hBoxName);
                Tooltip.install(hBoxName, new MyToolTip("Tên không được dài quá 30 kí tự"));
                checkValidateName = false;
            }
        });

        txtMoney.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !isNumber(newValue)) {
                showErrors(validateMoney, hBoxMoney);
                Tooltip.install(hBoxMoney, new MyToolTip("Số tiền phải là số "));
                checkValidateMoney = false;
            }
            if (newValue.equals("") || isNumber(newValue)) {
                validateMoney.setVisible(false);
                hBoxMoney.setStyle("-fx-border-color: transparent");
                checkValidateMoney = true;
            }
            if (newValue.length() > 7 && newValue.length() <= 11) {
                showWarning(validateMoney, hBoxMoney);
                Tooltip.install(hBoxMoney, new MyToolTip("Đm, Lương chục củ à ? "));
            }

            if (newValue.length() > 11) {
                showErrors(validateMoney, hBoxMoney);
                Tooltip.install(hBoxMoney, new MyToolTip("Số tiền không được quá 11 chữ số"));
                checkValidateMoney = false;
            }

            try {
                if (newValue != null && newValue.charAt(0) == '0') {
                    showErrors(validateMoney, hBoxMoney);
                    Tooltip.install(hBoxMoney, new MyToolTip("Số tiền không được bắt đầu bằng số 0"));
                    checkValidateMoney = false;
                }
            } catch (StringIndexOutOfBoundsException e) {

            }
        });
    }

    private boolean isNumber(String text) {
        try {
            Double i = Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrors(ImageView validate, HBox hBox) {
        validate.setVisible(true);
        validate.setImage(new Image("/Icon/error-icon-28.png"));
        hBox.setStyle("-fx-border-color: red;");
    }

    private void showWarning(ImageView validate, HBox hBox) {
        validate.setVisible(true);
        validate.setImage(new Image("/Icon/warning.png"));
        hBox.setStyle("-fx-border-color: yellow;");
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

    public void setTxtName(String s) {
        this.txtName.setText(s);
    }

    public void setTxtMoney(String s) {
        this.txtMoney.setText(s);
    }

    public void setTxtDescription(String s) {
        this.txtDescription.setText(s);
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public String getName() {
        return txtName.getText();
    }

    public String getMoney() {
        return txtMoney.getText();
    }

    public String getDescription() {
        return txtDescription.getText();
    }

    public void setSalaryId(int id) {
        salaryId = id;
    }


    @FXML
    void submit(ActionEvent event) {
        if (txtName.getText().equals("")) {
            showErrors(validateName, hBoxName);
            Tooltip.install(hBoxName, new MyToolTip("Tên không được để trống"));
            checkValidateName = false;
        }
        if (txtMoney.getText().equals("")) {
            showErrors(validateMoney, hBoxMoney);
            Tooltip.install(hBoxMoney, new MyToolTip("Số tiền không được để trống"));
            checkValidateMoney = false;
        }

        if (checkValidateMoney && checkValidateName) {
            if (!isUpdate) {
                if (checkDuplicated()) {
                    addSalary();
                    Notification.informationAlert("Thông báo", "Thêm Thành Công!");
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                } else {
                    Notification.ErrorsAlert("Thêm thất bại", "Hệ số lương này đã tồn tại!");
                }
            } else {
//                System.out.println("Update ");
                updateSalary();
                Notification.informationAlert("Thông báo", "Sửa Thành Công!");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        }
    }

    private boolean checkDuplicated() {
        try {
            return salaryB.checkDuplicatedSalary(txtName.getText(), txtMoney.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void addSalary() {
        String createdBy = loginB.getSession().getSessionName();
        try {
            salaryB.addSalary(txtName.getText(), txtMoney.getText(), txtDescription.getText(), createdBy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSalary() {
        String modifiedBy = loginB.getSession().getSessionName();
        try {
            salaryB.updateSalary(salaryId, txtName.getText(), txtMoney.getText(), txtDescription.getText(), modifiedBy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
