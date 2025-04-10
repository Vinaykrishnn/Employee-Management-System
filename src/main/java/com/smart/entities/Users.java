package com.smart.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "v_users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "key_seq")
	@SequenceGenerator(name = "key_seq", sequenceName = "key_sequence", allocationSize = 1, initialValue = 1000)
	@Column(name = "v_user_id")
	private Long id;

	@Column(name = "v_user_name")
	private String name;

	// @NotBlank(message ="Please enter your email")
	// @Pattern(
	// regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
	// message = "Please enter a valid email address"
	// )
	@Column(name = "v_user_email", unique = true)
	private String email;

	// @NotBlank(message ="Please enter your password")
	// @Pattern(
	// regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
	// message = "Password must be at least 8 characters long, contain at least one
	// digit, one lowercase letter, one uppercase letter, one special character, and
	// no spaces."
	// )
	@Column(name = "v_user_password")
	private String password;

	@Column(name = "v_user_role")
	private String role;

	@Column(name = "v_user_status")
	private boolean enable;

	@Column(name = "v_user_image")
	private String imageUrl;

	@Column(name = "v_user_about", length = 500)
	private String about;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contacts> contacts = new ArrayList<>();

	public List<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public int hashCode() {
		return Objects.hash(about, email, enable, id, imageUrl, name, password, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		return Objects.equals(about, other.about) && Objects.equals(email, other.email) && enable == other.enable
				&& Objects.equals(id, other.id) && Objects.equals(imageUrl, other.imageUrl)
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(role, other.role);
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + ", enable=" + enable + ", imageUrl=" + imageUrl + ", about=" + about + "]";
	}

	public Users(Long id, String name, String email, String password, String role, boolean enable, String imageUrl,
			String about) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enable = enable;
		this.imageUrl = imageUrl;
		this.about = about;
	}

	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

}
