//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.16 at 04:44:21 PM EST 
//


package org.example.megaballs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MegaBall complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MegaBall"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Numbers" type="{http://www.example.org/MegaBalls}Numbers"/&gt;
 *         &lt;element name="Mega" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MegaBall", propOrder = {
    "date",
    "numbers",
    "mega"
})
public class MegaBall {

    @XmlElement(name = "Date", required = true)
    protected String date;
    @XmlElement(name = "Numbers", required = true)
    protected Numbers numbers;
    @XmlElement(name = "Mega", required = true)
    protected String mega;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the numbers property.
     * 
     * @return
     *     possible object is
     *     {@link Numbers }
     *     
     */
    public Numbers getNumbers() {
        return numbers;
    }

    /**
     * Sets the value of the numbers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Numbers }
     *     
     */
    public void setNumbers(Numbers value) {
        this.numbers = value;
    }

    /**
     * Gets the value of the mega property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMega() {
        return mega;
    }

    /**
     * Sets the value of the mega property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMega(String value) {
        this.mega = value;
    }

}