//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.17 at 04:58:56 PM PDT 
//


package stepdev.express2xml.descriptions.util.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the main.java.com.boeing.stepdev.util.express2xml.jaxb package. 
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

    private final static QName _Ext_descriptions_QNAME = new QName("thisSchema", "ext_descriptions");
    private final static QName _ExpressRef_QNAME = new QName("thisSchema", "express_ref");
    private final static QName _ExtDescription_QNAME = new QName("thisSchema", "ext_description");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: main.java.com.boeing.stepdev.util.express2xml.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExtDescriptionType }
     * 
     */
    public ExtDescriptionType createExtDescriptionType() {
        return new ExtDescriptionType();
    }

    /**
     * Create an instance of {@link ExpressRefType }
     * 
     */
    public ExpressRefType createExpressRefType() {
        return new ExpressRefType();
    }

    /**
     * Create an instance of {@link Ext_descriptionsType }
     * 
     */
    public Ext_descriptionsType createExt_descriptionsType() {
        return new Ext_descriptionsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ext_descriptionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "thisSchema", name = "ext_descriptions")
    public JAXBElement<Ext_descriptionsType> createExt_descriptions(Ext_descriptionsType value) {
        return new JAXBElement<Ext_descriptionsType>(_Ext_descriptions_QNAME, Ext_descriptionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExpressRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "thisSchema", name = "express_ref")
    public JAXBElement<ExpressRefType> createExpressRef(ExpressRefType value) {
        return new JAXBElement<ExpressRefType>(_ExpressRef_QNAME, ExpressRefType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtDescriptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "thisSchema", name = "ext_description")
    public JAXBElement<ExtDescriptionType> createExtDescription(ExtDescriptionType value) {
        return new JAXBElement<ExtDescriptionType>(_ExtDescription_QNAME, ExtDescriptionType.class, null, value);
    }

}
