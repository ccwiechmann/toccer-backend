//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.07.10 um 06:54:52 PM CEST 
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
     * Create an instance of {@link Xpath }
     * 
     */
    public Xpath createXpath() {
        return new Xpath();
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

}
