/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    
    private final XMLReader saxReader;
    
    /** Creates a new instance of SimpleSaxParser */
    public SimpleSaxParser(FrmConf frm) throws SAXException, IOException {
        saxReader = XMLReaderFactory.createXMLReader();
        saxReader.setContentHandler(new XmlWorker(frm));
    }
    
    public void parse(String s){
        try{
        saxReader.parse(s);
        }catch(IOException | SAXException e){System.out.println(e.toString());}
    }
    
    
}
