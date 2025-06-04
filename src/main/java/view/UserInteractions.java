package view;

import controllers.FarmController;
import controllers.UserController;
import models.User;

import java.util.Scanner;
import java.util.function.Predicate;

public class UserInteractions {

    private static String getUniqueUsername(Scanner sc, UserController uc) {
        String username;
        while (true) {
            System.out.println("Username: ");
            username = sc.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again...");
                continue;
            }

            if(uc.usernameExists(username)){
                System.out.println("Username already taken, please try again....");
                continue;
            }
            return username;
        }
    }

    public static String getValidatedInput(Scanner sc, String inputType, Predicate<String> validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print("Your " + inputType + ": ");
            input = sc.nextLine().trim();

            if (validator.test(input)) {
                return input;
            }

            System.out.println(errorMessage);
        }
    }

    private static boolean isValidPassword(String password) {
        // At least 8 chars, at least 1 digit, at least 1 uppercase letter
        String regex = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        return password != null && password.matches(regex);
    }

    public static User newUserInputs(Scanner sc, UserController uc){
        System.out.println("Your First Name: ");
        String firstName = sc.nextLine();
        System.out.println("Your Last Name: ");
        String lastName = sc.nextLine();

        String userName = getUniqueUsername(sc, uc);
        String password = getValidatedInput(sc, "password", UserInteractions::isValidPassword,
                """
                    Invalid password format. It must be at least 8 characters long,
                    must contain at least one number and one uppercase letter.
                    """);

        System.out.println("One more thing...are you a farmer or a customer?");
        System.out.println("Please choose your intended purpose (farmer / customer):");
        String role = sc.nextLine().toLowerCase();


        User currentUser = uc.registerNewUser(userName, firstName, lastName, role, password);
        if(role.equalsIgnoreCase("farmer"))
            newFarmInput(sc, uc, currentUser);

        System.out.println("All done! Press any key to continue...");
        sc.nextLine();
        return currentUser;
    }

    public static void newFarmInput(Scanner sc, UserController uc, User u){
        String name = getValidatedInput(sc, "farm name", n -> n != null, "The farm name cannot be null!");
        String address = getValidatedInput(sc, "address", ad -> ad != null, "The address cannot be null!");

        String email = getValidatedInput(sc, "email", UserInteractions::isValidEmail, "Wrong email format!");

        String phone = getValidatedInput(sc, "phone", UserInteractions::isValidPhone, "Wrong phone number format!");

        System.out.println("Current balance: ");
        double budget = Double.parseDouble(sc.nextLine());

        uc.addNewFarmToCurrentUser(u, name, address, email, phone, budget);
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    private static boolean isValidPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phoneNumber.replaceAll("[\\s\\-().+]", "");
        String romanianPhoneRegex = "^(\\+40|0040|0)[2-9]\\d{8}$";
        return cleanPhone.matches(romanianPhoneRegex);
    }


    public static User loginCredentials(Scanner sc, UserController uc) {

        System.out.print("Your username: ");
        String username = sc.nextLine().trim();
        while(!uc.usernameExists(username)){
            System.out.println("Invalid username, please try again...");
            System.out.println("Your username: ");
            username = sc.nextLine().trim();
        }

        System.out.println("Your password: ");
        String password = sc.nextLine().trim();
        while(!uc.passwordMatches(username, password)){
            System.out.println("Wrong password, please try again...");
            System.out.println("Your password: ");
            password = sc.nextLine().trim();
        }

        return uc.login(username);
    }

    public static void updateContactInformation(Scanner sc, FarmController fc){

        String email = getValidatedInput(sc, "email", UserInteractions::isValidEmail, "Wrong email format!");

        String phone = getValidatedInput(sc, "phone", UserInteractions::isValidPhone, "Wrong phone number format!");

        fc.saveEmailPhone(email, phone);

        System.out.println("The contact information has been updated.\n");

    }
}
