/**
 * This class is generated by jOOQ
 */
package org.jooq.test.cubrid.generatedclasses.enums;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public enum TBookStatus implements org.jooq.EnumType {

	SOLD_OUT("SOLD OUT"),

	ORDERED("ORDERED"),

	ON_STOCK("ON STOCK"),

	;

	private final java.lang.String literal;

	private TBookStatus(java.lang.String literal) {
		this.literal = literal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String getName() {
		return "t_book_status";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String getLiteral() {
		return literal;
	}
}
