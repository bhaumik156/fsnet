package fr.univartois.ili.fsnet.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Hub extends Communaute {
	
	@Temporal(TemporalType.DATE)
	private Date dateCreation;
	@OneToMany(mappedBy="hub")
	private List<Topic> lesTopics;
	
	
	
	public Hub() {
	}

	public Hub(String nomCommunaute, Date dateCreation, List<Topic> lesTopics) {
		super(nomCommunaute);
		this.dateCreation = dateCreation;
		this.lesTopics = lesTopics;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public List<Topic> getLesTopics() {
		return lesTopics;
	}

	public void setLesTopics(List<Topic> lesTopics) {
		this.lesTopics = lesTopics;
	}



}
