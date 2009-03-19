<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://admin.ili.fsnet.com/" prefix="admin"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>    
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<html>
<head>
<link rel="icon" type="image/png" href="images/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="refresh"
	content="300;AddInterest.jsp?interet=current&showHide=hide&deploy=[%2B]&titleDeploy=D%E9ployer la liste">
<meta name="author" content="Luka Cvrk - www.solucija.com" />
<meta name="description" content="Site Description" />
<meta name="keywords" content="site, keywords" />
<meta name="robots" content="index, follow" />
<link rel="stylesheet" type="text/css" media="screen"
	href="css/style.css" />
<title>FSNet</title>
<script language="JavaScript" src="admin.js">
</script>
<script type="text/javascript">
</script>
</head>
<body onload="showMenu();${param.showHide}('listToDeploy');${param.recherche}('rechercheVide')">
<jsp:include page="header.jsp"></jsp:include>


<div class="wrap background"><jsp:include page="subHeader.jsp"></jsp:include>

<div id="left">
<h2><a
	href="options.jsp=current"
	title="configuration des options d'envoie de mail">Options</a></h2>
<jsp:include page="date.jsp"></jsp:include></div>


<html:javascript formName="/lesoptions"/>
<div id="tableauprincipal">
<p id="informationsOptions">Nb:Ce formulaire permet de configurer vos préférences pour l'envoie de mails. Par exemple: quand vous enregistrer un membre un mail lui ait automatiquement envoyé afin qu'il puisse finaliser son inscription.</p>
<html:form action="/lesoptions.do" method="post">
	<div id="options">
	<ul>
		<li><label for="serveurSMTP">serveur SMTP : </label></li>
		<li><label for="hote">Hôte : </label></li>
		<li><label for="pass">Mot de passe : </label></li>
		<li><label for="adresseFSNet">Adresse site FSNet : </label></li>
		<li><label for="port">Port : </label></li>
	</ul>
	</div>
	
	<div>
	<html:text property="serveursmtp" errorStyleClass="error" styleId="serveurSMTP" />
	<html:errors property="serveursmtp" /><br />
		
	<html:text property="hote" errorStyleClass="error" styleId="hote"/>
	<html:errors property="hote" /><br />
	
	<html:text property="motdepasse" errorStyleClass="error" styleId="pass"/>
	<html:errors property="motdepasse" /><br />
		
	<html:text property="adressefsnet" errorStyleClass="error" styleId="adresseFSNet"/>
	<html:errors property="adressefsnet" /><br />
	
	<html:text property="port" errorStyleClass="error" styleId="port"/>
	<html:errors property="port" /><br />
	
</div>
	<html:submit title="Enregistrer">Enregistrer</html:submit>
	
	
</html:form>
</div>
<div id="side"></div>
</div>


<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>