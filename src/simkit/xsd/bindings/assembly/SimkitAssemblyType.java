//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2004.04.02 at 03:27:54 PST 
//


package simkit.xsd.bindings.assembly;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/home/rmgold/CVS/Simkit/simkit/xsd/assembly/assembly.xsd line 64)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}SimEntity" maxOccurs="unbounded"/>
 *         &lt;element ref="{}PropertyChangeListener" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SimEventListenerConnection" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}PropertyChangeListenerConnection" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}Output" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SimkitAssemblyType {


    /**
     * Gets the value of the SimEventListenerConnection property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the SimEventListenerConnection property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimEventListenerConnection().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link simkit.xsd.bindings.assembly.SimEventListenerConnectionType}
     * {@link simkit.xsd.bindings.assembly.SimEventListenerConnection}
     * 
     */
    java.util.List getSimEventListenerConnection();

    /**
     * Gets the value of the PropertyChangeListener property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the PropertyChangeListener property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyChangeListener().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link simkit.xsd.bindings.assembly.PropertyChangeListener}
     * {@link simkit.xsd.bindings.assembly.PropertyChangeListenerType}
     * 
     */
    java.util.List getPropertyChangeListener();

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getVersion();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setVersion(java.lang.String value);

    /**
     * Gets the value of the Output property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Output property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutput().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link simkit.xsd.bindings.assembly.Output}
     * {@link simkit.xsd.bindings.assembly.OutputType}
     * 
     */
    java.util.List getOutput();

    /**
     * Gets the value of the PropertyChangeListenerConnection property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the PropertyChangeListenerConnection property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyChangeListenerConnection().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link simkit.xsd.bindings.assembly.PropertyChangeListenerConnection}
     * {@link simkit.xsd.bindings.assembly.PropertyChangeListenerConnectionType}
     * 
     */
    java.util.List getPropertyChangeListenerConnection();

    /**
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getName();

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setName(java.lang.String value);

    /**
     * Gets the value of the SimEntity property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the SimEntity property.
     * 
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimEntity().add(newItem);
     * </pre>
     * 
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link simkit.xsd.bindings.assembly.SimEntityType}
     * {@link simkit.xsd.bindings.assembly.SimEntity}
     * 
     */
    java.util.List getSimEntity();

}