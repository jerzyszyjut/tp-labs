package com.lab4;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.lab4.model.Mage;
import com.lab4.model.Tower;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String PERSISTENCE_UNIT_NAME = "Lab4PersistenceUnit";
    private static EntityManagerFactory entityManagerFactory;

    public Database() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public void insertMage(Mage mage) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(mage);
        transaction.commit();
        LOGGER.info("Mage inserted successfully: " + mage.getName());
        entityManager.close();
    }

    public void insertTower(Tower tower) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(tower);
        transaction.commit();
        LOGGER.info("Tower inserted successfully: " + tower.getName());
        entityManager.close();
    }

    public void updateMage(Mage mage) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(mage);
        transaction.commit();
        LOGGER.info("Mage updated successfully: " + mage.getName());
        entityManager.close();
    }

    public void updateTower(Tower tower) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(tower);
        transaction.commit();
        LOGGER.info("Tower updated successfully: " + tower.getName());
        entityManager.close();
    }

    public Mage getMage(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Mage mage = entityManager.find(Mage.class, name);
        entityManager.close();
        return mage;
    }

    public Tower getTower(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Tower tower = entityManager.find(Tower.class, name);
        entityManager.close();
        return tower;
    }

    public void deleteMage(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Mage mage = entityManager.find(Mage.class, name);
        entityManager.remove(mage);
        transaction.commit();
        LOGGER.info("Mage deleted successfully: " + name);
        entityManager.close();
    }

    public void deleteTower(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Tower tower = entityManager.find(Tower.class, name);
        entityManager.remove(tower);
        transaction.commit();
        LOGGER.info("Tower deleted successfully: " + name);
        entityManager.close();
    }

    public void logDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        Query mageQuery = entityManager.createQuery("SELECT m FROM Mage m");
        List<Mage> mages = mageQuery.getResultList();
        LOGGER.info("Mages in the database:");
        for (Mage mage : mages) {
            LOGGER.info(mage.toString());
        }
        
        Query towerQuery = entityManager.createQuery("SELECT t FROM Tower t");
        List<Tower> towers = towerQuery.getResultList();
        LOGGER.info("Towers in the database:");
        for (Tower tower : towers) {
            LOGGER.info(tower.toString());
        }
        
        entityManager.close();
    }

    public void clearDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        // Clear Mages
        Query mageClearQuery = entityManager.createQuery("DELETE FROM Mage");
        mageClearQuery.executeUpdate();
        
        // Clear Towers
        Query towerClearQuery = entityManager.createQuery("DELETE FROM Tower");
        towerClearQuery.executeUpdate();
        
        entityManager.getTransaction().commit();
        entityManager.close();
        
        LOGGER.info("Database cleared.");
    }

    public void dumpDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        // Dump Mages
        Query mageQuery = entityManager.createQuery("SELECT m FROM Mage m");
        List<Mage> mages = mageQuery.getResultList();
        LOGGER.info("Dumping Mages from the database:");
        for (Mage mage : mages) {
            LOGGER.info(mage.toString());
        }
        
        // Dump Towers
        Query towerQuery = entityManager.createQuery("SELECT t FROM Tower t");
        List<Tower> towers = towerQuery.getResultList();
        LOGGER.info("Dumping Towers from the database:");
        for (Tower tower : towers) {
            LOGGER.info(tower.toString());
        }
        
        entityManager.close();
    }

    public List<Mage> getAllMagesWithPowerHigherThan(int power) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT m FROM Mage m WHERE m.power > :power", Mage.class);
        query.setParameter("power", power);
        List<Mage> mages = query.getResultList();
        entityManager.close();
        return mages;
    }

    public List<Tower> getAllTowersWithHeightHigherThan(int height) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT t FROM Tower t WHERE t.height > :height", Tower.class);
        query.setParameter("height", height);
        List<Tower> towers = query.getResultList();
        entityManager.close();
        return towers;
    }

    public List<Mage> getAllMagesFromTowerHigherThan(String towerName, int power) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT m FROM Mage m WHERE m.tower.name = :towerName AND m.power > :power");
        query.setParameter("towerName", towerName);
        query.setParameter("power", power);
        List<Mage> mages = query.getResultList();
        entityManager.close();
        return mages;
    }

    public void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            LOGGER.info("EntityManagerFactory closed successfully.");
        }
    }
}
