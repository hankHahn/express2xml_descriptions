package stepmod.express2xml.descriptions.util;

import java.util.List;

public class Parses {

    // variables
	private List<Parse> parse;
	
    // constructors
    Parses(List<Parse> parse) {
    	this.parse = parse;
    }

	List<Parse> getParse() {
		return parse;
	}

	void setParse(List<Parse> parse) {
		this.parse = parse;
	}
    
}