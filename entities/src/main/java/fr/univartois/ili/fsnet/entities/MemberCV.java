package fr.univartois.ili.fsnet.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Author : Yoann VASSEUR
 * 
 *
 */

@Entity
public class MemberCV implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Id
	private long id;
	private String firstName;
	private String surname;
	private String mail;
	private String adress;	
	private int postCode;
	private String town;
	private String numberPhone;
	private Date birthDate;
	private String sex;
	private String situationFamilly;
	private Map<String,String> languages;

	public MemberCV() {

	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String theFirstName) {
		this.firstName = theFirstName;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname
	 *            the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * @param adress
	 *            the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

	/**
	 * @return the postCode
	 */
	public int getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode
	 *            the postCode to set
	 */
	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @return the numberPhone
	 */
	public String getNumberPhone() {
		return numberPhone;
	}

	/**
	 * @param numberPhone
	 *            the numberPhone to set
	 */
	public void setNumberPhone(String numberPhone) {
		this.numberPhone = numberPhone;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	
	/**
	 * @return the situationFamilly
	 */
	public String getSituationFamilly() {
		return situationFamilly;
	}

	/**
	 * @param situationFamilly
	 *            the situationFamilly to set
	 */
	public void setSituationFamilly(String situationFamilly) {
		this.situationFamilly = situationFamilly;
	}

	/**
	 * @return the languages
	 */
	public Map<String,String> getLanguages() {
		return languages;
	}

	/**
	 * @param languages
	 *            the languages to set
	 */
	public void setLanguages(Map<String,String> languages) {
		this.languages = languages;
	}
}
