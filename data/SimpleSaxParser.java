/*
 * SimpleSaxParser.java
 *
 * Created on 4 mars 2005, 10:59
 */

package data;

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import visu.FrmConf;

/**
 *
 * @author segond
 */
public class SimpleSaxParser {
    
    private XMLReader saxReader;
    
    /** Creates a new instance of SimpleSaxParser */
    public SimpleSaxParser(FrmConf frm) throws SAXException, IOException {
        saxReader = XMLReaderFactory.createXMLReader();
        saxReader.setContentHandler(new XmlWorker(frm));
    }
    
    public void parse(String s){
        try{
        saxReader.parse(s);
        }catch(IOException e){System.out.println(e.toString());} catch (SAXException e) {
            System.out.println(e.toString());
        }
    }
    
    
}
