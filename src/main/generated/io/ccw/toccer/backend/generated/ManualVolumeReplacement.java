package io.ccw.toccer.backend.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManualVolumeReplacement complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ManualVolumeReplacement">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="VolumeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="ifVolumeEquals" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the volumeName property.
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
     * Sets the value of the volumeName property.
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
     * Gets the value of the ifVolumeEquals property.
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
     * Sets the value of the ifVolumeEquals property.
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
