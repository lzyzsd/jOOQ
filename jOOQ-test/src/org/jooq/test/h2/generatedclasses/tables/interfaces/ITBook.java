/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.interfaces;

/**
 * This class is generated by jOOQ.
 *
 * An entity holding books
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public interface ITBook extends java.io.Serializable {

	/**
	 * Setter for <code>PUBLIC.T_BOOK.ID</code>. The book ID
	 */
	public void setId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.ID</code>. The book ID
	 */
	public java.lang.Integer getId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.AUTHOR_ID</code>. The author ID in entity 'author'
	 */
	public void setAuthorId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.AUTHOR_ID</code>. The author ID in entity 'author'
	 */
	public java.lang.Integer getAuthorId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>. 
	 */
	public void setCoAuthorId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CO_AUTHOR_ID</code>. 
	 */
	public java.lang.Integer getCoAuthorId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.DETAILS_ID</code>. 
	 */
	public void setDetailsId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.DETAILS_ID</code>. 
	 */
	public java.lang.Integer getDetailsId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.TITLE</code>. The book's title
	 */
	public void setTitle(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.TITLE</code>. The book's title
	 */
	public java.lang.String getTitle();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.PUBLISHED_IN</code>. The year the book was published in
	 */
	public void setPublishedIn(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.PUBLISHED_IN</code>. The year the book was published in
	 */
	public java.lang.Integer getPublishedIn();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.LANGUAGE_ID</code>. The language of the book
	 */
	public void setLanguageId(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.LANGUAGE_ID</code>. The language of the book
	 */
	public java.lang.Integer getLanguageId();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CONTENT_TEXT</code>. Some textual content of the book
	 */
	public void setContentText(java.lang.String value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CONTENT_TEXT</code>. Some textual content of the book
	 */
	public java.lang.String getContentText();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.CONTENT_PDF</code>. Some binary content of the book
	 */
	public void setContentPdf(byte[] value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.CONTENT_PDF</code>. Some binary content of the book
	 */
	public byte[] getContentPdf();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.REC_VERSION</code>. 
	 */
	public void setRecVersion(java.lang.Integer value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.REC_VERSION</code>. 
	 */
	public java.lang.Integer getRecVersion();

	/**
	 * Setter for <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>. 
	 */
	public void setRecTimestamp(java.sql.Timestamp value);

	/**
	 * Getter for <code>PUBLIC.T_BOOK.REC_TIMESTAMP</code>. 
	 */
	public java.sql.Timestamp getRecTimestamp();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITBook
	 */
	public void from(org.jooq.test.h2.generatedclasses.tables.interfaces.ITBook from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITBook
	 */
	public <E extends org.jooq.test.h2.generatedclasses.tables.interfaces.ITBook> E into(E into);
}
