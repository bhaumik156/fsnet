package fr.univartois.ili.fsnet.admin.actions;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.MappingDispatchAction;

import fr.univartois.ili.fsnet.commons.mail.FSNetConfiguration;
import fr.univartois.ili.fsnet.commons.mail.FSNetMailer;
import fr.univartois.ili.fsnet.commons.mail.Mail;
import fr.univartois.ili.fsnet.commons.security.Encryption;
import fr.univartois.ili.fsnet.commons.utils.DateUtils;
import fr.univartois.ili.fsnet.entities.Address;
import fr.univartois.ili.fsnet.entities.Interest;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.facade.forum.iliforum.SocialEntityFacade;


/**
 * Execute CRUD Actions (and more) for the entity member
 * 
 * @author Audrey Ruellan and Cerelia Besnainou
 * @author Mehdi Benzaghar
 */
public class ManageMembers extends MappingDispatchAction implements CrudAction {

	private static EntityManagerFactory factory = Persistence
			.createEntityManagerFactory("fsnetjpa");
	private static final Logger logger = Logger.getAnonymousLogger();

	@Override
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
		String name = (String) dynaForm.get("name");
		String firstName = (String) dynaForm.get("firstName");
		String mail = (String) dynaForm.get("email");

		logger.info("#### New User : " + mail);
		EntityManager em = factory.createEntityManager();

		SocialEntityFacade facadeSE = new SocialEntityFacade(em);
		SocialEntity socialEntity = facadeSE.createSocialEntity(name, firstName, mail);
	
		String generatedPassword = null;
		try {
			generatedPassword = Encryption.generateRandomPassword();
			logger.info("#### Password : " + generatedPassword);
			socialEntity.setPassword(Encryption.getEncodedPassword(generatedPassword));
			em.getTransaction().begin();
			em.persist(socialEntity);
			em.getTransaction().commit();
			sendConfirmationMail(socialEntity, generatedPassword);
		} catch(RollbackException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("email", new ActionMessage("members.user.exists"));
			saveErrors(request, errors);
		} catch (Exception e) {
			ActionErrors errors = new ActionErrors();
			errors.add("email", new ActionMessage("members.error.on.create"));
			saveErrors(request, errors);
		}
		em.close();
		
