//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.12.15 um 11:46:28 PM CET 
//


package de.ccw.toccer.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;choice>
 *           &lt;element name="numberStrategy" type="{http://www.example.org/inputSchema}numberStrategy"/>
 *           &lt;element name="countStrategy" type="{http://www.example.org/inputSchema}countStrategy"/>
 *           &lt;element name="multiDataOnOnePageCountStrategy" type="{http://www.example.org/inputSchema}multiDataOnOnePageCountStrategy"/>
 *           &lt;element name="fixedPostUrlStrategy" type="{http://www.example.org/inputSchema}fixedPostUrlStrategy"/>
 *         &lt;/choice>
 *         &lt;element name="next" type="{http://www.example.org/inputSchema}xpath" minOccurs="0"/>
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
    "numberStrategy",
    "countStrategy",
    "multiDataOnOnePageCountStrategy",
    "fixedPostUrlStrategy",
    "next"
})
public class Xpath {

    protected String expressionWithReplaces;
    protected NumberStrategy numberStrategy;
    protected CountStrategy countStrategy;
    protected MultiDataOnOnePageCountStrategy multiDataOnOnePageCountStrategy;
    protected FixedPostUrlStrategy fixedPostUrlStrategy;
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
     * Ruft den Wert der numberStrategy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NumberStrategy }
     *     
     */
    public NumberStrategy getNumberStrategy() {
        return numberStrategy;
    }

    /**
     * Legt den Wert der numberStrategy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NumberStrategy }
     *     
     */
    public void setNumberStrategy(NumberStrategy value) {
        this.numberStrategy = value;
    }

    /**
     * Ruft den Wert der countStrategy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CountStrategy }
     *     
     */
    public CountStrategy getCountStrategy() {
        return countStrategy;
    }

    /**
     * Legt den Wert der countStrategy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CountStrategy }
     *     
     */
    public void setCountStrategy(CountStrategy value) {
        this.countStrategy = value;
    }

    /**
     * Ruft den Wert der multiDataOnOnePageCountStrategy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MultiDataOnOnePageCountStrategy }
     *     
     */
    public MultiDataOnOnePageCountStrategy getMultiDataOnOnePageCountStrategy() {
        return multiDataOnOnePageCountStrategy;
    }

    /**
     * Legt den Wert der multiDataOnOnePageCountStrategy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiDataOnOnePageCountStrategy }
     *     
     */
    public void setMultiDataOnOnePageCountStrategy(MultiDataOnOnePageCountStrategy value) {
        this.multiDataOnOnePageCountStrategy = value;
    }

    /**
     * Ruft den Wert der fixedPostUrlStrategy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link FixedPostUrlStrategy }
     *     
     */
    public FixedPostUrlStrategy getFixedPostUrlStrategy() {
        return fixedPostUrlStrategy;
    }

    /**
     * Legt den Wert der fixedPostUrlStrategy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link FixedPostUrlStrategy }
     *     
     */
    public void setFixedPostUrlStrategy(FixedPostUrlStrategy value) {
        this.fixedPostUrlStrategy = value;
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
