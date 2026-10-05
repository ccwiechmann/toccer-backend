package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for multiDataOnOnePageCountStrategy complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="multiDataOnOnePageCountStrategy">
 *   <complexContent>
 *     <extension base="{http://www.ccw.io/toccer-backend/inputSchema}strategy">
 *       <sequence>
 *         <element name="maxXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="categoryForEachEntry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="titleForEachEntry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       </sequence>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "multiDataOnOnePageCountStrategy", propOrder = {
    "maxXpath",
    "categoryForEachEntry",
    "titleForEachEntry"
})
public class MultiDataOnOnePageCountStrategy
    extends Strategy
{

    @XmlElement(required = true)
    protected String maxXpath;
    protected Boolean categoryForEachEntry;
    protected Boolean titleForEachEntry;

    /**
     * Gets the value of the maxXpath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxXpath() {
        return maxXpath;
    }

    /**
     * Sets the value of the maxXpath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxXpath(String value) {
        this.maxXpath = value;
    }

    /**
     * Gets the value of the categoryForEachEntry property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCategoryForEachEntry() {
        return categoryForEachEntry;
    }

    /**
     * Sets the value of the categoryForEachEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCategoryForEachEntry(Boolean value) {
        this.categoryForEachEntry = value;
    }

    /**
     * Gets the value of the titleForEachEntry property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTitleForEachEntry() {
        return titleForEachEntry;
    }

    /**
     * Sets the value of the titleForEachEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTitleForEachEntry(Boolean value) {
        this.titleForEachEntry = value;
    }

}
