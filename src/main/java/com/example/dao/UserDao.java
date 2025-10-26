package com.example.dao;

import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserDao {

    private SessionFactory sessionFactory;

    /**
     * Инициализация Hibernate SessionFactory
     * Один экземпляр на весь DAO, чтобы переиспользовать коннекшн-пул
     */
    public UserDao() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * Создание нового пользователя
     * Используем persist(), так как Hibernate 6
     * Оборачиваем в транзакцию для атомарности
     */
    public void save(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Получение пользователя по ID
     * Возвращает null, если не найден
     */
    public User get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    /**
     * Получение списка всех пользователей
     * Обычно используется для админских панелей
     */
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    /**
     * Обновление данных пользователя
     * Требует открытой транзакции для коммита изменений
     */
    public void update(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Удаление пользователя
     * Проверка транзакции обязательна
     */
    public void delete(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}