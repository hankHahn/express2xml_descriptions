package stepmod.express2xml.descriptions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import stepmod.express2xml.descriptions.util.jaxb.ExpressRefType;
import stepmod.express2xml.descriptions.util.jaxb.ExtDescriptionType;
import stepmod.express2xml.descriptions.util.jaxb.ExtDescriptionsType;
import stepmod.express2xml.descriptions.util.jaxb.ObjectFactory;

public class Program {

	// TODO update this constant to use a relative path in an ant scenario
	private static final String FILE_WITH_RESOURCES = "C:\\Users\\rd761d\\Desktop\\fileWithResource.xml";
	public static void main (String[] arguments) throws Exception{
		
		// request a file from the user
		File inputFile = new File(chooseFile());
		/* TODO
		 * add an option to run the program in an ant scenario
		 * with a known location of the input; use a relative path
		 */
	
		// store the contents of the file
		List<String> remarks = extractRemarks(inputFile);
		
		// parse remarks
		ArrayList<Parses> parsedRemarks = parseRemarks(remarks);
		
		// populate classes with parses
		JAXBElement<ExtDescriptionsType> descriptions = generateDescriptions(parsedRemarks);
		
		// marshal classes to xml
		marshal2xml(descriptions, makeFilename(inputFile.getPath()));
	
		// end program
	}
	public enum Semantics { // each item is a meaning
		ATTRIBUTE,
		ELEMENT,
		TEXT,
		BOLD_TEXT,
		COMMENT;
	}
	public enum Regexs { // each item is a declaration of the position and meaning of content
		Quote_Content_Quote ("[\"][\\w ]+[.][\\w ]+[\"]", Semantics.ATTRIBUTE),
		Quote_Content_CaretOpen ("[\"][\\w ]*[<]", Semantics.TEXT),
		CaretOpen_Content_CaretClose ("[<][\\w]+[>]", Semantics.BOLD_TEXT),
		CaretClose_Content_BracketOpen ("[>][\\w ]+[\\[]", Semantics.TEXT),
		BracketOpen_Content_BracketClose ("[\\[][\\w]+[.][\\w]+[\\]]", Semantics.ATTRIBUTE);
		
		// TODO consider this as a profile; make new enum for each profile?
		
	    // variables
	    private String regex;
	    private Semantics semantic;
	    
	    // constructors
	    private Regexs(String enumRegex, Semantics enumSemantic) {
	        this.regex = enumRegex;
	        this.semantic = enumSemantic;
	    }
	    
	    // getters
	    public String getRegex() {
	        return this.regex;
	    }
	    
	    public Semantics getSemantic() {
	        return this.semantic;
	    }
	}
	private static String chooseFile() { // prompts the user to select an input file
		// initialize a new chooser
	    JFileChooser chooser = new JFileChooser();
	    
	    // define a filter
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("express files", "exp");
	    
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
	private static String findResource(String lookup, String fileWithResources) throws XPathException {
        try {
            // load file
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	File file = new File(fileWithResources);
            Document doc = factory.newDocumentBuilder().parse(file);
            
            // load factory
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xPath = xFactory.newXPath();
            
            // build expression in xPath 2.0
            // "the entity that ends with the lookup value"
//			String expression = "//entity[fn:ends-with(text()," + "\"" + lookup + "\"" + ")]";
            
            // build expression in xPath 1.0
            // "the entity that ends with the lookup value"
            String expression = 
					"//entity["
							+ "\""
							+ lookup
							+ "\""
							+ "= substring(text(), string-length(text()) - string-length("
							+ "\""
							+ lookup
							+ "\""
							+ ") +1)"
					+ "]";

            XPathExpression compiledExpression = xPath.compile(expression);
            
            // evaluate expression
			NodeList nodeList = (NodeList) compiledExpression.evaluate(doc, XPathConstants.NODESET);

			// handle evaluation
            if (nodeList.getLength() == 1) {
            	lookup = nodeList.item(0).getTextContent();
            }
			
			for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println("xPath found:" + node.getNodeName() + "<" + node.getNodeType() + "> " + node.getTextContent());
            }
        } catch (Exception ex) {
        	System.err.println(ex);
        }
		return lookup;
	}
	private static JAXBElement<ExtDescriptionsType> generateDescriptions(List<Parses> parsedRemarks) throws XPathException {

		// setup factory
		ObjectFactory factory = new ObjectFactory();
		
		// new root element
		ExtDescriptionsType descriptions = factory.createExtDescriptionsType();
		JAXBElement<ExtDescriptionsType> descriptionsWrapped = factory.createExtDescriptions(descriptions);
		
		for (Parses parsedRemark : parsedRemarks) {
			// stage the parses
			List<Parse> parses = parsedRemark.getParse();
			
			// new sub element
			ExpressRefType e2 = factory.createExpressRefType();
			JAXBElement<ExpressRefType> e2wrapped = factory.createExpressRef(e2);
			
			// assign content
			String attribute = getResourceValue(parses.get(4).getContent());
			e2.setLinkend(attribute);
			
			// new element
			ExtDescriptionType e1 = factory.createExtDescriptionType();
			String text = 
					parses.get(1).getContent()
					+ "<b>"
					+ parses.get(2).getContent()
					+ "</b>"
					+ parses.get(3).getContent();
			
			attribute = parses.get(0).getContent();
			e1.setLinkend(attribute);
			
			// assign mixed content
			List<Serializable> contents = e1.getContent();
			contents.add(text);
			contents.add(e2wrapped);
			
			// add to root element
			descriptions.getExtDescription().add(e1);
		}
		return descriptionsWrapped;
	}
	private static int getLastIndex(List<String> inputLines) {
		int lastIndex = inputLines.size() -1;
		return lastIndex;
	}
private static String getResourceValue(String parse) throws XPathException {
		// isolate the value
		int i = parse.indexOf(".");
		parse = parse.substring(i + 1);
		
		// lookup the value
		return findResource(parse, FILE_WITH_RESOURCES);
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
	private static void marshal2xml(JAXBElement<ExtDescriptionsType> descriptions, String filename) throws JAXBException {
		// initialize marshaller
		JAXBContext jaxbContext = JAXBContext.newInstance(ExtDescriptionsType.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		
		// set marshaller properties
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true );
		
		// marshal to ouput
		jaxbMarshaller.marshal(descriptions, new File(filename));
		jaxbMarshaller.marshal(descriptions, System.out);
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
				Semantics t = expression.getSemantic();
				parses.add(new Parse(e, t));

				// debug
				System.out.println("parseRemark" + parses.size() + "=" + parses.get(parses.size()-1).getContent());
			} else {
				System.out.println("this remark does not match a known profile: " + remark);
				parses.clear();
				break;
			}
		}
		return parses;
	}
	private static ArrayList<Parses> parseRemarks(List<String> remarks) {
		ArrayList<Parses> parses = new ArrayList<Parses>();
		
		// count remarks
		int totalRemarks = remarks.size();
		
		// process remarks
		for (int i = 0; i < totalRemarks; i++) {    
			
			// decompose into parses
			List<Parse> parsedRemark = parseRemark(remarks.get(i));
			
			if (parsedRemark.size() == 5) {
				parses.add(new Parses(parsedRemark));
			}
		}
		return parses;
	}
}