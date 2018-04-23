package stepdev.express2xml.descriptions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import stepdev.express2xml.descriptions.util.jaxb.DescriptionsType;
import stepdev.express2xml.descriptions.util.jaxb.ExpressRefType;
import stepdev.express2xml.descriptions.util.jaxb.ExtDescriptionType;
import stepdev.express2xml.descriptions.util.jaxb.ObjectFactory;

public class Program {

	public static final String DESCRIPTIONS = "descriptions";
	public static final String EXTENDED_DESCRIPTION = "ext_description";
	public static final String EXPRESS_REFERENCE = "express_ref";
	public static final String LINKEND = "linkend";
	private static final String FILE_WITH_RESOURCES = "C:\\Users\\rd761d\\Desktop\\fileWithResource.xml";
	
	public static void main (String[] arguments) throws Exception{
		
		// request a file from the user
		File inputFile = new File(chooseFile());
	
		// store the contents of the file
		List<String> remarks = extractRemarks(inputFile);
		
		// parse remarks
		ArrayList<Parses> parsedRemarks = parseRemarks(remarks);
		
		// populate classes with parses
		JAXBElement<DescriptionsType> descriptions = generateDescriptions(parsedRemarks);
		
		// marshal classes to xml
		marshal2xml(descriptions, makeFilename(inputFile.getPath()));

		// end program
	}
	private static JAXBElement<DescriptionsType> generateDescriptions(List<Parses> parsedRemarks) throws XPathException {

		// setup factory
		ObjectFactory factory = new ObjectFactory();
		
		// new root element
		DescriptionsType descriptions = factory.createDescriptionsType();
		JAXBElement<DescriptionsType> descriptionsWrapped = factory.createDescriptions(descriptions);
		
		for (Parses parsedRemark : parsedRemarks) {
			// create list to call values from
			List<Parse> parse = parsedRemark.getParse();
			
			// new sub element
			ExpressRefType e2 = factory.createExpressRefType();
			JAXBElement<ExpressRefType> e2wrapped = factory.createExpressRef(e2);
			
			// content
			String attribute = getResourceValue(parse.get(4).getContent());
			e2.setLinkend(attribute);
			
			// new element
			ExtDescriptionType e1 = factory.createExtDescriptionType();
			String text = 
					parse.get(1).getContent()
					+ XmlTags.BOLD_TEXT.getStart()
					+ parse.get(2).getContent()
					+ XmlTags.BOLD_TEXT.getFinish()
					+ parse.get(3).getContent();
			
			attribute = parse.get(0).getContent();
			e1.setLinkend(attribute);
			
			// assemble mixed content
			List contents = e1.getContent();
			contents.add(text);
			contents.add(e2wrapped);
			
			// add to root element
			descriptions.getExtDescription().add(e1);
		}
		return descriptionsWrapped;
	}
	private static String getResourceValue(String parse) throws XPathException {
		// modify the value
		int i = parse.indexOf(".");
		parse = parse.substring(i + 1);
		// lookup the value
		String attribute = findResource(parse, FILE_WITH_RESOURCES);
		return attribute;
	}
	private static String findResource(String lookup, String fileWithResources) throws XPathException {
        try {
            // load file
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	File file = new File(fileWithResources);
            Document doc = factory.newDocumentBuilder().parse(file);
            
            // load factory
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xPath = xFactory.newXPath();
            
            // build expression
//			String expression = "//asdf[descendant::entity[text()=" + "'" + lookup + "'" + "]]";
//			String expression = "//entity[text()=" + "'" + lookup + "'" + "]";
//			String expression = "//entity[fn:ends-with(text()," + "\"" + lookup + "\"" + ")]";
			String expression = 
					"//entity["
							+ "\""
							+ lookup
							+ "\""
							+ "= substring(text(), string-length(text()) - string-length("
							+ "\""
							+ lookup
							+ "\""
							+ ") +1)]";

            XPathExpression compiledExpression = xPath.compile(expression);
            
            // evaluate expression
			
//			lookup = (String) compiledExpression.evaluate(doc, XPathConstants.NODE);
//			Node node = (Node) compiledExpression.evaluate(doc.getFirstChild(), XPathConstants.NODE);
//            Object x = compiledExpression.evaluate(doc, XPathConstants.STRING);
            		
			NodeList nodeList = (NodeList) compiledExpression.evaluate(doc, XPathConstants.NODESET);

			// handle evaluation
            if (nodeList.getLength() == 1) {
            	lookup = nodeList.item(0).getTextContent();
            }
			
			for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println("xPath found:" + node.getNodeType() + "/" + node.getNodeName() + "/" + node.getTextContent());
            }
        } catch (Exception ex) {
        	System.err.println(ex);
//            Logger.getLogger(findResource.class.getName()).log(Level.SEVERE, null, ex);
        }
		return lookup;
	}
	private static void marshal2xml(JAXBElement<DescriptionsType> descriptions, String filename) throws JAXBException {
		// initialize marshaller
		JAXBContext jaxbContext = JAXBContext.newInstance(DescriptionsType.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		
		// set marshaller properties
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true );
		
		// marshal to ouput
		jaxbMarshaller.marshal(descriptions, new File(filename));
		jaxbMarshaller.marshal(descriptions, System.out);
	}
	public enum Regexs { // each item is a declaration of the position and meaning of content
		Quote_Content_Quote ("[\"][\\w ]+[\"]", XmlTypes.ATTRIBUTE),
		Quote_Content_CaretOpen ("[\"][\\w ]*[<]", XmlTypes.TEXT),
		CaretOpen_Content_CaretClose ("[<][\\w]+[>]", XmlTypes.BOLD_TEXT),
		CaretClose_Content_BracketOpen ("[>][\\w ]+[\\[]", XmlTypes.TEXT),
		BracketOpen_Content_BracketClose ("[\\[][\\w]+[.][\\w]+[\\]]", XmlTypes.ATTRIBUTE);
		
	    // variables
	    private String regex;
	    private XmlTypes semantic;
	    
	    // constructors
	    private Regexs(String enumRegex, XmlTypes enumSemantic) {
	        this.regex = enumRegex;
	        this.semantic = enumSemantic;
	    }
	    
	    // getters
	    public String getRegex() {
	        return this.regex;
	    }
	    
	    public XmlTypes getSemantic() {
	        return this.semantic;
	    }
	}
	public enum XmlTypes { // each item is a supported capability within XML
		ATTRIBUTE (XmlTags.ATTRIBUTE),
		ELEMENT (XmlTags.ELEMENT_START, XmlTags.ELEMENT_END),
		TEXT (),
		BOLD_TEXT (XmlTags.BOLD_TEXT),
		COMMENT (XmlTags.COMMENT);
		
	    // variables
	    private XmlTags open;
	    private XmlTags close;
	    private XmlTags tag;
	    
	    // constructors
	    private XmlTypes(XmlTags enumOpen, XmlTags enumClose) {
	        this.open = enumOpen;
	        this.close = enumClose;
	    }
	    
	    private XmlTypes(XmlTags enumTag) {
	    	this.tag = enumTag;
	    }

	    private XmlTypes() {
	    }
	    
	    // getters
	    public XmlTags getTag() {
	        return this.tag;
	    }
	    
	    public XmlTags getOpen() {
	        return this.open;
	    }
	    
	    public XmlTags getClose() {
	        return this.close;
	    }
	}
	public enum XmlTags { // each item is expected within XML
		ELEMENT_START ("<", ">"),
		ELEMENT_END ("</", ">"),
		COMMENT ("<!--", "-->"),
		ATTRIBUTE ("=\"", "\""),
		BOLD_TEXT ("<" + "b>", "<" + "/b>");

		// variables
	    private String start;
	    private String finish;
	    
	    // constructors
	    private XmlTags(String enumStart, String enumFinish) {
	        this.start = enumStart;
	        this.finish = enumFinish;
	    }
	    
	    // getters
	    public String getStart() {
	        return this.start;
	    }
	    
	    public String getFinish() {
	        return this.finish;
	    }
	}
