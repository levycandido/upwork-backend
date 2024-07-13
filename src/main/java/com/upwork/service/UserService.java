package com.upwork.service;

import com.upwork.ObjectMapperUtils;
import com.upwork.entity.User;
import com.upwork.repository.UserRepository;
import com.upwork.service.dao.UserDTO;
import com.upwork.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO UserDTO) {
        User user = ObjectMapperUtils.map(UserDTO, User.class);
//        if (UserDTO.getStartDate() == null) {
//            throw new IllegalArgumentException("Start date is required");
//        }
        return ObjectMapperUtils.map(userRepository.save(user), UserDTO.class);
    }

    public UserDTO updateUser(Long id, UserDTO UserDTO) {
//        if (UserDTO.getStartDate() == null) {
//            throw new IllegalArgumentException("Start date is required");
//        }
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setPassword(UserDTO.getPassword());
            updatedUser.setStatus(UserDTO.getStatus().getStatus());
            updatedUser.setUsername(UserDTO.getUsername());

            return ObjectMapperUtils.map(userRepository.save(updatedUser), UserDTO.class);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ObjectMapperUtils.map(user, UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users found");
        }
        return ObjectMapperUtils.mapAll(users, UserDTO.class);
    }

    public User findByUsername(String userId) {
        return userRepository.findByUsername(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User save(User user) {
        return user;
    }


}
