package stepmod.express2xml.descriptions.util;

import stepmod.express2xml.descriptions.util.Program.Semantics;

public class Parse {

    // variables
	private String content;
	private Semantics xmlType;
	
    // constructors
    Parse(String content, Semantics xmlType) {
        this.content = content;
        this.xmlType = xmlType;
    }
    
    // getters
    String getContent() {
        return this.content;
    }
    
    Semantics getXmlType() {
        return this.xmlType;
    }
    
    // setters
    void setContent(String content) {
        this.content = content;
    }
    
    void setXmlType(Semantics type) {
        this.xmlType = type;
    }
}