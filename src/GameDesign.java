import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameDesign {

    static int score = 0;

    public static void main(String[] args){
        Run();
//        System.out.println(score);

    }

    public static int Run(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("Welcome to Number Guessing Game:");
        List<Integer> randomGroup = new ArrayList<>();
        while(true){
            if(randomGroup.size() > 9 ){
                System.out.println("\n\nCongrats! You have completed this game~~~");
                System.out.printf("Your final score is: %d%n", score);
                break;
            }
            System.out.println("""               
                    ---------------------------------


                    Please enter a number among 1,2 and 3,
                    other number input will be seen as quitting game:""");
            String inputString = myObj.nextLine();
            if (!Objects.equals(inputString.trim(), "") && isNumeric(inputString)){
                try {
                    int inputValue = Integer.parseInt((inputString));
                    if (inputValue != 1 && inputValue != 2 && inputValue != 3) {
                        System.out.println("You are quitting this game~~~");
                        System.out.printf("Your final score is: %d%n", score);
                        break;
                    }
                    int randomNumber = Generate();
                    randomGroup.add(randomNumber);

                    int occu1 = Collections.frequency(randomGroup, 1);
                    int occu2 = Collections.frequency(randomGroup, 2);
                    int occu3 = Collections.frequency(randomGroup, 3);
                    System.out.printf("The occurrence frequency of 1,2,3 is: %d, %d, %d%n",occu1,occu2,occu3 );
                    System.out.println("The random number generated from system is: " + randomNumber);
                    Judge(randomNumber, inputValue);
                    System.out.println("Your current score is: " + score);
                }
                catch (Exception e){
                    System.out.println("Wrong");
                }
            }
            else{
                System.out.println("Input invalid...");
            }

        }
        return score;
    }

    public static void Judge(int a, int b){

        if (a == b) {
            System.out.println("\nNice, Correct guess!!! ");
            score += 1;
        }
        else {
            System.out.println("\nOps, Incorrect guess--- ");
            score -= 1;
        }

    }

    private static Integer Generate(){
        return ThreadLocalRandom.current().nextInt(1, 3 + 1);
    }

    private static boolean isNumeric(String str){
        for (int i=0; i < str.length(); i++){
//            System.out.println(str.charAt(i));
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public int OutputScore(int num){
        return num;
    }


}
