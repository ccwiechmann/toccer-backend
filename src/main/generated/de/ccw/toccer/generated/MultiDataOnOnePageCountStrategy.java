//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.12.15 um 11:46:28 PM CET 
//


package de.ccw.toccer.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für multiDataOnOnePageCountStrategy complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="multiDataOnOnePageCountStrategy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="categoryForEachEntry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "multiDataOnOnePageCountStrategy", propOrder = {
    "maxXpath",
    "categoryForEachEntry"
})
public class MultiDataOnOnePageCountStrategy {

    @XmlElement(required = true)
    protected String maxXpath;
    protected Boolean categoryForEachEntry;

    /**
     * Ruft den Wert der maxXpath-Eigenschaft ab.
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
     * Legt den Wert der maxXpath-Eigenschaft fest.
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
     * Ruft den Wert der categoryForEachEntry-Eigenschaft ab.
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
     * Legt den Wert der categoryForEachEntry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCategoryForEachEntry(Boolean value) {
        this.categoryForEachEntry = value;
    }

}
