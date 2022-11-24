import com.playfab.*;
import com.playfab.PlayFabErrors.PlayFabResult;

import javax.xml.catalog.Catalog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CustomIDLoginAndGetInventory
{
    private static boolean _running = true;

    public static void main(String[] args) {
        PlayFabSettings.TitleId = "833B6";
        PlayFabSettings.DeveloperSecretKey = "EQEWAHESUY1JKN9XF4WCNJR8UAMOICNIOPQEI8WIC8HEMK3I1W";

        PlayFabClientModels.LoginWithCustomIDRequest request = new PlayFabClientModels.LoginWithCustomIDRequest();
        request.CustomId = "testUser1";
        request.CreateAccount = true;



        FutureTask<PlayFabResult<com.playfab.PlayFabClientModels.LoginResult>> loginTask = PlayFabClientAPI.LoginWithCustomIDAsync(request);
        loginTask.run();

        // call GetaccountInfo API
         PlayFabClientModels.GetAccountInfoRequest request2 = new PlayFabClientModels.GetAccountInfoRequest();
        FutureTask<PlayFabResult<PlayFabClientModels.GetAccountInfoResult>> GetaccountTask = PlayFabClientAPI.GetAccountInfoAsync(request2);
        GetaccountTask.run();





        while (_running) {
            if (loginTask.isDone()) { // You would probably want a more sophisticated way of tracking pending async API calls in a real game
                OnLoginComplete(loginTask);
                OnLoginComplete2(GetaccountTask);
//                EventSetCatalogItems("4", "fourth item");
//                EventAddVirtualCurrency();
//                EventGrantUserItems("test-container3locked");

                EventGetInventory();

            }

            // Presumably this would be your main game loop, doing other things
            try {
//                Thread.sleep(1);
                EventSubmitScore(GameDesign.Run());
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


    private static void EventSubmitScore(int playerScore) {
        PlayFabClientModels.UpdatePlayerStatisticsRequest request = new PlayFabClientModels.UpdatePlayerStatisticsRequest();

        //先创建StatisticUpdate的类
        PlayFabClientModels.StatisticUpdate updatedstats = new PlayFabClientModels.StatisticUpdate();
        updatedstats.StatisticName = "stat1";
        updatedstats.Value = playerScore;

        // 再将先创建StatisticUpdate的类添加到request需要的ArrayList里面去
        ArrayList<PlayFabClientModels.StatisticUpdate> statlist = new ArrayList<>();
        statlist.add(updatedstats);
        request.Statistics = statlist;

        FutureTask<PlayFabResult<PlayFabClientModels.UpdatePlayerStatisticsResult>> updateTask = PlayFabClientAPI.UpdatePlayerStatisticsAsync(request);
        updateTask.run();
        if(updateTask.isDone()){
            OnUpstatisticsComplete(updateTask);
        }
    }

    private static void OnUpstatisticsComplete(FutureTask<PlayFabResult<PlayFabClientModels.UpdatePlayerStatisticsResult>> Task) {
        PlayFabResult<PlayFabClientModels.UpdatePlayerStatisticsResult> result = null;
        try {
            result = Task.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Update statistics successfully !");
            System.out.println(result.Result);

        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your update statis call.");
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

    public static void EventGrantUserItems(String itemID){
        PlayFabServerModels.GrantItemsToUserRequest request = new PlayFabServerModels.GrantItemsToUserRequest();
        PlayFabAdminModels.CatalogItem containerGift = new PlayFabAdminModels.CatalogItem();
        containerGift.Container.KeyItemId = "testContainer";
        request.ItemIds = containerGift.Container.ItemContents;


        ArrayList<String> itemIds = new ArrayList<>();
        itemIds.add(itemID);
//        itemIds.add("item22");
//        itemIds.add("item22");
        request.ItemIds = itemIds;
        request.PlayFabId = "214E34C873C9F8C3";
//        request.CatalogVersion = "test1";


        FutureTask<PlayFabResult<PlayFabServerModels.GrantItemsToUserResult>> grantUserItemTask = PlayFabServerAPI.GrantItemsToUserAsync(request);
        grantUserItemTask.run();
        if (grantUserItemTask.isDone()){
            OnGrantUserItems(grantUserItemTask);
        }
    }

    public static void EventSetCatalogItems(String itemID, String displayName){
        PlayFabAdminModels.UpdateCatalogItemsRequest request = new PlayFabAdminModels.UpdateCatalogItemsRequest();
        PlayFabAdminModels.CatalogItem item1 = new PlayFabAdminModels.CatalogItem();
        item1.ItemId = itemID;
        item1.DisplayName = displayName;

        ArrayList<PlayFabAdminModels.CatalogItem> currentCatalog = new ArrayList<>();
        currentCatalog.add(item1);

        request.Catalog = currentCatalog;

        FutureTask<PlayFabResult<PlayFabAdminModels.UpdateCatalogItemsResult>> setItemTask = PlayFabAdminAPI.UpdateCatalogItemsAsync(request);
        setItemTask.run();
        if (setItemTask.isDone()){
            OnUpdateUserItem(setItemTask);
        }
    }


    public static void EventAddVirtualCurrency(){
        PlayFabClientModels.AddUserVirtualCurrencyRequest request = new PlayFabClientModels.AddUserVirtualCurrencyRequest();
        request.VirtualCurrency = "AA";
        request.Amount= 10;


        FutureTask<PlayFabResult<PlayFabClientModels.ModifyUserVirtualCurrencyResult>> addCurrencyTask = PlayFabClientAPI.AddUserVirtualCurrencyAsync(request);
        addCurrencyTask.run();
        if(addCurrencyTask.isDone()){
            OnModifyCurrency(addCurrencyTask);
        }
    }

    private static void OnLoginComplete(FutureTask<PlayFabResult<com.playfab.PlayFabClientModels.LoginResult>> loginTask) {
        PlayFabResult<com.playfab.PlayFabClientModels.LoginResult> result = null;
        try {
            result = loginTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("Congratulations, Login successfully !");
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
            System.out.println("Get account info API call!");
            //System.out.println(result.Result.AccountInfo.CustomIdInfo.CustomId);
            System.out.println(result.Result.AccountInfo.CustomIdInfo);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your get account API call.");
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
            System.out.println("Your inventory is: " + result.Result.Inventory);
            System.out.println("Your virtual currency is: " + result.Result.VirtualCurrency);

            for(int i=0; i<result.Result.Inventory.size(); i++ ){
                System.out.println("Your item ID is: "+ result.Result.Inventory.get(i).ItemId);
                System.out.println("Your item name is: "+ result.Result.Inventory.get(i).DisplayName);
            }




        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your get inventory API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }

    private static void OnGrantUserItems(FutureTask<PlayFabResult<PlayFabServerModels.GrantItemsToUserResult>> testTask) {
        PlayFabResult<PlayFabServerModels.GrantItemsToUserResult> result = null;
        try {
            result = testTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("you made your grant item API call!");
            System.out.println("Your granted item ID is: " + result.Result.ItemGrantResults.get(0).ItemId);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your grant item to user API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }

    private static void OnUpdateUserItem(FutureTask<PlayFabResult<PlayFabAdminModels.UpdateCatalogItemsResult>> testTask) {
        PlayFabResult<PlayFabAdminModels.UpdateCatalogItemsResult> result = null;
        try {
            result = testTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("you made your update item API call!");
            System.out.println(result.Result);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your first API call.");
            System.out.println("Here's some debug information:");
            System.out.println(CompileErrorsFromResult(result));
        }

        //_running = false; // Because this is just an example, successful login triggers the end of the program
    }


    private static void OnModifyCurrency(FutureTask<PlayFabResult<PlayFabClientModels.ModifyUserVirtualCurrencyResult>> testTask) {
        PlayFabResult<PlayFabClientModels.ModifyUserVirtualCurrencyResult> result = null;
        try {
            result = testTask.get(); // Wait for the result from the async call
        } catch(Exception e) {
            System.out.println("Exception in PlayFab api call: " + e); // Did you assign your PlayFabSettings.TitleId correctly?
        }

        if (result != null && result.Result != null) {
            System.out.println("you made your modify currency API call!");
            System.out.println("The current balance is: " + result.Result.Balance);
            System.out.println("Modified amount is: " + result.Result.BalanceChange);
        } else if (result != null && result.Error != null) {
            System.out.println("Something went wrong with your modify user virtual currency API call.");
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