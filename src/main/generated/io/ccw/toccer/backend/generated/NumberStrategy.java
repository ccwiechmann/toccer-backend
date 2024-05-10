//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.05.07 um 05:07:19 PM CEST 
//


package io.ccw.toccer.backend.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r numberStrategy complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="numberStrategy">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.ccw.io/toccer-backend/inputSchema}strategy">
 *       &lt;sequence>
 *         &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="to" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "numberStrategy", propOrder = {
    "from",
    "to",
    "fillWithPrefixedZerosToLength"
})
public class NumberStrategy
    extends Strategy
{

    protected int from;
    protected int to;
    protected Integer fillWithPrefixedZerosToLength;

    /**
     * Ruft den Wert der from-Eigenschaft ab.
     * 
     */
    public int getFrom() {
        return from;
    }

    /**
     * Legt den Wert der from-Eigenschaft fest.
     * 
     */
    public void setFrom(int value) {
        this.from = value;
    }

    /**
     * Ruft den Wert der to-Eigenschaft ab.
     * 
     */
    public int getTo() {
        return to;
    }

    /**
     * Legt den Wert der to-Eigenschaft fest.
     * 
     */
    public void setTo(int value) {
        this.to = value;
    }

	public Integer getFillWithPrefixedZerosToLength() {
		return fillWithPrefixedZerosToLength;
	}

	public void setFillWithPrefixedZerosToLength(Integer fillWithPrefixedZerosToLength) {
		this.fillWithPrefixedZerosToLength = fillWithPrefixedZerosToLength;
	}

}
