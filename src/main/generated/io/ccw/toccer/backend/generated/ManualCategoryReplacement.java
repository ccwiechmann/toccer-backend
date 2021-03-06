//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.12.29 at 06:47:29 AM UTC 
//


package io.ccw.toccer.backend.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManualCategoryReplacement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManualCategoryReplacement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="categoryName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ifTitleContains" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="ifVolumeIs" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManualCategoryReplacement", propOrder = {
    "categoryName",
    "ifTitleContains",
    "ifVolumeIs"
})
public class ManualCategoryReplacement {

    @XmlElement(required = true)
    protected String categoryName;
    protected String ifTitleContains;
    protected String ifVolumeIs;

    /**
     * Gets the value of the categoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the value of the categoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryName(String value) {
        this.categoryName = value;
    }

    /**
     * Gets the value of the ifTitleContains property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIfTitleContains() {
        return ifTitleContains;
    }

    /**
     * Sets the value of the ifTitleContains property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIfTitleContains(String value) {
        this.ifTitleContains = value;
    }

    /**
     * Gets the value of the ifVolumeIs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIfVolumeIs() {
        return ifVolumeIs;
    }

    /**
     * Sets the value of the ifVolumeIs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIfVolumeIs(String value) {
        this.ifVolumeIs = value;
    }

}
