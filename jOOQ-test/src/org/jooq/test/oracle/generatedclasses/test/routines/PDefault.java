/**
 * This class is generated by jOOQ
 */
package org.jooq.test.oracle.generatedclasses.test.routines;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings({ "all", "unchecked" })
public class PDefault extends org.jooq.impl.AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = -430077595;

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_IN_NUMBER</code>. 
	 */
	public static final org.jooq.Parameter<java.math.BigDecimal> P_IN_NUMBER = createParameter("P_IN_NUMBER", org.jooq.impl.SQLDataType.NUMERIC, true);

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_OUT_NUMBER</code>. 
	 */
	public static final org.jooq.Parameter<java.math.BigDecimal> P_OUT_NUMBER = createParameter("P_OUT_NUMBER", org.jooq.impl.SQLDataType.NUMERIC);

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_IN_VARCHAR</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.String> P_IN_VARCHAR = createParameter("P_IN_VARCHAR", org.jooq.impl.SQLDataType.VARCHAR, true);

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_OUT_VARCHAR</code>. 
	 */
	public static final org.jooq.Parameter<java.lang.String> P_OUT_VARCHAR = createParameter("P_OUT_VARCHAR", org.jooq.impl.SQLDataType.VARCHAR);

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_IN_DATE</code>. 
	 */
	public static final org.jooq.Parameter<java.sql.Date> P_IN_DATE = createParameter("P_IN_DATE", org.jooq.impl.SQLDataType.DATE, true);

	/**
	 * The parameter <code>TEST.P_DEFAULT.P_OUT_DATE</code>. 
	 */
	public static final org.jooq.Parameter<java.sql.Date> P_OUT_DATE = createParameter("P_OUT_DATE", org.jooq.impl.SQLDataType.DATE);

	/**
	 * Create a new routine call instance
	 */
	public PDefault() {
		super("P_DEFAULT", org.jooq.test.oracle.generatedclasses.test.Test.TEST);

		addInParameter(P_IN_NUMBER);
		addOutParameter(P_OUT_NUMBER);
		addInParameter(P_IN_VARCHAR);
		addOutParameter(P_OUT_VARCHAR);
		addInParameter(P_IN_DATE);
		addOutParameter(P_OUT_DATE);
	}

	/**
	 * Set the <code>P_IN_NUMBER</code> parameter IN value to the routine
	 */
	public void setPInNumber(java.lang.Number value) {
		setNumber(org.jooq.test.oracle.generatedclasses.test.routines.PDefault.P_IN_NUMBER, value);
	}

	/**
	 * Set the <code>P_IN_VARCHAR</code> parameter IN value to the routine
	 */
	public void setPInVarchar(java.lang.String value) {
		setValue(org.jooq.test.oracle.generatedclasses.test.routines.PDefault.P_IN_VARCHAR, value);
	}

	/**
	 * Set the <code>P_IN_DATE</code> parameter IN value to the routine
	 */
	public void setPInDate(java.sql.Date value) {
		setValue(org.jooq.test.oracle.generatedclasses.test.routines.PDefault.P_IN_DATE, value);
	}

	/**
	 * Get the <code>P_OUT_NUMBER</code> parameter OUT value from the routine
	 */
	public java.math.BigDecimal getPOutNumber() {
		return getValue(P_OUT_NUMBER);
	}

	/**
	 * Get the <code>P_OUT_VARCHAR</code> parameter OUT value from the routine
	 */
	public java.lang.String getPOutVarchar() {
		return getValue(P_OUT_VARCHAR);
	}

	/**
	 * Get the <code>P_OUT_DATE</code> parameter OUT value from the routine
	 */
	public java.sql.Date getPOutDate() {
		return getValue(P_OUT_DATE);
	}
}
