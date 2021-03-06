package polytech.sgbd.blog.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import polytech.sgbd.blog.model.Keyword;
import polytech.sgbd.blog.model.Message;

public class MessageDAO {
	private EntityManager em;

	public MessageDAO(EntityManager em) {
		this.em = em;
	}

	public void insert(Message message) {
		em.getTransaction().begin();
		em.persist(message);
		em.getTransaction().commit();
	}

	public List<Message> selectAll() {
		String sql = "select message from Message message";
		TypedQuery<Message> query = em.createQuery(sql, Message.class);
		List<Message> results = query.getResultList();
		return results;
	}

	public List<Message> selectByUserId(int userId) {
		String sql = "select message from Message message where message.user.id = :userId";
		TypedQuery<Message> query = em.createQuery(sql, Message.class);
		query.setParameter("userId", userId);
		List<Message> results = query.getResultList();
		return results;
	}

	public List<Message> selectByUsername(String username) {
		String sql = "select message from Message message where message.user.userName like :username";
		TypedQuery<Message> query = em.createQuery(sql, Message.class);
		query.setParameter("username", "%" + username + "%");
		List<Message> results = query.getResultList();
		return results;
	}

	public List<Message> selectByDate(String date) {
		String sql = "select message from Message message where DATE_FORMAT(message.date,'%Y-%m-%d') like :date";
		Query query = em.createQuery(sql);
		query.setParameter("date", "%" + date + "%");
		List<Message> results = query.getResultList();
		return results;
	}

	public List<Message> selectByKeyword(String keyword) {
		String sql = "select keyword from Keyword keyword where keyword.text like :keyword";
		Query query = em.createQuery(sql);
		query.setParameter("keyword", "%" + keyword + "%");
		List<Message> messages = new ArrayList<Message>();
		boolean exist;
		for (Keyword k : (List<Keyword>) query.getResultList()) {
			exist = false;
			for (Message m : messages) {
				if (m.getId() == k.getMessage().getId()) {
					exist = true;
				}
			}
			if (!exist) {
				messages.add(k.getMessage());
			}
		}
		return messages;

	}

	public Message selectById(int id) {
		String sql = "select message from Message message where message.id = :id";
		TypedQuery<Message> query = em.createQuery(sql, Message.class);
		query.setParameter("id", id);
		Message result = query.getSingleResult();
		return result;
	}

	public Message selectByText(String text) {
		String sql = "select message from Message message where message.text like %:text%";
		TypedQuery<Message> query = em.createQuery(sql, Message.class);
		query.setParameter("text", text);
		Message result = query.getSingleResult();
		return result;
	}

	public void deleteById(int id) {
		Message message = em.find(Message.class, id);
		em.getTransaction().begin();
		em.remove(message);
		em.getTransaction().commit();
	}

	public void modifyById(Message message) {
		em.getTransaction().begin();
		em.persist(message);
		em.getTransaction().commit();
	}
}
