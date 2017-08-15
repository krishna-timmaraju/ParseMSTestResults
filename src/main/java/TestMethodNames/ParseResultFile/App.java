package TestMethodNames.ParseResultFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class App {
	 
    public static void main(String[] args) {
    	String filePath = "C:\\Users\\krishnasw\\Desktop\\TestResults.xml";
    	parseTestResults(filePath, 1);
    	filePath = "C:\\Users\\krishnasw\\Desktop\\TestResults2.xml";
    	parseTestResults(filePath, 2);
    }
    
private static void parseTestResults(String filePath, int result)
{
File xmlFile = new File(filePath);
DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
DocumentBuilder dBuilder;
try {
    dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(xmlFile);
    doc.getDocumentElement().normalize();
    NodeList nodeList2 = doc.getElementsByTagName("TestMethod");
    NodeList nodeList = doc.getElementsByTagName("UnitTest");
    //now XML is loaded as Document in memory, lets convert it to Object List
    List<String> testNameList = new ArrayList<String>();
    //Debug
    //System.out.println("UNIT TEST NODES::"+nodeList.getLength());
    for (int i = 0; i <nodeList.getLength(); i++) {
        testNameList.add(getTestName(nodeList.item(i)));
    }
    List<String> testSuiteList = new ArrayList<String>();
    List<String> testCaseList = new ArrayList<String>();
    
    HashMap<String,String> testCases = new HashMap<String,String>();

    //Debug
    System.out.println("TEST METHOD NODES::"+nodeList2.getLength());
    for (int i = 0; i < nodeList2.getLength();i++) {
    	testCaseList.add(getTestCaseName(nodeList2.item(i)));
    	testSuiteList.add(getTestSuiteName(nodeList2.item(i)));
    	testCases.put(testCaseList.get(i), testSuiteList.get(i));
    }
    
    String eol = System.getProperty("line.separator");

	try (Writer writer = new FileWriter("SystemTestCases_"+result+".csv")) {
		for (Map.Entry<String, String> entry : testCases.entrySet()) {
        String testCaseName = parseTestCaseName(entry.getKey());
        String testSuiteName = parseTestSuiteName(entry.getValue());
        writer.append(testCaseName)
        	.append(',')
        	.append(testSuiteName)
        	.append(eol);
        //DEBUG
        System.out.println("TEST METHODS MAP::"+testCases.size());
        System.out.println(testCaseName + "," + testSuiteName);
     }
	}
    catch(IOException ex) {
    	ex.printStackTrace();
    }  
    
} catch (SAXException | ParserConfigurationException | IOException e1) {
    e1.printStackTrace();
}

}

private static String getTestName(Node node) {
	return node.getAttributes().getNamedItem("name").toString();
}

private static String getTestSuiteName(Node node) {
	return node.getAttributes().getNamedItem("className").toString();
}

private static String getTestCaseName(Node node) {
	return node.getAttributes().getNamedItem("name").toString();
}

private static String parseTestCaseName(String str){
	String[] temp = str.split("=");
	String tempName=temp[1].replace("\"", "");
	return tempName;
}

private static String parseTestSuiteName(String str){
	String[] temp = str.split("=");
	String tempName=temp[1].replace("\"", "");
	temp = tempName.split(",");
	String tempSuite = temp[0].replace("SystemTests.", "");
	return tempSuite;
}


}
