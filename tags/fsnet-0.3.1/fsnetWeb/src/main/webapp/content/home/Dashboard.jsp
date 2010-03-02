<%-- 
    Document   : dashboard
    Created on : 23 janv. 2010, 13:55:48
    Author     : Matthieu Proucelle <matthieu.proucelle at gmail.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="../../WEB-INF/ili.tld" prefix="ili"%>

<table id="dashboardMessages" class="inLineTableDashBoard homeFrame">
    <caption>Vos derniers messages</caption>
    <logic:empty name="messages">
        <tr>
            <td>
                No messages yet.
            </td>
        </tr>
    </logic:empty>
    <logic:notEmpty name="messages">
        <c:forEach items="${requestScope.messages}" var="message" begin="0" end="2">
            <c:if test="${not message.reed}">
                <tr class="notReed">
                    <td class="messagePhoto">
                        <img src="GetMiniature.do?memberId=${message.from.id}" />
                    </td>
                    <td style="width: 0%">
                        <html:link action="/DisplayMessage">
                            <html:param name="messageId" value="${message.id}" />
                            <span>
                                <ili:substring beginIndex="0" endIndex="20"><ili:noxml>${message.subject}</ili:noxml></ili:substring> :
                            </span>
                            <span style="color: gray">
                                <ili:substring beginIndex="0" endIndex="20"><ili:noxml>${message.body}</ili:noxml></ili:substring>
                            </span>
                        </html:link>
                    </td>
                </tr>
            </c:if>
            <c:if test="${message.reed}">
                <tr>
                    <td class="messagePhoto">
                        <html:link action="/DisplayProfile">
                            <html:param name="id" value="${message.from.id}"/>
                            <img src="GetMiniature.do?memberId=${message.from.id}"/>
                        </html:link>
                    </td>
                    <td>
                        <html:link action="/DisplayMessage">
                            <html:param name="messageId" value="${message.id}" />
                            <span>
                                <ili:substring beginIndex="0" endIndex="20"><ili:noxml>${message.subject}</ili:noxml></ili:substring> :
                            </span>
                            <span style="color: gray">
                                <ili:substring beginIndex="0" endIndex="20"><ili:noxml>${message.body}</ili:noxml></ili:substring>
                            </span>
                        </html:link>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
    </logic:notEmpty>
</table>
<table id="lastVisits" class="inLineTableDashBoard homeFrame">
    <caption><bean:message key="visite.last.title"/></caption>
    <logic:empty name="visitors">
        <tr>
            <td>
                No visites yet.
            </td>
        </tr>
    </logic:empty>
    <logic:notEmpty name="visitors">
        <c:forEach var="pv" items="${visitors}">
            <tr>
                <td class="messagePhoto">
                    <html:link action="/DisplayProfile">
                        <html:param name="id" value="${pv.visitor.id}"/>
                        <img src="GetMiniature.do?memberId=${pv.visitor.id}"/>
                    </html:link>
                </td>
                <td>
                    <html:link action="/DisplayProfile">
                        <html:param name="id" value="${pv.visitor.id}"/>
                        ${pv.visitor.firstName} ${pv.visitor.name}
                    </html:link>
                </td>
                <td>
                    <bean:write name="pv" property="lastVisite" formatKey="date.format" />
                </td>
            </tr>
        </c:forEach>

    </logic:notEmpty>
</table>

<div class="clear homeGap"></div>

<table id="lastInteractions" class="inLineTableDashBoard homeFrame">
    <caption><bean:message key="lastInteractions.title"/></caption>
    <logic:empty name="lastInteractions">
        <tr>
            <td>
                No new interactions.
            </td>
        </tr>
    </logic:empty>

    <logic:notEmpty name="lastInteractions">
        <logic:iterate id="interaction" collection="${requestScope.lastInteractions}">
            <tr>
                <td class="messagePhoto">
                    <html:link action="/DisplayProfile">
                        <html:param name="id" value="${interaction.key.creator.id}"/>
                        <img src="GetMiniature.do?memberId=${interaction.key.creator.id}"/>
                    </html:link>
                </td>
                <td>
                    <bean:message key="events.16"/>
                    <html:link action="/DisplayProfile">
                        <html:param name="id" value="${interaction.key.creator.id}"/>
                        ${interaction.key.creator.firstName} ${interaction.key.creator.name}
                    </html:link>
                </td>
                <td>
                    <html:link action="${interaction.value[1]}">
                        <html:param name="${interaction.value[2]}" value="${interaction.key.id}"/>
                        ${interaction.key.title}
                    </html:link>
                </td>
                <td class="tableButton">
                    <bean:define id="interkey" name="interaction" property="key"/>
                    <bean:write name="interkey" property="lastModified" format="dd/MM/yyyy" />
                </td>
            </tr>
        </logic:iterate>
    </logic:notEmpty>

</table>
