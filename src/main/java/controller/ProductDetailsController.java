package controller;

import businessLogicLayer.ProductB;
import businessLogicLayer.ProviderB;
import businessLogicLayer.Validation;
import entity.Node;
import entity.Product;
import entity.Provider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.File;
import java.sql.SQLException;

public class ProductDetailsController {
    private ProductB productB;
    private ProviderB providerB;
    private Validation validation;
    private boolean checkValidName, checkValidCode, checkValidQuantity, checkValidPrice, checkValidUnit, checkValidProvider, checkValidType;
    private String url;
    private boolean isUpdating;
    private Product product;
    @FXML
    private ImageView imageProduct;
    @FXML
    private HBox hBoxName;
    @FXML
    private TextField txtName;
    @FXML
    private ImageView validateName;
    @FXML
    private HBox hBoxCode;
    @FXML
    private TextField txtCode;
    @FXML
    private ImageView validateCode;
    @FXML
    private HBox hBoxQuantity;
    @FXML
    private TextField txtQuantity;
    @FXML
    private ImageView validateQuantity;
    @FXML
    private HBox hBoxPrice;
    @FXML
    private TextField txtPrice;
    @FXML
    private ImageView validatePrice;
    @FXML
    private HBox hBoxUnit;
    @FXML
    private ComboBox<Node> comboUnit;
    @FXML
    private ImageView validateUnit;
    @FXML
    private HBox hBoxProvider;
    @FXML
    private ComboBox<Provider> comboProvider;
    @FXML
    private ImageView validateProvider;
    @FXML
    private HBox hBoxType;
    @FXML
    private ComboBox<Node> comboType;
    @FXML
    private ImageView validateType;
    @FXML
    private HBox hBoxDescription;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ImageView validateDescription;
    @FXML
    private ImageView validateImage;

