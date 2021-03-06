/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.mysql.sakila.tables;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class SakilaFilmText extends org.jooq.impl.TableImpl<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord> {

	private static final long serialVersionUID = 1571314619;

	/**
	 * The singleton instance of <code>sakila.film_text</code>
	 */
	public static final org.jooq.examples.mysql.sakila.tables.SakilaFilmText FILM_TEXT = new org.jooq.examples.mysql.sakila.tables.SakilaFilmText();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord> getRecordType() {
		return org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord.class;
	}

	/**
	 * The column <code>sakila.film_text.film_id</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord, java.lang.Short> FILM_ID = createField("film_id", org.jooq.impl.SQLDataType.SMALLINT, this);

	/**
	 * The column <code>sakila.film_text.title</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord, java.lang.String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>sakila.film_text.description</code>. 
	 */
	public final org.jooq.TableField<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB.length(65535), this);

	/**
	 * Create a <code>sakila.film_text</code> table reference
	 */
	public SakilaFilmText() {
		super("film_text", org.jooq.examples.mysql.sakila.SakilaSakila.SAKILA);
	}

	/**
	 * Create an aliased <code>sakila.film_text</code> table reference
	 */
	public SakilaFilmText(java.lang.String alias) {
		super(alias, org.jooq.examples.mysql.sakila.SakilaSakila.SAKILA, org.jooq.examples.mysql.sakila.tables.SakilaFilmText.FILM_TEXT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord> getPrimaryKey() {
		return org.jooq.examples.mysql.sakila.Keys.KEY_FILM_TEXT_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.jooq.examples.mysql.sakila.tables.records.SakilaFilmTextRecord>>asList(org.jooq.examples.mysql.sakila.Keys.KEY_FILM_TEXT_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.examples.mysql.sakila.tables.SakilaFilmText as(java.lang.String alias) {
		return new org.jooq.examples.mysql.sakila.tables.SakilaFilmText(alias);
	}
}
