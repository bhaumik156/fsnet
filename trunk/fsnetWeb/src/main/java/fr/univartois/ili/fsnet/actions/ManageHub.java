package fr.univartois.ili.fsnet.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.MappingDispatchAction;
import org.eclipse.persistence.exceptions.DatabaseException;

import fr.univartois.ili.fsnet.actions.utils.UserUtils;
import fr.univartois.ili.fsnet.commons.pagination.Paginator;
import fr.univartois.ili.fsnet.commons.utils.PersistenceProvider;
import fr.univartois.ili.fsnet.entities.Community;
import fr.univartois.ili.fsnet.entities.Hub;
import fr.univartois.ili.fsnet.entities.Interest;
import fr.univartois.ili.fsnet.entities.Message;
import fr.univartois.ili.fsnet.entities.SocialEntity;
import fr.univartois.ili.fsnet.entities.Topic;
import fr.univartois.ili.fsnet.entities.TopicMessage;
import fr.univartois.ili.fsnet.facade.CommunityFacade;
import fr.univartois.ili.fsnet.facade.HubFacade;
import fr.univartois.ili.fsnet.facade.InteractionFacade;
import fr.univartois.ili.fsnet.facade.InterestFacade;

/**
 * 
 * @author Cerelia Besnainou and Audrey Ruellan
 */
public class ManageHub extends MappingDispatchAction implements CrudAction {

	private static final Logger logger = Logger.getAnonymousLogger();

	@Override
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
		String hubName = (String) dynaForm.get("hubName");
		String communityId = (String) dynaForm.get("communityId");
		EntityManager em = PersistenceProvider.createEntityManager();
		Community community = em.find(Community.class, Integer.parseInt(communityId));
		HubFacade hubFacade = new HubFacade(em);
		boolean doesNotExists = false;
		try{
			hubFacade.getHubByName(hubName, community);

		} catch (NoResultException e){
			doesNotExists = true;
		}

