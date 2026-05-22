package com.mycompany.progetto_finale;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CatalogoDao {

   

    public Catalogo getCatalogo(int id) {

        Transaction transaction = null;
        Catalogo catalogo = null;

        try (Session session =
                     HibernateUtil
                     .getSessionFactory()
                     .openSession()) {

            transaction = session.beginTransaction();

            catalogo = session.get(
                    Catalogo.class,
                    id
            );

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null &&
                transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();
        }

        return catalogo;
    }

   

    public List<Catalogo> getAllCatalogo() {

        Transaction transaction = null;

        List<Catalogo> lista = null;

        Session session = null;

        try {

            session = HibernateUtil
                    .getSessionFactory()
                    .openSession();

            transaction = session.beginTransaction();

            CriteriaBuilder builder =
                    session.getCriteriaBuilder();

            CriteriaQuery<Catalogo> criteria =
                    builder.createQuery(Catalogo.class);

            Root<Catalogo> root =
                    criteria.from(Catalogo.class);

            criteria.select(root);

            lista = session
                    .createQuery(criteria)
                    .getResultList();

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null &&
                transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();

        } finally {

            if (session != null &&
                session.isOpen()) {

                session.close();
            }
        }

        return lista;
    }

   

    public void saveCatalogo(Catalogo catalogo) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil
                     .getSessionFactory()
                     .openSession()) {

            transaction = session.beginTransaction();

            session.persist(catalogo);

            transaction.commit();

            System.out.println(
                    "Categoria salvata"
            );

        } catch (Exception e) {

            if (transaction != null &&
                transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();
        }
    }


    public void updateCatalogo(Catalogo catalogo) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil
                     .getSessionFactory()
                     .openSession()) {

            transaction = session.beginTransaction();

            session.merge(catalogo);

            transaction.commit();

            System.out.println(
                    "Categoria aggiornata"
            );

        } catch (Exception e) {

            if (transaction != null &&
                transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();
        }
    }

   

    public boolean deleteCatalogo(int id) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil
                     .getSessionFactory()
                     .openSession()) {

            transaction = session.beginTransaction();

            Catalogo catalogo =
                    session.get(
                            Catalogo.class,
                            id
                    );

            if (catalogo != null) {

                session.remove(catalogo);

                transaction.commit();

                System.out.println(
                        "Categoria eliminata"
                );

                return true;
            }

        } catch (Exception e) {

            if (transaction != null &&
                transaction.isActive()) {

                transaction.rollback();
            }

            e.printStackTrace();
        }

        return false;
    }
}