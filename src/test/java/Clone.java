import java.sql.Date;
import java.time.LocalDate;

public class Clone {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        System.out.println(date);
    }
}
