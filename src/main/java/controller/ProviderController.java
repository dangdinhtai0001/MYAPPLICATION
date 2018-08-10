package controller;

import businessLogicLayer.LoginB;
import businessLogicLayer.ProviderB;
import entity.Provider;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import presentation.Notification;

import java.io.IOException;
import java.sql.SQLException;

public class ProviderController {
    private ProviderB providerB;
    private LoginB loginB;
    private ObservableList<Provider> listProvider;
    private boolean isUpdating;
    private int totalPercentage, liquidatePercentage;
    @FXML
    private TableView<Provider> tableProvider;
    @FXML
    private TextField txtFilter;
    @FXML
    private TableColumn<Provider, Number> number;
    @FXML
    private TableColumn<Provider, String> nameCol;
    @FXML
    private TableColumn<Provider, String> shortNameCol;
    @FXML
    private TableColumn<Provider, String> addressCol;
    @FXML
    private TableColumn<Provider, String> phoneCol;
    @FXML
    private TableColumn<Provider, String> mailCol;
    @FXML
    private TableColumn<Provider, String> faxCol;
    @FXML
    private TableColumn<Provider, String> bankAccountCol;
    @FXML
    private TableColumn<Provider, String> statusCol;
    @FXML
    private TableColumn<Provider, Boolean> activeCol;
    @FXML
    private TableColumn<Provider, Number> liquidateCol;
    @FXML
    private TableColumn<Provider, Number> totalCol;
    @FXML
    private TableColumn<Provider, String> descriptionCol;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart totalMoneyPieChart;
    @FXML
    private PieChart liquidatePieChart;
    @FXML
    private Label lblLiquidate;
    @FXML
    private Label lblTotalMoney;

    public ProviderController() throws SQLException, ClassNotFoundException {
        providerB = new ProviderB();
        loginB = new LoginB();
    }

    @FXML
    void initialize() {
        number.setCellValueFactory(param -> new ReadOnlyObjectWrapper(String.valueOf(tableProvider.getItems().indexOf(param.getValue()) + 1)));

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        shortNameCol.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mail"));
        faxCol.setCellValueFactory(new PropertyValueFactory<>("fax"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        liquidateCol.setCellValueFactory(new PropertyValueFactory<>("liquidate"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        bankAccountCol.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));

        //Làm cột có checkbox
        //đổi từ boolean sang booleanProperty
        activeCol.setCellValueFactory(param -> {
            Provider provider = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(provider.isActive());
            return booleanProp;
        });
        //Tạo checkbox
        activeCol.setCellFactory(param -> {
            CheckBoxTableCell<Provider, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        //Phải có đủ 2 đoạn này

        try {
            listProvider = providerB.getAllProvider();
            tableProvider.setItems(listProvider);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Sự kiện filter bảng
        ObservableList<Provider> data = tableProvider.getItems();
        txtFilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tableProvider.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<Provider> subentries = FXCollections.observableArrayList();

            long count = (long) tableProvider.getColumns().size();
            for (int i = 0; i < tableProvider.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = String.valueOf(tableProvider.getColumns().get(j).getCellData(i));
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tableProvider.getItems().get(i));
                        break;
                    }
                }
            }
            tableProvider.setItems(subentries);
        });

        //Dẩy dữ liệu lên biểu đồ (Chưa làm cập nhật )
        initChart();
    }

    private void initBarChart() {
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Tiền đã trả");

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Tổng tiền");

        for (Provider provider : listProvider) {
            dataSeries.getData().add(new XYChart.Data<>(provider.getName(), (provider.getTotalMoney() - provider.getLiquidate())));
            dataSeries1.getData().add(new XYChart.Data<>(provider.getName(), provider.getTotalMoney()));
        }

        barChart.getData().add(dataSeries);
        barChart.getData().add(dataSeries1);
    }

    private void initTotalPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        totalPercentage = 0;
        for (Provider provider : listProvider) {
//            System.out.println(salary.getName() + "\n" +(Integer) getPieData().get(salary.getId()) );
            if (provider.getTotalMoney() == 0) {
                pieChartData.add(new PieChart.Data(provider.getName(), 0));
            } else {
                pieChartData.add(new PieChart.Data(provider.getName(), provider.getTotalMoney()));
                totalPercentage += provider.getTotalMoney();
            }
        }
        totalMoneyPieChart.setData(pieChartData);

