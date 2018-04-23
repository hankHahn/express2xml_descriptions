package stepdev.express2xml.descriptions.util;

public enum XmlLabels {
	EXTENDED_DESCRIPTION (Program.EXTENDED_DESCRIPTION),
	EXPRESS_REFERENCE (Program.EXPRESS_REFERENCE),
	LINKEND (Program.LINKEND),
	MULTI_PLUS (" +++++++++++ ");
	
    // variables
    private String label;
    
    // constructors
    private XmlLabels(String label) {
        this.label = label;
    }
    
    // getters
    public String getLabel() {
        return this.label;
    }
}