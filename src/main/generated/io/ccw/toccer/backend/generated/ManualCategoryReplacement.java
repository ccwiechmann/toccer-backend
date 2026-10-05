package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManualCategoryReplacement complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ManualCategoryReplacement">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="categoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <choice>
 *           <element name="ifTitleContains" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           <element name="ifVolumeIs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         </choice>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
