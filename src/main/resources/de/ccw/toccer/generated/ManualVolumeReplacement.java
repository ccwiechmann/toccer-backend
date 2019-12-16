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
 * <p>Java-Klasse für ManualVolumeReplacement complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ManualVolumeReplacement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VolumeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ifVolumeEquals" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManualVolumeReplacement", propOrder = {
    "volumeName",
    "ifVolumeEquals"
})
public class ManualVolumeReplacement {

    @XmlElement(name = "VolumeName", required = true)
    protected String volumeName;
    @XmlElement(required = true)
    protected String ifVolumeEquals;

    /**
     * Ruft den Wert der volumeName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeName() {
        return volumeName;
    }

    /**
     * Legt den Wert der volumeName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeName(String value) {
        this.volumeName = value;
    }

    /**
     * Ruft den Wert der ifVolumeEquals-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIfVolumeEquals() {
        return ifVolumeEquals;
    }

    /**
     * Legt den Wert der ifVolumeEquals-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIfVolumeEquals(String value) {
        this.ifVolumeEquals = value;
    }

}
