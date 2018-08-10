package controller;

import businessLogicLayer.LoginB;
import businessLogicLayer.ProviderB;
import businessLogicLayer.Validation;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import presentation.Notification;

import java.sql.SQLException;

public class ProviderDetailsController {
    private ProviderB providerB;
    private Validation validation;
    private boolean checkValidName, checkValidShortName, checkValidAddress;
    private boolean showConfrimDialog;
    private boolean isUpdate, goodProvider, badProvider;
    private LoginB loginB;
    private int providerID, contactID;
    @FXML
    private HBox hBoxName;
    @FXML
    private TextField txtName;
    @FXML
    private ImageView validateName;
    @FXML
    private HBox hBoxShortName;
    @FXML
    private TextField txtShortName;
    @FXML
    private ImageView validateShortName;
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
    private HBox hBoxMail;
    @FXML
    private TextField txtMail;
    @FXML
    private ImageView validateMail;
    @FXML
    private HBox hBoxFax;
    @FXML
    private TextField txtFax;
    @FXML
    private ImageView validateFax;
    @FXML
    private HBox hBoxStatus;
    @FXML
    private ComboBox<String> comboBoxStatus;
    @FXML
    private ImageView validateStatus;
    @FXML
    private HBox hBoxDescription;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ImageView validateDescription;
    @FXML
    private HBox hBoxBankAccount;
    @FXML
    private TextField txtBankAccount;
    @FXML
    private ImageView validateBankAccount;

