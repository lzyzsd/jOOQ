/**
 * This class is generated by jOOQ
 */
package org.jooq.test.mysql2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 *
 * VIEW
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
@javax.persistence.Entity
@javax.persistence.Table(name = "v_author", schema = "test2")
public class VAuthor implements java.io.Serializable {

	private static final long serialVersionUID = 908768719;

	private java.lang.Integer id;
	private java.lang.String  firstName;
	private java.lang.String  lastName;
	private java.sql.Date     dateOfBirth;
	private java.lang.Integer yearOfBirth;
	private java.lang.String  address;

	@javax.persistence.Column(name = "ID", nullable = false, precision = 10)
	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "FIRST_NAME", length = 50)
	public java.lang.String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	@javax.persistence.Column(name = "LAST_NAME", nullable = false, length = 50)
	public java.lang.String getLastName() {
		return this.lastName;
	}

	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	@javax.persistence.Column(name = "DATE_OF_BIRTH")
	public java.sql.Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(java.sql.Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@javax.persistence.Column(name = "YEAR_OF_BIRTH", precision = 10)
	public java.lang.Integer getYearOfBirth() {
		return this.yearOfBirth;
	}

	public void setYearOfBirth(java.lang.Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	@javax.persistence.Column(name = "ADDRESS", length = 200)
	public java.lang.String getAddress() {
		return this.address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}
}
