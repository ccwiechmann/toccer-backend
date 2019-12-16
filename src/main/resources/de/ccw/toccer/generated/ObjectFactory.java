//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.12.15 um 11:46:28 PM CET 
//


package de.ccw.toccer.generated;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.ccw.toccer.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.ccw.toccer.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InputSchema }
     * 
     */
    public InputSchema createInputSchema() {
        return new InputSchema();
    }

    /**
     * Create an instance of {@link FixedPostUrlStrategy }
     * 
     */
    public FixedPostUrlStrategy createFixedPostUrlStrategy() {
        return new FixedPostUrlStrategy();
    }

    /**
     * Create an instance of {@link Xpath }
     * 
     */
    public Xpath createXpath() {
        return new Xpath();
    }

    /**
     * Create an instance of {@link InputSchema.EmptyCategoryReplacements }
     * 
     */
    public InputSchema.EmptyCategoryReplacements createInputSchemaEmptyCategoryReplacements() {
        return new InputSchema.EmptyCategoryReplacements();
    }

    /**
     * Create an instance of {@link ManualVolumeReplacement }
     * 
     */
    public ManualVolumeReplacement createManualVolumeReplacement() {
        return new ManualVolumeReplacement();
    }

    /**
     * Create an instance of {@link CountStrategy }
     * 
     */
    public CountStrategy createCountStrategy() {
        return new CountStrategy();
    }

    /**
     * Create an instance of {@link NumberStrategy }
     * 
     */
    public NumberStrategy createNumberStrategy() {
        return new NumberStrategy();
    }

    /**
     * Create an instance of {@link ManualCategoryReplacement }
     * 
     */
    public ManualCategoryReplacement createManualCategoryReplacement() {
        return new ManualCategoryReplacement();
    }

    /**
     * Create an instance of {@link MultiDataOnOnePageCountStrategy }
     * 
     */
    public MultiDataOnOnePageCountStrategy createMultiDataOnOnePageCountStrategy() {
        return new MultiDataOnOnePageCountStrategy();
    }

    /**
     * Create an instance of {@link FixedPostUrlStrategy.FixedUrls }
     * 
     */
    public FixedPostUrlStrategy.FixedUrls createFixedPostUrlStrategyFixedUrls() {
        return new FixedPostUrlStrategy.FixedUrls();
    }

}
