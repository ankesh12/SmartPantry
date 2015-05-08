package sg.edu.nus.iss.smartpantry.application.util;

import org.w3c.dom.Document;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by A0134493A on 8/5/2015.
 */
public class XMLUtil {


    public String getElementText(String tag, InputStream stream){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(stream);
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName(tag).item(0).getTextContent();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
