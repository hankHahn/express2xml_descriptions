package stepdev.express2xml.descriptions.util;

import stepdev.express2xml.descriptions.util.Program.XmlTypes;

public class XmlPart {

    // private variables
	private String text;
	private String elementOpen;
	private String elementClose;
	private XmlTypes xmlType;
		
    // constructors
    XmlPart(String text, XmlTypes xmlType) {
        this.text = text;
        this.xmlType = xmlType;
    }
    
    XmlPart(String elementOpen, String elementClose, XmlTypes xmlType) {
        this.elementOpen = elementOpen;
        this.elementClose = elementClose;
        this.xmlType = xmlType;
    }
    
    // getters
    public String getText() {
        return this.text;
    }
    
    public String getElementOpen() {
        return this.elementOpen;
    }
    
    public String getElementClose() {
        return this.elementClose;
    }
    
    public XmlTypes getEnumXmlType() {
        return this.xmlType;
    }
    
    // setters
	public void setText(String text) {
		this.text = text;
	}
	
	public void setElementOpen(String elementOpen) {
		this.elementOpen = elementOpen;
	}
	
	public void setElementClose(String elementClose) {
		this.elementClose = elementClose;
	}

	public void setXmlType(XmlTypes xmlType) {
		this.xmlType = xmlType;
	}
	    
}