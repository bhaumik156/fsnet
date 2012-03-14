<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="../../WEB-INF/ili.tld" prefix="ili"%>

<c:choose>
	<c:when test="${not empty requestScope.interest}">
		<h2>
			${requestScope.interest.name}
			<c:choose>
				<c:when test="${requestScope.own}">
					<html:link action="/DeleteInterestFromInterestInformations">
						<html:param name="removedInterestId"
							value="${requestScope.interest.id}" />
						<img src="images/mini-delete.png" alt="delete" />
					</html:link>
				</c:when>
				<c:otherwise>
					<html:link action="/AddInterestFromInterestInformations">
						<img src="images/add.png" alt="add" />
						<html:param name="addedInterestId"
							value="${requestScope.interest.id}" />
					</html:link>
				</c:otherwise>
			</c:choose>
		</h2>

		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="interests.title.parent" />
			</legend>

			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.interest.parentInterest}">
								<div class="cloud">
									<div>
										<span class="tag"> <html:link
												action="/InterestInformations">
												<html:param name="infoInterestId"
													value="${interest.parentInterest.id}" />
											${interest.parentInterest.name}
							</html:link>
										</span>
									</div>
								</div>
								<div class="clear"></div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>


		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="interests.title.chidren" />
			</legend>

			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when
								test="${not empty requestScope.interest.childrenInterests}">
								<div class="cloud">
									<c:forEach var="interestChild"
										items="${requestScope.interest.childrenInterests}">
										<div>
											<span class="tag"><html:link
													action="/InterestInformations">
													<html:param name="infoInterestId"
														value="${interestChild.id}" />
								${interestChild.name}								
							</html:link></span>
										</div>
									</c:forEach>
								</div>
								<div class="clear"></div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>

		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="interests.title.entitySocial" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.interest.entities}">
								<div class="cloud">
									<c:forEach var="socialEntities"
										items="${requestScope.interest.entities}">
										<span class="tagSE"> <ili:getMiniature
												socialEntity="${socialEntities}" /> <ili:getSocialEntityInfos
												socialEntity="${socialEntities}" />
										</span>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>

		<div class="clear"></div>
		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="pageTitle.15" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.Community}">
								<div class="cloud">
									<c:forEach var="community" items="${requestScope.Community}">
										<div>
											<span class="tagInteraction"> <html:link
													action="/DisplayCommunity">
													<html:param name="communityId" value="${community.id}" />
											${community.title}								
							</html:link>
											</span>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>

		<div class="clear"></div>
		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="pageTitle.4" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.Hub}">
								<div class="cloud">
									<c:forEach var="hub" items="${requestScope.Hub}">
										<div>
											<span class="tagInteraction"> <html:link
													action="/DisplayHub">
													<html:param name="hubId" value="${hub.id}" />
											${hub.title}								
							</html:link>
											</span>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>

		<div class="clear"></div>
		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="pageTitle.6" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.Topic}">
								<div class="cloud">
									<c:forEach var="topic" items="${requestScope.Topic}">
										<div>
											<span class="tagInteraction"> <html:link
													action="/Topic">
													<html:param name="topicId" value="${topic.id}" />
											${topic.title}								
							</html:link>
											</span>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>
		<div class="clear"></div>

		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="pageTitle.1" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.Meeting}">
								<div class="cloud">
									<c:forEach var="meeting" items="${requestScope.Meeting}">
										<div>
											<span class="tagInteraction"> <html:link
													action="/DisplayEvent">
													<html:param name="eventId" value="${meeting.id}" />
											${meeting.title}								
							</html:link>
											</span>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>
		<div class="clear"></div>

		<fieldset class="fieldsetAdmin">
			<legend class="legendAdmin">
				<bean:message key="pageTitle.8" />
			</legend>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><c:choose>
							<c:when test="${not empty requestScope.Announcement}">
								<div class="cloud">
									<c:forEach var="announce" items="${requestScope.Announcement}">
										<div>
											<span class="tagInteraction"> <html:link
													action="/DisplayAnnounce">
													<html:param name="idAnnounce" value="${announce.id}" />
											${announce.title}								
							</html:link>
											</span>
										</div>
									</c:forEach>
								</div>
							</c:when>
							<c:otherwise>
								<bean:message key="interests.no" />
							</c:otherwise>
						</c:choose></td>
				</tr>
			</table>
		</fieldset>
	</c:when>
	<c:otherwise>
		<bean:message key="interests.noResult" />
	</c:otherwise>
</c:choose>
