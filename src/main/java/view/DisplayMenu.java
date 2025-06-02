package view;

public class DisplayMenu {

    public static void displayFarmersMenu(){
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

    public static void displayCustomerMenu(){
        System.out.println("""
                1. View All Farms
                0. Save changes and LogOut
                """);
    }

    public static void displayLoginMenu(){
        System.out.println("""
                        1. Register
                        2. Login
                        3. Exit
                    """);
    }
}
