package services;

import models.Farm;
import models.User;

import java.util.Arrays;
import java.util.Scanner;

///  manages all users and all farms
public class UsersService {

    //TODO: foloseste UserRepository pt a comunica cu baza de date
    private User[] allUsers;
    int currentUserIndex;
    private FarmService[] allFarms;

    public UsersService(int size) {
        allUsers = new User[size];
        allFarms = new FarmService[size];
        currentUserIndex = 0;

        // create a test user and a farm
        allUsers[currentUserIndex] = new User("john", "doe", "johnnydoe", "a1b2c3");
        allUsers[currentUserIndex].setRole("FARMER");

        allFarms[currentUserIndex] = new FarmService(allUsers[currentUserIndex]);
        allFarms[currentUserIndex].setFarm(new Farm("Test Farm", allUsers[currentUserIndex],
                "Sos. Pantelimon nr. 25", "test@test.com", "0700111222", 10000));

        currentUserIndex++;
    }


    public User addNewUser(Scanner sc){

        System.out.println("Your First Name: ");
        String firstName = sc.nextLine();
        System.out.println("Your Last Name: ");
        String lastName = sc.nextLine();

        System.out.println("Username: ");
        String username = sc.nextLine();
        while(findUserByUsername(username) != null){
            System.out.println("This username is already taken, please try again...");
            System.out.println("Username: ");
            username = sc.nextLine();
        }

        System.out.println("Password: ");
        String password = sc.nextLine();

        User newUser = new User(firstName, lastName, username, password);

        allUsers[currentUserIndex] = newUser;
        System.out.println("You have successfully signed up!");
        System.out.println("------------------------------------\n");

        System.out.println("Are you a FARMER or a CUSTOMER?");
        String role = sc.nextLine();

        allUsers[currentUserIndex].setRole(role);
        if(role.equals("FARMER")){
            allFarms[currentUserIndex] = new FarmService(allUsers[currentUserIndex]);

            System.out.println("Please enter the credentials for your farm:");
            allFarms[currentUserIndex].newFarm(sc);
            Farm currentFarm = allFarms[currentUserIndex].getFarm();
            System.out.println(currentFarm);
        }
        currentUserIndex++;
        return newUser;
    }


    private User findUserByUsername(String inputUserName){
        if (currentUserIndex == 0)
            return null;
        for(var u : allUsers){
            if (u != null && u.getUsername().equalsIgnoreCase(inputUserName)){
                return u;
            }
        }
        return null;
    }


    public User login(Scanner sc){
        System.out.println("Please enter your username: ");
        String username = sc.nextLine();

        User user;
        while ((user = findUserByUsername(username)) == null) {
            System.out.println("Invalid username, please try again...");
            System.out.println("Please enter your username: ");
            username = sc.nextLine();
        }

        System.out.println("Please enter your password: ");
        String inputPassword = sc.nextLine();
        while(!passwordMatches(user, inputPassword)){
            System.out.println("Invalid password, please try again...");
            System.out.println("Please enter your password: ");
            inputPassword = sc.nextLine();
        }

        System.out.println("Login successful!\n-----------------------------");
        return user;
    }


    private boolean passwordMatches(User user, String inputPassword){
        return user.getEncryptedPassword().equals(inputPassword);
    }


    public void getAllUsers(){
        for(var u : allUsers){
            if(u != null){
                System.out.println(u);
            }
        }
    }

    public FarmService getFarmServiceAttachedToUser(User user){
        for (var farmService : allFarms){
            if(farmService.getFarm().getOwner().equals(user)){
                return farmService;
            }
        }
        return null;
    }

    public void getAllFarms(){
        for(var farmService: allFarms){
            if(farmService != null){
                System.out.println(farmService.getFarm());
            }
        }
    }


}
