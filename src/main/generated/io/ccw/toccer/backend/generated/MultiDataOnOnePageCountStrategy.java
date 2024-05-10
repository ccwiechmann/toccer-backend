//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.05.07 um 05:07:19 PM CEST 
//


package io.ccw.toccer.backend.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r multiDataOnOnePageCountStrategy complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="multiDataOnOnePageCountStrategy">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ccw.io/toccer-backend/inputSchema}strategy">
 *       &lt;sequence>
 *         &lt;element name="maxXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="categoryForEachEntry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
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

	public Boolean getTitleForEachEntry() {
		return titleForEachEntry;
	}

	public void setTitleForEachEntry(Boolean titleForEachEntry) {
		this.titleForEachEntry = titleForEachEntry;
	}

	public Boolean getCategoryForEachEntry() {
		return categoryForEachEntry;
	}

}
