package fr.univartois.ili.fsnet.facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fr.univartois.ili.fsnet.entities.Consultation;
import fr.univartois.ili.fsnet.entities.ConsultationChoice;
import fr.univartois.ili.fsnet.entities.ConsultationChoiceVote;
import fr.univartois.ili.fsnet.entities.ConsultationVote;
import fr.univartois.ili.fsnet.entities.InteractionGroups;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.entities.SocialGroup;
import fr.univartois.ili.fsnet.facade.security.UnauthorizedOperationException;

public class ConsultationFacade {
	private final EntityManager em;
	
	private static final int CONSULTATION_MAXDATE_HOUR = 23 ;
	private static final int CONSULTATION_MIN = 0 ;
	private static final int CONSULTATION_MAXDATE_MINUTE = 59 ;
	private static final int CONSULTATION_MAXDATE_SECOND = 59 ;

	public ConsultationFacade(EntityManager em) {
		this.em = em;
	}

	public final Consultation createConsultation(SocialEntity creator,
			String title, String description, String[] choices,
			Consultation.TypeConsultation type,
			List<SocialGroup> groupsRigthsAccepted) {

		Consultation consultation = new Consultation(creator, title,
				description, type, groupsRigthsAccepted);
		for (String s : choices) {
			consultation.addChoice(new ConsultationChoice(consultation, s));
		}

		List<InteractionGroups> igList = new ArrayList<InteractionGroups>();
		for (SocialGroup group : groupsRigthsAccepted) {
			igList.add(new InteractionGroups(consultation, group));
			consultation.setInteractionGroups(igList);
		}
		
		em.persist(consultation);
		return consultation;
	}

	public final Consultation getConsultation(int consultationId) {
		return em.find(Consultation.class, consultationId);
	}

	public ConsultationVote getVote(int voteId) {
		return em.find(ConsultationVote.class, voteId);
	}

	public void deleteVote(Consultation consultation, SocialEntity entity,
			ConsultationVote vote) {
		if (vote != null && entity != null) {
			if (!vote.getVoter().equals(entity)) {
				throw new UnauthorizedOperationException("exception.message");
			} else {
				consultation.getConsultationVotes().remove(vote);
				entity.getVotes().remove(vote);
				deleteVote(vote);
			}
		}
	}

	public void deleteVote(ConsultationVote vote) {
		if (vote == null) {
			throw new IllegalArgumentException();
		}
		em.remove(vote);
	}

	public List<Consultation> getUserConsultations(SocialEntity member) {
		if (member == null) {
			throw new IllegalArgumentException();
		}
		TypedQuery<Consultation> query = em
				.createQuery(
						"SELECT c FROM Consultation c WHERE c.creator = :member ORDER BY c.creationDate DESC",
						Consultation.class);
		query.setParameter("member", member);
		return query.getResultList();
	}

	public final void voteForConsultation(Consultation consultation,
			ConsultationVote vote) {
		if (consultation != null && vote != null) {
			vote.getVoter().getVotes().add(vote);
			vote.setConsultation(consultation);
			consultation.getConsultationVotes().add(vote);
			em.persist(vote);
		}
	}

	public void deleteConsultation(Consultation consultation,
			SocialEntity member) {
		if (consultation != null && member != null) {
			member.getInteractions().remove(consultation);
			em.remove(consultation);
		}

	}

	public void closeConsultation(Consultation consultation) {
		if (consultation != null) {
			consultation.setOpened(false);
			em.merge(consultation);
		}
	}

	public void openConsultation(Consultation consultation) {
		if (consultation != null) {
			consultation.setOpened(true);
			em.merge(consultation);
		}
	}

	public List<Consultation> getConsultationsContaining(String searchText) {
		TypedQuery<Consultation> query = em
				.createQuery(
						"SELECT consultation FROM Consultation consultation WHERE consultation.title LIKE :pattern ORDER BY consultation.creationDate DESC",
						Consultation.class);
		query.setParameter("pattern", "%" + searchText + "%");
		return query.getResultList();
	}

	public List<String> getOtherChoice(int idConsultation, String voteOther) {
		TypedQuery<String> query = em
				.createQuery(
						"SELECT DISTINCT vote.other FROM ConsultationVote vote WHERE vote.consultation.id = :idConsultation AND vote.other LIKE :pattern ORDER BY vote.other",
						String.class);
		query.setParameter("pattern", "%" + voteOther + "%");
		query.setParameter("idConsultation", idConsultation);
		return query.getResultList();
	}

	public List<ConsultationChoiceVote> getChoicesVote(int id) {
		TypedQuery<ConsultationChoiceVote> query = em
				.createQuery(
						"SELECT DISTINCT voteChoice FROM ConsultationChoiceVote voteChoice WHERE voteChoice.choice.id = :idChoice ",
						ConsultationChoiceVote.class);
		query.setParameter("idChoice", id);
		return query.getResultList();
	}
	/**
	 * Get consultation wiwh occur today
	 * @return List<Consultation> the list containing consultations which occur today
	 */
	
	public final List<Consultation> getConsultationsWhichOccurToday(){
		List<Consultation> listConsultation;
		
		Calendar calendar = GregorianCalendar.getInstance();
				
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE), CONSULTATION_MIN,
				CONSULTATION_MIN, CONSULTATION_MIN);
		Date startToday = calendar.getTime();
		
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE), CONSULTATION_MAXDATE_HOUR,
				CONSULTATION_MAXDATE_MINUTE, CONSULTATION_MAXDATE_SECOND);
		Date endToday = calendar.getTime();
		
		listConsultation = em
				.createQuery(
						"SELECT c FROM Consultation c where c.maxDate BETWEEN :startToday AND :endToday",
						Consultation.class)
						.setParameter("startToday", startToday)
						.setParameter("endToday", endToday).getResultList();		
		return listConsultation;
	}

}
