package com.lab4;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.lab4.model.Mage;
import com.lab4.model.Tower;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static SessionFactory sessionFactory;

    public Database() {
        sessionFactory = getSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                LOGGER.log(Level.SEVERE, "Initial SessionFactory creation failed.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    public void insertMage(Mage mage) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(mage);
            transaction.commit();
            LOGGER.info("Mage inserted successfully: " + mage.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to insert Mage: " + mage.getName(), e);
        }
    }

    public void insertTower(Tower tower) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(tower);
            transaction.commit();
            LOGGER.info("Tower inserted successfully: " + tower.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to insert Tower: " + tower.getName(), e);
        }
    }

    public void updateMage(Mage mage) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(mage);
            transaction.commit();
            LOGGER.info("Mage updated successfully: " + mage.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to update Mage: " + mage.getName(), e);
        }
    }

    public void updateTower(Tower tower) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(tower);
            transaction.commit();
            LOGGER.info("Tower updated successfully: " + tower.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to update Tower: " + tower.getName(), e);
        }
    }

    public Mage getMage(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Mage.class, name);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve Mage: " + name, e);
            return null;
        }
    }

    public Tower getTower(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Tower.class, name);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve Tower: " + name, e);
            return null;
        }
    }

    public void deleteMage(String name) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Mage mage = session.get(Mage.class, name);
            if (mage != null) {
                session.delete(mage);
                LOGGER.info("Mage deleted successfully: " + name);
            } else {
                LOGGER.warning("Mage not found with name: " + name);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to delete Mage: " + name, e);
        }
    }

    public void deleteTower(String name) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Tower tower = session.get(Tower.class, name);
            if (tower != null) {
                session.delete(tower);
                LOGGER.info("Tower deleted successfully: " + name);
            } else {
                LOGGER.warning("Tower not found with name: " + name);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to delete Tower: " + name, e);
        }
    }

    public void logDatabase() {
        try (Session session = sessionFactory.openSession()) {
            List<Mage> mages = session.createQuery("FROM Mage", Mage.class).getResultList();
            LOGGER.info("Mages in the database:");
            for (Mage mage : mages) {
                LOGGER.info(mage.toString());
            }
            List<Tower> towers = session.createQuery("FROM Tower", Tower.class).getResultList();
            LOGGER.info("Towers in the database:");
            for (Tower tower : towers) {
                LOGGER.info(tower.toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to log database", e);
        }
    }

    public void clearDatabase() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<Mage> mageQuery = session.createQuery("DELETE FROM Mage");
            int mageDeletedCount = mageQuery.executeUpdate();
            LOGGER.info("Deleted " + mageDeletedCount + " Mages.");
            Query<Tower> towerQuery = session.createQuery("DELETE FROM Tower");
            int towerDeletedCount = towerQuery.executeUpdate();
            LOGGER.info("Deleted " + towerDeletedCount + " Towers.");

            transaction.commit();
            LOGGER.info("Database cleared successfully.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "Failed to clear database", e);
        }
    }

    public void dumpDatabase() {
        try (Session session = sessionFactory.openSession()) {
            List<Mage> mages = session.createQuery("FROM Mage", Mage.class).getResultList();
            LOGGER.info("Dumping Mages from the database:");
            for (Mage mage : mages) {
                LOGGER.info(mage.toString());
            }
            List<Tower> towers = session.createQuery("FROM Tower", Tower.class).getResultList();
            LOGGER.info("Dumping Towers from the database:");
            for (Tower tower : towers) {
                LOGGER.info(tower.toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to dump database", e);
        }
    }

    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            LOGGER.info("SessionFactory closed successfully.");
        }
    }

    public void getAllMagesWithPowerHigherThan(int power) {
        try (Session session = sessionFactory.openSession()) {
            Query<Mage> query = session.createQuery("FROM Mage WHERE level > :power", Mage.class);
            query.setParameter("power", power);
            List<Mage> mages = query.getResultList();
            LOGGER.info("Mages with power higher than " + power + ":");
            for (Mage mage : mages) {
                LOGGER.info(mage.toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to get Mages with power higher than " + power, e);
        }
    }

    public void getAllTowersWithHeightHigherThan(int height) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tower> query = session.createQuery("FROM Tower WHERE height > :height", Tower.class);
            query.setParameter("height", height);
            List<Tower> towers = query.getResultList();
            LOGGER.info("Towers with height higher than " + height + ":");
            for (Tower tower : towers) {
                LOGGER.info(tower.toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to get Towers with height higher than " + height, e);
        }
    }

    public void getAllMagesFromTowerHigherThan(String towerName, int power) {
        try (Session session = sessionFactory.openSession()) {
            Query<Mage> query = session.createQuery("FROM Mage WHERE tower.name = :towerName AND level > :power", Mage.class);
            query.setParameter("towerName", towerName);
            query.setParameter("power", power);
            List<Mage> mages = query.getResultList();
            LOGGER.info("Mages from Tower " + towerName + " with power higher than " + power + ":");
            for (Mage mage : mages) {
                LOGGER.info(mage.toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to get Mages from Tower " + towerName + " with power higher than " + power, e);
        }
    }
}
