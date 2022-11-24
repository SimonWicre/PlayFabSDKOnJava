import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabErrors.PlayFabResult;
import com.playfab.PlayFabSettings;

import javax.management.ObjectInstance;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class CloudScriptTest1
{
    private static boolean _running = true;

    public static void main(String[] args) {
        PlayFabSettings.TitleId = "833B6";

        PlayFabClientModels.LoginWithCustomIDRequest request = new PlayFabClientModels.LoginWithCustomIDRequest();
        request.CustomId = "Ge";
        request.CreateAccount = true;



        FutureTask<PlayFabResult<PlayFabClientModels.LoginResult>> loginTask = PlayFabClientAPI.LoginWithCustomIDAsync(request);
        loginTask.run();


        // call GetaccountInfo API
        PlayFabClientModels.GetAccountInfoRequest request2 = new PlayFabClientModels.GetAccountInfoRequest();
        FutureTask<PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> GetaccountTask = PlayFabClientAPI.GetAccountInfoAsync(request2);
        GetaccountTask.run();



        while (_running) {
            if (loginTask.isDone()) { // You would probably want a more sophisticated way of tracking pending async API calls in a real game
                OnLoginComplete(loginTask);
                OnLoginComplete2(GetaccountTask);
                CallCloudScript();
            }

            // Presumably this would be your main game loop, doing other things
            try {
                Thread.sleep(1);
            } catch(Exception e) {
                System.out.println("Critical error in the example main loop: " + e);
            }
        }
    }

    private static void CallCloudScript(){
        PlayFabClientModels.ExecuteCloudScriptRequest request = new PlayFabClientModels.ExecuteCloudScriptRequest();


        //
//        int score = GameDesign.Run();
        // 如果分数更新得太快或者差值过大，就false
        request.FunctionName = "updateGameScore";
        JsonObject d1 = new JsonObject();
        d1.addProperty("currentScore",10);
        request.FunctionParameter = d1;
        request.GeneratePlayStreamEvent = true;
        request.SpecificRevision = 29;

//        request.FunctionName = "makeAPICallGetData";
////        JsonObject d1 = new JsonObject();
////        d1.addProperty("playerMove", "test");
////        d1.addProperty("monstersKilled", 200);
//        request.FunctionParameter = "testGetData";
//        request.GeneratePlayStreamEvent = true;
//        request.SpecificRevision = 10;



//        request.FunctionName = "updatePlayerMove";
////        JsonObject d1 = new JsonObject();
////        d1.addProperty("playerMove", "test");
////        d1.addProperty("monstersKilled", 200);
//        request.FunctionParameter = "test3";
//        request.GeneratePlayStreamEvent = true;
//        request.SpecificRevision = 18;



//        request.FunctionName = "";
//        JsonObject d1 = new JsonObject();
//        d1.addProperty("levelName", "aa");
//        d1.addProperty("monstersKilled", 200);
//        request.FunctionParameter = d1;
//        request.GeneratePlayStreamEvent = true;
//        request.SpecificRevision = 2;



//        request.FunctionName = "completedLevel";
//        JsonObject d1 = new JsonObject();
//        d1.addProperty("levelName", "aa");
//        d1.addProperty("monstersKilled", 200);
//        request.FunctionParameter = d1;
//        request.GeneratePlayStreamEvent = true;
//        request.SpecificRevision = 1;


        // 调用helloWorld方法
//        request.FunctionName = "helloWorld";
//        JsonObject da1 = new JsonObject();
//        da1.addProperty("inputValue", "aaaaa");
//        request.FunctionParameter = da1;
//        request.GeneratePlayStreamEvent = true;
//        request.SpecificRevision = 21;


        FutureTask<PlayFabResult<PlayFabClientModels.ExecuteCloudScriptResult>> task = PlayFabClientAPI.ExecuteCloudScriptAsync(request);
        task.run();

        if(task.isDone()){
            OnExecuteCloudScriptComplete(task);
        }
    }

    private static void OnExecuteCloudScriptComplete(FutureTask<PlayFabResult<PlayFabClientModels.ExecuteCloudScriptResult>> task) {
        PlayFabResult<PlayFabClientModels.ExecuteCloudScriptResult> result = null;
        try {
            result = task.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Congratulations, you made your execute cloud script API call!");
            System.out.println(result.Result.FunctionResult);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your execute cloud script API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }

    private static void OnLoginComplete(FutureTask<PlayFabResult<PlayFabClientModels.LoginResult>> loginTask) {
        PlayFabResult<PlayFabClientModels.LoginResult> result = null;
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
            System.out.println("get account successfully!");
            System.out.println("Your playfab ID is: " + result.Result.AccountInfo.CustomIdInfo.CustomId);
//            System.out.println(result.Result.AccountInfo.Created);
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

