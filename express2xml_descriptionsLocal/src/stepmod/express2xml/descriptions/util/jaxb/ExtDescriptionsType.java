//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.24 at 07:52:35 PM PDT 
//


package stepmod.express2xml.descriptions.util.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ext_descriptionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ext_descriptionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{thisSchema}ext_description" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{thisSchema}rootAttributeGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ext_descriptionsType", propOrder = {
    "extDescription"
})
public class ExtDescriptionsType {

    @XmlElement(name = "ext_description", required = true)
    protected List<ExtDescriptionType> extDescription;
    @XmlAttribute(name = "module_directory", namespace = "thisSchema", required = true)
    protected String moduleDirectory;
    @XmlAttribute(name = "schema_file", namespace = "thisSchema", required = true)
    protected String schemaFile;
    @XmlAttribute(name = "rcs.date", namespace = "thisSchema", required = true)
    protected String rcsDate;
    @XmlAttribute(name = "rcs.revision", namespace = "thisSchema", required = true)
    protected String rcsRevision;
    @XmlAttribute(name = "describe.selects", namespace = "thisSchema")
    protected String describeSelects;
    @XmlAttribute(name = "describe.subtype_constraints", namespace = "thisSchema")
    protected String describeSubtypeConstraints;

    /**
     * Gets the value of the extDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtDescriptionType }
     * 
     * 
     */
    public List<ExtDescriptionType> getExtDescription() {
        if (extDescription == null) {
            extDescription = new ArrayList<ExtDescriptionType>();
        }
        return this.extDescription;
    }

    /**
     * Gets the value of the moduleDirectory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModuleDirectory() {
        return moduleDirectory;
    }

    /**
     * Sets the value of the moduleDirectory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModuleDirectory(String value) {
        this.moduleDirectory = value;
    }

    /**
     * Gets the value of the schemaFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaFile() {
        return schemaFile;
    }

    /**
     * Sets the value of the schemaFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaFile(String value) {
        this.schemaFile = value;
    }

    /**
     * Gets the value of the rcsDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRcsDate() {
        return rcsDate;
    }

    /**
     * Sets the value of the rcsDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRcsDate(String value) {
        this.rcsDate = value;
    }

    /**
     * Gets the value of the rcsRevision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRcsRevision() {
        return rcsRevision;
    }

    /**
     * Sets the value of the rcsRevision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRcsRevision(String value) {
        this.rcsRevision = value;
    }

    /**
     * Gets the value of the describeSelects property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescribeSelects() {
        if (describeSelects == null) {
            return "NO";
        } else {
            return describeSelects;
        }
    }

    /**
     * Sets the value of the describeSelects property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescribeSelects(String value) {
        this.describeSelects = value;
    }

    /**
     * Gets the value of the describeSubtypeConstraints property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescribeSubtypeConstraints() {
        if (describeSubtypeConstraints == null) {
            return "NO";
        } else {
            return describeSubtypeConstraints;
        }
    }

    /**
     * Sets the value of the describeSubtypeConstraints property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescribeSubtypeConstraints(String value) {
        this.describeSubtypeConstraints = value;
    }

}
