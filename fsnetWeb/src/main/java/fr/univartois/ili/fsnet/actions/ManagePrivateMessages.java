package fr.univartois.ili.fsnet.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionSupport;

import fr.univartois.ili.fsnet.actions.utils.UserUtils;
import fr.univartois.ili.fsnet.commons.pagination.Paginator;
import fr.univartois.ili.fsnet.commons.utils.PersistenceProvider;
import fr.univartois.ili.fsnet.entities.PrivateMessage;
import fr.univartois.ili.fsnet.entities.Right;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.facade.PrivateMessageFacade;
import fr.univartois.ili.fsnet.facade.SocialEntityFacade;
import fr.univartois.ili.fsnet.facade.SocialGroupFacade;
import fr.univartois.ili.fsnet.facade.security.UnauthorizedOperationException;

/**
 * 
 * @author Matthieu Proucelle <matthieu.proucelle at gmail.com>
 */
public class ManagePrivateMessages extends ActionSupport implements
CrudAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageTo;
	private String messageSubject;
	private String messageBody;
	private int messageId;
	private String[] selectedMessages;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.univartois.ili.fsnet.actions.CrudAction#create(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String create(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EntityManager em = PersistenceProvider.createEntityManager();
		SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
				request, em);
		SocialGroupFacade fascade = new SocialGroupFacade(em);
		if (!fascade.isAuthorized(authenticatedUser, Right.SEND_MESSAGE)) {
			em.close();
			response.sendRedirect("/Inbox");
		}

		messageTo = messageTo.replaceAll("\\(.*\\)", "");
		messageTo = messageTo.replaceAll(" ", "");
		messageTo = messageTo.replaceAll("\t", "");

		em.getTransaction().begin();
		SocialEntityFacade sef = new SocialEntityFacade(em);
		StringTokenizer stk = new StringTokenizer(messageTo, ",");
		List<SocialEntity> receivers = new ArrayList<SocialEntity>();
		addRightToRequest(request);

		while (stk.hasMoreTokens()) {
			String email = stk.nextToken();
			SocialEntity findByEmail = sef.findByEmail(email);

			if (findByEmail == null) {
				addFieldError("messageTo", getText("privatemessages.to.error")+" "+email);
				em.getTransaction().commit();
				em.close();
				return INPUT;
			}

			receivers.add(findByEmail);
		}

		for (SocialEntity se : receivers) {
			PrivateMessageFacade pmf = new PrivateMessageFacade(em);
			pmf.sendPrivateMessage(messageBody, authenticatedUser, messageSubject, se);
		}

		em.getTransaction().commit();
		em.close();

		return SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.univartois.ili.fsnet.actions.CrudAction#modify(org.apache.struts.action
	 * .ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String modify(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.univartois.ili.fsnet.actions.CrudAction#delete(org.apache.struts.action
	 * .ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager em = PersistenceProvider.createEntityManager();
		addRightToRequest(request);
		String fromPage = request.getParameter("fromPage");
		try {
			SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
					request, em);

			PrivateMessageFacade pmf = new PrivateMessageFacade(em);
			PrivateMessage privateMessage = pmf.getPrivateMessage(messageId);
			if (privateMessage == null) {
				em.close();
				throw new UnauthorizedOperationException("");
			}
			em.getTransaction().begin();
			pmf.deletePrivateMessage(authenticatedUser, privateMessage,fromPage);
			em.getTransaction().commit();
		} catch (NumberFormatException e) {
			//servlet.log("GRAVE ERROR : MUST BE VALIDATE BY STRUTS", e);
		} finally {
			em.close();
		}

		return SUCCESS;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String deleteMulti(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager em = PersistenceProvider.createEntityManager();
		addRightToRequest(request);
		String fromPage = request.getParameter("fromPage");
		try {
			SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
					request, em);
			PrivateMessageFacade pmf = new PrivateMessageFacade(em);
			em.getTransaction().begin();

			for (int i = 0; i < selectedMessages.length; i++) {
				PrivateMessage m = pmf.getPrivateMessage(Integer
						.valueOf(selectedMessages[i]));
				pmf.deletePrivateMessage(authenticatedUser, m, fromPage);
			}

			em.flush();
			em.getTransaction().commit();
		} catch (NumberFormatException e) {

		} finally {
			em.close();
		}

		return SUCCESS;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String deleteMulti2(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EntityManager em = PersistenceProvider.createEntityManager();
		addRightToRequest(request);
		String fromPage = request.getParameter("fromPage");
		try {

			SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
					request, em);
			PrivateMessageFacade pmf = new PrivateMessageFacade(em);
			em.getTransaction().begin();

			for (int i = 0; i < selectedMessages.length; i++) {
				PrivateMessage m = pmf.getPrivateMessage(Integer
						.valueOf(selectedMessages[i]));
				pmf.deletePrivateMessage(authenticatedUser, m, fromPage);
			}

			em.flush();
			em.getTransaction().commit();
		} catch (NumberFormatException e) {

		} finally {
			em.close();
		}

		return SUCCESS;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String inbox(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager em = PersistenceProvider.createEntityManager();
		SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
				request, em);
		addRightToRequest(request);
		List<PrivateMessage> userMessages = new ArrayList<PrivateMessage>(
				authenticatedUser.getReceivedPrivateMessages());
		Collections.reverse(userMessages);

		/*
		 * Paginator<PrivateMessage> paginator = new
		 * Paginator<PrivateMessage>(userMessages, request, "inboxMessages");
		 * 
		 * request.setAttribute("inBoxMessagesPaginator", paginator);
		 */

		request.setAttribute("inBoxMessages", userMessages);

		refreshNumNewMessages(request, userMessages);
		refreshNumNewMessages(request, em);
		em.close();

		return SUCCESS;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String outbox(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		EntityManager em = PersistenceProvider.createEntityManager();
		SocialEntity authenticatedUser = UserUtils.getAuthenticatedUser(
				request, em);
		addRightToRequest(request);

		List<PrivateMessage> userMessages = new ArrayList<PrivateMessage>(
				authenticatedUser.getSentPrivateMessages());
		Collections.reverse(userMessages);

		request.setAttribute("outBoxMessages", userMessages);


		em.close();
		return SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.univartois.ili.fsnet.actions.CrudAction#search(org.apache.struts.action
	 * .ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String search(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.univartois.ili.fsnet.actions.CrudAction#display(org.apache.struts.
	 * action.ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String display(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		EntityManager em = PersistenceProvider.createEntityManager();
		addRightToRequest(request);

		try {
			SocialEntity authenticatedUser = UserUtils
					.getAuthenticatedUser(request, em);
			PrivateMessageFacade pmf = new PrivateMessageFacade(em);
			PrivateMessage privateMessage = pmf
					.getPrivateMessage(messageId);
			if(privateMessage==null){
				throw new NullPointerException("privateMessage is null");
			}
			Collection<PrivateMessage> tmpUserMessages = pmf
					.getConversation(privateMessage.getFrom(),
							privateMessage.getSubject(),
							privateMessage.getTo());
			List<PrivateMessage> userMessages;
			if (tmpUserMessages == null) {
				userMessages = new ArrayList<>();
			} else {
				userMessages = new ArrayList<>(tmpUserMessages);
			}

			Collections.reverse(userMessages);

			if ((authenticatedUser.equals(privateMessage.getFrom()) || authenticatedUser
					.equals(privateMessage.getTo()))) {
				if (authenticatedUser.equals(privateMessage.getTo())) {
					em.getTransaction().begin();
					privateMessage.setReed(true);
					refreshNumNewMessages(request, em);
					em.getTransaction().commit();
				}

				Paginator<PrivateMessage> paginator = new Paginator<PrivateMessage>(
						userMessages, request, "conversationMessages");

				List<PrivateMessage> listPrivateMessage = new ArrayList<PrivateMessage>();
				listPrivateMessage.add(privateMessage);
				Paginator<PrivateMessage> paginator1 = new Paginator<PrivateMessage>(
						listPrivateMessage, request,
						"conversationMessages1");


				request.setAttribute("conversationMessages", paginator);
				request.setAttribute("conversationMessages1", paginator1);
				request.setAttribute("theMessage", privateMessage);

				return SUCCESS;
			} else {
				throw new UnauthorizedOperationException(
						"Must be the owner of message");
			}

		} catch (NumberFormatException e) {

		} finally {
			em.close();
		}

		// NEVER HAPPEND
		return null;
	}

	/**
	 * @param messages
	 * @return
	 */
	private static final int calculateNumNewMessage(
			Collection<PrivateMessage> messages) {
		int numNonReedPrivateMessages = 0;
		for (PrivateMessage pm : messages) {
			if (!pm.isReed()) {
				numNonReedPrivateMessages++;
			}
		}
		return numNonReedPrivateMessages;
	}

	/**
	 * @param request
	 * @param messages
	 */
	private static final void refreshNumNewMessages(HttpServletRequest request,
			Collection<PrivateMessage> messages) {
		HttpSession session = request.getSession();
		int numNonReedPrivateMessages = calculateNumNewMessage(messages);
		session.setAttribute("numNonReedPrivateMessages",
				numNonReedPrivateMessages);
	}

	/**
	 * Store the number of non read private messages in the session
	 * 
	 * @param request
	 * @param em
	 */
	public static final void refreshNumNewMessages(HttpServletRequest request,
			EntityManager em) {
		HttpSession session = request.getSession();
		SocialEntity se = UserUtils.getAuthenticatedUser(request, em);
		int numNonReedPrivateMessages = calculateNumNewMessage(se
				.getReceivedPrivateMessages());
		session.setAttribute("numNonReedPrivateMessages",
				numNonReedPrivateMessages);
	}

	/**
	 * @param request
	 */
	private void addRightToRequest(HttpServletRequest request) {
		SocialEntity socialEntity = UserUtils.getAuthenticatedUser(request);
		Right rightAnswerMessage = Right.ANSWER_MESSAGE;
		Right rightSendMessage = Right.SEND_MESSAGE;
		request.setAttribute("rightAnswerMessage", rightAnswerMessage);
		request.setAttribute("rightSendMessage", rightSendMessage);
		request.setAttribute("socialEntity", socialEntity);
	}

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String[] getSelectedMessages() {
		return selectedMessages;
	}

	public void setSelectedMessages(String[] selectedMessages) {
		this.selectedMessages = selectedMessages;
	}

}
