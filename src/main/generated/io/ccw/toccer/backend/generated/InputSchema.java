//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.05.07 um 05:07:19 PM CEST 
//


package io.ccw.toccer.backend.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xpath"/>
 *         &lt;element name="titleXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/>
 *         &lt;element name="authorXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/>
 *         &lt;element name="pageXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression" minOccurs="0"/>
 *         &lt;element name="volumeXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression" minOccurs="0"/>
 *         &lt;element name="categoryXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/>
 *         &lt;element name="startingHtmlPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="baseUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="labelNameOfPublication" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="labelToc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="labelIssuePage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="urlSuffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sortCategories" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="emptyCategoryReplacements" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="fallbackCategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="manualCategoryReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualCategoryReplacement" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="manualVolumeReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualVolumeReplacement" maxOccurs="unbounded" minOccurs="0"/>
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
    "labelNameOfPublication",
    "labelToc",
    "labelIssuePage",
    "urlSuffix",
    "sortCategories",
    "emptyCategoryReplacements",
    "manualVolumeReplacement"
})
@XmlRootElement(name = "inputSchema")
public class InputSchema {

    @XmlElement(required = true)
    protected Xpath xpath;
    @XmlElement(required = true)
    protected XPathExpression titleXpath;
    @XmlElement(required = true)
    protected XPathExpression authorXpath;
    protected XPathExpression pageXpath;
    protected XPathExpression volumeXpath;
    @XmlElement(required = true)
    protected XPathExpression categoryXpath;
    protected String startingHtmlPage;
    @XmlElement(required = true)
    protected String baseUrl;
    @XmlElement(required = true)
    protected String labelNameOfPublication;
    @XmlElement(required = true)
    protected String labelToc;
    @XmlElement(required = true)
    protected String labelIssuePage;
    protected String urlSuffix;
    protected Boolean sortCategories;
    protected InputSchema.EmptyCategoryReplacements emptyCategoryReplacements;
    protected List<ManualVolumeReplacement> manualVolumeReplacement;

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
     *     {@link XPathExpression }
     *     
     */
    public XPathExpression getTitleXpath() {
        return titleXpath;
    }

    /**
     * Legt den Wert der titleXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XPathExpression }
     *     
     */
    public void setTitleXpath(XPathExpression value) {
        this.titleXpath = value;
    }

    /**
     * Ruft den Wert der authorXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XPathExpression }
     *     
     */
    public XPathExpression getAuthorXpath() {
        return authorXpath;
    }

    /**
     * Legt den Wert der authorXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XPathExpression }
     *     
     */
    public void setAuthorXpath(XPathExpression value) {
        this.authorXpath = value;
    }

    /**
     * Ruft den Wert der pageXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XPathExpression }
     *     
     */
    public XPathExpression getPageXpath() {
        return pageXpath;
    }

    /**
     * Legt den Wert der pageXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XPathExpression }
     *     
     */
    public void setPageXpath(XPathExpression value) {
        this.pageXpath = value;
    }

    /**
     * Ruft den Wert der volumeXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XPathExpression }
     *     
     */
    public XPathExpression getVolumeXpath() {
        return volumeXpath;
    }

    /**
     * Legt den Wert der volumeXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XPathExpression }
     *     
     */
    public void setVolumeXpath(XPathExpression value) {
        this.volumeXpath = value;
    }

    /**
     * Ruft den Wert der categoryXpath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XPathExpression }
     *     
     */
    public XPathExpression getCategoryXpath() {
        return categoryXpath;
    }

    /**
     * Legt den Wert der categoryXpath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XPathExpression }
     *     
     */
    public void setCategoryXpath(XPathExpression value) {
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
     * Ruft den Wert der labelNameOfPublication-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelNameOfPublication() {
        return labelNameOfPublication;
    }

    /**
     * Legt den Wert der labelNameOfPublication-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelNameOfPublication(String value) {
        this.labelNameOfPublication = value;
    }

    /**
     * Ruft den Wert der labelToc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelToc() {
        return labelToc;
    }

    /**
     * Legt den Wert der labelToc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelToc(String value) {
        this.labelToc = value;
    }

    /**
     * Ruft den Wert der labelIssuePage-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelIssuePage() {
        return labelIssuePage;
    }

    /**
     * Legt den Wert der labelIssuePage-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelIssuePage(String value) {
        this.labelIssuePage = value;
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
     *     {@link Boolean }
     *     
     */
    public Boolean isSortCategories() {
        return sortCategories;
    }

    /**
     * Legt den Wert der sortCategories-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSortCategories(Boolean value) {
        this.sortCategories = value;
    }

    /**
     * Ruft den Wert der emptyCategoryReplacements-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link InputSchema.EmptyCategoryReplacements }
     *     
     */
    public InputSchema.EmptyCategoryReplacements getEmptyCategoryReplacements() {
        return emptyCategoryReplacements;
    }

    /**
     * Legt den Wert der emptyCategoryReplacements-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link InputSchema.EmptyCategoryReplacements }
     *     
     */
    public void setEmptyCategoryReplacements(InputSchema.EmptyCategoryReplacements value) {
        this.emptyCategoryReplacements = value;
    }

    /**
     * Gets the value of the manualVolumeReplacement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the manualVolumeReplacement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManualVolumeReplacement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ManualVolumeReplacement }
     * 
     * 
     */
    public List<ManualVolumeReplacement> getManualVolumeReplacement() {
        if (manualVolumeReplacement == null) {
            manualVolumeReplacement = new ArrayList<ManualVolumeReplacement>();
        }
        return this.manualVolumeReplacement;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="fallbackCategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="manualCategoryReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualCategoryReplacement" maxOccurs="unbounded" minOccurs="0"/>
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
        "fallbackCategoryName",
        "manualCategoryReplacement"
    })
    public static class EmptyCategoryReplacements {

        protected String fallbackCategoryName;
        protected List<ManualCategoryReplacement> manualCategoryReplacement;

        /**
         * Ruft den Wert der fallbackCategoryName-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFallbackCategoryName() {
            return fallbackCategoryName;
        }

        /**
         * Legt den Wert der fallbackCategoryName-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFallbackCategoryName(String value) {
            this.fallbackCategoryName = value;
        }

        /**
         * Gets the value of the manualCategoryReplacement property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the manualCategoryReplacement property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getManualCategoryReplacement().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ManualCategoryReplacement }
         * 
         * 
         */
        public List<ManualCategoryReplacement> getManualCategoryReplacement() {
            if (manualCategoryReplacement == null) {
                manualCategoryReplacement = new ArrayList<ManualCategoryReplacement>();
            }
            return this.manualCategoryReplacement;
        }

    }

}