        for (final PieChart.Data data : totalMoneyPieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.ANY,
                    e -> lblTotalMoney.setText(data.getName() + ":" + String.valueOf((data.getPieValue() / totalPercentage) * 100) + "%")
            );
        }
    }

    private void initLiquidatePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        liquidatePercentage = 0;
        for (Provider provider : listProvider) {
//            System.out.println(salary.getName() + "\n" +(Integer) getPieData().get(salary.getId()) );
            if (provider.getLiquidate() == 0) {
                pieChartData.add(new PieChart.Data(provider.getName(), 0));
            } else {
                pieChartData.add(new PieChart.Data(provider.getName(), provider.getLiquidate()));
                liquidatePercentage += provider.getLiquidate();
            }
        }
        liquidatePieChart.setData(pieChartData);

        for (final PieChart.Data data : liquidatePieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.ANY,
                    e -> lblLiquidate.setText(data.getName() + ":" + String.valueOf((data.getPieValue() / liquidatePercentage) * 100) + "%")
            );
        }
    }

    private void initChart() {
        //đẩy dữ liệu lên biểu đồ cột
        initBarChart();
        //đẩy dữ liệu lên biểu đồ quạt1
        initTotalPieChart();

        //đẩy dữ liệu lên biểu đồ quạt2
        initLiquidatePieChart();
    }

    private void refreshTable() {
        try {
            listProvider = providerB.getAllProvider();
            tableProvider.getItems().clear();
            tableProvider.setItems(listProvider);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addProvider(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ProviderDetails.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnHiding(event1 -> refreshTable());
        stage.show();
    }

    @FXML
    void delProvider(ActionEvent event) {
        if (tableProvider.getSelectionModel().getSelectedItem() != null) {
            String r = Notification.ConfrimAlert("Thông báo", "Bạn có thực sự muốn xóa không?");
            if (r != null) {
                if (r.equals("Y")) {
                    if (providerB.deleteProvider(tableProvider.getSelectionModel().getSelectedItem().getProviderID(),
                            loginB.getSession().getSessionName())) {
                        Notification.informationAlert("Thông Báo", "Xóa thành công");
                        refreshTable();
                    } else {
                        Notification.informationAlert("Thông Báo", "Something wrong");
                    }
                }
            }
        } else {
            Notification.informationAlert("Thông Báo", "Hãy chọn hàng để xóa");
        }
    }

    @FXML
    void showLiquidate(ActionEvent event) {

    }

    @FXML
    void showQuotation(ActionEvent event) throws IOException {
        if (tableProvider.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Quotation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);

            QuotationController controller = loader.getController();
            stage.setOnShowing(event1 -> controller.loadProviderID(tableProvider.getSelectionModel().getSelectedItem().getProviderID()));
            stage.show();
        } else {
            Notification.informationAlert("Thông Báo", "Hãy chọn nhà cung cấp ");
        }
    }

    @FXML
    void updateProvider(ActionEvent event) throws IOException {
        if (tableProvider.getSelectionModel().getSelectedItem() != null) {
            Provider provider = tableProvider.getSelectionModel().getSelectedItem();
            isUpdating = true;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ProviderDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            ProviderDetailsController controller = loader.getController();

            controller.loadName(provider.getName());
            controller.loadShortName(provider.getShortName());
            controller.loadAddress(provider.getAddress());
            controller.loadStatus(provider.getStatus());
            controller.loadPhone(provider.getPhone());
            controller.loadMail(provider.getMail());
            controller.loadFax(provider.getFax());
            controller.loadDescription(provider.getDescription());
            controller.loadProviderID(provider.getProviderID());
            controller.loadContactID(provider.getContactID());
            controller.isUpdate(true);

            stage.setOnHiding(event1 -> {
                refreshTable();
                isUpdating = false;
            });
            stage.show();
        } else {
            Notification.informationAlert("Thông báo", "Hãy chọn nhà cung cấp để tiến hành cập nhật");
        }
    }

    @FXML
    void stopActive(ActionEvent event) {
        if (tableProvider.getSelectionModel().getSelectedItem() != null) {
            if (!tableProvider.getSelectionModel().getSelectedItem().isActive()) {
                Notification.informationAlert("Thông Báo", "Bạn đã dừng hợp tác với nhà cung cấp này");
            } else {
                if (providerB.updateActive(tableProvider.getSelectionModel().getSelectedItem().getProviderID(),
                        loginB.getSession().getSessionName(), false)) {
                    Notification.informationAlert("Thông Báo", "Đã dừng hợp tác \n Thông tin về nhà cung cấp vẫn đang được lưu lại");
                    refreshTable();
                } else {
                    Notification.informationAlert("Thông Báo", "Something wrong");
                }
            }
        } else {
            Notification.informationAlert("Thông Báo", "Hãy chọn hàng để thao tác");
        }

    }

    @FXML
    void continueActive(ActionEvent event) {
        if (tableProvider.getSelectionModel().getSelectedItem() != null) {
            if (tableProvider.getSelectionModel().getSelectedItem().isActive()) {
                Notification.informationAlert("Thông Báo", "Bạn đã hợp tác với nhà cung cấp này");
            } else {

                if (providerB.updateActive(tableProvider.getSelectionModel().getSelectedItem().getProviderID(),
                        loginB.getSession().getSessionName(), true)) {
                    Notification.informationAlert("Thông Báo", "Đã hợp tác với nhà cung cấp này");
                    refreshTable();
                } else {
                    Notification.informationAlert("Thông Báo", "Something wrong");
                }
            }
        } else {
            Notification.informationAlert("Thông Báo", "Hãy chọn hàng để thao tác");
        }
    }
}
