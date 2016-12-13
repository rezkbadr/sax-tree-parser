import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * Uses SAX parser to stream the XML file without loading the whole file in memory
 * and builds a map with XML file structure
 */
public class XMLParser {

	public static void main(String[] args) {

		try {
			// init in
			String filePath = null;
			if(args.length > 0)
				filePath = args[0];
			else {
				System.err.println("Input File is Required.");
				return;
			}
			
			// init SAX parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			UserHandler userHandler = new UserHandler();
			saxParser.parse(filePath, userHandler);

			// Print the structure map
			for (Entry<String, Integer> entry : userHandler.nodes.entrySet()) {
				System.out.println(entry.getKey() + ": [" + entry.getValue() + "]");
			}

		} catch (IOException e) {
			System.err.println("Error loading input file");
		} catch (SAXException e){
			System.err.println("Error parsing XML file");
		} catch (ParserConfigurationException e) {
			System.err.println("Error confirguring SAX");
		}
	}
}

/*
 * Event Handler Class
 * At the start of each tag do the following:
 * - append tag name to currentParent
 * - add currentParent to the map of tag names
 * At the end of each tag do the following:
 * - set currentParent with the previous parent 
 */
class UserHandler extends DefaultHandler {

	String currentParent = "";
	
	// Structure map which holds tag names as keys, and count as values
	Map<String, Integer> nodes = new LinkedHashMap<String, Integer>();

	@Override
	// Gets called at the start of each tag
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		// append tag name to currentParent
		currentParent = currentParent + "/" + qName;

		// add currentParent to the map of tag names
		if (nodes.get(currentParent.toString()) != null)
			nodes.put(currentParent, ((Integer) nodes.get(currentParent)) + 1);
		else
			nodes.put(currentParent, 1);
	}

	@Override
	// Gets called at the end of each tag
	public void endElement(String uri, String localName, String qName) throws SAXException {

		// set currentParent with the previous parent 
		currentParent = currentParent.substring(0, currentParent.lastIndexOf("/"));
	}
}
