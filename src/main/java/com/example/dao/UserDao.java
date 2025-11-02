package com.example.dao;

import com.example.entity.UserEntity;
import java.util.List;

/**
 * Интерфейс для доступа к данным пользователя.
 * Определяет основные CRUD-операции.
 */
public interface UserDao {
    void save(UserEntity user);
    UserEntity get(Long id);
    List<UserEntity> getAll();
    void update(UserEntity user);
    void delete(UserEntity user);
}
