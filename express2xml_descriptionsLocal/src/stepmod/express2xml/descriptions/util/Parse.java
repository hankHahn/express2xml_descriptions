package stepmod.express2xml.descriptions.util;

import stepmod.express2xml.descriptions.util.Program.Semantics;

public class Parse {

    // variables
	private String content;
	private Semantics semantic;
	
    // constructors
    Parse(String content, Semantics semantic) {
        this.content = content;
        this.semantic = semantic;
    }
    
    // getters
    String getContent() {
        return this.content;
    }
    
    Semantics getSemantic() {
        return this.semantic;
    }
    
    // setters
    void setContent(String content) {
        this.content = content;
    }
    
    void setSemantic(Semantics semantic) {
        this.semantic = semantic;
    }
}