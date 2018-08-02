package presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class Notification {

    public static void showErors(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        String stackTrace = errors.toString();
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(label, textArea);

        // Sét đặt nội dung cho Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    public static void informationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void ErrorsAlert(String header, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText(header);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public static String ConfrimAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setResizable(false);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            return null;
        } else if (option.get() == ButtonType.OK) {
            return "Y";
        } else
            return "N";
    }

}
