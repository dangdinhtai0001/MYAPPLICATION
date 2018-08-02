package presentation;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class MyToolTip extends Tooltip {

    public MyToolTip(String text) {
        this.setText(text);
        this.setShowDelay(new Duration(10));
        this.setStyle("-fx-font-size:12px;");
    }

}
