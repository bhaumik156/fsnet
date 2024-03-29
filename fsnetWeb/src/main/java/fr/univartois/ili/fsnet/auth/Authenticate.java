package fr.univartois.ili.fsnet.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.univartois.ili.fsnet.actions.ManageAnnounces;
import fr.univartois.ili.fsnet.actions.ManageConsultations;
import fr.univartois.ili.fsnet.actions.ManageContacts;
import fr.univartois.ili.fsnet.actions.ManageEvents;
import fr.univartois.ili.fsnet.actions.ManagePrivateMessages;
import fr.univartois.ili.fsnet.actions.ManageVisits;
import fr.univartois.ili.fsnet.actions.utils.UserUtils;
import fr.univartois.ili.fsnet.commons.security.Encryption;
import fr.univartois.ili.fsnet.commons.utils.PersistenceProvider;
import fr.univartois.ili.fsnet.core.LoggedUsersContainer;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.entities.SocialGroup;
import fr.univartois.ili.fsnet.facade.SocialEntityFacade;
import fr.univartois.ili.fsnet.facade.SocialGroupFacade;

/**
 * This class represents a servlet that is used in order to authenticate members
 * 
 */
@WebServlet("/Authenticate")
public class Authenticate extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Welcome page path when the user is authenticated
	 */
	private static final String WELCOME_AUTHENTICATED_PAGE = "Home.do";
	/**
	 * Welcome page path when the user is NOT authenticated
	 */
	public static final String WELCOME_NON_AUTHENTICATED_PAGE = "/WEB-INF/jsp/login.jsp";
	/**
	 * Authenticated user key in session scope
	 */
	public static final String AUTHENTICATED_USER = "userId";
	/**
	 * Name of the cookie containing the login
	 */
	public static final String LOGIN_COOKIE = "login";
	/**
	 * Name of the cookie containing the password
	 */
	public static final String PSSWD_COOKIE = "password";

	/**
	 * Set the cookies for automatic login
	 * 
	 * @param rep
	 *            the {@link HttpServletResponse}
	 * @param login
	 *            the login email
	 * @param passwd
	 *            the password
	 */
	private void setCookies(HttpServletResponse rep, String login, String passwd) {
		Cookie logCookie = new Cookie(LOGIN_COOKIE, login);
		Cookie passwdCookie = new Cookie(PSSWD_COOKIE, passwd);

		logCookie.setMaxAge(Integer.MAX_VALUE);
		passwdCookie.setMaxAge(Integer.MAX_VALUE);

		rep.addCookie(logCookie);
		rep.addCookie(passwdCookie);
	}

	/**
	 * This method is called when an user user tries to sign in
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		boolean authenticated = false;
		EntityManager em = PersistenceProvider.createEntityManager();

		String memberMail = req.getParameter("memberMail");
		String memberPass = req.getParameter("memberPass");

		SocialEntity se = null;

		if (memberMail == null && memberPass == null) {
			// Already logged ?
			Integer userIdentifier = (Integer) req.getSession(true)
					.getAttribute(AUTHENTICATED_USER);
			if (userIdentifier != null) {
				authenticated = true;
				se = em.find(SocialEntity.class, userIdentifier);
				addUserLogginInformations(req, se, em, true);
			}
			// Remember has been checked ?
			if (req.getCookies() != null) {
				for (Cookie c : req.getCookies()) {
					if (c.getName().equals(LOGIN_COOKIE)) {
						memberMail = c.getValue();
						if (memberPass != null) {
							break;
						}
					} else if (c.getName().equals(PSSWD_COOKIE)) {
						memberPass = c.getValue();
						if (memberMail != null) {
							break;
						}
					}
				}
			}
		}

		if (!authenticated && memberMail != null && memberPass != null) {
			memberMail = memberMail.toLowerCase();
			SocialEntityFacade socialEntityFacade = new SocialEntityFacade(em);
			se = socialEntityFacade.findByEmail(memberMail);
			if (se != null
					&& Encryption.testPassword(memberPass, se.getPassword())
					&& se.getIsEnabled()) {
				authenticated = true;
				addUserLogginInformations(req, se, em, false);
			} else {
				req.setAttribute("loginMessage", "login.error");
			}
		}

		if (authenticated) {
			// the user is now authenticated
			String remember = req.getParameter("remember");
			if (remember != null && remember.equals("on")) {
				setCookies(resp, memberMail, memberPass);
			}

			HttpSession session = req.getSession(true);
			String lastRequestedURL = (String) session
					.getAttribute("requestedURL");
			if (lastRequestedURL != null) {
				resp.sendRedirect(lastRequestedURL);
				session.removeAttribute("requestedURL");
			} else {
				resp.sendRedirect(WELCOME_AUTHENTICATED_PAGE);
			}

			SocialEntity user;
			user = em.find(SocialEntity.class, se.getId());

			String userName = user.getFirstName() + " " + user.getName();
			LoggedUsersContainer.getInstance().addUser(user.getId(), userName);

			SocialGroupFacade socialGroupFacade = new SocialGroupFacade(em);
			String groupTree = socialGroupFacade.treeParentName(user);
			req.getSession().setAttribute("groupTree", groupTree);
			req.getSession().setAttribute("hisGroup",
					UserUtils.getHisGroup(req));
			req.getSession().setAttribute("isMasterGroup",
					socialGroupFacade.isMasterGroup(user));
			req.getSession().setAttribute("isGroupResponsible",
					socialGroupFacade.isGroupResponsible(user));
			req.getSession().setAttribute("currentUserId", user.getId());

			// add under groups to select them to add rights
			// to participate at consultation
			if (socialGroupFacade.isSuperAdmin(user)
					|| socialGroupFacade.isGroupResponsible(user)) {
				List<SocialGroup> listOfChildGroup = socialGroupFacade
						.getAllChildGroups(user.getGroup());
				req.getSession().setAttribute("allUnderGroupsNoRights",
						listOfChildGroup);
			} else {
				List<SocialGroup> listOfChildGroup = ((List<SocialGroup>) new ArrayList<SocialGroup>());
				listOfChildGroup.add(user.getGroup());
				req.getSession().setAttribute("allUnderGroupsNoRights",
						listOfChildGroup);
			}
			req.getSession().setAttribute("color", user.getGroup().getColor());

		} else {
			// the user is not authenticated
			RequestDispatcher dispatcher = req
					.getRequestDispatcher(WELCOME_NON_AUTHENTICATED_PAGE);
			dispatcher.forward(req, resp);
		}
		em.close();
	}

	/**
	 * Perform task when a user is logged or being logged !
	 * 
	 * @param req
	 *            The http request object
	 * @param userSocialEntity
	 *            The authenticated user
	 * @param em
	 *            The entity manager
	 * @param alreadyLogged
	 *            User already logged or being logged
	 */
	private void addUserLogginInformations(HttpServletRequest req,
			SocialEntity userSocialEntity, EntityManager em,
			boolean alreadyLogged) {
		if (!alreadyLogged) {
			req.getSession(true).setAttribute(AUTHENTICATED_USER,
					userSocialEntity.getId());
			req.getSession(true).setAttribute("userName",
					userSocialEntity.getName());
			req.getSession(true).setAttribute("userFirstName",
					userSocialEntity.getFirstName());
		}

		SocialEntity user = em.find(SocialEntity.class,
				userSocialEntity.getId());
		if (user.getLastConnection() != null) {
			// Last connection in session before a new session
			req.getSession(true).setAttribute("LastConnection",
					user.getLastConnection());
		} else {
			req.getSession(true).setAttribute("LastConnection", new Date());
		}
		user.setLastConnection(new Date());
		ManagePrivateMessages.refreshNumNewMessages(req, em);
		ManageContacts.refreshNumNewContacts(req, em);
		ManageVisits.refreshNumNewVisits(req, em);
		ManageAnnounces.refreshNumNewAnnonces(req, em);
		ManageEvents.refreshNumNewEvents(req, em);
		ManageConsultations.refreshNumNewConsultations(req, em);
	}

	/**
	 * Delegated to doGet
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
