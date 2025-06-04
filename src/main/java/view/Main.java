package view;

import controllers.FarmController;
import controllers.SeedsLotController;
import controllers.UserController;
import models.User;
import utils.AuditService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        UserController uc = new UserController();
        FarmController fc = new FarmController();
        SeedsLotController slc = new SeedsLotController();

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
                        AuditService.logSystemAction("register_new_user");
                        currentUser = UserInteractions.newUserInputs(sc, uc);

                        break;
                    case 2:
                        enterApp = true;
                        AuditService.logSystemAction("login_user");
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
                AuditService.logSystemAction("attached_farm_to_user_scope");
                fc.attachUserToFarm(currentUser);
                while(true){
                    System.out.println("Your choice: ");
                    choice = Integer.parseInt(sc.nextLine());

                    try{
                        switch(choice){
                            case 0:
                                System.out.println("Changes were saved, back to the main menu....\n");
                                enterApp = false;
                                break;
                            case 1:
                                AuditService.logSystemAction("update_farm_contact_info");
                                UserInteractions.updateContactInformation(sc, fc);
                                break;
                            case 2:
                                AuditService.logSystemAction("check_balance");
                                fc.checkFunds();
                                break;
                            case 3:
                                AuditService.logSystemAction("inspect_seed_market");
                                slc.printSeedLotsListedForSale();
                                break;
                            case 4:
                                AuditService.logSystemAction("buy_seeds");
                                fc.buySeeds(sc);
                                fc.checkFunds();
                                break;
                            case 5:
                                AuditService.logSystemAction("list_seed_inventory");
                                fc.listSeedInventory();
                                break;
                            case 6:
                                AuditService.logSystemAction("add_land_lot");
                                fc.addLandLot(sc);
                                break;
                            case 7:
                                AuditService.logSystemAction("view_all_land_lots");
                                fc.viewFarmLand();
                                break;
                            case 8:
                                AuditService.logSystemAction("plant_seeds");
                                fc.plantSeeds(sc);
                                break;
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
                            AuditService.logSystemAction("list_farm_contact_info");
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
