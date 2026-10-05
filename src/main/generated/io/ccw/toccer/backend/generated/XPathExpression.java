package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for xPathExpression complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="xPathExpression">
 *   <simpleContent>
 *     <extension base="<http://www.w3.org/2001/XMLSchema>string">
 *       <attribute name="onlyHandleAsReplacement" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     </extension>
 *   </simpleContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xPathExpression", propOrder = {
    "value"
})
public class XPathExpression {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "onlyHandleAsReplacement")
    protected Boolean onlyHandleAsReplacement;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the onlyHandleAsReplacement property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOnlyHandleAsReplacement() {
        if (onlyHandleAsReplacement == null) {
            return false;
        } else {
            return onlyHandleAsReplacement;
        }
    }

    /**
     * Sets the value of the onlyHandleAsReplacement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOnlyHandleAsReplacement(Boolean value) {
        this.onlyHandleAsReplacement = value;
    }

}
