import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner myObj = new Scanner(System.in);
////
////        System.out.println("Hello world! Enter smth below: ");
////        String receivedText = myObj.nextLine();
////        System.out.println(receivedText);
////        //ss();
////        GetDisplayName();
////        System.out.println(GetDisplayName());
////        System.out.println(GetEmail());
////        System.out.println(GetPassword());
        tets();

    }
    static void ss(){
        System.out.println("aa");
    }

    public static String GetEmail() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please enter your email address: ");
        String receivedEmail = new String(myObj.nextLine());
        return receivedEmail;
    }

    public static String GetPassword(){
        System.out.println("Please enter your password: ");
        Scanner myObj = new Scanner(System.in);
        String receivedPassword = new String(myObj.nextLine());
        return receivedPassword;
    }

    private static String GetDisplayName(){
        System.out.println("Please enter your name for display: ");
        Scanner myObj = new Scanner(System.in);
        String receivedDisplayName = new String(myObj.nextLine());
        return receivedDisplayName;
    }

    private static void tets(){

        System.out.println("its score is:" + GameDesign.Run());
    }

}