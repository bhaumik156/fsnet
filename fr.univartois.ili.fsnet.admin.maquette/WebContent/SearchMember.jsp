<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" type="image/png" href="images/favicon.ico" />
<meta http-equiv="content-type"
	content="application/xhtml+xml; charset=UTF-8" />
<meta name="author" content="Luka Cvrk - www.solucija.com" />
<meta name="description" content="Site Description" />
<meta name="keywords" content="site, keywords" />
<meta name="robots" content="index, follow" />
<link rel="stylesheet" type="text/css" media="screen"
	href="css/style.css" />
<title>FSNet</title>
<script language="JavaScript" src="admin.js">
</script>
</head>
<body onload="show();">
<div class="wrap background">
<div id="search">
<form action="">
<fieldset><input type="text" class="field" value="Mot clé" />
<input type="submit" class="button" value="" /></fieldset>
</form>
</div>
<div id="menu">
<dl>
	<dt><a href="BureauAdmin.html" title="Retour à l'accueil">Accueil</a></dt>
</dl>

<dl>
	<dt onmouseover="show('smenu1');" onmouseout="show();"><a
		class="current" href="#">Membres</a></dt>
	<dd id="smenu1">
	<ul>
		<li onmouseover="show('smenu1');" onmouseout="show();"><a
			href="AddUser.jsp">Ajouter un membre</a></li>
		<li onmouseover="show('smenu1');" onmouseout="show();"><a
			href="SearchMember.jsp">Rechercher un membre</a></li>
	</ul>
	</dd>
</dl>
<dl>
	<dt onmouseover="show('smenu2');" onmouseout="show();"><a href="#">Intérêts</a></dt>
	<dd id="smenu2">
	<ul>
		<li onmouseover="show('smenu2');" onmouseout="show();"><a
			href="AddInterest.jsp">Ajouter des intérêts</a></li>
	</ul>
	</dd>
</dl>
<dl>
	<dt><a href="#">Communautés</a></dt>
</dl>
<dl>
	<dt><a href="#">Interactions</a></dt>
</dl>
<dl>
	<dt><a href="#">Demande Insc (3)</a></dt>
</dl>
<dl>
	<dt><a href="#">Messagerie (3)</a></dt>
</dl>
<dl>
	<dt><a href="#">Rapport d'activités</a></dt>
</dl>
</div>

<div id="logo">
<h1><a href="BureauAdmin.html">FSNet<br />
</a></h1>
<h2 class="slogan">Réseau social</h2>
<h2 class="slogan">Administration</h2>
</div>

<div id="features">
<ul id="feature_menu">
	<li><a class="current" href="">Actualité</a></li>
	<li><a href="#">AAAAAAA</a></li>
	<li><a href="#">BBBBBBBBB</a></li>
</ul>

<div id="feature"><img src="images/feature_img.gif" alt="Featured" />
<p>Une nouvelle communauté vient d'être créée.</p>
<p><a class="more" href="#">&not;Detail</a></p>
</div>
</div>

<div class="clear"></div>

<div id="left">
<h2><a href="SearchMember.jsp">Rechercher un membre</a></h2>
<p class="date">Date<br />
JJ-MM-AA</p>
</div>
<div id="tableauprincipal">
<table style="width: 90%">
	<tr>
		<td height="2"></td>
		<td></td>
		<td></td>
	</tr>

	<tr>
		<td valign="top" style="background-color: #EDF3F8">
		<h2 class="Style8">Rechercher Membre(s)</h2>
		</td>
		<td style="height: 38"></td>
	</tr>
	<tr>
		<td></td>
		<td style="height: 2"></td>
	</tr>

	<tr>
		<td valign="top">
		<form id="form1" method="post" action="">

		<div style="text-align: right">Date d'entrée <input type="text"
			name="textfield" /> <br />
		<br />
		Nom : <input type="text" name="textfield2" /> <br />
		<br />
		Prénom : <input type="text" name="textfield3" /> <br />
		<br />
		<input type="submit" name="Submit" value="Rechercher" /></div>

		</form>
		</td>
		<td style="height: 38"></td>
	</tr>
	<tr>
		<td></td>
		<td style="height: 2"></td>
	</tr>
	<tr>
		<td valign="top" style="background-color: #EDF3F8FFFFF">
		<h2 class="Style8">R&eacute;sultat de la Recherche</h2>
		</td>
		<td style="height: 63"></td>
	</tr>
	<tr>
		<td>
		<div style="text-align: center"></div>
		</td>
		<td style="height: 2"></td>
	</tr>
	<tr>
		<td valign="top">
		<div style="text-align: center">
		<table style="width: 100%; border: 2">
			<tr style="background-color: #CCCCCC">
				<td style="width: 64">
				<h4>Nom</h4>
				</td>
				<td style="width: 84">
				<h4>Pr&eacute;nom</h4>
				</td>
				<td style="width: 106">
				<h4>Date d'entr&eacute;e</h4>
				</td>
				<td style="width: 112">
				<h4>Afficher D&eacute;tails</h4>
				</td>
				<td style="width: 98">
				<h4>Supprimer</h4>
				</td>
			</tr>
			<tr>
				<td>Caramba</td>
				<td>Simpson</td>
				<td>10/02/2009</td>
				<td><a href="#">Détails</a></td>
				<td><a href="#">Supprimer</a></td>
			</tr>
			<tr>
				<td>Speedy</td>
				<td>Gonzalez</td>
				<td>12/01/2009</td>
				<td><a href="#">Détails</a></td>
				<td><a href="#">Supprimer</a></td>
			</tr>
		</table>
		</div>
		</td>
		<td style="height: 40"></td>
	</tr>
</table>

</div>

<div id="side">
<div class="boxtop"></div>
<div class="box">
<h3>Mes communaut&eacute;s</h3>
<a href="#"> <span class="item"> <span class="sidedate">JEE<br />
&nbsp;&nbsp;&nbsp;&nbsp;</span> <strong>Nouveauté J2EE </strong><br />
Detail</span> </a> <a href="#"> <span class="item"> <span
	class="sidedate">JAVA&nbsp;&nbsp;&nbsp;&nbsp;</span> <strong>Eclipse
... </strong><br />
Detail </span> </a> <a href="#"> <span class="item last"> <span
	class="sidedate">JSP<br />
</span> <strong>Nouveauté JSP </strong><br />
Detail</span> </a></div>
<div class="boxbottom"></div>
</div>
<p id="ad">&nbsp;</p>
</div>

<div id="promo" style="text-align: center">
<div class="wrap">FSnet licence</div>
</div>
</body>
</html>