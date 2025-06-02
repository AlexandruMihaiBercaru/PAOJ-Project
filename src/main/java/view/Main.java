package view;

import controllers.FarmController;
import controllers.UserController;
import models.User;
import services.FarmService;
import services.SeedService;
import services.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        UserController uc = new UserController();
        FarmController fc = new FarmController();

        User currentUser = null;

        Scanner sc = new Scanner(System.in);
        boolean enterApp = false;
        int choice;

        while(!enterApp){

            if(!enterApp) {
                DisplayMenu.displayLoginMenu();
                System.out.println("Your choice: ");
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        enterApp = true;
                        currentUser = UserInteractions.newUserInputs(sc, uc);
                        break;
                    case 2:
                        enterApp = true;
                        currentUser = UserInteractions.loginCredentials(sc, uc);
                        break;
                    case 3:
                        sc.close();
                        return;
                    default:
                        System.out.println("Wrong choice. Try again.");
                        System.out.println("Enter your choice: ");
                        break;
                }
            }
            if (currentUser != null && currentUser.getRole().equalsIgnoreCase("farmer")){
                DisplayMenu.displayFarmersMenu();
                //FarmService currentFarmService = userService.getFarmServiceAttachedToUser(currentUser);
                while(true){
                    System.out.println("Your choice: ");
                    choice = Integer.parseInt(sc.nextLine());

                    try{
                        switch(choice){
                            case 0:
                                System.out.println("Changes were saved, back to the main menu....\n");
                                enterApp = false;
                                break;
                            /*case 1:
                                currentFarmService.updateContactInfo(sc);
                                break;
                            case 2:
                                currentFarmService.checkFunds();
                                break;
                            case 3:
                                seedService.inspectSeedMarket();
                                break;
                            case 4:
                                seedService.buySeeds(sc, currentFarmService);
                                currentFarmService.checkFunds();
                                break;
                            case 5:
                                currentFarmService.showInventory();
                                break;
                            case 6:
                                currentFarmService.createLandParcel(sc);
                                break;
                            case 7:
                                currentFarmService.viewFarmLand();
                                break;*/
                            default:
                                System.out.println("Wrong choice. Try again.");
                                System.out.println("Enter your choice: ");
                                break;
                        }
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(!enterApp)
                        break;
                    DisplayMenu.displayFarmersMenu();
                }
            } else if (currentUser != null && currentUser.getRole().equalsIgnoreCase("customer")) {
                DisplayMenu.displayCustomerMenu();
                while(true){
                    System.out.println("Your choice: ");
                    choice = Integer.parseInt(sc.nextLine());
                    switch(choice){
                        case 0:
                            System.out.println("Changes were saved, back to the main menu....\n");
                            enterApp = false;
                            break;
                        case 1:
                            fc.listAllFarmsContactInformation();
                            break;
                        default:
                            System.out.println("Wrong choice. Try again.");
                            System.out.println("Enter your choice: ");
                            break;
                    }
                    if(!enterApp)
                        break;
                    DisplayMenu.displayCustomerMenu();
                }
            }
        }
    }
}
