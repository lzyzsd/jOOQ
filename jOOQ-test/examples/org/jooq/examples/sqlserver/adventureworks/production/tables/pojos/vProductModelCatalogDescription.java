/**
 * This class is generated by jOOQ
 */
package org.jooq.examples.sqlserver.adventureworks.production.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "vProductModelCatalogDescription", schema = "Production")
public class vProductModelCatalogDescription implements java.io.Serializable {

	private static final long serialVersionUID = -707722308;

	private java.lang.Integer  ProductModelID;
	private java.lang.String   Name;
	private java.lang.String   Summary;
	private java.lang.String   Manufacturer;
	private java.lang.String   Copyright;
	private java.lang.String   ProductURL;
	private java.lang.String   WarrantyPeriod;
	private java.lang.String   WarrantyDescription;
	private java.lang.String   NoOfYears;
	private java.lang.String   MaintenanceDescription;
	private java.lang.String   Wheel;
	private java.lang.String   Saddle;
	private java.lang.String   Pedal;
	private java.lang.String   BikeFrame;
	private java.lang.String   Crankset;
	private java.lang.String   PictureAngle;
	private java.lang.String   PictureSize;
	private java.lang.String   ProductPhotoID;
	private java.lang.String   Material;
	private java.lang.String   Color;
	private java.lang.String   ProductLine;
	private java.lang.String   Style;
	private java.lang.String   RiderExperience;
	private java.lang.String   rowguid;
	private java.sql.Timestamp ModifiedDate;

	@javax.persistence.Column(name = "ProductModelID", nullable = false)
	public java.lang.Integer getProductModelID() {
		return this.ProductModelID;
	}

	public void setProductModelID(java.lang.Integer ProductModelID) {
		this.ProductModelID = ProductModelID;
	}

	@javax.persistence.Column(name = "Name", nullable = false)
	public java.lang.String getName() {
		return this.Name;
	}

	public void setName(java.lang.String Name) {
		this.Name = Name;
	}

	@javax.persistence.Column(name = "Summary")
	public java.lang.String getSummary() {
		return this.Summary;
	}

	public void setSummary(java.lang.String Summary) {
		this.Summary = Summary;
	}

	@javax.persistence.Column(name = "Manufacturer")
	public java.lang.String getManufacturer() {
		return this.Manufacturer;
	}

	public void setManufacturer(java.lang.String Manufacturer) {
		this.Manufacturer = Manufacturer;
	}

	@javax.persistence.Column(name = "Copyright")
	public java.lang.String getCopyright() {
		return this.Copyright;
	}

	public void setCopyright(java.lang.String Copyright) {
		this.Copyright = Copyright;
	}

	@javax.persistence.Column(name = "ProductURL")
	public java.lang.String getProductURL() {
		return this.ProductURL;
	}

	public void setProductURL(java.lang.String ProductURL) {
		this.ProductURL = ProductURL;
	}

	@javax.persistence.Column(name = "WarrantyPeriod")
	public java.lang.String getWarrantyPeriod() {
		return this.WarrantyPeriod;
	}

	public void setWarrantyPeriod(java.lang.String WarrantyPeriod) {
		this.WarrantyPeriod = WarrantyPeriod;
	}

	@javax.persistence.Column(name = "WarrantyDescription")
	public java.lang.String getWarrantyDescription() {
		return this.WarrantyDescription;
	}

	public void setWarrantyDescription(java.lang.String WarrantyDescription) {
		this.WarrantyDescription = WarrantyDescription;
	}

	@javax.persistence.Column(name = "NoOfYears")
	public java.lang.String getNoOfYears() {
		return this.NoOfYears;
	}

	public void setNoOfYears(java.lang.String NoOfYears) {
		this.NoOfYears = NoOfYears;
	}

	@javax.persistence.Column(name = "MaintenanceDescription")
	public java.lang.String getMaintenanceDescription() {
		return this.MaintenanceDescription;
	}

	public void setMaintenanceDescription(java.lang.String MaintenanceDescription) {
		this.MaintenanceDescription = MaintenanceDescription;
	}

	@javax.persistence.Column(name = "Wheel")
	public java.lang.String getWheel() {
		return this.Wheel;
	}

	public void setWheel(java.lang.String Wheel) {
		this.Wheel = Wheel;
	}

	@javax.persistence.Column(name = "Saddle")
	public java.lang.String getSaddle() {
		return this.Saddle;
	}

	public void setSaddle(java.lang.String Saddle) {
		this.Saddle = Saddle;
	}

	@javax.persistence.Column(name = "Pedal")
	public java.lang.String getPedal() {
		return this.Pedal;
	}

	public void setPedal(java.lang.String Pedal) {
		this.Pedal = Pedal;
	}

	@javax.persistence.Column(name = "BikeFrame")
	public java.lang.String getBikeFrame() {
		return this.BikeFrame;
	}

	public void setBikeFrame(java.lang.String BikeFrame) {
		this.BikeFrame = BikeFrame;
	}

	@javax.persistence.Column(name = "Crankset")
	public java.lang.String getCrankset() {
		return this.Crankset;
	}

	public void setCrankset(java.lang.String Crankset) {
		this.Crankset = Crankset;
	}

	@javax.persistence.Column(name = "PictureAngle")
	public java.lang.String getPictureAngle() {
		return this.PictureAngle;
	}

	public void setPictureAngle(java.lang.String PictureAngle) {
		this.PictureAngle = PictureAngle;
	}

	@javax.persistence.Column(name = "PictureSize")
	public java.lang.String getPictureSize() {
		return this.PictureSize;
	}

	public void setPictureSize(java.lang.String PictureSize) {
		this.PictureSize = PictureSize;
	}

	@javax.persistence.Column(name = "ProductPhotoID")
	public java.lang.String getProductPhotoID() {
		return this.ProductPhotoID;
	}

	public void setProductPhotoID(java.lang.String ProductPhotoID) {
		this.ProductPhotoID = ProductPhotoID;
	}

	@javax.persistence.Column(name = "Material")
	public java.lang.String getMaterial() {
		return this.Material;
	}

	public void setMaterial(java.lang.String Material) {
		this.Material = Material;
	}

	@javax.persistence.Column(name = "Color")
	public java.lang.String getColor() {
		return this.Color;
	}

	public void setColor(java.lang.String Color) {
		this.Color = Color;
	}

	@javax.persistence.Column(name = "ProductLine")
	public java.lang.String getProductLine() {
		return this.ProductLine;
	}

	public void setProductLine(java.lang.String ProductLine) {
		this.ProductLine = ProductLine;
	}

	@javax.persistence.Column(name = "Style")
	public java.lang.String getStyle() {
		return this.Style;
	}

	public void setStyle(java.lang.String Style) {
		this.Style = Style;
	}

	@javax.persistence.Column(name = "RiderExperience")
	public java.lang.String getRiderExperience() {
		return this.RiderExperience;
	}

	public void setRiderExperience(java.lang.String RiderExperience) {
		this.RiderExperience = RiderExperience;
	}

	@javax.persistence.Column(name = "rowguid", nullable = false)
	public java.lang.String getrowguid() {
		return this.rowguid;
	}

	public void setrowguid(java.lang.String rowguid) {
		this.rowguid = rowguid;
	}

	@javax.persistence.Column(name = "ModifiedDate", nullable = false)
	public java.sql.Timestamp getModifiedDate() {
		return this.ModifiedDate;
	}

	public void setModifiedDate(java.sql.Timestamp ModifiedDate) {
		this.ModifiedDate = ModifiedDate;
	}
}