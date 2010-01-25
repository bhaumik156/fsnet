package fr.univartois.ili.fsnet.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * The class Message.
 * 
 */
@Entity
public class Message implements Serializable {

    /**
     * The identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    /**
     * The content of the message.
     */
    @Column(nullable = false, length = 5000)
    private String body;
    /**
     * The date of creation of the message.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;
    /**
     * The author of the message.
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    private SocialEntity from;

    public Message() {
    }

    /**
     *
     * @param body the body of the message
     * @param topic the topic where is this message
     * @param from the author of the message
     */
    public Message(String body, Topic topic, SocialEntity from) {
        if (body == null || topic == null || from == null) {
            throw new IllegalArgumentException();
        }
        this.creationDate = new Date();
        this.body = body;
        this.from = from;
    }

    /**
     *
     * @return the identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Gives an identifier to the Message.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the content of the message.
     */
    public String getBody() {
        return body;
    }

    /**
     * Gives a content of the message.
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return the date of creation of the message.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Gives a date of creation to the message.
     *
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     *
     * @return the creator of the message.
     */
    public SocialEntity getFrom() {
        return from;
    }

    /**
     * Gives a creator to the message.
     *
     * @param from
     */
    public void setPropMsg(SocialEntity from) {
        this.from = from;
    }
}
