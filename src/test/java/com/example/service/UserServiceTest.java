package com.example.service;

import com.example.dao.UserDaoImpl;
import com.example.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDaoImpl userDaoImpl;

    @InjectMocks
    private UserService userService;

    /**
     * Тестируем метод получения всех пользователей.
     * Проверяем, что сервис возвращает список пользователей, который отдаёт DAO.
     * Используем Mockito для контроля вызова метода DAO.
     */
    @Test
    void testGetAllUsers() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        when(userDaoImpl.getAll()).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("Misha", result.get(0).getName());
        verify(userDaoImpl, times(1)).getAll();
    }

    /**
     * Тестируем метод сохранения пользователя.
     * Проверяем, что сервис вызывает DAO.save() с переданным объектом.
     */
    @Test
    void testSaveUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Alex");

        userService.saveUser(userEntity);

        verify(userDaoImpl, times(1)).save(userEntity);
    }

    /**
     * Тестируем метод получения пользователя по ID.
     * Проверяем, что сервис возвращает именно того пользователя, которого отдаёт DAO.
     * Также убеждаемся, что метод DAO.get() вызван один раз с нужным ID.
     */
    @Test
    void testGetUserById() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Lena");
        when(userDaoImpl.get(1L)).thenReturn(userEntity);

        UserEntity result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Lena", result.getName());
        verify(userDaoImpl).get(1L);
    }

    /**
     * Тестируем метод обновления пользователя.
     * Проверяем, что сервис вызывает DAO.update() с переданным пользователем.
     * Сам объект мы не изменяем — важно только, что вызов метода произошёл.
     */
    @Test
    void testUpdateUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Old");
        userService.updateUser(userEntity);

        verify(userDaoImpl, times(1)).update(userEntity);
    }

    /**
     * Тестируем метод удаления пользователя.
     * Проверяем, что сервис вызывает DAO.delete() именно с тем объектом, который был передан.
     * База данных не участвует, т.к. это чистый юнит-тест.
     */
    @Test
    void testDeleteUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("ToDelete");
        userService.deleteUser(userEntity);

        verify(userDaoImpl, times(1)).delete(userEntity);
    }
}