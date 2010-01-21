/*
 *  Copyright (C) 2010 Matthieu Proucelle <matthieu.proucelle at gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.univartois.ili.fsnet.actions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.MappingDispatchAction;

import fr.univartois.ili.fsnet.auth.Authenticate;
import fr.univartois.ili.fsnet.entities.EntiteSociale;
import fr.univartois.ili.fsnet.entities.Hub;
import fr.univartois.ili.fsnet.entities.Message;
import fr.univartois.ili.fsnet.entities.Topic;

/**
 *
 * @author Zhu Rui <zrhurey at gmail.com>
 */
public class ManageTopic extends MappingDispatchAction implements CrudAction {

	private static EntityManagerFactory factory = Persistence
	.createEntityManagerFactory("fsnetjpa");

	private static final Logger logger = Logger.getAnonymousLogger();
	
    @Override
    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	EntityManager em = factory.createEntityManager();
		DynaActionForm dynaForm = (DynaActionForm) form;
		String topicSujet = (String) dynaForm.get("topicSujet");
		Date date = new Date();
		EntiteSociale entiteSociale = (EntiteSociale) request.getSession().getAttribute(Authenticate.AUTHENTICATED_USER);
		Topic topic = new Topic(topicSujet,date,null,null,entiteSociale);
        em.getTransaction().begin();
        em.persist(topic);
	    em.getTransaction().commit();
	    em.close();
	    System.out.println("----------Create topic "+topicSujet+" OK------------");
		return mapping.findForward("success");
    }

    @Override
    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	EntityManager em = factory.createEntityManager();
		DynaActionForm dynaForm = (DynaActionForm) form;
		Topic topic = null;
		Hub hub = null;
		String hubNom = null;
		if (request.getParameterMap().containsKey("find")) {
			int topicId = Integer.valueOf(request.getParameter("topicId"));
			topic = em.find(Topic.class, topicId);
			request.getSession().setAttribute("topicFind", topic);
			request.getSession().setAttribute("creatorTopic", topic.getPropTopic());
			request.getSession().setAttribute("hubTopic", topic.getHub());
			request.getSession().setAttribute("messageTopic", topic.getLesMessages());
			List<Hub> hubs = null;
	    	hubs = em.createQuery("SELECT OBJECT(hub) FROM Hub hub order by hub.nomCommunaute").getResultList();
	    	request.getSession().setAttribute("listHubs", hubs);
		}
		if (request.getParameterMap().containsKey("update")) {
			System.out.println("-----------prepare to update--------------");
			int topicIdUpdate = Integer.valueOf(request.getParameter("topicId"));
			hubNom = request.getParameter("hubNom");
			/**
			 * toto
			 * il faut trouver l'identification de Hub
			 * non resolu pour l'instant
			 */
			/*
			hub = em.find(Hub.class, hubNom);
			em.getTransaction().begin();
	        Query query = em.createQuery("UPDATE Topic SET hub = :hub WHERE id = :topicId");
	    		query.setParameter("hub",hub);		        	
		    	query.setParameter("topicId",topicIdUpdate);
		    	query.executeUpdate();
	        em.getTransaction().commit();
	        System.out.println("-----------update Ok--------------");
	        em.close();
	        */
		}
		return mapping.findForward("success");
		
    }

    @Override
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	EntityManager em = factory.createEntityManager();
    	if (request.getParameterMap().containsKey("topicId")) {
			int topicId = Integer.valueOf(request.getParameter("topicId"));
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Topic topic WHERE topic.id = :topicId");
            	query.setParameter("topicId", topicId);
            	query.executeUpdate();
            em.getTransaction().commit();
            System.out.println("----------Delete topic "+topicId+" OK------------");
	        em.close();
		}
		
		return mapping.findForward("success");
    }

    @Override
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	EntityManager em = factory.createEntityManager();
		DynaActionForm dynaForm = (DynaActionForm) form;
		String topicSujet = (String) dynaForm.get("topicSujetSearch");
        Query query = em.createQuery("SELECT OBJECT(topic) FROM Topic topic WHERE topic.sujet LIKE :sujetRea ");
        	query.setParameter("sujetRea", "%"+topicSujet+"%");
        List<Topic> result = query.getResultList();
        request.setAttribute("resRearchTopics", result);
		return mapping.findForward("success");
    }

    @Override
    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	EntityManager em = factory.createEntityManager();
    	EntiteSociale entiteSociale = (EntiteSociale) request.getSession().getAttribute(Authenticate.AUTHENTICATED_USER);
    	List<Topic> result = null;
    	Query query = em.createQuery("SELECT OBJECT(topic) FROM Topic topic WHERE topic.propTopic = :es order by topic.sujet");
    		query.setParameter("es",entiteSociale);
    	result = query.getResultList();
        request.getSession().setAttribute("listTopics", result);
    	return mapping.findForward("success");
    }
}
