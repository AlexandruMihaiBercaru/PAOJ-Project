import models.User;
import services.FarmService;
import services.SeedService;
import services.UsersService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        UsersService usersService = new UsersService(100);
        SeedService seedService = new SeedService();
        User currentUser = null;

        Scanner sc = new Scanner(System.in);
        boolean enterApp = false;
        int choice;

        while(!enterApp){
            if(!enterApp) {
                System.out.println("""
                        1. Register
                        2. Login
                        3. Exit
                    """);
                System.out.println("Your choice: ");
                choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1:
                        enterApp = true;
                        currentUser = usersService.addNewUser(sc);
                        break;
                    case 2:
                        enterApp = true;
                        currentUser = usersService.login(sc);
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
            if (currentUser != null && currentUser.getRole().equals("FARMER")){
                printFarmerMenu();
                FarmService currentFarmService = usersService.getFarmServiceAttachedToUser(currentUser);
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
                    printFarmerMenu();
                }
            } else if (currentUser != null && currentUser.getRole().equals("CUSTOMER")) {
                printCustomerMenu();
                while(true){
                    System.out.println("Your choice: ");
                    choice = Integer.parseInt(sc.nextLine());
                    switch(choice){
                        case 0:
                            System.out.println("Changes were saved, back to the main menu....\n");
                            enterApp = false;
                            break;
                        case 1:
                            usersService.getAllFarms();
                            break;
                        default:
                            System.out.println("Wrong choice. Try again.");
                            System.out.println("Enter your choice: ");
                            break;
                    }
                    if(!enterApp)
                        break;
                    printCustomerMenu();
                }
            }
        }





    }

    private static void printFarmerMenu(){
        System.out.println("""
                1. Update Farm Data
                2. Check Funds
                3. Inspect Seed Market
                4. Buy Seeds
                5. List Seed Inventory 
                5. Add New Farmland Parcel
                6. View All FarmLand Parcels
                0. Save changes and LogOut
                """);
    }

    private static void printCustomerMenu(){
        System.out.println("""
                1. View All Farms
                0. Save changes and LogOut
                """);
    }
}
