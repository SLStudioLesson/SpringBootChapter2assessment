package com.example.taskmanagerapp;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.taskmanagerapp.model.Label;
import com.example.taskmanagerapp.model.Status;
import com.example.taskmanagerapp.model.Task;
import com.example.taskmanagerapp.model.User;

public class TestDBUtil {

    public static void resetDB(TestEntityManager em) throws Exception {
        em.getEntityManager().createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE task_label RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE labels RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE tasks RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE status RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    public static void insertUsers(TestEntityManager em, int start, int end) throws Exception {
        for (int i = start; i <= end; i++) {
            User user = new User();
            user.setName("user" + i);
            em.persist(user);
        }
    }

    public static void insertStatus(TestEntityManager em, int start, int end) throws Exception {
        for (int i = start; i <= end; i++) {
            Status status = new Status();
            status.setName("status" + i);
            em.persist(status);
        }
    }

    public static void insertTasks(TestEntityManager em, int start, int end) throws Exception {
        for (int i = start; i <= end; i++) {
            User user = new User();
            user.setName("user" + i);

            Status status = new Status();
            status.setName("status" + i);

            Task task = new Task();
            task.setName("task" + i);
            task.setUser(user);
            task.setStatus(status);
            em.persist(user);
            em.persist(status);
            em.persist(task);
        }
    }

    public static void insertLabels(TestEntityManager em, int start, int end) throws Exception {
        for (int i = start; i <= end; i++) {
            Label label = new Label();
            label.setName("label" + i);
            em.persist(label);
        }
    }
}
