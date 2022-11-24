import com.playfab.PlayFabClientAPI;
import com.playfab.PlayFabClientModels;
import com.playfab.PlayFabErrors.PlayFabResult;
import com.playfab.PlayFabSettings;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class UnlockContainer
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
                EventGetInventory();
//                EventUnlockContainerInstance();
                EventUnlockContainer();
                EventGetInventory();
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

    public static void EventUnlockContainer(){
        PlayFabClientModels.UnlockContainerItemRequest request = new PlayFabClientModels.UnlockContainerItemRequest();
        request.ContainerItemId = "newcontainer1";
        request.CatalogVersion = "test1";


        FutureTask<PlayFabResult<PlayFabClientModels.UnlockContainerItemResult>> unlockContainerTask = PlayFabClientAPI.UnlockContainerItemAsync(request);
        unlockContainerTask.run();
        if(unlockContainerTask.isDone()){
            OnUnlockContainerComplete(unlockContainerTask);
        }
    }

    private static void OnUnlockContainerComplete(FutureTask<PlayFabResult<PlayFabClientModels.UnlockContainerItemResult>> task) {
        PlayFabResult<PlayFabClientModels.UnlockContainerItemResult> result = null;
        try {
            result = task.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("unlock container successfully!");
            System.out.println(result.Result.UnlockedItemInstanceId);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your unlock container item call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }


    public static void EventUnlockContainerInstance(){
        PlayFabClientModels.UnlockContainerInstanceRequest request = new PlayFabClientModels.UnlockContainerInstanceRequest();
        request.ContainerItemInstanceId = "FBE4AFC5DA168840";
//        request.KeyItemInstanceId = "welcome";


        FutureTask<PlayFabResult<PlayFabClientModels.UnlockContainerItemResult>> unlockContainerInstanceTask = PlayFabClientAPI.UnlockContainerInstanceAsync(request);
        unlockContainerInstanceTask.run();
        if(unlockContainerInstanceTask.isDone()){
            OnUnlockContainerInstanceComplete(unlockContainerInstanceTask);
        }
    }

    private static void OnUnlockContainerInstanceComplete(FutureTask<PlayFabResult<PlayFabClientModels.UnlockContainerItemResult>> task) {
        PlayFabResult<PlayFabClientModels.UnlockContainerItemResult> result = null;
        try {
            result = task.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("unlock container successfully!");
            System.out.println(result.Result.UnlockedItemInstanceId);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your unlock container instance call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }


    public static void EventGetInventory(){
        PlayFabClientModels.GetUserInventoryRequest request = new PlayFabClientModels.GetUserInventoryRequest();
        FutureTask<PlayFabResult<PlayFabClientModels.GetUserInventoryResult>> getInventoryTask = PlayFabClientAPI.GetUserInventoryAsync(request);
        getInventoryTask.run();
        if (getInventoryTask.isDone()){
            OnGetInventoryComplete(getInventoryTask);
        }
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
            System.out.println("Your inventory is: " + result.Result.Inventory);
            System.out.println("Your virtual currency is: " + result.Result.VirtualCurrency);

            for(int i=0; i<result.Result.Inventory.size(); i++ ){
                System.out.println("Your item ID is: "+ result.Result.Inventory.get(i).ItemId);
                System.out.println("Your item name is: "+ result.Result.Inventory.get(i).DisplayName);
            }
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your first API call.");
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
            System.out.println("Congratulations, You logged in successfully!");
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
            System.out.println("get account info successful API call!");
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