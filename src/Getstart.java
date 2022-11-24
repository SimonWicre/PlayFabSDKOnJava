import java.util.concurrent.*;
import java.util.*;

import com.playfab.PlayFabErrors;
import com.playfab.PlayFabErrors.*;
import com.playfab.PlayFabSettings;
import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabClientAPI;

public class Getstart
{
    private static boolean _running = true;

    public static void main(String[] args) {
        PlayFabSettings.TitleId = "833B6";

        PlayFabClientModels.LoginWithCustomIDRequest request = new PlayFabClientModels.LoginWithCustomIDRequest();
        request.CustomId = "Ge";
        request.CreateAccount = true;



        FutureTask<PlayFabResult<com.playfab.PlayFabClientModels.LoginResult>> loginTask = PlayFabClientAPI.LoginWithCustomIDAsync(request);
        loginTask.run();


        // call GetaccountInfo API
        PlayFabClientModels.GetAccountInfoRequest request2 = new PlayFabClientModels.GetAccountInfoRequest();
        FutureTask<PlayFabErrors.PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> GetaccountTask = PlayFabClientAPI.GetAccountInfoAsync(request2);
        GetaccountTask.run();



        while (_running) {
            if (loginTask.isDone()) { // You would probably want a more sophisticated way of tracking pending async API calls in a real game
                OnLoginComplete(loginTask);
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

    private static void OnLoginComplete(FutureTask<PlayFabResult<com.playfab.PlayFabClientModels.LoginResult>> loginTask) {
        PlayFabResult<com.playfab.PlayFabClientModels.LoginResult> result = null;
        try {
            result = loginTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Congratulations, login successfully!");
            System.out.println(result.Result.PlayFabId);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your login API call.");
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
            System.out.println("Get account info successful API call!");
            System.out.println(result.Result.AccountInfo.CustomIdInfo.CustomId);
            System.out.println(result.Result.AccountInfo.Created);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your get account info API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        _running = false; // Because this is just an example, successful login triggers the end of the program
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