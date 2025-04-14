package com.smart.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="v_contact")
public class Contacts {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_seq_generator")
	@SequenceGenerator(name = "custom_seq_generator", sequenceName = "custom_sequence", allocationSize = 1, initialValue = 100)
	@Column(name="v_contact_id")
	private int contactId;
	
	@Column(name="v_contact_name")
	private String name;
	
	@Column(name="v_contact_second_name")
	private String secondName;
	
	@Column(name="v_contact_work")
	private String work;
	
	@Column(name="v_contact_email")
	private String email;
	
	@Column(name="v_contact_phone")
	private String phone;
	
	@Column(name="v_contact_image", nullable = true)
	private String image;
	
	@Column(name="v_contact_description", length=500)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private Users user;

	@Transient
	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String  getImage() {
		return image;
	}

	public void setImage(String  image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contactId, description, email, image, name, phone, secondName, user, work);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contacts other = (Contacts) obj;
		return contactId == other.contactId && Objects.equals(description, other.description)
				&& Objects.equals(email, other.email) && Objects.equals(image, other.image)
				&& Objects.equals(name, other.name) && Objects.equals(phone, other.phone)
				&& Objects.equals(secondName, other.secondName) && Objects.equals(user, other.user)
				&& Objects.equals(work, other.work);
	}

	@Override
	public String toString() {
		return "Contacts [contactId=" + contactId + ", name=" + name + ", secondName=" + secondName + ", work=" + work
				+ ", email=" + email + ", phone=" + phone + ", image=" + image + ", description=" + description
				+ ", user=" + user + "]";
	}

	public Contacts(int contactId, String name, String secondName, String work, String email, String phone,
					String  image, String description, Users user) {
		super();
		this.contactId = contactId;
		this.name = name;
		this.secondName = secondName;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.description = description;
		this.user = user;
	}

	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
