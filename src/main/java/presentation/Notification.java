package presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Notification {

    public static void showErors(Exception e , String title){
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

    public static void messages(String title ,String messeage){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(messeage);
        alert.showAndWait();
    }

}
