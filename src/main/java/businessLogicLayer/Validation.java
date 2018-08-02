package businessLogicLayer;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import presentation.MyToolTip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validation {

    private static final String DELIMITERS = "[\\.\\(\\)\\*\\%\\!\\@\\#\\$\\^\\&\\*\\-\\_\\+\\=]+";

    public void showErrors(HBox hBox, ImageView validate, String toolTip) {
        validate.setVisible(true);
        validate.setImage(new Image("/Icon/error-icon-28.png"));
        hBox.setStyle("-fx-border-color: red;");
        Tooltip.install(hBox, new MyToolTip(toolTip));
    }

    public void showWarning(HBox hBox, ImageView validate, String toolTip) {
        validate.setVisible(true);
        validate.setImage(new Image("/Icon/warning.png"));
        hBox.setStyle("-fx-border-color: yellow;");
        Tooltip.install(hBox, new MyToolTip(toolTip));
    }

    public void showValid(HBox hBox, ImageView validate) {
        validate.setVisible(false);
        hBox.setStyle("-fx-border-color: transparent");
    }

    public boolean checkIsNumber(String text) {
        try {
            Double aDouble = Double.parseDouble(text);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public boolean checkFormatDate(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:ms");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(text.trim());
            return true;
        } catch (ParseException | NullPointerException e) {
            return false;
        }
    }

    public boolean checkEmpty(String text) {
        try {
            return text.trim().length() == 0 || text == null;
        } catch (NullPointerException e) {
            return true;
        }
    }

    public boolean checkContainNumber(String text) {
        try {
            return Pattern.compile("[0-9]").matcher(text).find();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkContainWord(String text) {
        try {
            return Pattern.compile("[a-z]").matcher(text).find();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkContainRegex(String text) {
        try {
            return Pattern.compile(DELIMITERS).matcher(text).find();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkMaxLength(String text, int length) {
        try {
            return text.length() > length;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkMinLength(String text, int length) {
        try {
            return text.length() < length;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean checkContainSpace(String text) {
        try {
            return !text.matches("\\S+");
        } catch (NullPointerException e) {
            return true;
        }
    }

    public boolean checkTheDayBefore(LocalDate before, LocalDate after) {
        return before.isBefore(after);
    }

    public boolean checkStartByNumber(String text) {
        try {
            return Character.isDigit(text.charAt(0));
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }
}
