/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.udt.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class UAuthorTypeRecord extends org.jooq.impl.UDTRecordImpl<org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord> {

	private static final long serialVersionUID = 1930939886;


	/**
	 * Setter for <code>TEST.U_AUTHOR_TYPE.ID</code>. 
	 */
	public void setId(java.lang.Integer value) {
		setValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.ID, value);
	}

	/**
	 * Getter for <code>TEST.U_AUTHOR_TYPE.ID</code>. 
	 */
	public java.lang.Integer getId() {
		return getValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.ID);
	}

	/**
	 * Setter for <code>TEST.U_AUTHOR_TYPE.FIRST_NAME</code>. 
	 */
	public void setFirstName(java.lang.String value) {
		setValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.FIRST_NAME, value);
	}

	/**
	 * Getter for <code>TEST.U_AUTHOR_TYPE.FIRST_NAME</code>. 
	 */
	public java.lang.String getFirstName() {
		return getValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.FIRST_NAME);
	}

	/**
	 * Setter for <code>TEST.U_AUTHOR_TYPE.LAST_NAME</code>. 
	 */
	public void setLastName(java.lang.String value) {
		setValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.LAST_NAME, value);
	}

	/**
	 * Getter for <code>TEST.U_AUTHOR_TYPE.LAST_NAME</code>. 
	 */
	public java.lang.String getLastName() {
		return getValue(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.LAST_NAME);
	}

	/**
	 * Call <code>TEST.U_AUTHOR_TYPE.COUNT_BOOKS</code>
	 */
	public java.math.BigDecimal countBooks() {
		org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.CountBooks f = new org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.CountBooks();
		f.setSelf(this);

		f.execute(getConfiguration());
		return f.getReturnValue();
	}

	/**
	 * Call <code>TEST.U_AUTHOR_TYPE.GET_AUTHOR</code>
	 */
	public static org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord getAuthor(org.jooq.Configuration configuration, java.lang.Number pId) {
		org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetAuthor f = new org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetAuthor();
		f.setPId(pId);

		f.execute(configuration);
		return f.getReturnValue();
	}

	/**
	 * Call <code>TEST.U_AUTHOR_TYPE.GET_BOOKS</code>
	 */
	public org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetBooks getBooks() {
		org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetBooks p = new org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.GetBooks();
		p.setSelf(this);

		p.execute(getConfiguration());
		from(p.getSelf());
		return p;
	}

	/**
	 * Call <code>TEST.U_AUTHOR_TYPE.LOAD</code>
	 */
	public org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord load() {
		org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.Load p = new org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.Load();
		p.setSelf(this);

		p.execute(getConfiguration());
		from(p.getSelf());
		return p.getSelf();
	}

	/**
	 * Call <code>TEST.U_AUTHOR_TYPE.NEW_AUTHOR</code>
	 */
	public static org.jooq.test.oracle.generatedclasses.test.udt.records.UAuthorTypeRecord newAuthor(org.jooq.Configuration configuration, java.lang.Number pId, java.lang.String pFirstName, java.lang.String pLastName) {
		org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.NewAuthor p = new org.jooq.test.oracle.generatedclasses.test.udt.u_author_type.NewAuthor();
		p.setPId(pId);
		p.setPFirstName(pFirstName);
		p.setPLastName(pLastName);

		p.execute(configuration);
		return p.getPAuthor();
	}

	/**
	 * Create a new <code>TEST.U_AUTHOR_TYPE</code> record
	 */
	public UAuthorTypeRecord() {
		super(org.jooq.test.oracle.generatedclasses.test.udt.UAuthorType.U_AUTHOR_TYPE);
	}
}
