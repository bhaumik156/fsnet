package fr.univartois.ili.fsnet.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import fr.univartois.ili.fsnet.entities.SocialEntity;

public class GetMiniatureTag extends SimpleTagSupport {

	private SocialEntity socialEntity;

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		if (socialEntity.getIsEnabled()) {
			out.print("<a href=\"DisplayProfile.do?id=");
			out.print(socialEntity.getId());
			out.print("\" class=\"miniature\">");
		}
		else {
			out.print("<a class=\"miniature\">");
		}
		out.print("<img src=\"avatar/");
		out.print(socialEntity.getId());
		out.print(".png\" ");
                out.print("title='"+socialEntity.getFirstName()+" " + socialEntity.getName()+"' alt=\"Avatar\"></img>");
		
		
        out.print("</a>");
		
	}

	public SocialEntity getSocialEntity() {
		return socialEntity;
	}

	public void setSocialEntity(SocialEntity socialEntity) {
		this.socialEntity = socialEntity;
	}

}
