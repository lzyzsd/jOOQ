/**
 * This class is generated by jOOQ
 */
package org.jooq.test.hsqldb.generatedclasses.tables;


import java.sql.Date;

import org.jooq.TableField;
import org.jooq.impl.TableFieldImpl;
import org.jooq.impl.UpdatableTableImpl;
import org.jooq.test.hsqldb.generatedclasses.Public;
import org.jooq.test.hsqldb.generatedclasses.tables.records.TAuthorRecord;


/**
 * This class is generated by jOOQ.
 */
public class TAuthor extends UpdatableTableImpl {

	private static final long serialVersionUID = 1L;

	/**
	 * The singleton instance of T_AUTHOR
	 */
	public static final TAuthor T_AUTHOR = new TAuthor();

	/**
	 * The class holding records for this table
	 */
	private static final Class<TAuthorRecord> RECORD_TYPE = TAuthorRecord.class;

	/**
	 * The class holding records for this table
	 */
	@Override
	public Class<TAuthorRecord> getRecordType() {
		return RECORD_TYPE;
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY 'SYS_PK_11598'
	 */
	public static final TableField<Integer> ID = new TableFieldImpl<Integer>("ID", Integer.class, T_AUTHOR);

	/**
	 * An uncommented item
	 */
	public static final TableField<String> FIRST_NAME = new TableFieldImpl<String>("FIRST_NAME", String.class, T_AUTHOR);

	/**
	 * An uncommented item
	 */
	public static final TableField<String> LAST_NAME = new TableFieldImpl<String>("LAST_NAME", String.class, T_AUTHOR);

	/**
	 * An uncommented item
	 */
	public static final TableField<Date> DATE_OF_BIRTH = new TableFieldImpl<Date>("DATE_OF_BIRTH", Date.class, T_AUTHOR);

	/**
	 * No further instances allowed
	 */
	private TAuthor() {
		super("T_AUTHOR", Public.PUBLIC);
	}

	/*
	 * static initialiser
	 */
	static {
		T_AUTHOR.addToPrimaryKey(ID);
	}

}
