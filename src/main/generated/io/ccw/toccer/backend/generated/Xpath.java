//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.05.07 um 05:07:19 PM CEST 
//


package io.ccw.toccer.backend.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für xpath complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="xpath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="expressionWithReplaces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strategy" type="{http://www.ccw.io/toccer-backend/inputSchema}strategy"/>
 *         &lt;element name="cachedXpathResult" type="{http://www.ccw.io/toccer-backend/inputSchema}cachedXpathResult" minOccurs="0"/>
 *         &lt;element name="next" type="{http://www.ccw.io/toccer-backend/inputSchema}xpath" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xpath", propOrder = {
    "expressionWithReplaces",
    "strategy",
    "cachedXpathResult",
    "next"
})
public class Xpath {

    protected String expressionWithReplaces;
    @XmlElement(required = true)
    protected Strategy strategy;
    protected CachedXpathResult cachedXpathResult;
    protected Xpath next;

    /**
     * Ruft den Wert der expressionWithReplaces-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressionWithReplaces() {
        return expressionWithReplaces;
    }

    /**
     * Legt den Wert der expressionWithReplaces-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressionWithReplaces(String value) {
        this.expressionWithReplaces = value;
    }

    /**
     * Ruft den Wert der strategy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Strategy }
     *     
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Legt den Wert der strategy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Strategy }
     *     
     */
    public void setStrategy(Strategy value) {
        this.strategy = value;
    }

    /**
     * Ruft den Wert der cachedXpathResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CachedXpathResult }
     *     
     */
    public CachedXpathResult getCachedXpathResult() {
        return cachedXpathResult;
    }

    /**
     * Legt den Wert der cachedXpathResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CachedXpathResult }
     *     
     */
    public void setCachedXpathResult(CachedXpathResult value) {
        this.cachedXpathResult = value;
    }

    /**
     * Ruft den Wert der next-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Xpath }
     *     
     */
    public Xpath getNext() {
        return next;
    }

    /**
     * Legt den Wert der next-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Xpath }
     *     
     */
    public void setNext(Xpath value) {
        this.next = value;
    }

}
