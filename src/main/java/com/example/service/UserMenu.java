package com.example.service;

import com.example.dao.UserDaoImpl;
import com.example.entity.UserEntity;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, отвечающий за консольное меню и взаимодействие с пользователем.
 */
public class UserMenu {

    private final UserService userService;
    private final Scanner scanner;

    public UserMenu(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public UserMenu() {
        this(new UserService(new UserDaoImpl()));
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
        UserEntity userEntity = new UserEntity();

        System.out.print("Имя: ");
        userEntity.setName(scanner.nextLine());

        System.out.print("Email: ");
        userEntity.setEmail(scanner.nextLine());

        userEntity.setAge(readInt("Возраст: "));

        userService.saveUser(userEntity);
        System.out.println("Пользователь успешно создан!");
    }

    private void showAllUsers() {
        List<UserEntity> userEntities = userService.getAllUsers();

        if (userEntities.isEmpty()) {
            System.out.println("Пользователи отсутствуют.");
            return;
        }

        for (UserEntity u : userEntities) {
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
        UserEntity userEntity = userService.getUserById(id);

        if (userEntity == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Новое имя: ");
        userEntity.setName(scanner.nextLine());

        System.out.print("Новый email: ");
        userEntity.setEmail(scanner.nextLine());

        userEntity.setAge(readInt("Новый возраст: "));

        userService.updateUser(userEntity);
        System.out.println("Пользователь обновлён.");
    }

    private void deleteUser() {
        long id = readLong("ID пользователя для удаления: ");
        UserEntity userEntity = userService.getUserById(id);

        if (userEntity == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        userService.deleteUser(userEntity);
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
