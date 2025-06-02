package services;

import exceptions.NameAlreadyExistsException;
import models.Farm;
import models.User;
import persistence.FarmRepository;
import persistence.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

///  manages all users and all farms
public class UserService {

    private final UserRepository userRepository = UserRepository.getInstance();
    private FarmRepository farmRepository = FarmRepository.getInstance();

    private List<User> userList = new ArrayList<>();
    private List<Farm> farmList = new ArrayList<>();

    public UserService() {
        // incarc toti userii si toate fermele in colectii, pentru acces mai rapid
        userList = userRepository.findAll();
        farmList = farmRepository.findAll();
    }


    public User addNewUser(User newUuser) {
        return userRepository.save(newUuser);
    }

    public User findUserByUsername(String inputUserName) {
        Optional<User> user = userRepository.findByName(inputUserName);
        return user.orElse(null);
    }

    public void retrieveAllUsers(){
        userList = userRepository.findAll();
    }


//    public FarmService getFarmServiceAttachedToUser(User user){
//        for (var farmService : allFarms){
//            if(farmService.getFarm().getOwner().equals(user)){
//                return farmService;
//            }
//        }
//        return null;
//    }

//    public void getAllFarms(){
//        for(var farmService: allFarms){
//            if(farmService != null){
//                System.out.println(farmService.getFarm());
//            }
//        }
//    }


}