    public ProductDetailsController() {
        try {
            isUpdating = false;
            productB = new ProductB();
            providerB = new ProviderB();
            validation = new Validation();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Chọn ảnh sản phẩm");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".png", "*.png"),
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".jpeg", "*.jpeg"));
        File source = chooser.showOpenDialog(null);
        url = "";
        if (source != null) {
            url = source.getAbsolutePath();
            url = url.replace('\\', '/');
            url = "/ProductImage" + url.substring(url.lastIndexOf('/'));
            imageProduct.setImage(new Image(url));
        }

    }

    @FXML
    void initialize() throws SQLException {
        checkValidName = true;
        checkValidCode = true;
        checkValidQuantity = true;
        checkValidPrice = true;
        checkValidUnit = true;
        checkValidProvider = true;
        checkValidType = true;

        comboUnit.setItems(productB.getAllUnit());
        comboType.setItems(productB.getAllType());
        comboProvider.setItems(providerB.getAllProvider());

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validation.checkMaxLength(newValue, 256)) {
                validation.showErrors(hBoxName, validateName, "Tên không được dài uqas 256 kí tự");
                checkValidName = false;
            } else {
                validation.showValid(hBoxName, validateName);
                checkValidName = true;
            }
        });

        txtCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validation.checkMaxLength(newValue, 5)) {
                validation.showErrors(hBoxCode, validateCode, "Mã sản phẩm không được dài quá 5 kí tự");
                checkValidCode = false;
            } else {
                validation.showValid(hBoxCode, validateCode);
                checkValidCode = true;
            }
        });

        txtQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!validation.checkIsNumber(newValue)) {
                validation.showErrors(hBoxQuantity, validateQuantity, "Số lượng sản phẩm phải là dạng số ");
                checkValidQuantity = false;
            } else if (validation.checkMaxLength(newValue, 11)) {
                validation.showErrors(hBoxQuantity, validateQuantity, "Số nhập vào quá lớn");
                checkValidQuantity = false;
            } else {
                validation.showValid(hBoxQuantity, validateQuantity);
                checkValidQuantity = true;
            }
        });

        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!validation.checkIsNumber(newValue)) {
                validation.showErrors(hBoxPrice, validatePrice, "Giá sản phẩm phải là dạng số ");
                checkValidPrice = false;
            } else if (validation.checkMaxLength(newValue, 11)) {
                validation.showErrors(hBoxPrice, validatePrice, "Số nhập vào quá lớn");
                checkValidPrice = false;
            } else {
                validation.showValid(hBoxPrice, validatePrice);
                checkValidPrice = true;
            }
        });
    }

    @FXML
    void actionCancel(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void actionOK(ActionEvent event) throws SQLException {
        if (checkFinished()) {
//            System.out.println("check finised true");
            if (!isUpdating) {
                product = addProduct();
            } else {
                updateProduct();
            }
//            System.out.println(product.toString());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Notification.informationAlert("Thông báo", "Một số trường nhập sai định dạng");
        }
    }

    private void updateProduct() {
        System.out.println("update");
        product = addProduct();
    }

    private Product addProduct() {
        int quantity = Integer.parseInt(txtQuantity.getText());
        int impPrice = Integer.parseInt(txtPrice.getText());
        int typeID = comboType.getSelectionModel().getSelectedItem().getId();
        int unitID = comboUnit.getSelectionModel().getSelectedItem().getId();
        int providerID = comboProvider.getSelectionModel().getSelectedItem().getProviderID();
        String name = txtName.getText();
        String shortName = txtCode.getText().toUpperCase();
        String productCode = null;
        String imageLink;
        if (!isUpdating) {
            imageLink = url;
        } else {
            imageLink = imageProduct.getImage().getUrl();
            imageLink = imageLink.replace('\\', '/');
            imageLink = "/ProductImage" + imageLink.substring(imageLink.lastIndexOf('/'));
        }
        String type = comboType.getSelectionModel().getSelectedItem().getName();
        String providerName = comboProvider.getSelectionModel().getSelectedItem().getName();
        String unit = comboUnit.getSelectionModel().getSelectedItem().getName();
        String description = txtDescription.getText();
        return new Product(0, quantity, impPrice, 0, typeID, unitID, providerID, name, shortName,
                productCode, imageLink, type, providerName, unit, description);
    }

    private boolean checkFinished() throws SQLException {
        if (!checkValidName || !checkValidCode || !checkValidPrice || !checkValidQuantity || !checkValidProvider || !checkValidType || !checkValidUnit) {
            return false;
        }

        //check trường trống
        if (!checkContainEmptyfield()) {
            return false;
        }
        //Check logic
//        System.out.println("!checkDuplicatedCode()" + !checkDuplicatedCode() + "\n checkCodeLength()" + checkCodeLength());
        return !checkDuplicatedCode() && checkCodeLength();
    }

    private boolean checkContainEmptyfield() {
        boolean b = true;
        if (validation.checkEmpty(txtName.getText())) {
            validation.showErrors(hBoxName, validateName, "Tên sản phẩm không được để trống ");
            b = false;
        }

        if (validation.checkEmpty(txtCode.getText())) {
            validation.showErrors(hBoxCode, validateCode, "Mã sản phẩm không được để trống");
            b = false;
        }

        if (validation.checkEmpty(txtPrice.getText())) {
            validation.showErrors(hBoxPrice, validatePrice, "Giá sản phẩm không được để trống");
            b = false;
        }

        if (validation.checkEmpty(txtQuantity.getText())) {
            validation.showErrors(hBoxQuantity, validateQuantity, "Số lượng sản phẩm không được để trống");
            b = false;
        }
        if (validation.checkEmpty(txtDescription.getText())) {
            validation.showWarning(hBoxDescription, validateDescription, "Ghi chú đang bị bỏ trống");
        }

        if (comboProvider.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(hBoxProvider, validateProvider, "Nhà cung cấp không được bỏ trống");
            b = false;
        }
        if (comboType.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(hBoxType, validateType, "Loại sản phẩm không được bỏ trống");
            b = false;
        }
        if (comboUnit.getSelectionModel().getSelectedItem() == null) {
            validation.showErrors(hBoxUnit, validateUnit, "Đơn vị không được bỏ trống ");
            b = false;
        }

        if (!isUpdating) {
            if (validation.checkEmpty(url)) {
                validation.showErrors(null, validateImage, "Ảnh sản phẩm không được bỏ trống");
                b = false;
            }
        }
        return b;
    }

    private boolean checkDuplicatedCode() throws SQLException {
        return productB.checkDuplicatedProductCode(txtCode.getText());
    }

    private boolean checkCodeLength() {
        if (validation.checkMinLength(txtCode.getText(), 5)) {
            validation.showErrors(hBoxCode, validateCode, "Phải có đủ 5 ki tự");
            return false;
        }
        return true;
    }

    Product getProduct() {
        return product;
    }

    void loadName(String s) {
        txtName.setText(s);
    }

    void loadCode(String s) {
        txtCode.setText(s);
    }

    void loadPrice(int s) {
        txtPrice.setText(String.valueOf(s));
    }

    void loadQuantity(int s) {
        txtQuantity.setText(String.valueOf(s));
    }

    void loadDescription(String s) {
        txtDescription.setText(s);
    }

    void loadType(int typeID, String type) {
        Node node = new Node(typeID, type);
        comboType.getSelectionModel().select(node);
    }

    void loadUnit(int unitID, String unit) {
        Node node = new Node(unitID, unit);
        comboUnit.getSelectionModel().select(node);
    }

    void loadProvider(int providerID) {
        for (Provider provider : comboProvider.getItems()) {
            if (provider.getProviderID() == providerID) {
                comboProvider.getSelectionModel().select(provider);
                break;
            }
        }
    }

    void loadImage(String url) {
        imageProduct.setImage(new Image(url));
    }

    void isUpdate(boolean b) {
        this.isUpdating = b;
    }

    void loadProduct(Product product) {
        this.product = product;
    }
}
