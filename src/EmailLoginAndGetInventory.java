import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabErrors.PlayFabResult;
import com.playfab.PlayFabSettings;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.FutureTask;

public class EmailLoginAndGetInventory
{
    private static boolean _running = true;

    public static void main(String[] args) {
        PlayFabSettings.TitleId = "833B6";

        PlayFabClientModels.RegisterPlayFabUserRequest request = new PlayFabClientModels.RegisterPlayFabUserRequest();
        request.DisplayName = GetDisplayName();

        request.Email = GetEmail();

        request.Password = GetPassword();
        request.Username = GetUserName();
        request.RequireBothUsernameAndEmail = true;




        FutureTask<PlayFabResult<PlayFabClientModels.RegisterPlayFabUserResult>> registerTask = PlayFabClientAPI.RegisterPlayFabUserAsync(request);
        registerTask.run();


        // call GetaccountInfo API
        PlayFabClientModels.GetAccountInfoRequest request2 = new PlayFabClientModels.GetAccountInfoRequest();
        FutureTask<PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> GetaccountTask = PlayFabClientAPI.GetAccountInfoAsync(request2);
        GetaccountTask.run();

        EventGetInventory();




        while (_running) {
            if (registerTask.isDone()) { // You would probably want a more sophisticated way of tracking pending async API calls in a real game
                OnRegisterComplete(registerTask);
                OnLoginComplete2(GetaccountTask);
            }

            // Presumably this would be your main game loop, doing other things
            try {
                Thread.sleep(1);
            } catch(Exception e) {
                System.out.println("Critical error in the example main loop: " + e);
            }
        }
    }

//    public static FutureTask<PlayFabErrors.PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> GetAccountInfoAsync(final PlayFabClientModels.GetAccountInfoRequest request) {
//        return new FutureTask(new Callable<PlayFabErrors.PlayFabResult<PlayFabClientModels.GetAccountInfoResult>>() {
//            public PlayFabErrors.PlayFabResult<PlayFabClientModels.GetAccountInfoResult> call() throws Exception {
//                return PlayFabClientAPI.privateGetAccountInfoAsync(request);
//            }
//        });
//    }
    public static void EventGetInventory(){
        PlayFabClientModels.GetUserInventoryRequest request = new PlayFabClientModels.GetUserInventoryRequest();
        FutureTask<PlayFabResult<PlayFabClientModels.GetUserInventoryResult>> getInventoryTask = PlayFabClientAPI.GetUserInventoryAsync(request);
        getInventoryTask.run();
        if (getInventoryTask.isDone()){
            OnGetInventoryComplete(getInventoryTask);
        }

}


    public static String GetEmail() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please enter your email address: ");
        return myObj.nextLine();
    }

    public static String GetPassword(){
        System.out.println("Please enter your password: ");
        Scanner myObj = new Scanner(System.in);
        return myObj.nextLine();
    }

    private static String GetDisplayName(){
        System.out.println("Please enter your name for display: ");
        Scanner myObj = new Scanner(System.in);
        return myObj.nextLine();
    }

    private static String GetUserName(){
        System.out.println("Please enter your username: ");
        Scanner myObj = new Scanner(System.in);
        return myObj.nextLine();
    }

    private static void OnRegisterComplete(FutureTask<PlayFabResult<PlayFabClientModels.RegisterPlayFabUserResult>> registerTask) {
        PlayFabResult<PlayFabClientModels.RegisterPlayFabUserResult> result = null;
        try {
            result = registerTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Congratulations, you made your first successful API call!");
            System.out.println(result.Result.PlayFabId);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your first API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }

    private static void OnLoginComplete2(FutureTask<PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> getTask) {
        PlayFabResult<PlayFabClientModels.GetAccountInfoResult> result = null;
        try {
            result = getTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Second successful API call!");
            //System.out.println(result.Result.AccountInfo.CustomIdInfo.CustomId);
            System.out.println(result.Result);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your first API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        _running = false; // Because this is just an example, successful login triggers the end of the program
    }


    private static void OnGetInventoryComplete(FutureTask<PlayFabResult<PlayFabClientModels.GetUserInventoryResult>> testTask) {
        PlayFabResult<PlayFabClientModels.GetUserInventoryResult> result = null;
        try {
            result = testTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("you made your get inventory API call!");
            System.out.println(result.Result.Inventory);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your first API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }






    // This is a utility function we haven't put into the core SDK yet.  Feel free to use it.
    private static <RT> String CompileErrorsFromResult(PlayFabResult<RT> result) {
        if (result == null || result.Error == null)
            return null;

        String errorMessage = "";
        if (result.Error.errorMessage != null)
            errorMessage += result.Error.errorMessage;
        if (result.Error.errorDetails != null)
            for (Map.Entry<String, List<String>> pair : result.Error.errorDetails.entrySet() )
                for (String msg : pair.getValue())
                    errorMessage += "\n" + pair.getKey() + ": " + msg;
        return errorMessage;
    }


}