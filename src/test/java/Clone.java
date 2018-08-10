import businessLogicLayer.Validation;

public class Clone {
    public static void main(String[] args) {
        Validation validation = new Validation();
        String s = "[a-z A-Z]+";
        System.out.println("aaA21".matches(s));
    }
}
