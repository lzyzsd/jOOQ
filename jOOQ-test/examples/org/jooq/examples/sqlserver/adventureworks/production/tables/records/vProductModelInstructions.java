/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.records;

/**
 * This class is generated by jOOQ.
 */
@java.lang.SuppressWarnings("all")
@javax.persistence.Entity
@javax.persistence.Table(name = "vProductModelInstructions", schema = "Production")
public class vProductModelInstructions extends org.jooq.impl.TableRecordImpl<org.jooq.examples.sqlserver.adventureworks.production.tables.records.vProductModelInstructions> implements org.jooq.Record11<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp> {

	private static final long serialVersionUID = 885720336;

	/**
	 * Setter for <code>Production.vProductModelInstructions.ProductModelID</code>. 
	 */
	public void setProductModelID(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ProductModelID, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.ProductModelID</code>. 
	 */
	@javax.persistence.Column(name = "ProductModelID", nullable = false, precision = 10)
	public java.lang.Integer getProductModelID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ProductModelID);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.Name</code>. 
	 */
	public void setName(java.lang.String value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Name, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.Name</code>. 
	 */
	@javax.persistence.Column(name = "Name", nullable = false, length = 50)
	public java.lang.String getName() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Name);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.Instructions</code>. 
	 */
	public void setInstructions(java.lang.String value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Instructions, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.Instructions</code>. 
	 */
	@javax.persistence.Column(name = "Instructions")
	public java.lang.String getInstructions() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Instructions);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.LocationID</code>. 
	 */
	public void setLocationID(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LocationID, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.LocationID</code>. 
	 */
	@javax.persistence.Column(name = "LocationID", precision = 10)
	public java.lang.Integer getLocationID() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LocationID);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.SetupHours</code>. 
	 */
	public void setSetupHours(java.math.BigDecimal value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.SetupHours, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.SetupHours</code>. 
	 */
	@javax.persistence.Column(name = "SetupHours", precision = 9, scale = 4)
	public java.math.BigDecimal getSetupHours() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.SetupHours);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.MachineHours</code>. 
	 */
	public void setMachineHours(java.math.BigDecimal value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.MachineHours, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.MachineHours</code>. 
	 */
	@javax.persistence.Column(name = "MachineHours", precision = 9, scale = 4)
	public java.math.BigDecimal getMachineHours() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.MachineHours);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.LaborHours</code>. 
	 */
	public void setLaborHours(java.math.BigDecimal value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LaborHours, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.LaborHours</code>. 
	 */
	@javax.persistence.Column(name = "LaborHours", precision = 9, scale = 4)
	public java.math.BigDecimal getLaborHours() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LaborHours);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.LotSize</code>. 
	 */
	public void setLotSize(java.lang.Integer value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LotSize, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.LotSize</code>. 
	 */
	@javax.persistence.Column(name = "LotSize", precision = 10)
	public java.lang.Integer getLotSize() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LotSize);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.Step</code>. 
	 */
	public void setStep(java.lang.String value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Step, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.Step</code>. 
	 */
	@javax.persistence.Column(name = "Step", length = 1024)
	public java.lang.String getStep() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Step);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.rowguid</code>. 
	 */
	public void setrowguid(java.lang.String value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.rowguid, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.rowguid</code>. 
	 */
	@javax.persistence.Column(name = "rowguid", nullable = false)
	public java.lang.String getrowguid() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.rowguid);
	}

	/**
	 * Setter for <code>Production.vProductModelInstructions.ModifiedDate</code>. 
	 */
	public void setModifiedDate(java.sql.Timestamp value) {
		setValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ModifiedDate, value);
	}

	/**
	 * Getter for <code>Production.vProductModelInstructions.ModifiedDate</code>. 
	 */
	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return getValue(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ModifiedDate);
	}

	// -------------------------------------------------------------------------
	// Record11 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row11<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp> fieldsRow() {
		return org.jooq.impl.DSL.row(field1(), field2(), field3(), field4(), field5(), field6(), field7(), field8(), field9(), field10(), field11());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row11<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp> valuesRow() {
		return org.jooq.impl.DSL.row(value1(), value2(), value3(), value4(), value5(), value6(), value7(), value8(), value9(), value10(), value11());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ProductModelID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Instructions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LocationID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field5() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.SetupHours;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field6() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.MachineHours;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field7() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LaborHours;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.LotSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.Step;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field10() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.rowguid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field11() {
		return org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions.ModifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getProductModelID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getInstructions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getLocationID();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value5() {
		return getSetupHours();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value6() {
		return getMachineHours();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value7() {
		return getLaborHours();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getLotSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getStep();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value10() {
		return getrowguid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value11() {
		return getModifiedDate();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached vProductModelInstructions
	 */
	public vProductModelInstructions() {
		super(org.jooq.examples.sqlserver.adventureworks.production.tables.vProductModelInstructions.vProductModelInstructions);
	}
}
