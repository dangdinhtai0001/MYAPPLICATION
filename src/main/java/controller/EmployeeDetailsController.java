package controller;

import businessLogicLayer.EmployeeB;
import businessLogicLayer.LoginB;
import businessLogicLayer.SalaryB;
import businessLogicLayer.Validation;
import entity.Salary;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.File;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EmployeeDetailsController {
    private EmployeeB employeeB;
    private SalaryB salaryB;
    private Validation validation;
    private LoginB loginB;
    private boolean isUpdate;
    private boolean checkValidAccount, checkValidPassword, checkValidDateOfBegin, checkValidDateOfEnd, checkValidSalary;
    private boolean checkValidName, checkValidGender, checkValidDateOfBirth, checkValidAddress, checkValidPhone, checkValidFacebook;
    private boolean checkValidImageUrl;
    private int employeeID, oldSalaryID;

    private boolean showConfrimDialog;
    private String url;
    @FXML
    private ComboBox<Salary> comboSalary;
    @FXML
    private HBox hBoxAccount;
    @FXML
    private TextField txtAccount;
    @FXML
    private ImageView validateAccount;
    @FXML
    private HBox hBoxPassword;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ImageView validatePassword;
    @FXML
    private HBox hBoxDateOfBegin;
    @FXML
    private DatePicker txtDateOfBegin;
    @FXML
    private ImageView validateDateOfBegin;
    @FXML
    private HBox hBoxDateOfEnd;
    @FXML
    private DatePicker txtDateOfEnd;
    @FXML
    private ImageView validateDateOfEnd;
    @FXML
    private HBox hBoxSalary;
    @FXML
    private ImageView validateSalary;
    @FXML
    private HBox hBoxName;
    @FXML
    private TextField txtName;
    @FXML
    private ImageView validateName;
    @FXML
    private HBox hBoxGender;
    @FXML
    private ComboBox<String> comboGender;
    @FXML
    private ImageView validateGender;
    @FXML
    private HBox hBoxDateOfBirth;
    @FXML
    private DatePicker txtDateOfBirth;
    @FXML
    private ImageView validateDateOfBirth;
    @FXML
    private HBox hBoxAddress;
    @FXML
    private TextField txtAddress;
    @FXML
    private ImageView validateAddress;
    @FXML
    private HBox hBoxPhone;
    @FXML
    private TextField txtPhone;
    @FXML
    private ImageView validatePhone;
    @FXML
    private HBox hBoxFacebook;
    @FXML
    private TextField txtFacebook;
    @FXML
    private ImageView validateFacebook;
    @FXML
    private CheckBox checkBoxAdmin;
    @FXML
    private ImageView employeeImage;
    @FXML
    private HBox hBoxImage;
    @FXML
    private ImageView validateImage;
    @FXML
    private Label title;
    private int contactID;

    public EmployeeDetailsController() {
        try {
            salaryB = new SalaryB();
            validation = new Validation();
            loginB = new LoginB();
            employeeB = new EmployeeB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() throws SQLException {
        checkValidName = true;
        checkValidSalary = true;
        checkValidPassword = true;
        checkValidGender = true;
        checkValidFacebook = true;
        checkValidDateOfEnd = true;
        checkValidDateOfBirth = true;
        checkValidDateOfBegin = true;
        checkValidAddress = true;
        checkValidPhone = true;
        checkValidAccount = true;
        checkValidImageUrl = true;

        showConfrimDialog = false;

        txtDateOfBegin.setPromptText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        //Set các lực chọn cho combobox
        comboSalary.getItems().addAll(salaryB.getAllSalary());
        comboGender.getItems().addAll("Nam", "Nữ", "Khác");

        txtFacebook.textProperty().addListener(this::checkValidateFacebook);
        txtName.textProperty().addListener(this::checkValidateName);
        txtPhone.textProperty().addListener(this::checkValidatePhone);
        txtAccount.textProperty().addListener(this::checkValidateAccount);
        txtPassword.textProperty().addListener(this::checkValidatePassword);
        txtAddress.textProperty().addListener(this::checkValidateAddress);

        comboGender.getSelectionModel().selectedIndexProperty().addListener(this::checkValidateGender);
        comboSalary.getSelectionModel().selectedIndexProperty().addListener(this::checkValidateSalary);

        txtDateOfBirth.valueProperty().addListener(this::checkValidateBirth);
        txtDateOfBegin.valueProperty().addListener(this::checkValidateBegin);
        txtDateOfEnd.valueProperty().addListener(this::checkValidateEnd);
    }

    void isUpdate(boolean b) {
        isUpdate = b;
    }

    private String getComboSalaryValue() {
        if (comboSalary.getSelectionModel().getSelectedItem() != null) {
            return String.valueOf(comboSalary.getSelectionModel().getSelectedItem().getId());
        }
        return null;
    }

    @FXML
    void openImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn ảnh của nhân viên");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".png", "*.png"),
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".jpeg", "*.jpeg"));
        File source = chooser.showOpenDialog(null);
        url = "";
        if (source != null) {
            url = source.getAbsolutePath();
            url = url.replace('\\', '/');
            url = "/EmployeeImage" + url.substring(url.lastIndexOf('/'));
            employeeImage.setImage(new Image(url));
        }

    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void submit(ActionEvent event) {

        showConfrimDialog = false;
        if (checkSubmit()) {
            if (showConfrimDialog) {
                String r = Notification.ConfrimAlert("Thông báo", "Có một số trường để trống," + "\n" + "Bạn có muốn tiếp tục ? ");
                if (r != null) {
                    if (r.equals("Y")) {
                        if (!isUpdate) {
                            addEmployee();
                        } else {
                            String res = Notification.ConfrimAlert("Thông báo", "Bạn có thực sự muốn sửa như trên không?" + "\n" + "Các chỉnh sửa sẽ không thể hoàn tác");
                            if (res != null) {
                                if (res.equals("Y")) {
                                    updateEmployee();
                                }
                            }
                        }
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                }
            } else {
                if (!isUpdate) {
                    addEmployee();
                } else {
                    updateEmployee();
                }
            }
        } else {
            Notification.informationAlert("Thông báo", "Một số trường nhập sai định dạng");
        }

    }

    private void updateEmployee() {
        String createdBy = loginB.getSession().getSessionName();

        boolean b = employeeB.updateEmployee(txtName.getText(), comboGender.getValue(), txtDateOfBirth.getValue(),
                txtAddress.getText(), txtPhone.getText(), txtFacebook.getText(), txtDateOfBegin.getValue(),
                txtDateOfEnd.getValue(), url, txtAccount.getText(), txtPassword.getText(), checkBoxAdmin.isSelected(),
                createdBy, getComboSalaryValue(), employeeID, oldSalaryID, contactID);
        if (b)
            Notification.informationAlert("Thông báo", "Cập nhật thành công");
        else
            Notification.informationAlert("Thông báo", "SOmething wrong");
    }

    private void addEmployee() {
        String createdBy = loginB.getSession().getSessionName();
        boolean b = employeeB.addEmployee(txtName.getText(), comboGender.getValue(), txtDateOfBirth.getValue(),
                txtAddress.getText(), txtPhone.getText(), txtFacebook.getText(), txtDateOfBegin.getValue(),
                txtDateOfEnd.getValue(), url, txtAccount.getText(), txtPassword.getText(), checkBoxAdmin.isSelected(),
                createdBy, getComboSalaryValue());
        if (b)
            Notification.informationAlert("Thông báo", "Thêm thành công");
        else
            Notification.informationAlert("Thông báo", "SOmething wrong");
    }

    private boolean checkSubmit() {
        //Đoạn ni là đoạn check từ lúc filter rồi
        if (!checkValidImageUrl || !checkValidAddress || !checkValidDateOfBegin || !checkValidDateOfBirth
                || !checkValidGender || !checkValidName || !checkValidAccount || !checkValidPhone || !checkValidDateOfEnd
                || !checkValidFacebook || !checkValidPassword || !checkValidSalary) {
            return false;
        }
        //Xong mới đến đoạn check trường trống
        if (!checkContainFieldEmpty()) {
            return false;
        }
        //Đến đọa check logic cuối cùng
        return checkTimeLine() && checkBirthday();
    }

    private boolean checkContainFieldEmpty() {
        boolean b = true;
        if (validation.checkEmpty(txtName.getText())) {
            validation.showErrors(hBoxName, validateName, "Tên không được để trống");
            b = false;
        }
        if (comboGender.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(hBoxGender, validateGender, "Giới tính không được để trống ");
            b = false;
        }
        if (txtDateOfBirth.getValue() == null) {
            validation.showErrors(hBoxDateOfBirth, validateDateOfBirth, "Ngày sinh không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtAddress.getText())) {
            validation.showErrors(hBoxAddress, validateAddress, "Địa chỉ không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtPhone.getText())) {
            validation.showWarning(hBoxPhone, validatePhone, "Số điện thoại đang bỏ trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtFacebook.getText())) {
            validation.showWarning(hBoxFacebook, validateFacebook, "Địa chỉ facebook đang bỏ trống");
            showConfrimDialog = true;
        }
        if (txtDateOfBegin.getValue() == null) {
            validation.showErrors(hBoxDateOfBegin, validateDateOfBegin, "ngày bắt đầu không được để trống");
            b = false;
        }
        if (txtDateOfEnd.getValue() == null) {
            validation.showWarning(hBoxDateOfEnd, validateDateOfEnd, "Ngày kết thúc đang được để trống");
            showConfrimDialog = true;

        }
        if (!isUpdate) {
            if (validation.checkEmpty(url)) {
                validation.showErrors(hBoxImage, validateImage, "Ảnh không được để trống");
                b = false;
            } else {
                validation.showValid(hBoxImage, validateImage);
            }
        }
        if (validation.checkEmpty(txtAccount.getText())) {
            validation.showWarning(hBoxAccount, validateAccount, "Tài khoản đang bỏ trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtPassword.getText())) {
            validation.showWarning(hBoxPassword, validatePassword, "Mật khâu đang bỏ trống");
            showConfrimDialog = true;
        }
        if (checkBoxAdmin.isSelected()) {
            validation.showWarning(hBoxAccount, validateAccount, "Đang chọn làm admin");
            showConfrimDialog = true;
        }
        if (comboSalary.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(hBoxSalary, validateSalary, "Lương không được để trống");
            b = false;
        }
        return b;
    }

    private void checkValidateName(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (validation.checkContainNumber(newValue)) {
            validation.showErrors(hBoxName, validateName, "Tên không được chứa chữ số ");
            checkValidName = false;
        } else if (validation.checkMaxLength(newValue, 50)) {
            validation.showErrors(hBoxName, validateName, "Tên không được quá 50 kí tự");
            checkValidName = false;
        } else if (validation.checkContainDelimiter(newValue)) {
            validation.showErrors(hBoxName, validateName, "Tên không được chưa kí tự đặc biệt");
            checkValidName = false;
        } else {
            validation.showValid(hBoxName, validateName);
            checkValidName = true;
        }
    }

    private void checkValidatePhone(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!validation.checkPhoneNumber(newValue)) {
            validation.showErrors(hBoxPhone, validatePhone, "Số điện thoại chỉ dài tối đa 20 kí tự");
            checkValidPhone = false;
        } else {
            validation.showValid(hBoxPhone, validatePhone);
            checkValidPhone = true;
        }
    }

    private void checkValidateAccount(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (validation.checkMaxLength(newValue, 256)) {
            validation.showErrors(hBoxAccount, validateAccount, "Tên tài khoản không quá 256 kí tự");
            checkValidAccount = false;
        } else if (validation.checkStartByNumber(newValue)) {
            validation.showErrors(hBoxAccount, validateAccount, "Tên tài khoản không bắt đầu bằng số ");
            checkValidAccount = false;
        } else {
            validation.showValid(hBoxAccount, validateAccount);
            checkValidAccount = true;
        }

        if (newValue != null && (txtPassword.getText().equals("\\s+") || txtPassword.getText() == null)) {
            validation.showWarning(hBoxPassword, validatePassword, "Chưa nhập password");
        }
    }

    private boolean checkTimeLine() {
        if (txtDateOfBegin.getValue() != null && txtDateOfEnd.getValue() != null) {
            if (!validation.checkTheDayBefore(txtDateOfBegin.getValue(), txtDateOfEnd.getValue())) {
                validation.showErrors(hBoxDateOfBegin, validateDateOfBegin, "Xem lại thứ tự ngày tháng");
                validation.showErrors(hBoxDateOfEnd, validateDateOfEnd, "Xem lại thứ tự ngày tháng");
                return false;
            } else {
                validation.showValid(hBoxDateOfBegin, validateDateOfBegin);
                validation.showValid(hBoxDateOfEnd, validateDateOfEnd);
            }
        }
        return true;
    }

    private boolean checkBirthday() {
        if (txtDateOfBirth.getValue() != null) {
            Date date = new Date();
            LocalDate current = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
            if (!validation.checkTheDayBefore(txtDateOfBirth.getValue(), current)) {
                validation.showErrors(hBoxDateOfBirth, validateDateOfBirth, "Sinh từ tương lai =)) ");
                return false;
            } else {
                validation.showValid(hBoxDateOfBirth, validateDateOfBirth);
                return true;
            }
        }
        return false;
    }

    private void checkValidatePassword(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (validation.checkMaxLength(newValue, 256)) {
            validation.showErrors(hBoxPassword, validatePassword, "Mật khẩu không được dài quá 256 kí tự");
            checkValidPassword = false;
        } else if (validation.checkEmpty(txtAccount.getText()) && newValue != null) {
            validation.showErrors(hBoxAccount, validateAccount, "Chưa có tài khoản");
            checkValidPassword = false;
        } else {
            validation.showValid(hBoxPassword, validatePassword);
        }
    }

    private void checkValidateAddress(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (validation.checkMaxLength(newValue, 256)) {
            validation.showErrors(hBoxAddress, validateAddress, "Địa chỉ không dài quá 256 kí tự");
            checkValidAddress = false;
        } else {
            validation.showValid(hBoxAddress, validateAddress);
            checkValidAddress = true;
        }
    }

    private void checkValidateSalary(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (comboSalary.getSelectionModel().getSelectedItem() != null) {
            validation.showValid(hBoxSalary, validateSalary);
            checkValidSalary = true;
        }
    }

    private void checkValidateGender(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (newValue != null) {
            validation.showValid(hBoxGender, validateGender);
            checkValidGender = true;
        }
    }

    private void checkValidateBirth(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        if (txtDateOfBirth.getValue() != null) {
            validation.showValid(hBoxDateOfBirth, validateDateOfBirth);
        }
    }

    private void checkValidateBegin(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        if (txtDateOfBegin.getValue() != null) {
            validation.showValid(hBoxDateOfBegin, validateDateOfBegin);
        }
    }

    private void checkValidateEnd(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        if (txtDateOfEnd.getValue() != null) {
            validation.showValid(hBoxDateOfEnd, validateDateOfEnd);
        }
    }

    private void checkValidateFacebook(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!validation.checkEmpty(newValue)) {
            validation.showValid(hBoxFacebook, validateFacebook);
        }
    }

    public void loadName(String s) {
        txtName.setText(s);
    }

    void loadPassword(String s) {
        if (s != null)
            txtPassword.setText(s);
    }

    void loadFacebook(String s) {
        if (s != null)
            txtFacebook.setText(s);
    }

    void loadAddress(String s) {
        txtAddress.setText(s);
    }

    void loadPhone(String s) {
        if (s != null)
            txtPhone.setText(s);
    }

    void loadAccount(String s) {
        if (s != null)
            txtAccount.setText(s);
    }

    void loadImage(Image s) {
        employeeImage.setImage(s);
    }

    void loadGender(String s) {
        for (String ignored : comboGender.getItems()) {
            if (ignored.equals(s)) {
                comboGender.getSelectionModel().select(ignored);
            }
        }
    }

    void loadSalary(int salaryId) {
        for (Salary salary : comboSalary.getItems()) {
            if (salary.getId() == salaryId) {
                comboSalary.getSelectionModel().select(salary);
            }
        }
    }

    void loadDateOfBirth(Date s) {
        txtDateOfBirth.setValue(Instant.ofEpochMilli(s.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
    }

    void loadDateOfBegin(Date s) {
        txtDateOfBegin.setValue(Instant.ofEpochMilli(s.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
    }

    void loadDateOfEnd(Date s) {
        try {
            txtDateOfEnd.setValue(Instant.ofEpochMilli(s.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        } catch (NullPointerException e) {

        }
    }

    void isAdmin(boolean b) {
        checkBoxAdmin.setSelected(b);
    }

    void setTitle(String s) {
        title.setText(s);
    }

    void loadEmployeeID(int i) {
        employeeID = i;
    }

    void loadOldSalaryID(int i) {
        oldSalaryID = i;
    }

    void loadContactID(int i) {
        contactID = i;
    }

    void loadUrl(String s) {
        url = s;
    }
}
