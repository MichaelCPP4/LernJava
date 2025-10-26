package com.example;

import com.example.dao.UserDao;
import com.example.entity.User;
import com.example.service.UserMenu;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Консольное CRUD-приложение для управления пользователями.
 * Использует Hibernate для взаимодействия с базой данных.
 */
public class Main {
    public static void main(String[] args) {
        UserMenu userMenu = new UserMenu();
        userMenu.start();
    }
}