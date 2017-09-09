//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.09.09 um 12:49:49 AM CEST 
//


package de.ccw.toccer.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xpath" type="{http://www.example.org/inputSchema}xpath"/>
 *         &lt;element name="titleXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pageXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="volumeXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="categoryXpath" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startingHtmlPage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="baseUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="urlSuffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortCategories" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xpath",
    "titleXpath",
    "authorXpath",
    "pageXpath",
    "volumeXpath",
    "categoryXpath",
    "startingHtmlPage",
    "baseUrl",
    "urlSuffix",
    "sortCategories"
})
@XmlRootElement(name = "inputSchema")
public class InputSchema {

    @XmlElement(required = true)
    protected Xpath xpath;
    @XmlElement(required = true)
    protected String titleXpath;
    @XmlElement(required = true)
    protected String authorXpath;
    @XmlElement(required = true)
    protected String pageXpath;
    @XmlElement(required = true)
    protected String volumeXpath;
    @XmlElement(required = true)
    protected String categoryXpath;
    @XmlElement(required = true)
    protected String startingHtmlPage;
    @XmlElement(required = true)
    protected String baseUrl;
    protected String urlSuffix;
    protected boolean sortCategories;

    /**
     * Ruft den Wert der xpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Xpath }
     *     
     */
    public Xpath getXpath() {
        return xpath;
    }

    /**
     * Legt den Wert der xpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Xpath }
     *     
     */
    public void setXpath(Xpath value) {
        this.xpath = value;
    }

    /**
     * Ruft den Wert der titleXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitleXpath() {
        return titleXpath;
    }

    /**
     * Legt den Wert der titleXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitleXpath(String value) {
        this.titleXpath = value;
    }

    /**
     * Ruft den Wert der authorXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorXpath() {
        return authorXpath;
    }

    /**
     * Legt den Wert der authorXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorXpath(String value) {
        this.authorXpath = value;
    }

    /**
     * Ruft den Wert der pageXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageXpath() {
        return pageXpath;
    }

    /**
     * Legt den Wert der pageXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageXpath(String value) {
        this.pageXpath = value;
    }

    /**
     * Ruft den Wert der volumeXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeXpath() {
        return volumeXpath;
    }

    /**
     * Legt den Wert der volumeXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeXpath(String value) {
        this.volumeXpath = value;
    }

    /**
     * Ruft den Wert der categoryXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryXpath() {
        return categoryXpath;
    }

    /**
     * Legt den Wert der categoryXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryXpath(String value) {
        this.categoryXpath = value;
    }

    /**
     * Ruft den Wert der startingHtmlPage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartingHtmlPage() {
        return startingHtmlPage;
    }

    /**
     * Legt den Wert der startingHtmlPage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartingHtmlPage(String value) {
        this.startingHtmlPage = value;
    }

    /**
     * Ruft den Wert der baseUrl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Legt den Wert der baseUrl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseUrl(String value) {
        this.baseUrl = value;
    }

    /**
     * Ruft den Wert der urlSuffix-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlSuffix() {
        return urlSuffix;
    }

    /**
     * Legt den Wert der urlSuffix-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlSuffix(String value) {
        this.urlSuffix = value;
    }
    
    /**
     * Ruft den Wert der sortCategories-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public boolean getSortCategories() {
        return sortCategories;
    }

    /**
     * Legt den Wert der sortCategories-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link boolean }
     *     
     */
    public void setSortCategories(boolean value) {
        this.sortCategories = value;
    }

}
