<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<link rel=stylesheet type="text/css" href="css/osx.css" />
<!-- <script type="text/javascript" src="js/jquery.js"></script>-->
<script type="text/javascript" src="js/jquery.simplemodal.js"></script>
<script type="text/javascript" src="js/osx.js"></script>

<fieldset class="fieldsetAdmin">
	<legend class="legendAdmin">
		<bean:message key="communities.search" />
	</legend>
	<html:form action="SearchCommunity">
		<div>
			<table class="inLineTable  fieldsetTableAdmin">
				<tr>
					<td><html:text property="searchText" /> <html:submit
							styleClass="button">
							<bean:message key="communities.searchButton" />
						</html:submit></td>
				</tr>
			</table>
		</div>
	</html:form>
</fieldset>
<br />
<fieldset class="fieldsetAdmin">
	<legend class="legendAdmin">
		<bean:message key="communities.listCommunities" />
	</legend>

	<c:choose>
		<c:when test="${not empty requestScope.communitiesList}">
			<script type="text/javascript">
			$(document).ready(
					function pagination() {
						var nomTable = "communitiesTable";
						var idColonneATrier = 0;
						var sensDeTri = "asc";
						var aoColumns = [ null,{
							"bSortable" : false
						}, null, null,{
							"bSortable" : false
						}];
						miseEnPageTable(nomTable, idColonneATrier, sensDeTri,
								aoColumns, false);
					});
		</script>
			<table id="communitiesTable"
				class="tablesorter inLineTable fieldsetTableAdmin">
				<thead>
					<tr>
						<th><bean:message key="tableheader.communityname" /></th>
						<th><bean:message key="tableheader.by" /></th>
						<th><bean:message key="members.firstName" /></th>
						<th><bean:message key="members.name" /></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="community" items="${requestScope.communitiesList}">
						<tr class="content">
							<td>${community.title}</td>
							<td></td>
							<td><html:link action="/DisplayMember">
									<html:param name="idMember" value="${community.creator.id}" />
	                    ${community.creator.firstName}
	                </html:link></td>
							<td><html:link action="/DisplayMember">
									<html:param name="idMember" value="${community.creator.id}" />
	                    ${community.creator.name}
	                </html:link></td>
							<td class="tableButton"><a class="button"
								onclick="confirmDelete('DeleteCommunity.do?communityId='+${community.id})">
									<bean:message key="communities.delete" />
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<table class="inLineTable fieldsetTableAdmin">
				<tr>
					<td><bean:message key="communities.noResult" /></td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
</fieldset>
<c:if test="${!empty success}">
	<script type="text/javascript">
		jQuery(function () { popup(); });
		success = null;
	</script>
	<div id="osx-modal-content" class="simplemodal-data">
		<div id="osx-modal-title">Message</div>
		<div class="close">
			<a class="simplemodal-close" href="#">X</a>
		</div>
		<div id="osx-modal-data">
			<p>
				<c:out value="${success}" />
			</p>
		</div>
	</div>
</c:if>