//	public enum XmlConstructors { // this enum develops the parts needed for the final assembly
//		HEADER 
//			(2, XmlLabels.MULTI_PLUS, XmlTypes.COMMENT),
//		ELEMENT_1 
//			(XmlLabels.EXTENDED_DESCRIPTION, XmlTypes.ELEMENT),
//		ATTRIBUTE_1 
//			(0, XmlLabels.LINKEND, XmlTypes.ATTRIBUTE),
//		TEXT_1 
//			(1, XmlTypes.TEXT),
//		TEXT_BOLD_1 
//			(2, XmlTypes.BOLD_TEXT),
//		TEXT_2 
//			(3, XmlTypes.TEXT),
//		ELEMENT_2 
//			(XmlLabels.EXPRESS_REFERENCE, XmlTypes.ELEMENT),
//		ATTRIBUTE_2 
//			(4, XmlLabels.LINKEND, XmlTypes.ATTRIBUTE);
//		
//	    // variables
//	    private int contentId;
//	    private XmlLabels xmlLabel;
//	    private XmlTypes xmlType;
//	    
//	    // constructors
//	    private XmlConstructors(int enumContentId, XmlLabels enumXmlLabel, XmlTypes enumXmlType) {
//	        this.contentId = enumContentId;
//	        this.xmlLabel = enumXmlLabel;
//	        this.xmlType = enumXmlType;
//	    }
//	    
//	    private XmlConstructors(XmlLabels enumXmlLabel, XmlTypes enumXmlType) {
//	        this.xmlLabel = enumXmlLabel;
//	        this.xmlType = enumXmlType;
//	    }
//	    
//	    private XmlConstructors(int enumContentId, XmlTypes enumXmlType) {
//	        this.contentId = enumContentId;
//	        this.xmlType = enumXmlType;
//	    }
//	    
//	    // getters
//	    public int getContentId() {
//	        return this.contentId;
//	    }
//	    
//	    public XmlLabels getXmlLabel() {
//	        return this.xmlLabel;
//	    }
//	    
//	    public XmlTypes getXmlType() {
//	        return this.xmlType;
//	    }
//	}	
	private static String chooseFile() { // prompts the user to select an input file
		// initialize a new chooser
	    JFileChooser chooser = new JFileChooser();
	    
	    // define a filter
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("text files", "txt");
	    
	    // apply the filter
	    chooser.setFileFilter(filter);
	    
	    // show the dialog to the user and store the response
	    int returnVal = chooser.showOpenDialog(null);
	    
	    // handle the response
	    switch (returnVal) {
		    case JFileChooser.APPROVE_OPTION:
		    	System.out.println("chooseFile=" + chooser.getSelectedFile().getAbsolutePath());
		    	return chooser.getSelectedFile().getAbsolutePath();
			case JFileChooser.CANCEL_OPTION:
				System.out.println("chooseFile=Cancelled by user.");
				return null;
			case JFileChooser.ERROR_OPTION:
				System.err.println("chooseFile=error");
				return null;
		}
	    return null;
    }
	private static int getLastIndex(List<String> inputLines) {
		int lastIndex = inputLines.size() -1;
		return lastIndex;
	}
	private static String makeFilename(String filePath) {
		// declare variable
		String outputFilename;
		
		// define the extension
		final String FILE_EXTENSION = ".xml";
		
		// declare formatter independent of locale
		DateFormat time = DateFormat.getDateTimeInstance();
		
		// try to cast to extended class to utilize method for custom pattern definition
		try {
			// cast
			SimpleDateFormat timeFormat = (SimpleDateFormat) time;
			
			// define pattern and apply
			String pattern = "'_'YYYYMMdd'_'kkmmss";
			timeFormat.applyPattern(pattern);
			
			// construct the name
			outputFilename = filePath + timeFormat.format(new Date()) + FILE_EXTENSION;
			
		} catch (Exception e) {
			e.printStackTrace();
			// inform reader of exception handling
			System.out.println("Exception thrown regarding localization; filename will not include timestamp.");
			
			// construct the name
			outputFilename = filePath + FILE_EXTENSION;
		}
		return outputFilename;
	}
	private static ArrayList<Parses> parseRemarks(List<String> remarks) {
		ArrayList<Parses> parses = new ArrayList<Parses>();
		
		// count remarks
		int totalRemarks = remarks.size();
		
		// process remarks
		for (int i = 0; i < totalRemarks; i++) {    
			
			// decompose into parses
			List<Parse> parsedRemark = parseRemark(remarks.get(i));
			
			parses.add(new Parses(parsedRemark));
		}
		return parses;
	}
	private static List<Parse> parseRemark(String remark) {
		// parse the content elements of the remark using regular expressions then store
		List<Parse> parses = new ArrayList<Parse>();
		
		// declare variables to support regular expressions
		Pattern pattern;
		Matcher matcher;
		boolean foundMatch;
		
		// pass enum to array for looping
		Regexs[] expressions = Regexs.values();
		
		for (Regexs expression : expressions) {
			
			// initialize variables to support regular expressions
			pattern = Pattern.compile(expression.getRegex());
			matcher = pattern.matcher(remark);
			foundMatch = matcher.find();
			
			// test if the parse matches an expression
			if (foundMatch == true) {
				// store the parse
				String e = remark.substring(matcher.start() + 1, matcher.end() - 1);
				XmlTypes t = expression.getSemantic();
				parses.add(new Parse(e, t));

				// debug
				System.out.println("parseRemark" + parses.size() + "=" + parses.get(parses.size()-1).getContent());
			} 
		}
		return parses;
	}
	private static List<String> extractRemarks(File inputFile) throws IOException, FileNotFoundException {
		final String remarkOpener = "(*";
		final String remarkCloser = "*)";

		try (
				FileReader fileContents = new FileReader(inputFile);
				BufferedReader bufferedContents = new BufferedReader(fileContents)){

			String inputLine;
	  
			List<String> inputLines = new ArrayList<String>();

			boolean previousLineIsRemarkLine = false;
			int lastIndex;
			
			// while input lines have content
			while((inputLine = bufferedContents.readLine()) != null){
				// if it starts and ends properly
				if (inputLine.startsWith(remarkOpener) && inputLine.endsWith(remarkCloser)) {
					inputLines.add(inputLine);
					previousLineIsRemarkLine = false;
				// if it starts but doesn't end properly
				} else if (inputLine.startsWith(remarkOpener) && !inputLine.endsWith(remarkCloser)) {
					inputLines.add(inputLine);
					previousLineIsRemarkLine = true;
				// if it ends but doesn't start properly
				} else if (!inputLine.startsWith(remarkOpener) && inputLine.endsWith(remarkCloser) && previousLineIsRemarkLine == true) {
					lastIndex = getLastIndex(inputLines);
					inputLines.set(lastIndex, inputLines.get(lastIndex) + inputLine);
					previousLineIsRemarkLine = false;
				// if this line is a continuation
				} else if (previousLineIsRemarkLine == true) {
					lastIndex = getLastIndex(inputLines);
					inputLines.set(lastIndex, inputLines.get(lastIndex) + inputLine);
				}
			}
			// debug
			for (String e : inputLines) {
				System.out.println("storeContents=" + e);
			}
			return inputLines;
		}
	}
