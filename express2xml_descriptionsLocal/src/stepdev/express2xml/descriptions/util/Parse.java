package stepdev.express2xml.descriptions.util;

import stepdev.express2xml.descriptions.util.Program.ContentTypes;

public class Parse {

    // variables
	private String content;
	private ContentTypes xmlType;
	
    // constructors
    Parse(String content, ContentTypes xmlType) {
        this.content = content;
        this.xmlType = xmlType;
    }
    
    // getters
    String getContent() {
        return this.content;
    }
    
    ContentTypes getXmlType() {
        return this.xmlType;
    }
    
    // setters
    void setContent(String content) {
        this.content = content;
    }
    
    void setXmlType(ContentTypes type) {
        this.xmlType = type;
    }
}