		return mapping.findForward("success");
	}
	/**
	 * Send mails to a list of recipient.
	 * 
	 * @param socialEntity
	 *            the new EntiteSociale
	 * @return true if success false if fail
	 * 
	 * @author Mathieu Boniface < mat.boniface {At} gmail.com >
	 */
	private void sendConfirmationMail(SocialEntity socialEntity, String password) {
		FSNetConfiguration conf = FSNetConfiguration.getInstance();
		String fsnetAddress = conf.getFSNetWebAddress();
		String message = createMessageRegistration(socialEntity.getName(), socialEntity
				.getFirstName(), fsnetAddress, password);
		// send a mail
		FSNetMailer mailer = FSNetMailer.getInstance();
		Mail mail = mailer.createMail();
		mail.setSubject("Inscription FSNet");
		mail.addRecipient(socialEntity.getEmail());
		mail.setContent(message);
		mailer.sendMail(mail);
	}
	/**
	 * Method that creates an welcome message to FSNet.
	 * 
	 * @param nom
	 * @param prenom
	 * @param email
	 * @return the message .
	 */
	private String createMessageRegistration(String nom, String prenom,
			String addressFsnet, String password) {
		StringBuilder message = new StringBuilder();
		message.append("Bonjour ").append(nom).append(" ").append(prenom);
		message.append(",<br/><br/>");
		message.append("Vous venez d'être enregistré sur FSNet (Firm Social Network).<br/><br/>");
		message.append("Désormais vous pouvez vous connecter sur le site ");
		message.append(addressFsnet);
		message.append(" .<br/><br/>");
		message.append("Un mot de passe a été généré automatiquement : <em>");
		message.append(password);
		message.append("</em><br/><br/>Cet e-mail vous a été envoyé d'une adresse servant uniquement à expédier des messages. Merci de ne pas répondre à ce message.");
		return message.toString();
	}
	
	@Override
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String entitySelected = request.getParameter("entitySelected");

		logger.info("delete social entity: " + entitySelected);

		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em
				.createQuery(
						"DELETE FROM SocialEntity socialentity WHERE socialentity.id = :entitySelected ")
				.setParameter("entitySelected",
						Integer.parseInt(entitySelected)).executeUpdate();
		em.getTransaction().commit();
		em.close();
		return mapping.findForward("success");
	}
	
	

	@Override
	public ActionForward display(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
		EntityManager entityManager = factory.createEntityManager();
		
		Integer idMember = Integer.valueOf(request.getParameter("idMember"));

		SocialEntity member = entityManager.find(SocialEntity.class, idMember);
	
		entityManager.close();
		if (member.getAddress() != null) {
			dynaForm.set("address",member.getAddress().getAddress()+" "+member.getAddress().getCity());
		}
		dynaForm.set("phone",member.getPhone());
		dynaForm.set("sexe",member.getSex());
		dynaForm.set("job",member.getProfession());
		dynaForm.set("birthDay",member.getBirthDate());
		dynaForm.set("name", member.getName());
		dynaForm.set("email", member.getEmail());
		dynaForm.set("firstName", member.getFirstName());
		dynaForm.set("id",member.getId());
		request.setAttribute("interests", member.getInterests());
		request.setAttribute("id",member.getId());

		return mapping.findForward("success");
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager entityManager = factory.createEntityManager();
		DynaActionForm formSocialENtity = (DynaActionForm) form;// NOSONAR
		String name = (String) formSocialENtity.get("name");
		String firstName = (String) formSocialENtity.get("firstName");
		String email = (String) formSocialENtity.get("email");
		String job = (String) formSocialENtity.get("job");
		String address = (String) formSocialENtity.get("address");
		String phone = (String) formSocialENtity.get("phone");
		String sexe = (String) formSocialENtity.get("sexe");
		Date birthDay=null;
		try {
			birthDay = DateUtils.format((String) formSocialENtity.get("formatBirthDay"));
			formSocialENtity.set("birthDay", birthDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Integer idMember = (Integer) formSocialENtity.get("id");
		
		SocialEntityFacade facadeSE = new SocialEntityFacade(entityManager);
		
		SocialEntity member = facadeSE.getSocialEntity(idMember);
		member.setPrenom(firstName);
		member.setName(name);
		member.setEmail(email);
		member.setAddress(new Address("",address));
		member.setBirthDate(birthDay);
		member.setPhone(phone);
		member.setSex(sexe);
		member.setProfession(job);
		entityManager.getTransaction().begin();
		entityManager.merge(member);
		entityManager.getTransaction().commit();
		request.setAttribute("member", member);
		
		ActionMessages errors = new ActionErrors();
		errors.add("message", new ActionMessage("member.success.update"));
		saveErrors(request, errors);
		return mapping.findForward("success");
	}

	@Override
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager em = factory.createEntityManager();
		TypedQuery<SocialEntity> query = null;
		List<SocialEntity> resultOthers = null;

		if (form != null) {
			DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
			String searchText = (String) dynaForm.get("searchText");

			query = em
					.createQuery(
							"SELECT es FROM SocialEntity es WHERE (es.name LIKE :searchText"
									+ " OR es.firstName LIKE :searchText OR es.email LIKE :searchText)",
							SocialEntity.class);
			query.setParameter("searchText", "%" + searchText + "%");

		} else {
			query = em.createQuery("SELECT es FROM SocialEntity es",
					SocialEntity.class);
		}
		resultOthers = query.getResultList();
		em.close();

		request.setAttribute("membersResult", resultOthers);
		return mapping.findForward("success");
	}
	
	/**
	 * delete interest member by admin
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward deleteInterestMember(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Integer interestSelected = Integer.valueOf(request.getParameter("interestSelected"));
		Integer idSocialEntity = Integer.valueOf(request.getParameter("idMember"));
		
		EntityManager em = factory.createEntityManager();
		
		
		logger.info("delete interest social entity");
		SocialEntityFacade ise = new SocialEntityFacade(em);
		em.getTransaction().begin();
		ise.removeInterest(em.find(Interest.class, interestSelected), em.find(SocialEntity.class, idSocialEntity));
		em.getTransaction().commit();
		em.close();
		
		return mapping.findForward("success");
	}
}
