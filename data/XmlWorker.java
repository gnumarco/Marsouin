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

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import visu.FrmConf;

/**
 *
 * @author segond
 */
public class XmlWorker extends DefaultHandler{
    
    private FrmConf maFrmConf;
    
    /** Creates a new instance of WriteXml */
    public XmlWorker(FrmConf f) {
        super();
        maFrmConf = f;
    }
    
    public XmlWorker() {
        super();
    }
    
    /**
     * Evenement envoye au demarrage du parse du flux xml.
     * @throws SAXException en cas de probleme quelquonque ne permettant pas de
     * se lancer dans l'analyse du document.
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Starting config file analysis");
    }
    
    /**
     * Evenement envoye a la fin de l'analyse du flux xml.
     * @throws SAXException en cas de probleme quelquonque ne permettant pas de
     * considerer l'analyse du document comme etant complete.
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("Ending config file analysis" );
    }
    

    @Override
    public void startPrefixMapping(String prefix, String URI) throws SAXException {
        System.out.println("Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);
    }
    

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        System.out.println("Fin de traitement de l'espace de nommage : " + prefix);
    }
    
    /**
     * Evenement recu a chaque fois que l'analyseur rencontre une balise xml ouvrante.
     * @param nameSpaceURI l'url de l'espace de nommage.
     * @param localName le nom local de la balise.
     * @param rawName nom de la balise en version 1.0 <code>nameSpaceURI + ":" + localName</code>
     * @throws SAXException si la balise ne correspond pas a ce qui est attendu,
     * comme par exemple non respect d'une dtd.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String nameSpaceURI, String localName, String rawName, Attributes attributs) throws SAXException {
        //System.out.println("Ouverture de la balise : " + localName);
        
        if ( ! "".equals(nameSpaceURI)) { // espace de nommage particulier
            System.out.println("  appartenant a l'espace de nom : "  + nameSpaceURI);
        }
        
        //System.out.println("  Attributs de la balise : ");
        
        for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
            System.out.println("     - " +  attributs.getLocalName(index) + " = " + attributs.getValue(index));
            theBigCase(localName,attributs.getLocalName(index),attributs.getValue(index));
        }
    }
    
    public void theBigCase(String balise, String param, String value){
        if("fourmis".equals(balise)){
            if("nbfourm".equals(param)){
                maFrmConf.setNbFourmis(Integer.parseInt(value));
            }
            if("nbgen".equals(param)){
                maFrmConf.setNbGen(Integer.parseInt(value));
            }
            if("nbcolo".equals(param)){
                maFrmConf.setNbColo(Integer.parseInt(value));
            }
            if("depot".equals(param)){
                maFrmConf.setDepot(Double.parseDouble(value));
            }
            if("evap".equals(param)){
                maFrmConf.setEvap(Double.parseDouble(value));
            }
            if("biais".equals(param)){
                maFrmConf.setBiais(Double.parseDouble(value));
            }
        }
    }
    /**
     * Evenement recu a chaque fermeture de balise.
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {
        //System.out.print("Fermeture de la balise : " + localName);
        
        if ( ! "".equals(nameSpaceURI)) { // name space non null
            System.out.print("appartenant a l'espace de nommage : " + localName);
        }
        
        System.out.println();
    }
    
    /**
     * Evenement recu a chaque fois que l'analyseur rencontre des caracteres (entre
     * deux balises).
     * @param ch les caracteres proprement dits.
     * @param start le rang du premier caractere a traiter effectivement.
     * @param end le rang du dernier caractere a traiter effectivement
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int end) throws SAXException {
        System.out.println("#PCDATA : " + new String(ch, start, end));
    }
    
    /**
     * Recu chaque fois que des caracteres d'espacement peuvent etre ignores au sens de
     * XML. C'est a dire que cet evenement est envoye pour plusieurs espaces se succedant,
     * les tabulations, et les retours chariot se succedants ainsi que toute combinaison de ces
     * trois types d'occurrence.
     * @param ch les caracteres proprement dits.
     * @param start le rang du premier caractere a traiter effectivement.
     * @param end le rang du dernier caractere a traiter effectivement
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    @Override
    public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
        System.out.println("espaces inutiles rencontres : ..." + new String(ch, start, end) +  "...");
    }
    
    /**
     * Rencontre une instruction de fonctionnement.
     * @param target la cible de l'instruction de fonctionnement.
     * @param data les valeurs associees a cette cible. En general, elle se presente sous la forme
     * d'une serie de paires nom/valeur.
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        System.out.println("Instruction de fonctionnement : " + target);
        System.out.println("  dont les arguments sont : " + data);
    }
    
    /**
     * Recu a chaque fois qu'une balise est evitee dans le traitement a cause d'un
     * probleme non bloque par le parser. Pour ma part je ne pense pas que vous
     * en ayez besoin dans vos traitements.
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    @Override
    public void skippedEntity(String arg0) throws SAXException {
        // Je ne fais rien, ce qui se passe n'est pas franchement normal.
        // Pour eviter cet evenement, le mieux est quand meme de specifier une dtd pour vos
        // documents xml et de les faire valider par votre parser.
    }
    
}
