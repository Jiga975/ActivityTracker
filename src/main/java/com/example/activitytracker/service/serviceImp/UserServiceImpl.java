package com.example.activitytracker.service.serviceImp;

import com.example.activitytracker.dto.requestDto.UserDto;
import com.example.activitytracker.exception.UserAlreadyExist;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.UserRepository;
import com.example.activitytracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HttpSession session;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, HttpSession session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail( email);
    }


    @Override
    public User RegisterUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail()).orElse(null);
        if (existingUser == null) {
            User newUser = new User();
            newUser.setFirstName(userDto.getFirst_name());
            newUser.setLastName(userDto.getLast_name());
            newUser.setEmail(userDto.getEmail());
            newUser.setPhoneNumber(userDto.getPhone_number());
            newUser.setPassword(userDto.getPassword());
            userRepository.save(newUser);
            return newUser;
        } else {
            throw new UserAlreadyExist("User already exists");
        }
    }
    public Boolean loginUser(String email, String password){
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser == null){
            return  false;
        }else {
            session.setAttribute("userId",existingUser.getEmail());
            return true;
        }
    }


    @Override
    public void updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id).orElse(null);
        if(existingUser != null){
            existingUser.setFirstName(userDto.getFirst_name());
            existingUser.setLastName(userDto.getLast_name());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPhoneNumber(userDto.getPhone_number());
            existingUser.setPassword(userDto.getPassword());
            userRepository.save(existingUser);
        }

    }

    @Override
    public void deleteUser(Long id, String email) {
        userRepository.deleteByIdAndEmail(id,email);

    }
}
