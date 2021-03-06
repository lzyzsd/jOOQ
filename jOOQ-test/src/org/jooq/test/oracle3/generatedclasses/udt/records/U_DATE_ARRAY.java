/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle3.generatedclasses.udt.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class U_DATE_ARRAY extends org.jooq.impl.ArrayRecordImpl<java.sql.Date> implements java.lang.Cloneable {

	private static final long serialVersionUID = -1536762022;

	/**
	 * Create a new <code>U_DATE_ARRAY</code> record
	 */
	public U_DATE_ARRAY(org.jooq.Configuration configuration) {
		super(org.jooq.test.oracle3.generatedclasses.DefaultSchema.DEFAULT_SCHEMA, "U_DATE_ARRAY", org.jooq.impl.SQLDataType.DATE, configuration);
	}

	/**
	 * Create a new <code>U_DATE_ARRAY</code> record
	 */
	public U_DATE_ARRAY(org.jooq.Configuration configuration, java.sql.Date... array) {
		this(configuration);
		set(array);
	}

	/**
	 * Create a new <code>U_DATE_ARRAY</code> record
	 */
	public U_DATE_ARRAY(org.jooq.Configuration configuration, java.util.List<? extends java.sql.Date> list) {
		this(configuration);
		setList(list);
	}
}