    public ProviderDetailsController() {
        try {
            providerB = new ProviderB();
            validation = new Validation();
            loginB = new LoginB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getComboBoxStatusValue() {
        if (comboBoxStatus.getSelectionModel().getSelectedItem() == null || comboBoxStatus.getSelectionModel().getSelectedItem().equals("Chưa đánh giá")) {
            goodProvider = false;
            badProvider = false;
        } else {
            if (comboBoxStatus.getSelectionModel().getSelectedItem().equals("Kém")) {
                badProvider = true;
                goodProvider = false;
            }
            if (comboBoxStatus.getSelectionModel().getSelectedItem().equals("Tốt")) {
                badProvider = false;
                goodProvider = true;
            }
        }
    }


    @FXML
    void initialize() {
        comboBoxStatus.getItems().addAll("Tốt", "Kém", "Chưa đánh giá");

        checkValidName = true;
        checkValidAddress = true;
        checkValidShortName = true;

        showConfrimDialog = false;

        txtName.textProperty().addListener(this::checkValidateName);
        txtShortName.textProperty().addListener(this::checkValiateShortName);
        txtAddress.textProperty().addListener(this::checkValidateAddress);
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
                            addProvider();
                        } else {
                            String res = Notification.ConfrimAlert("Thông báo", "Bạn có thực sự muốn sửa như trên không?" + "\n" + "Các chỉnh sửa sẽ không thể hoàn tác");
                            if (res != null) {
                                if (res.equals("Y")) {
                                    updateProvider();
                                }
                            }
                        }
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                    }
                }
            } else {
                if (!isUpdate) {
                    addProvider();
                } else {
                    updateProvider();
                }
            }
        } else {
            Notification.informationAlert("Thông báo", "Một số trường nhập sai định dạng");
        }

    }

    private void updateProvider() {
        String modifiedBy = loginB.getSession().getSessionName();
        boolean b = providerB.updateProvider(txtName.getText(), txtShortName.getText().toUpperCase(), txtAddress.getText(), txtDescription.getText(),
                txtPhone.getText(), txtMail.getText(), txtFax.getText(), goodProvider, badProvider, modifiedBy, providerID, contactID, txtBankAccount.getText());
        if (b)
            Notification.informationAlert("Thông báo", "Cập nhật thành công");
        else
            Notification.informationAlert("Thông báo", "Something wrong");
    }

    private void addProvider() {
        getComboBoxStatusValue();
        String createdBy = loginB.getSession().getSessionName();
        boolean b = providerB.addProvider(txtName.getText(), txtShortName.getText().toUpperCase(), txtAddress.getText(), txtDescription.getText(),
                txtPhone.getText(), txtMail.getText(), txtFax.getText(), goodProvider, badProvider, createdBy, txtBankAccount.getText());
        if (b)
            Notification.informationAlert("Thông báo", "Thêm thành công");
        else
            Notification.informationAlert("Thông báo", "Something wrong");
    }

    private boolean checkSubmit() {
        boolean b = true;
        //check lúc filter đầu tiên
        if (!checkValidAddress || !checkValidShortName || !checkValidName) {
            b = false;
        }
        //check trường bỏ trống
        if (!checkContainFieldEmpty()) {
            b = false;
        }
        //check điều kiện logic cuối cùng

        if (!isUpdate) {
            if (!checkPhoneNumber() || !checkMail() || !checkShortName()) {
                b = false;
            }
        } else {
            if (!checkPhoneNumber() || !checkMail()) {
                b = false;
            }
        }
        //Qua hết thì cho submit
        return b;
    }

    private boolean checkContainFieldEmpty() {
        boolean b = true;

        if (validation.checkEmpty(txtName.getText())) {
            validation.showErrors(hBoxName, validateName, "Tên không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtShortName.getText())) {
            validation.showErrors(hBoxShortName, validateShortName, "Mã nhà cung cấp không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtAddress.getText())) {
            validation.showErrors(hBoxAddress, validateAddress, "Địa chỉ không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtPhone.getText())) {
            validation.showWarning(hBoxPhone, validatePhone, "Số điện thoại đang được để trống");
            showConfrimDialog = true;
        }
        if (comboBoxStatus.getSelectionModel().getSelectedItem() == null) {
            validation.showWarning(hBoxStatus, validateStatus, "Trạng thái đang được để trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtMail.getText())) {
            validation.showWarning(hBoxMail, validateMail, "Địa chỉ mail đang được để trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtFax.getText())) {
            validation.showWarning(hBoxFax, validateFax, "Địa chỉ fax đang được để trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtDescription.getText())) {
            validation.showWarning(hBoxDescription, validateDescription, "Ghi chú đang được để trống");
            showConfrimDialog = true;
        }
        if (validation.checkEmpty(txtBankAccount.getText())) {
            validation.showWarning(hBoxBankAccount, validateBankAccount, "Số tài khoản đang được để trống");
            showConfrimDialog = true;
        }
        return b;
    }

    private void checkValidateName(ObservableValue<? extends String> observable, String oldValue, String
            newValue) {
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

    private void checkValiateShortName(ObservableValue<? extends String> observable, String oldValue, String
            newValue) {
        if (validation.checkContainDelimiter(newValue)) {
            validation.showErrors(hBoxShortName, validateShortName, "Không được chứa kí tự đặc biệt ");
            checkValidShortName = false;
        } else if (validation.checkContainSpace(newValue) && newValue.trim().length() != 0) {
            validation.showErrors(hBoxShortName, validateShortName, "Không được chưa khoảng trắng ");
            checkValidShortName = false;
        } else if (validation.checkMaxLength(newValue, 3) && newValue.trim().length() != 0) {
            validation.showErrors(hBoxShortName, validateShortName, "Độ dài tối đa là 3 ");
            checkValidShortName = false;
        } else if (validation.checkIsNumber(newValue) && newValue.trim().length() == 3) {
            validation.showErrors(hBoxShortName, validateShortName, "Mã nhà cung cấp không được chứa toàn chữ số");
            checkValidShortName = false;
        } else {
            validation.showValid(hBoxShortName, validateShortName);
            checkValidShortName = true;
        }
    }

    private void checkValidateAddress(ObservableValue<? extends String> observable, String oldValue, String
            newValue) {
        if (validation.checkMaxLength(newValue, 256)) {
            validation.showErrors(hBoxAddress, validateAddress, "Độ dài tối đa là 256 kí tự");
            checkValidAddress = false;
        } else {
            validation.showValid(hBoxAddress, validateAddress);
            checkValidAddress = true;
        }
    }

    private boolean checkPhoneNumber() {
        if (validation.checkEmpty(txtPhone.getText())) {
            return true;
        } else {
            if (validation.checkPhoneNumber(txtPhone.getText())) {
                validation.showValid(hBoxPhone, validatePhone);
                return true;
            } else {
                validation.showErrors(hBoxPhone, validatePhone, "Sai định dạng só điện thoại");
                return false;
            }
        }
    }

    private boolean checkMail() {
        if (validation.checkEmpty(txtMail.getText())) {
            return true;
        } else {
            if (validation.checkMail(txtMail.getText())) {
                validation.showValid(hBoxMail, validateMail);
                return true;
            } else {
                validation.showErrors(hBoxMail, validateMail, "Sai định dạng mail");
                return false;
            }
        }
    }

    private boolean checkShortName() {
        try {
            if (validation.checkMinLength(txtShortName.getText(), 3)) {
                validation.showErrors(hBoxShortName, validateShortName, "Độ dài phải là 3 kí tự");
                return false;
            }

            if (providerB.checkDuplicatedShortName(txtShortName.getText())) {
                validation.showErrors(hBoxShortName, validateShortName, "Mã nhà cung cấp đã bị trùng");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    void loadName(String text) {
        txtName.setText(text);
    }

    void loadShortName(String text) {
        txtShortName.setText(text);
    }

    void loadAddress(String text) {
        txtAddress.setText(text);
    }

    void loadStatus(String status) {
        comboBoxStatus.getSelectionModel().select(status);
    }

    void loadPhone(String text) {
        try {
            txtPhone.setText(text);
        } catch (NullPointerException e) {
            txtPhone.setText(null);
        }
    }

    void loadMail(String text) {
        try {
            txtMail.setText(text);
        } catch (NullPointerException e) {
            txtMail.setText(null);
        }
    }

    void loadFax(String text) {
        try {
            txtFax.setText(text);
        } catch (NullPointerException e) {
            txtFax.setText(null);
        }
    }

    void loadDescription(String text) {
        try {
            txtDescription.setText(text);
        } catch (NullPointerException e) {
            txtDescription.setText(null);
        }
    }

    void isUpdate(boolean b) {
        isUpdate = b;
    }

    void loadProviderID(int i) {
        providerID = i;
    }

    void loadContactID(int i) {
        contactID = i;
    }
}
