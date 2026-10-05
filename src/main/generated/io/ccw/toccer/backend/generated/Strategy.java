package io.ccw.toccer.backend.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for strategy complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="strategy">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="json" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="post" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="postContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="headers" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "strategy", propOrder = {
    "json",
    "post",
    "postContent",
    "headers"
})
@XmlSeeAlso({
    NumberStrategy.class,
    CountStrategy.class,
    FixedPostUrlStrategy.class,
    MultiDataOnOnePageCountStrategy.class
})
public abstract class Strategy {

    protected Boolean json;
    protected Boolean post;
    protected String postContent;
    protected List<String> headers;

    /**
     * Gets the value of the json property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJson() {
        return json;
    }

    /**
     * Sets the value of the json property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJson(Boolean value) {
        this.json = value;
    }

    /**
     * Gets the value of the post property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPost() {
        return post;
    }

    /**
     * Sets the value of the post property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPost(Boolean value) {
        this.post = value;
    }

    /**
     * Gets the value of the postContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * Sets the value of the postContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostContent(String value) {
        this.postContent = value;
    }

    /**
     * Gets the value of the headers property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore, any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a {@code set} method for the headers property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getHeaders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * </p>
     * 
     * 
     * @return
     *     The value of the headers property.
     */
    public List<String> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<>();
        }
        return this.headers;
    }

}
