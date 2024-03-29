<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="../../WEB-INF/ili.tld" prefix="ili"%>




<fieldset class="fieldsetCadre">
	<legend>
		<bean:message key="visits.conts" />
	</legend>


	<c:if test="${empty requestScope.lastVisitors}">
		<table class="inLineTable tableStyle">
			<tr>
				<td><bean:message key="visits.voidlist" />.</td>
			</tr>
		</table>
	</c:if>


	<c:if test="${not empty requestScope.lastVisitors}">

		<script type="text/javascript">
			$(document).ready(
					function pagination() {
						var nomTable = "tablelastvisitors";
						var idColonneATrier = 3;
						var sensDeTri = "desc";
						var aoColumns = [ {
							"bSortable" : false
						}, null, null, {
							"sType" : "date-euro"
						} ];
						miseEnPageTable(nomTable, idColonneATrier, sensDeTri,
								aoColumns, false, 5);
					});
		</script>

		<table id="tablelastvisitors"
			class="tablesorter inLineTable tableStyle">
			<thead>
				<tr>
					<th><bean:message key="privatemessages.from" /></th>
					<th width="20%"><bean:message key="members.firstName" /></th>
					<th width="20%"><bean:message key="members.name" /></th>
					<th width="40%"><bean:message key="privatemessages.date" /></th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="visitor" items="${requestScope.lastVisitors}">
					<tr>
						<td class="miniatureContainer"><ili:getMiniature
								socialEntity="${visitor.visitor}" /></td>
						<td><ili:getSocialEntityInfosFirstname
								socialEntity="${visitor.visitor}" /></td>
						<td><ili:getSocialEntityInfosName
								socialEntity="${visitor.visitor}" /></td>
						<td><bean:write name="visitor" property="lastVisite"
								formatKey="date.format" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</fieldset>

<fieldset class="fieldsetCadre">
	<legend>
		<bean:message key="visits.old" />
	</legend>


	<c:if test="${empty requestScope.beforeLastVisitors}">
		<table class="inLineTable tableStyle">
			<tr>
				<td><bean:message key="visits.voidlist" />.</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${not empty requestScope.beforeLastVisitors}">

		<script type="text/javascript">
			$(document).ready(
					function pagination() {
						var nomTable = "tablelastvisitorsBeforeLastConnection";
						var idColonneATrier = 3;
						var sensDeTri = "desc";
						var aoColumns = [ {
							"bSortable" : false
						}, null, null, {
							"sType" : "date-euro"
						} ];
						miseEnPageTable(nomTable, idColonneATrier, sensDeTri,
								aoColumns, false, 5);
					});
		</script>

		<table id="tablelastvisitorsBeforeLastConnection"
			class="tablesorter inLineTable tableStyle">
			<thead>
				<tr>
					<th><bean:message key="privatemessages.from" /></th>
					<th width="20%"><bean:message key="members.firstName" /></th>
					<th width="20%"><bean:message key="members.name" /></th>
					<th width="40%"><bean:message key="privatemessages.date" /></th>
				</tr>
			</thead>
			<tbody>

				<c:forEach var="visitor" items="${requestScope.beforeLastVisitors}">
					<tr>
						<td class="miniatureContainer"><ili:getMiniature
								socialEntity="${visitor.visitor}" /></td>
						<td><ili:getSocialEntityInfosFirstname
								socialEntity="${visitor.visitor}" /></td>
						<td><ili:getSocialEntityInfosName
								socialEntity="${visitor.visitor}" /></td>
						<td><bean:write name="visitor" property="lastVisite"
								formatKey="date.format" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</fieldset>