//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.12.29 at 06:47:29 AM UTC 
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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="xpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xpath"/&gt;
 *         &lt;element name="titleXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/&gt;
 *         &lt;element name="authorXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/&gt;
 *         &lt;element name="pageXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression" minOccurs="0"/&gt;
 *         &lt;element name="volumeXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression" minOccurs="0"/&gt;
 *         &lt;element name="categoryXpath" type="{http://www.ccw.io/toccer-backend/inputSchema}xPathExpression"/&gt;
 *         &lt;element name="startingHtmlPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="baseUrl" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="labelNameOfPublication" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="labelToc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="labelIssuePage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="urlSuffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sortCategories" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="emptyCategoryReplacements" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="fallbackCategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="manualCategoryReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualCategoryReplacement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="manualVolumeReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualVolumeReplacement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Gets the value of the xpath property.
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
     * Sets the value of the xpath property.
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
     * Gets the value of the titleXpath property.
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
     * Sets the value of the titleXpath property.
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
     * Gets the value of the authorXpath property.
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
     * Sets the value of the authorXpath property.
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
     * Gets the value of the pageXpath property.
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
     * Sets the value of the pageXpath property.
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
     * Gets the value of the volumeXpath property.
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
     * Sets the value of the volumeXpath property.
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
     * Gets the value of the categoryXpath property.
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
     * Sets the value of the categoryXpath property.
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
     * Gets the value of the startingHtmlPage property.
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
     * Sets the value of the startingHtmlPage property.
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
     * Gets the value of the baseUrl property.
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
     * Sets the value of the baseUrl property.
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
     * Gets the value of the labelNameOfPublication property.
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
     * Sets the value of the labelNameOfPublication property.
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
     * Gets the value of the labelToc property.
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
     * Sets the value of the labelToc property.
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
     * Gets the value of the labelIssuePage property.
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
     * Sets the value of the labelIssuePage property.
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
     * Gets the value of the urlSuffix property.
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
     * Sets the value of the urlSuffix property.
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
     * Gets the value of the sortCategories property.
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
     * Sets the value of the sortCategories property.
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
     * Gets the value of the emptyCategoryReplacements property.
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
     * Sets the value of the emptyCategoryReplacements property.
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="fallbackCategoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="manualCategoryReplacement" type="{http://www.ccw.io/toccer-backend/inputSchema}ManualCategoryReplacement" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
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
         * Gets the value of the fallbackCategoryName property.
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
         * Sets the value of the fallbackCategoryName property.
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
