package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xpath complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="xpath">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="expressionWithReplaces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="strategy" type="{http://www.ccw.io/toccer-backend/inputSchema}strategy"/>
 *         <element name="cachedXpathResult" type="{http://www.ccw.io/toccer-backend/inputSchema}cachedXpathResult" minOccurs="0"/>
 *         <element name="next" type="{http://www.ccw.io/toccer-backend/inputSchema}xpath" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the expressionWithReplaces property.
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
     * Sets the value of the expressionWithReplaces property.
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
     * Gets the value of the strategy property.
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
     * Sets the value of the strategy property.
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
     * Gets the value of the cachedXpathResult property.
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
     * Sets the value of the cachedXpathResult property.
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
     * Gets the value of the next property.
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
     * Sets the value of the next property.
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