//	private static List<XmlPart> buildParts(List<Parse> parses) {
//		// make parts
//		List<XmlPart> parts = new ArrayList<XmlPart>();
//
//		// load the constructors
//		XmlConstructors[] xmlConstructors = XmlConstructors.values();
//
//		String partText = null;
//		Parse parse;
//		String content;
//		String label = null;
//		XmlTypes type = null;
//		String tagStart;
//		String tagFinish;
//		String tagOpenStart;
//		String tagOpenFinish;
//		String tagCloseStart;
//		String tagCloseFinish;
//		
//		// use each constructor to make the parts
//		for (XmlConstructors xmlConstructor : xmlConstructors) {
//			
//			// get the type
//			type = xmlConstructor.getXmlType();
//					
//			// test the XmlType
//			switch (type) {
//				case ATTRIBUTE: 
//					// get the parse
//					parse = parses.get(xmlConstructor.getContentId());
//					// get the content
//					content = parse.getContent();
//					// get the tags
//					tagStart = type.getTag().getStart();
//					tagFinish = type.getTag().getFinish();
//					// get the label
//					label = xmlConstructor.getXmlLabel().getLabel(); // this tells you the label for the tag
//					// assemble tag
//					partText = tagStart + label + tagFinish;
//					parts.add(new XmlPart (partText, type));
//					break;
//				case BOLD_TEXT: 
//					// get the parse
//					parse = parses.get(xmlConstructor.getContentId());
//					// get the content
//					content = parse.getContent();
//					// get the tags
//					tagStart = type.getTag().getStart();
//					tagFinish = type.getTag().getFinish();
//					// assemble tag
//					partText = tagStart + content + tagFinish;
//					parts.add(new XmlPart (partText, type));
//					break;
//				case TEXT: 
//					// get the parse
//					parse = parses.get(xmlConstructor.getContentId());
//					// get the content
//					content = parse.getContent();
//					partText = content;
//					parts.add(new XmlPart (partText, type));
//					break;
//				case COMMENT:
//					// get the parse
//					parse = parses.get(xmlConstructor.getContentId());
//					// get the content
//					content = parse.getContent();
//					// get the tags
//					tagStart = type.getTag().getStart();
//					tagFinish = type.getTag().getFinish();
//					// get the label
//					label = xmlConstructor.getXmlLabel().getLabel(); // this tells you the label for the tag
//					// assemble tag
//					partText = tagStart + label + content + label + tagFinish;
//					parts.add(new XmlPart (partText, type));
//					break;
//					
//				case ELEMENT:
//					// get the tags
//					tagOpenStart = type.getOpen().getStart();
//					tagOpenFinish = type.getOpen().getFinish();
//					tagCloseStart = type.getClose().getStart();
//					tagCloseFinish = type.getClose().getFinish();
//					
//					// get the label
//					label = xmlConstructor.getXmlLabel().getLabel(); // this tells you the label for the tag
//					// assemble the opening tag
//					String elementOpen = tagOpenStart + label + tagOpenFinish;
//					// assemble the closing tag
//					String elementClose = tagCloseStart + label + tagCloseFinish;
//					
//					parts.add(new XmlPart (elementOpen, elementClose, type));
//					break;
//				default:
//					break;
//			}
//			XmlPart debug = parts.get(parts.size() - 1);
//			switch (type) {
//				case ELEMENT:
//					System.out.println("part" + parts.size() + "(" + debug.getEnumXmlType().name() + ")" + "=" + debug.getElementOpen() + debug.getElementClose());
//					break;
//				default:
//					System.out.println("part" + parts.size() + "(" + debug.getEnumXmlType().name() + ")" + "=" + debug.getText());
//					break;
//			}
//
//		}
//		return parts;
//	}
//	private static String assembleParts(XmlTypes type, String content) {
//			String e = type.getOpen().getStart();
//			String f = content;
//			String g = type.getClose().getStart();
//		return e + f + g;
//	}
//	private static void writeContents(List<String> outputLines, String ouputFilename) throws IOException {
//		try (FileWriter outputFile = new FileWriter(ouputFilename)) {
//			for(String outputLine : outputLines) {
//				outputFile.write(outputLine);
//			}
//		}
//	}
}