package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for numberStrategy complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="numberStrategy">
 *   <complexContent>
 *     <extension base="{http://www.ccw.io/toccer-backend/inputSchema}strategy">
 *       <sequence>
 *         <element name="from" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="to" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="fillWithPrefixedZerosToLength" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       </sequence>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the from property.
     * 
     */
    public int getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     */
    public void setFrom(int value) {
        this.from = value;
    }

    /**
     * Gets the value of the to property.
     * 
     */
    public int getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     */
    public void setTo(int value) {
        this.to = value;
    }

    /**
     * Gets the value of the fillWithPrefixedZerosToLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFillWithPrefixedZerosToLength() {
        return fillWithPrefixedZerosToLength;
    }

    /**
     * Sets the value of the fillWithPrefixedZerosToLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFillWithPrefixedZerosToLength(Integer value) {
        this.fillWithPrefixedZerosToLength = value;
    }

}
