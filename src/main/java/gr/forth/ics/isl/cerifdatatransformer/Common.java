package gr.forth.ics.isl.cerifdatatransformer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Common {
    
    public static boolean exportToXMLAndCheckSpace(org.w3c.dom.Document document, String folder, String filename, int folderCnt) throws TransformerException, IOException{
        TransformerFactory transformerFactory=TransformerFactory.newInstance();
        Transformer transformer=transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source=new DOMSource(document);
        File newFile=createFileFolderCheck(folder, folderCnt, filename);
        StreamResult result=new StreamResult(newFile);
        transformer.transform(source, result);
//        Files.copy(new File("classification.xml").toPath(),
//                   new File(folder+folderCnt+"/classification.xml").toPath(),
//                   StandardCopyOption.REPLACE_EXISTING);
//        return checkSpace(folder+folderCnt);
        return false;
    }
        
    private static File createFileFolderCheck(String folderName, int folderCnt, String filename){
        if(!new File(folderName+""+folderCnt).exists()){
            new File(folderName+""+folderCnt).mkdir();
        }
        return new File(folderName+""+folderCnt+"/"+filename);
    }
    
    private static boolean checkSpace(String folderName){
        int size=0;
        for(File file : new File(folderName).listFiles()){
            size+=file.length();
        }
        return size>=10*1024*1024;
    }
}