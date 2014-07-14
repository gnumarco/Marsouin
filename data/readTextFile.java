/*
 * readTextFile.java
 *
 * Created on 25 janvier 2002, 21:36
 */

/*
 * @author Mahler,Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package data;

import java.io.IOException;

public class readTextFile implements constants.balise {

    private final boolean dBug = false;

    private java.io.FileReader fr;
    private java.io.BufferedReader br;

    public readTextFile(String s) throws Exception{
        try{
            fr= new java.io.FileReader(s);
            br = new java.io.BufferedReader(fr);
        }catch(java.io.FileNotFoundException e){ throw e;}
    }

    public String lectureIntegraleLigne() {
        // lit aussi les commentaires
        String temp = "";
        try{
            temp =br.readLine();
            /*while ((temp != null) && (temp.length()==0))
                temp =br.readLine();
            */
             }catch(IOException e){System.out.println("readTextFile lectureIntegraleLigne Le fichier specifie n'existe pas");}
        return temp;
    }

    public String lectureLigne() {
        String temp = "";
        try{
            temp =br.readLine();
            while((temp != null) && ((temp.length()==0) || ((temp.charAt(0)==CARACTERE_DE_COMMENTAIRE) || (temp.charAt(1)==CARACTERE_DE_COMMENTAIRE) )))
                temp =br.readLine();
        }catch(IOException e){if(dBug) e.printStackTrace();
                System.out.println("readTextFile lectureLigne "+e+"Le fichier specifie n'existe pas");}
        return temp;
    }

    public java.util.ArrayList decomposeLigneEnMotsSansEspaces() {
        String s = lectureLigne();
        return decomposeLigneEnMotsSansEspaces(s);
    }

    public static java.util.ArrayList decomposeLigneEnMotsSansEspaces(String s) {

        java.util.ArrayList ret = new java.util.ArrayList();
        String tmp;
        boolean espace = true;
        int i,j,memDeb = 0, memFin = 0;
        if((s!=null)&&(!"".equals(s))){
            i = 0;
            while(i<s.length()) {
                if(! java.lang.Character.isWhitespace(s.charAt(i))) {
                    j=i;
                    tmp = "";
                    while((j<s.length())&&(!java.lang.Character.isWhitespace(s.charAt(j))))
                        j++;
                    //j--;
                    memFin=j;
                    tmp = s.substring(memDeb,memFin);
                    if (!"".equals(tmp))
                        ret.add(tmp);
                    //if (dBug) System.out.println(tmp);
                }
                i++;
                memDeb=i;
            }
        }
        else ret=null;
        return ret;
    }

    public java.util.ArrayList decomposeLigneEnMots() {
        String s = lectureLigne();
        return decomposeLigneEnMots(s);
    }

    public static java.util.ArrayList decomposeLigneEnMots(String s) {
        java.util.ArrayList v =null;
        String tmp;
        boolean espace = true;
        int memDeb = 0, memFin = 0;
        if(s!=null){
            v = new java.util.ArrayList();
            for(int i=0;i<s.length();i++){
                if(!java.lang.Character.isWhitespace(s.charAt(i))){
                    if (espace){
                        if(i == s.length()-1){
                            tmp = s.substring(i,i+1);
                            v.add(tmp);
                            //if (dBug) System.out.println(tmp);
                        }else{
                            memDeb = i;
                            espace = false;
                        }
                    }
                    else if(i==s.length()-1){
                        memFin = i+1;
                        espace = true;
                        tmp = s.substring(memDeb,memFin);
                        v.add(tmp);
                        //if (dBug) System.out.println(tmp);
                    }else
                        espace=false;
                }
                else if(!espace) {
                    memFin = i;
                    espace = true;
                    tmp = s.substring(memDeb,memFin);
                    v.add(tmp);
                    //if (dBug) System.out.println(tmp);
                }
            }
        }
        return v;
    }




    public void fermer(){
        try{
            br.close();
        }catch(IOException e){ System.out.println("BufferedReader close "+ e);  }
        try{
            fr.close();
        }catch(IOException e){ System.out.println("FileReader close "+ e);}
    }
    

}
