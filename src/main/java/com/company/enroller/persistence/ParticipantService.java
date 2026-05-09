package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Participant findByLogin(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("login", login);
        return (Participant) query.uniqueResult();

    }

    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();

    }

    public Participant delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
        return participant;
    }

    public Participant update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().update(participant);
        transaction.commit();
        return participant;
    }

    public Collection<Participant> getAllSortedByLogin(String sortOrder) {
        String order = "DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC"; // domyślnie ASC
        String hql = "FROM Participant ORDER BY login " + order;
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Collection<Participant> getAllFilteredByLogin(String key) {
        String hql = "FROM Participant WHERE login LIKE :key";
        Query query = connector.getSession().createQuery(hql);
        query.setParameter("key", "%" + key + "%");
        return query.list();
    }

}
