package gr.forth.ics.isl.cerifdatatransformer.utils;

import gr.forth.ics.isl.cerifdatatransformer.Common;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class XmlAppender {
    private String sourceFolder;
    private String targetFolder;
    
    public XmlAppender(String source, String target){
        this.sourceFolder=source;
        this.targetFolder=target;
    }

    public void appendAllDocs() throws ParserConfigurationException, SAXException, IOException, TransformerException{
        int folderCnt=1;
        for(File file : new File(this.sourceFolder).listFiles()){
            Document newDoc=this.appendFile(file);
            boolean res=Common.exportToXMLAndCheckSpace(newDoc, this.targetFolder,file.getName(), folderCnt);
            if(res){
                folderCnt+=1;
            }
        }
    }
    
    private Document appendFile(File file) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        
        Document newDoc=db.newDocument();
        Node rootNode=newDoc.createElement("CERIF-API");
        Node payloadNode=newDoc.createElement("Payload");
        rootNode.appendChild(payloadNode);
        newDoc.appendChild(rootNode);
        
        doc.renameNode(doc.getDocumentElement(), null, "CERIF");
        
        Node newNode=newDoc.importNode(doc.getDocumentElement(), true);
        payloadNode.appendChild(newNode);
        
        return newDoc;
        
    }
    
    public static void main(String []args) throws ParserConfigurationException, SAXException, IOException, TransformerException{
        XmlAppender xmlAp=new XmlAppender("sourceFiles", "inputFiles");
        xmlAp.appendAllDocs();
    }
}