package com.example.service;

import com.example.dao.UserDaoImpl;
import com.example.entity.UserEntity;
import java.util.List;

public class UserService {
    private final UserDaoImpl userDaoImpl;

    public UserService(UserDaoImpl userDaoImpl) {
        this.userDaoImpl = userDaoImpl;
    }

    public void saveUser(UserEntity userEntity) {
        userDaoImpl.save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userDaoImpl.getAll();
    }

    public UserEntity getUserById(long id) {
        return userDaoImpl.get(id);
    }

    public void updateUser(UserEntity userEntity) {
        userDaoImpl.update(userEntity);
    }

    public void deleteUser(UserEntity userEntity) {
        userDaoImpl.delete(userEntity);
    }
}