		if (doesNotExists) {
			String InterestsIds[] = (String[]) dynaForm.get("selectedInterests");
			InterestFacade fac = new InterestFacade(em);
			List<Interest> interests = new ArrayList<Interest>();
			int currentId;
			for (currentId = 0; currentId < InterestsIds.length; currentId++) {
				interests.add(fac.getInterest(Integer.valueOf(InterestsIds[currentId])));
			}

			logger.info("new hub: " + hubName);
			SocialEntity user = UserUtils.getAuthenticatedUser(request, em);
			em.getTransaction().begin();
			Hub createdHub = hubFacade.createHub(community, user, hubName);

			InteractionFacade ifacade = new InteractionFacade(em);
			ifacade.addInterests(createdHub, interests);
			em.getTransaction().commit();
		} else {
			ActionErrors actionErrors = new ActionErrors();
			ActionMessage msg = new ActionMessage("hubs.alreadyExists");
			actionErrors.add("createdHubName", msg);
			saveErrors(request, actionErrors);
		}
		em.close();
		ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
		redirect.addParameter("communityId", communityId);
		return redirect;
	}

	@Override
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		System.out.println("ici 1");
		boolean doesNotExists = false;
		EntityManager em = PersistenceProvider.createEntityManager();
		DynaActionForm dynaForm = (DynaActionForm) form;// NOSONAR
		System.out.println("ici 2");

		int communityId = Integer.valueOf((String) dynaForm
				.get("communityId"));
		int hubId = Integer.valueOf((String) dynaForm
				.get("hubId"));
		String hubName= (String) dynaForm.get("modifiedHubName");
		HubFacade facade = new HubFacade(em);
		CommunityFacade facadeCommunity = new CommunityFacade(em);


		Community community = facadeCommunity.getCommunity(communityId);
		Hub myHub = facade.getHubById(hubId, community);


		System.out.println("avant test myHub");

		if (myHub != null) {
			logger.info("hub modification: " + myHub.getTitle());

			try{
				facade.getHubByName(hubName, community);	
			} catch (NoResultException e){
				doesNotExists = true;
			}

			if (doesNotExists) {	
				try {
					em.getTransaction().begin();
					System.out.println("transaction");
					facade.modifyName(hubName, myHub);
					em.getTransaction().commit();
				} catch (DatabaseException ex) {
					ActionErrors actionErrors = new ActionErrors();
					ActionMessage msg = new ActionMessage("hubs.alreadyExists");
					actionErrors.add("hubAlreadyExistsErrors", msg);
					saveErrors(request, actionErrors);
				}
			}else {
					ActionErrors actionErrors = new ActionErrors();
					ActionMessage msg = new ActionMessage("hubs.alreadyExists");
					actionErrors.add("hubAlreadyExistsErrors", msg);
					saveErrors(request, actionErrors);
				}
			}
			System.out.println("apres test myHub");

			em.close();
			dynaForm.set("modifiedHubName", "");		
			return mapping.findForward("success");
		}

		@Override
		public ActionForward delete(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
			DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
			int hubId = Integer.parseInt((String) dynaForm.get("hubId"));
			int communityId = Integer.valueOf((String) dynaForm.get("communityId"));

			logger.info("delete hub: " + hubId);

			EntityManager em = PersistenceProvider.createEntityManager();
			SocialEntity user = UserUtils.getAuthenticatedUser(request, em);
			HubFacade hubFacade = new HubFacade(em);
			CommunityFacade communityFacade = new CommunityFacade(em);
			InteractionFacade interactionFacade = new InteractionFacade(em);
			em.getTransaction().begin();

			Hub hub = hubFacade.getHub(hubId);
			Community community = communityFacade.getCommunity(communityId);
			community.getHubs().remove(hub);
			for(SocialEntity se : hub.getFollowingEntitys()){
				se.getFavoriteInteractions().remove(hub);
			}
			interactionFacade.deleteInteraction(user, hub);
			em.getTransaction().commit();
			em.close();
			return mapping.findForward("success");
		}

		@Override
		public ActionForward search(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {


			DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
			String communityId = (String) dynaForm.get("communityId");
			String hubName = (String) dynaForm.get("searchText");

			if (hubName == null) {
				hubName = "";
			} 
			logger.info("search hub: " + hubName);
			EntityManager em = PersistenceProvider.createEntityManager();
			Community community = em.find(Community.class, Integer.parseInt(communityId));
			HubFacade hubFacade = new HubFacade(em);
			em.getTransaction().begin();
			List<Hub> result = hubFacade.searchHub(hubName,community);
			em.getTransaction().commit();
			em.close();
			Paginator<Hub> paginator = new Paginator<Hub>(result, request, "hubList", "communityId");

			request.setAttribute("listHubPaginator", paginator);
			request.setAttribute("Community", community);
			request.setAttribute("hubResults", result);
			return mapping.findForward("success");
		}

		@Override
		public ActionForward display(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
			DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
			String hubId = (String) dynaForm.get("hubId");
			logger.info("display hub: " + hubId);
			Map<Topic, Message> topicsLastMessage = new HashMap<Topic, Message>();
			EntityManager em = PersistenceProvider.createEntityManager();
			em.getTransaction().begin();
			HubFacade hubFacade = new HubFacade(em);
			Hub result = hubFacade.getHub(Integer.parseInt(hubId));

			for (Topic t : result.getTopics()) {
				List<TopicMessage> messages = t.getMessages();
				Message lastMessage = null;
				if (messages.size() > 0) {
					lastMessage = messages.get(messages.size() - 1);
				}
				topicsLastMessage.put(t, lastMessage);
			}
			em.getTransaction().commit();
			em.close();

			// TODO modify paginator for accepting HasMap

			request.setAttribute("hubResult", result);
			request.setAttribute("topicsLastMessage", topicsLastMessage);
			return mapping.findForward("success");
		}

		public ActionForward getAllInterest(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
			EntityManager em = PersistenceProvider.createEntityManager();
			InterestFacade fac = new InterestFacade(em);
			List<Interest> listInterests = fac.getInterests();
			request.setAttribute("Interests", listInterests);
			em.close();
			return mapping.findForward("success");
		}

		public ActionForward searchYourHubs(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
			//TODO use facade
			DynaActionForm dynaForm = (DynaActionForm) form; // NOSONAR
			String pattern = (String) dynaForm.get("hubName");
			String communityId = (String) dynaForm.get("communityId");

			if (pattern==null) {
				pattern = "";
			}

			logger.info("search hub: " + pattern);

			EntityManager em = PersistenceProvider.createEntityManager();
			Community community = em.find(Community.class, Integer.parseInt(communityId));
			SocialEntity creator = UserUtils.getAuthenticatedUser(request, em);

			em.getTransaction().begin();
			List<Hub> hubs = em.createQuery("SELECT hub FROM Hub hub WHERE hub.title LIKE :hubName AND hub.community = :com AND hub.creator = :creator",
					Hub.class).setParameter("hubName", "%" + pattern + "%").setParameter("com", community).setParameter("creator", creator).getResultList();

			em.getTransaction().commit();
			em.close();
			request.setAttribute("hubResults", hubs);

			return mapping.findForward("success");
		}



	}
