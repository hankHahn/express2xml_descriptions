package stepdev.express2xml.descriptions.util;

import stepdev.express2xml.descriptions.util.Program.XmlTypes;

public class Parse {

    // variables
	private String content;
	private XmlTypes xmlType;
	
    // constructors
    Parse(String content, XmlTypes xmlType) {
        this.content = content;
        this.xmlType = xmlType;
    }
    
    // getters
    String getContent() {
        return this.content;
    }
    
    XmlTypes getXmlType() {
        return this.xmlType;
    }
    
    // setters
    void setContent(String content) {
        this.content = content;
    }
    
    void setXmlType(XmlTypes type) {
        this.xmlType = type;
    }
}