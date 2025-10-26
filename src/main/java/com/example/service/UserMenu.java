package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, отвечающий за консольное меню и взаимодействие с пользователем.
 */
public class UserMenu {

    private final UserDao userDao;
    private final Scanner scanner;

    public UserMenu() {
        this.userDao = new UserDao();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запуск консольного меню.
     */
    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Выберите действие: ");

            switch (choice) {
                case 1:
                    createUser();
                    break;

                case 2:
                    showAllUsers();
                    break;

                case 3:
                    updateUser();
                    break;

                case 4:
                    deleteUser();
                    break;

                case 5:
                    System.out.println("Выход...");
                    return;

                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
                    break;
            }

            System.out.println(); // пустая строка для разделения итераций
        }
    }

    private void printMenu() {
        System.out.println("1. Создать пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Выход");
    }

    private void createUser() {
        User user = new User();

        System.out.print("Имя: ");
        user.setName(scanner.nextLine());

        System.out.print("Email: ");
        user.setEmail(scanner.nextLine());

        user.setAge(readInt("Возраст: "));

        userDao.save(user);
        System.out.println("Пользователь успешно создан!");
    }

    private void showAllUsers() {
        List<User> users = userDao.getAll();

        if (users.isEmpty()) {
            System.out.println("Пользователи отсутствуют.");
            return;
        }

        for (User u : users) {
            System.out.println(
                    u.getId() + " | " +
                            u.getName() + " | " +
                            u.getEmail() + " | " +
                            u.getAge() + " | " +
                            u.getCreatedAt()
            );
        }
    }

    private void updateUser() {
        long id = readLong("ID пользователя для обновления: ");
        User user = userDao.get(id);

        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Новое имя: ");
        user.setName(scanner.nextLine());

        System.out.print("Новый email: ");
        user.setEmail(scanner.nextLine());

        user.setAge(readInt("Новый возраст: "));

        userDao.update(user);
        System.out.println("Пользователь обновлён.");
    }

    private void deleteUser() {
        long id = readLong("ID пользователя для удаления: ");
        User user = userDao.get(id);

        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        userDao.delete(user);
        System.out.println("Пользователь удалён.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // очистка буфера
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: нужно ввести число!");
                scanner.nextLine(); // очистка ввода
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                long value = scanner.nextLong();
                scanner.nextLine(); // очистка буфера
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: нужно ввести число!");
                scanner.nextLine(); // очистка ввода
            }
        }
    }
}
