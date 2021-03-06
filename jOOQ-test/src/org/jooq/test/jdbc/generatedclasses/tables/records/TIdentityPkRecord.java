/**
 * This class is generated by jOOQ
 */
package org.jooq.test.jdbc.generatedclasses.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings("all")
public class TIdentityPkRecord extends org.jooq.impl.TableRecordImpl<org.jooq.test.jdbc.generatedclasses.tables.records.TIdentityPkRecord> implements org.jooq.Record2<java.lang.Integer, java.lang.Integer>, org.jooq.test.jdbc.generatedclasses.tables.interfaces.ITIdentityPk {

	private static final long serialVersionUID = -900014437;

	/**
	 * Setter for <code>PUBLIC.T_IDENTITY_PK.ID</code>. 
	 */
	@Override
	public void setId(java.lang.Integer value) {
		setValue(org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.ID, value);
	}

	/**
	 * Getter for <code>PUBLIC.T_IDENTITY_PK.ID</code>. 
	 */
	@Override
	public java.lang.Integer getId() {
		return getValue(org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.ID);
	}

	/**
	 * Setter for <code>PUBLIC.T_IDENTITY_PK.VAL</code>. 
	 */
	@Override
	public void setVal(java.lang.Integer value) {
		setValue(org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.VAL, value);
	}

	/**
	 * Getter for <code>PUBLIC.T_IDENTITY_PK.VAL</code>. 
	 */
	@Override
	public java.lang.Integer getVal() {
		return getValue(org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.VAL);
	}

	// -------------------------------------------------------------------------
	// Foreign key navigation methods
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> fieldsRow() {
		return org.jooq.impl.DSL.row(field1(), field2());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Integer, java.lang.Integer> valuesRow() {
		return org.jooq.impl.DSL.row(value1(), value2());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.VAL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getVal();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TIdentityPkRecord
	 */
	public TIdentityPkRecord() {
		super(org.jooq.test.jdbc.generatedclasses.tables.TIdentityPk.T_IDENTITY_PK);
	}
}
