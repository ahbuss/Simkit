//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2004.03.23 at 11:39:48 PST 
//


package simkit.xsd.bindings;


/**
 * Java content class for Event element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/rickg/CVS/Simkit/simkit/xml/simkit.xsd line 81)
 * <p>
 * <pre>
 * &lt;element name="Event">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}Comment" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}Argument" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}LocalVariable" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}StateTransition" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}EdgeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface EventElement
    extends javax.xml.bind.Element, simkit.xsd.bindings.EventType
{


}
