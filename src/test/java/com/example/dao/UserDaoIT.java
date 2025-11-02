package com.example.dao;

import com.example.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.hibernate.cfg.Configuration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoIT {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("postgres");

    private SessionFactory sessionFactory;
    private UserDaoImpl userDaoImpl;

    @BeforeAll
    void setupAll() {
        Configuration cfg = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgresContainer.getUsername())
                .setProperty("hibernate.connection.password", postgresContainer.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .addAnnotatedClass(UserEntity.class);

        sessionFactory = cfg.buildSessionFactory();
        userDaoImpl = new UserDaoImpl(sessionFactory);
    }

    @AfterAll
    void cleanup() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void beforeEach() {
        // очистка таблицы перед тестом
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM UserEntity").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void testSaveAndGetById() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Misha");
        userEntity.setEmail("misha@example.com");
        userEntity.setAge(25);

        userDaoImpl.save(userEntity);

        UserEntity retrieved = userDaoImpl.get(userEntity.getId());
        assertNotNull(retrieved);
        assertEquals("Misha", retrieved.getName());
    }

    @Test
    void testGetAll() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setName("Alex");
        userEntity1.setEmail("alex@example.com");
        userEntity1.setAge(30);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setName("Lena");
        userEntity2.setEmail("lena@example.com");
        userEntity2.setAge(28);

        userDaoImpl.save(userEntity1);
        userDaoImpl.save(userEntity2);

        List<UserEntity> allUserEntities = userDaoImpl.getAll();
        assertEquals(2, allUserEntities.size());
    }

    @Test
    void testUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("OldName");
        userEntity.setEmail("old@example.com");
        userEntity.setAge(40);
        userDaoImpl.save(userEntity);

        userEntity.setName("NewName");
        userEntity.setEmail("new@example.com");
        userEntity.setAge(41);
        userDaoImpl.update(userEntity);

        UserEntity updated = userDaoImpl.get(userEntity.getId());
        assertEquals("NewName", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(41, updated.getAge());
    }

    @Test
    void testDelete() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("ToDelete");
        userEntity.setEmail("delete@example.com");
        userEntity.setAge(50);
        userDaoImpl.save(userEntity);

        userDaoImpl.delete(userEntity);

        UserEntity deleted = userDaoImpl.get(userEntity.getId());
        assertNull(deleted);
    }
}