package stepmod.express2xml.descriptions.util.express;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpressSchema {
    // variables
//	private String schemaName;
//	private String schemaAttributes;
	private String schemaName;
	private String schemaAttributes;
	protected List<Serializable> schemaContent;
	
	
    // constructors
    ExpressSchema(String schemaName, String schemaAttributes) {
        this.schemaName = schemaName;
        this.schemaAttributes = schemaAttributes;
    }
    
    // getters
    String getContent() {
        return this.schemaName;
    }
    
    String getSemantic() {
        return this.schemaAttributes;
    }
    
    // setters
    void setContent(String content) {
        this.schemaName = content;
    }
    
    void setSemantic(String schemaAttributes) {
        this.schemaAttributes = schemaAttributes;
    }

    public List<Serializable> schemaContent() {
        if (schemaContent == null) {
            schemaContent = new ArrayList<Serializable>();
        }
        return this.schemaContent;
    }

}
