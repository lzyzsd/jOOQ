/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.records;

/**
 * This class is generated by jOOQ.
 */
public class ProductmodelillustrationRecord extends org.jooq.impl.UpdatableRecordImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.ProductmodelillustrationRecord> {

	private static final long serialVersionUID = 1070319935;

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.ProductModelID]
	 * REFERENCES ProductModel [Production.ProductModel.ProductModelID]
	 * </pre></code>
	 */
	public void setProductmodelid(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.PRODUCTMODELID, value);
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.ProductModelID]
	 * REFERENCES ProductModel [Production.ProductModel.ProductModelID]
	 * </pre></code>
	 */
	public java.lang.Integer getProductmodelid() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.PRODUCTMODELID);
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.ProductModelID]
	 * REFERENCES ProductModel [Production.ProductModel.ProductModelID]
	 * </pre></code>
	 */
	public org.jooq.examples.sqlserver.adventureworks.production.tables.records.ProductmodelRecord fetchProductmodel() {
		return create()
			.selectFrom(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodel.PRODUCTMODEL)
			.where(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodel.PRODUCTMODEL.PRODUCTMODELID.equal(getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.PRODUCTMODELID)))
			.fetchOne();
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.IllustrationID]
	 * REFERENCES Illustration [Production.Illustration.IllustrationID]
	 * </pre></code>
	 */
	public void setIllustrationid(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.ILLUSTRATIONID, value);
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.IllustrationID]
	 * REFERENCES Illustration [Production.Illustration.IllustrationID]
	 * </pre></code>
	 */
	public java.lang.Integer getIllustrationid() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.ILLUSTRATIONID);
	}

	/**
	 * An uncommented item
	 * 
	 * PRIMARY KEY
	 * <p>
	 * <code><pre>
	 * FOREIGN KEY [Production.ProductModelIllustration.IllustrationID]
	 * REFERENCES Illustration [Production.Illustration.IllustrationID]
	 * </pre></code>
	 */
	public org.jooq.examples.sqlserver.adventureworks.production.tables.records.IllustrationRecord fetchIllustration() {
		return create()
			.selectFrom(org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration.ILLUSTRATION)
			.where(org.jooq.examples.sqlserver.adventureworks.production.tables.Illustration.ILLUSTRATION.ILLUSTRATIONID.equal(getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.ILLUSTRATIONID)))
			.fetchOne();
	}

	/**
	 * An uncommented item
	 */
	public void setModifieddate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.MODIFIEDDATE, value);
	}

	/**
	 * An uncommented item
	 */
	public java.sql.Timestamp getModifieddate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION.MODIFIEDDATE);
	}

	/**
	 * Create a detached ProductmodelillustrationRecord
	 */
	public ProductmodelillustrationRecord() {
		super(org.jooq.examples.sqlserver.adventureworks.production.tables.Productmodelillustration.PRODUCTMODELILLUSTRATION);
	}
}