package com.example.dao;

import com.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private SessionFactory sessionFactory;

    /**
     * Инициализация Hibernate SessionFactory
     * Один экземпляр на весь DAO, чтобы переиспользовать коннекшн-пул
     */
    public UserDaoImpl() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Создание нового пользователя
     * Используем persist(), так как Hibernate 6
     * Оборачиваем в транзакцию для атомарности
     */
    public void save(UserEntity userEntity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(userEntity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Получение пользователя по ID
     * Возвращает null, если не найден
     */
    public UserEntity get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(UserEntity.class, id);
        }
    }

    /**
     * Получение списка всех пользователей
     * Обычно используется для админских панелей
     */
    public List<UserEntity> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", UserEntity.class).list();
        }
    }

    /**
     * Обновление данных пользователя
     * Требует открытой транзакции для коммита изменений
     */
    public void update(UserEntity userEntity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(userEntity);
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
    public void delete(UserEntity userEntity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(userEntity);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }
}