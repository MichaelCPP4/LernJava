package com.example;

import com.example.service.UserMenu;

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