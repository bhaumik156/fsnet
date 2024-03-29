package fr.univartois.ili.fsnet.actions.utils;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import fr.univartois.ili.fsnet.auth.Authenticate;
import fr.univartois.ili.fsnet.commons.utils.PersistenceProvider;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.entities.SocialGroup;
import fr.univartois.ili.fsnet.facade.SocialEntityFacade;
import fr.univartois.ili.fsnet.facade.SocialGroupFacade;

/**
 * 
 * @author Matthieu Proucelle <matthieu.proucelle at gmail.com>
 */
public final class UserUtils {

	private UserUtils() {
	}

	public static final SocialEntity getAuthenticatedUser(
			HttpServletRequest request) {
		int id = getAuthenticatesUserId(request);
		EntityManager em = PersistenceProvider.createEntityManager();
		SocialEntityFacade socialEntityFacade = new SocialEntityFacade(em);
		SocialEntity user = socialEntityFacade.getSocialEntity(id);
		em.close();
		return user;
	}

	public static final SocialGroup getHisGroup(HttpServletRequest request) {
		SocialEntity socialEntity = getAuthenticatedUser(request);

		return socialEntity.getGroup();
	}

	public static final Integer getAuthenticatesUserId(
			HttpServletRequest request) {
		return (Integer) request.getSession().getAttribute(
				Authenticate.AUTHENTICATED_USER);
	}

	public static final SocialEntity getAuthenticatedUser(
			HttpServletRequest request, EntityManager em) {
		int id = getAuthenticatesUserId(request);
		SocialEntity user;
		SocialEntityFacade socialEntityFacade = new SocialEntityFacade(em);
		if (em.getTransaction().isActive()) {
			user = socialEntityFacade.getSocialEntity(id);
		} else {
			em.getTransaction().begin();
			user = socialEntityFacade.getSocialEntity(id);
			em.getTransaction().commit();
		}
		request.getSession().setAttribute("user", user);
		refreshGroupInSession(user, request, em);
		return user;
	}

	public static final void refreshGroupInSession(SocialEntity user,
			HttpServletRequest request, EntityManager em) {

		SocialGroupFacade socialGroupFacade = new SocialGroupFacade(em);
		SocialEntityFacade socialEntityFacade = new SocialEntityFacade(em);
		SocialGroup tmp = socialEntityFacade.findByEmail(user.getEmail())
				.getGroup();
		int parentGroupId=0;
		
		request.getSession().setAttribute("groupTree", tmp);
		request.getSession().setAttribute("hisGroup", tmp);
		request.getSession().setAttribute("isMasterGroup",
				socialGroupFacade.isMasterGroup(user));
		while(tmp!=null)
		{	
			parentGroupId=tmp.getId();
			tmp=tmp.getGroup();			
		}
		request.getSession().setAttribute("parentGroupId", parentGroupId);
	}
	
}
