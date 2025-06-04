package controllers;

import models.Farm;
import models.User;
import services.FarmService;
import services.UserService;
import utils.EncryptionDecryptionAES;

public class UserController {

    private final UserService userService;
    private final FarmService farmService ;
    private final EncryptionDecryptionAES enc = EncryptionDecryptionAES.getInstance();

    public UserController() {
        userService = new UserService();
        farmService = new FarmService();
    }

    public boolean usernameExists(String inputUserName){
        return userService.findUserByUsername(inputUserName) != null;
    }

    public boolean passwordMatches(String inputUsername, String plainPassword) {
        User user = userService.findUserByUsername(inputUsername);
        try {
            if (user != null && enc != null)  {
                return enc.decrypt(user.getEncryptedPassword()).equals(plainPassword);
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        return false;
    }

    public User registerNewUser(String inputUserName,
                                String inputFirstName,
                                String inputLastName,
                                String inputRole,
                                String plainPassword){
        try{
            String encryptedPassword = enc.encrypt(plainPassword);
            User user = new User(inputFirstName, inputLastName, inputUserName, inputRole.toLowerCase(), encryptedPassword);
            user.setUserId();
            return userService.addNewUser(user);
        }catch (Exception e) {
            System.out.println("Unexpected error occurred while encrypting password: " + e.getMessage());
        }
        return null;
    }

    public User login(String inputUserName){
        User user = userService.findUserByUsername(inputUserName);
        if(user != null){
            System.out.println("Login successful!");
            Farm f = farmService.findFarmByUserId(user.getUserId());
            if(f!=null)
                System.out.println(f);
        }
        return user;
    }

    public void addNewFarmToCurrentUser(User u, String name, String address, String email, String phone, double budget){
        Farm f = new Farm(name, address, email, phone, budget);
        f.setOwner(u);
        System.out.println(farmService.addNewFarm(f));

    }


}
