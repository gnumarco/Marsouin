/*
 * OceanFile.java
 *
 * Created on 18 septembre 2002, 12:58
 */

/*
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package visu;

import data.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ConfigHistoFile implements constants.centre, constants.balise{

    private final boolean dBug = true;
    private final boolean dBug1 = false;

    private String nomFichier = null;

    public ConfigHistoFile() {
        // trouver le fichier g�n�ral
        trouverFichier();
    }

    private void trouverFichier(){
        // prouve l'existance du fichier g�n�ral, ou le cr�e !
        nomFichier = CONFIG_FILE;
        FileReader fr = null;
        try {
            // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
            File homeDir, configDir, configFile;
            String userHome = System.getProperty("user.home");
            try {
                homeDir = new File(userHome);
                if ((homeDir==null)||(!homeDir.isDirectory()))
                    throw new Exception("homeDir is no dir");
            } catch(Exception e){System.out.println("ConfigHistoFile : impossible de trouver HomeDirectory "+e);e.printStackTrace();throw e;}

            try {
                configDir = new File(homeDir,CONFIG_DIR);
                if (dBug1)
                    System.out.println("repertoire configuration...");
                if (configDir.mkdir())
                    System.out.println("cr�ation de "+configDir.getAbsolutePath() );
                if (dBug1)
                    System.out.println("entr�e dans le repertoire de configuration...");
            } catch(Exception e){
                System.out.println("ConfigHistoFile : Probleme au repertoire de configuration...");e.printStackTrace();throw e;}

            try {
                configFile = new File(configDir,CONFIG_FILE);
                if (dBug1)
                    System.out.println("fichier configuration...");
                // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
                try {
                    fr = new FileReader(configFile.getAbsolutePath());
                    fr.close();
                    fr = null;
                } catch(Exception e){ // si lengthfichier n'extiste pas
                    System.out.println("cr�ation du fichier de configuration...");
                    try {
                        writeTextFile nouvo = new writeTextFile(configFile.getAbsolutePath());
                        this.ecrireEnteteGeneral(nouvo);
                        nouvo.fermer();
                        nouvo=null;
                        System.out.println("");
                    }catch(Exception f){
                        System.out.println("ConfigHistoFile : configFile : erreur de creation" +f);f.printStackTrace();}
                }
                nomFichier = configFile.getAbsolutePath();
                if (dBug1)
                    System.out.println("ok fichier de configuration...");
            } catch(Exception e){
                System.out.println("ConfigHistoFile : Probleme au fichier de configuration...");e.printStackTrace();throw e;}
            fr=null;
            configFile=null;
        }catch(Exception e){e.printStackTrace();}
    }

    public void ecrireEnteteGeneral(writeTextFile ecrit) {
        ecrit.uneLigne(" # <-> Nouveau Fichier de config : " + nomFichier);
        ecrit.uneLigne(" # <-> Date : " + java.util.Calendar.getInstance().getTime().toString());
    }

    public void appliquerConfig(String nomConfig, FrmConf frm) {
        ArrayList listeNoms,cfg = null;
        int num =-1;
        try {
            trouverFichier();
            listeNoms = listerNomConfig();
            if (!listeNoms.contains(nomConfig))
               throw new Exception();

            cfg = decouperConfig(nomConfig);


        }catch(Exception e){ e.printStackTrace();}
    }

    private void appliquerConfig(ArrayList cfg,FrmConfig frm) {
        ArrayList tab = null;
        int j,i;
        double[] limitC = new double[LENGTH_LIMITC];
        boolean[] cbox = new boolean[LENGTH_CFGCENTRE];
        int[] fla = new int[LENGTH_FLAG_AFFICH];
        int[] combiGC = new int[LENGTH_COMBINER_GC];

        if (dBug) System.out.println("appl P " + cfg.size());

        for (j=0;j<cfg.size();j++) {
            tab = (ArrayList) cfg.get(j);
            if (((String)tab.get(0)).equals(CHECKBOXC)) {
                for( i=0; i< cbox.length; i++){
                    try {
                        cbox[i] = (Boolean.valueOf((String)tab.get(i+2)));
                    }catch (Exception e) {cbox[i] = DEFAULT_CHECKBOXC[i];}
                    if (dBug1) System.out.println("appl P cbc "+Boolean.valueOf((String)tab.get(i+2)).toString() );
                    if (dBug1) System.out.println("appl P cbc "+cbox[i]);
                }
            }
            else if (((String)tab.get(0)).equals(LIMITC)) {
                for( i=0; i< limitC.length; i++) {
                    try {
                        limitC[i] = (Double.valueOf((String)tab.get(i+2)));
                    }catch (NumberFormatException e) {limitC[i] = DEFAULT_LIMITC[i];}
                    if (dBug1) System.out.println("appl P limitc  "+Double.valueOf((String)tab.get(i+2)).toString() );
                    if (dBug1) System.out.println("appl P limitc  "+Double.toString(limitC[i]));
                }
            }
            else if (((String)tab.get(0)).equals(FLAGAFFICH)){
                fla[AFF_CENTRE]=AFF_C_CAMEMBERT;
                fla[AFF_COMBINER_GC]= DEFAULT_SEUIL_COMBINER_GC;
                for( i=0; i< fla.length; i++) {
                    try {
                        fla[i] = (Integer.valueOf((String)tab.get(i+2)));
                    }catch (NumberFormatException e) {}
                    if (dBug1) System.out.println("appl P flagaff   "+Integer.valueOf((String)tab.get(i+2)).toString() );
                    if (dBug1) System.out.println("appl P flagaff  "+fla[i]);
                }
            }
            else if (((String)tab.get(0)).equals(COMBI_GC)){
                for( i=0; i< combiGC.length; i++) {
                    try {
                        combiGC[i] = (Integer.valueOf((String)tab.get(i+2)));
                    }catch (NumberFormatException e) {combiGC[i]=DEFAULT_COMBINER_GC[i];}
                    if (dBug1) System.out.println("appl P combic   "+Integer.valueOf((String)tab.get(i+2)).toString() );
                    if (dBug1) System.out.println("appl P combic  "+combiGC[i]);
                }
            }
        }
        frm.toFront();
        frm.setLimitC(limitC);
        frm.setCheckBoxC(cbox);
        frm.setFlagAffich(fla);
        frm.setCombinerGC(combiGC);
    }


    public void supprimerConfig(String nomConfig) {
        ArrayList maLigne,oldFile = null;
        String ligne;
        int debut=-1,fin=-1,num =-1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num=0;
            while(num<oldFile.size()) {
                ligne = (String)oldFile.get(num);
                maLigne = readTextFile.decomposeLigneEnMots(ligne);
                num++;
                if ((maLigne!=null)&&(maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    debut= num-1;
                    ligne = (String)oldFile.get(num);
                    maLigne = readTextFile.decomposeLigneEnMots(ligne);
                    num++;
                    while((maLigne!=null)&&(!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMots(ligne);
                        num++;
                    }
                    if (!maLigne.contains(nomConfig)) {
                            // pas la bonne config : laisser filer
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMots(ligne);
                        num++;
                    }
                    else {
                      // la bonne config
                         while((maLigne!=null)&&(!maLigne.contains(END_CONFIG))) {
                               ligne = (String)oldFile.get(num);
                               maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                               num++;
                         }
                         if((maLigne!=null)&&(maLigne.contains(END_CONFIG))) {
                             fin = num-1;
                             // arreter :
                             num = oldFile.size()+10;
                         }
                    }
                }
            }


            if ((num == oldFile.size() + 10)&((debut>-1)&(fin>-1))) System.out.println("config trouv�e");
            else throw new Exception("config inconnue !#? ");

            for (num=0;num < (fin -debut + 1);num++)
                oldFile.remove(debut);

            writeTextFile fichier = new writeTextFile(nomFichier);
            copyFile(oldFile,fichier);
            fichier.fermer();
        }catch(Exception e){System.out.println("ConfigHistoFile : supprimerConfig erreur "+e);}

    }

    public void setCommentaire( String nomConf,String comment) {

        ArrayList maLigne,oldFile = null;
        String ligne;

        int num =-1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num=0;
            while(num<oldFile.size()) {
                ligne = (String)oldFile.get(num);
                maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                num++;
                if ((maLigne!=null)&&(maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    while((maLigne!=null)&&(!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                        num++;
                    }
                    if(!maLigne.contains(nomConf)) {
                    // mauvaise pioche on continue
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                        num++;
                    }
                    else {
                        // la bonne config
                        while((maLigne!=null)&&(!maLigne.contains(COMMENTAIRE_CONFIG))) {
                            ligne = (String)oldFile.get(num);
                            maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                            num++;
                        }
                        if((maLigne!=null)&&(maLigne.contains(COMMENTAIRE_CONFIG))) {
                            // remplacer lengthcommentaire
                            oldFile.add(num,comment);
                            num++;
                            // supprimer les lignes de commentaire anciennes
                            ligne = (String)oldFile.get(num);
                            maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                            while ((maLigne!=null)&&(!maLigne.contains(END_COMMENTAIRE_CONFIG))) {
                                    oldFile.remove(num);
                                    ligne = (String)oldFile.get(num);
                                    maLigne = readTextFile.decomposeLigneEnMotsSansEspaces(ligne);
                            }
                            if ((maLigne!=null)&&(maLigne.contains(END_COMMENTAIRE_CONFIG)))
                                // arreter :
                                num = oldFile.size()+10;
                            else throw new Exception(" balise fin de commentaire non trouv�e");
                        }
                    }
                }
            }
            if (num == oldFile.size() + 10) System.out.println("commentaire remplac�");
            else throw new Exception("commentaire inconnu !#? ");
            writeTextFile fichier = new writeTextFile(nomFichier);
            copyFile(oldFile,fichier);
            fichier.fermer();
        }catch(Exception e){System.out.println("ConfigHistoFile : setCommentaire erreur "+e);}

    }

    public void renommerConfig(String oldName,String newName) {
        ArrayList maLigne,oldFile = null;
        String ligne;
        int num =-1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num=0;
            while(num<oldFile.size()) {
                ligne = (String)oldFile.get(num);
                maLigne = readTextFile.decomposeLigneEnMots(ligne);
                num++;
                if ((maLigne!=null)&&(maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    ligne = (String)oldFile.get(num);
                    maLigne = readTextFile.decomposeLigneEnMots(ligne);
                    num++;
                    while((maLigne!=null)&&(!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMots(ligne);
                        num++;
                    }
                    if (!maLigne.contains(oldName)) {
                            // pas la bonne config : laisser filer
                        ligne = (String)oldFile.get(num);
                        maLigne = readTextFile.decomposeLigneEnMots(ligne);
                        num++;
                    }
                    else {
                      // la bonne config
                        num--;
                        oldFile.add(num,(NOM_CONFIG+" "+newName));
                        num = oldFile.size() + 10;
                    }
                }
            }

            if ((num == oldFile.size() + 10)) System.out.println("config trouv�e");
            else throw new Exception("config inconnue !#? ");

            writeTextFile fichier = new writeTextFile(nomFichier);
            copyFile(oldFile,fichier);
            fichier.fermer();
        }catch(Exception e){System.out.println("ConfigHistoFile : renommerConfig erreur "+e);}

    }

    private ArrayList decouperConfig(String nom) throws Exception{
        readTextFile rtf = null;
        ArrayList ret,tab,ligne = null;
        int i=-1,lng=0;
        String tmp="";
        int numero = this.listerNomConfig().indexOf(nom);
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new readTextFile(nomFichier);
            atteindreConfig(numero,rtf);

            ret = new ArrayList();
            ligne = rtf.decomposeLigneEnMots();
            while ((ligne!=null)&&(!ligne.contains(END_CONFIG))) {

                if (ligne.contains(CHECKBOXC)|((ligne.contains(LIMITC))|((ligne.contains(FLAGAFFICH))
                |(ligne.contains(COMBI_GC)))))    {
                    tab = new ArrayList();

                    if (ligne.contains(CHECKBOXC)) {
                        tab.add(CHECKBOXC);
                        tab.add(ligne.get(ligne.indexOf(CHECKBOXC)+1));
                    }
                    else if (ligne.contains(LIMITC)) {
                        tab.add(LIMITC);
                        tab.add(ligne.get(ligne.indexOf(LIMITC)+1));
                    }
                    else if (ligne.contains(FLAGAFFICH)) {
                        tab.add(FLAGAFFICH);
                        tab.add(ligne.get(ligne.indexOf(FLAGAFFICH)+1));
                    }
                    else if (ligne.contains(COMBI_GC)) {
                        tab.add(COMBI_GC);
                        tab.add(ligne.get(ligne.indexOf(COMBI_GC)+1));
                    }

                    lng = Integer.parseInt((String)tab.get(1));
                    if (dBug) System.out.println("cfghf lng : "+lng);

                    ligne = rtf.decomposeLigneEnMots();
                    for (i=0; i<lng;i++) {
                        tab.add((String)ligne.get(i));
                        if (dBug1) System.out.println("cfghf "+(String)ligne.get(i));
                    }
                    ret.add(tab);
                }
                ligne = rtf.decomposeLigneEnMots();
            }
            if (dBug) System.out.println("balisefin");

            tab = null;
            rtf.fermer();
            rtf = null;
        }catch(Exception e){System.out.println("ConfigHistoFile : decouperConfig erreur "+e);e.printStackTrace();rtf.fermer();ret=null;throw e; }
        return ret;
    }



    private void atteindreConfig(int num, readTextFile rtf) throws Exception {
        ArrayList ligne = rtf.decomposeLigneEnMots();
        int i=-1;
        while (ligne!=null) {
            if (!ligne.contains(NEW_CONFIG))              //chercher encore
            ligne = rtf.decomposeLigneEnMots();
            else {                                         //config trouv�e
                i++;
                if (i==num)                                 // C EST LA BONNE CONFIG !
                    ligne=null;
                else                                        // pas la bonne config
                    ligne = rtf.decomposeLigneEnMots();
            }
        }
        if (i!=num) throw new Exception("mauvaise position : not found");
    }



    //////////////////////////////////////////////////////////////////////////////////////
    public void ajouterConfig(String nomConfig, String commentaire, Memoire m, int id) {
        // cote frm visu et data carte
        writeTextFile fichier = null;
        // trouver le fichier general
        trouverFichier();
        // on memorise le contenu du fichier existant:
        ArrayList fichierInitial = this.getOldFile(nomFichier);
        try {
            fichier = new writeTextFile(nomFichier);
            this.ajouter(fichier,nomConfig,commentaire,
            m.getFlagAffich(id));
            this.copyFile(fichierInitial,fichier);
            fichier.fermer();
            fichier = null;
            System.out.println(" Configuration actuelle enregistree dans "+nomFichier);
        }catch(Exception e){System.out.println("ConfigHistoFile : erreur a l'enregistrement "+e);}
    }

    public void ajouterConfig(String nomConfig, String commentaire, FrmConfig frm) {
        // cote frm visu et data carte
        writeTextFile fichier = null;
        // trouver le fichier general
        trouverFichier();
        // on memorise le contenu du fichier existant:
        ArrayList fichierInitial = this.getOldFile(nomFichier);
        try {
            fichier = new writeTextFile(nomFichier);
            this.ajouter(fichier,nomConfig,commentaire,
            frm.getFlagAffich());
            this.copyFile(fichierInitial,fichier);
            fichier.fermer();
            fichier = null;
            System.out.println(" Configuration actuelle enregistree dans "+nomFichier);
        }catch(Exception e){System.out.println("ConfigHistoFile : erreur a l'enregistrement "+e);}
    }

    private void ajouter(writeTextFile fichier, String nomConfig, String commentaire,
    int[] flagAff) {

        try{
            int i;
            String maligne = "";

            this.ecrireEnteteConfig(fichier);

            fichier.uneLigne(NOM_CONFIG+ " "+nomConfig);
            fichier.uneLigne(COMMENTAIRE_CONFIG);
            fichier.uneLigne(commentaire);
            fichier.uneLigne(END_COMMENTAIRE_CONFIG);

            fichier.uneLigne();
            maligne = "";
            fichier.uneLigne(maligne);
            fichier.uneLigne();
           
            maligne = "";
            fichier.uneLigne(maligne);
            fichier.uneLigne();
            
            maligne = "";
            
            fichier.uneLigne(maligne);
            fichier.uneLigne();
            fichier.uneLigne(FLAGAFFICH + " " + Integer.toString(flagAff.length));
            maligne = "";
            for (i=0;i<flagAff.length;i++)
                maligne = maligne +" "+Integer.toString(flagAff[i]);
            fichier.uneLigne(maligne);
            fichier.uneLigne();
            this.ecrirePiedConfig(fichier);
        }catch(Exception f){System.out.println(f);f.printStackTrace();}
    }

    private void ecrireEnteteConfig(writeTextFile ecrit ) {
        ecrit.uneLigne(NEW_CONFIG);
        ecrit.uneLigne(" # <-> Date : " + java.util.Calendar.getInstance().getTime().toString());
    }
    private void ecrirePiedConfig(writeTextFile ecrit ) {
        ecrit.uneLigne(" # <-> Auteur : "+ System.getProperty("user.home"));
        ecrit.uneLigne(END_CONFIG);
        ecrit.uneLigne();
    }


    public ArrayList getOldFile(String fileName) {

        FileReader fr = null;
        ArrayList ret= new ArrayList();
        // recopier :
        try {
            data.readTextFile rtf = new data.readTextFile(fileName);
            String ln = rtf.lectureIntegraleLigne();
            while(ln!=null) {
                ret.add(ln);
                ln = rtf.lectureIntegraleLigne();
            }
            rtf.fermer();
            rtf = null;
        }catch(Exception e){System.out.println("ConfigHistoFile : initFile recopy "+e);e.printStackTrace();}
        return ret;
    }

    public void importerDepuis(String source) {
        try{
            this.trouverFichier();
            this.trouverFichier(source);
            ArrayList oldConfig = getOldFile(nomFichier);
            ArrayList src = getOldFile(source);

            // verifier les doublons
            int oc = 0,s = 0;
            ArrayList listeTotale = this.listerNomConfig();
            ArrayList ligne = null;
            String ret,lenom;
            JOptionPane JO = new JOptionPane("ATTENTION",JOptionPane.WARNING_MESSAGE,JOptionPane.DEFAULT_OPTION,null);

            try{
                for (s=0; s<src.size();s++) {
                    ligne = readTextFile.decomposeLigneEnMots(((String)src.get(s)));
                    if (ligne.contains(NOM_CONFIG)) {
                        lenom = new String((String)ligne.get(ligne.indexOf(NOM_CONFIG +1)));
                        while ((listeTotale.contains(lenom))|(lenom.lastIndexOf(" ")!=-1)) {
                           // changer de nom
                           JO.setInitialValue(lenom + "_"+(new File(source)).getName());
                           ret = JOptionPane.showInputDialog(this,(lenom+" existe, entrez un nouveau nom SANS ESPACES !"));
                           lenom = ret;
                        }
                        // effecuter le changement
                        src.add(s,(NOM_CONFIG +" "+lenom));
                        listeTotale.add(lenom);
                    }
                }
            }catch (Exception e) {}

            writeTextFile fichier;
            fichier = new writeTextFile(nomFichier);
            this.ecrireEnteteGeneral(fichier);

            fichier.uneLigne(NEW_IMPORT_CONFIG);
            this.copyFile(src,fichier);
            fichier.uneLigne(END_IMPORT_CONFIG);

            this.copyFile(oldConfig,fichier);

            fichier.fermer();
            fichier = null;

        }catch (Exception e) {System.out.println(" ConfigHistoFile : erreur d'importation! "+e);}
    }


    public void exporterVers(String fileName) {
        try {
            this.trouverFichier();
            writeTextFile fichier = new writeTextFile(fileName);
            this.copyFile(getOldFile(nomFichier),fichier);
            fichier.fermer();
            fichier = null;
        }catch (Exception e) {System.out.println(" ConfigHistoFile : erreur d'exportation! "+e);}
    }


    public ArrayList listerNomConfig() {

        ArrayList liste = new ArrayList();
        readTextFile rtf = null;
        ArrayList ligne = null;

        this.trouverFichier();
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new readTextFile(nomFichier);
            ligne = rtf.decomposeLigneEnMots();
            while (ligne!=null) {
                if(ligne.contains(NEW_CONFIG)) {
                    ligne = rtf.decomposeLigneEnMots();
                    while ((ligne!=null)&&(!ligne.contains(END_CONFIG))) {
                        if (ligne.contains(NOM_CONFIG)) {
                            liste.add(ligne.get(ligne.indexOf(NOM_CONFIG)+1));
                            ligne=null;
                        }
                        else ligne = rtf.decomposeLigneEnMots();
                    }
                }
                ligne = rtf.decomposeLigneEnMots();
            }
            rtf.fermer();
            rtf = null;
            if (liste.size()==0) { System.out.println(" aucune configuration trouvee"); throw new Exception("NO_CONFIG");}
        }catch(Exception e){
            if (!e.getMessage().equalsIgnoreCase("NO_CONFIG")) {
                System.out.println("ConfigHistoFile : listerNomConfig erreur "+e);
                e.printStackTrace();}
            }
            return liste;
    }


    public ArrayList listerCommentaireConfig() {

        ArrayList liste = new ArrayList();
        readTextFile rtf = null;
        ArrayList ligne = null;
        String maLigne,comment = null;
        this.trouverFichier();
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new readTextFile(nomFichier);
            ligne = rtf.decomposeLigneEnMots();
            while (ligne!=null) {
                if(ligne.contains(NEW_CONFIG)) {
                    ligne = rtf.decomposeLigneEnMots();
                    while ((ligne!=null)&&(!ligne.contains(COMMENTAIRE_CONFIG))) {
                        ligne = rtf.decomposeLigneEnMots();
                    }
                    // le commentaire demarre la ligne suivante
                    maLigne = rtf.lectureIntegraleLigne();
                    ligne = rtf.decomposeLigneEnMots(maLigne);
                    comment = "";
                    if ((ligne!=null)&&(!ligne.contains(END_COMMENTAIRE_CONFIG))) {
                        comment = new String(maLigne);
                        maLigne = rtf.lectureIntegraleLigne();
                        ligne = rtf.decomposeLigneEnMots(maLigne);
                    }
                    while ((ligne!=null)&&(!ligne.contains(END_COMMENTAIRE_CONFIG))) {
                        comment = comment + '\n' + new String(maLigne);
                        maLigne = rtf.lectureIntegraleLigne();
                        ligne = rtf.decomposeLigneEnMots(maLigne);
                    }
                    liste.add(comment);
                    ligne=null;
                }
                else
                    ligne = rtf.decomposeLigneEnMots();
            }

            rtf.fermer();
            rtf = null;
            if (liste.size()==0) { liste=null;throw new Exception(); }

        }catch(Exception e){System.out.println("ConfigHistoFile : listerCommConfig erreur "+e);e.printStackTrace();}
        return liste;
    }

    ///////////////////////////////////////////////////////////////
    private void trouverFichier(String nomFichier) throws Exception{
        FileReader fr = null;
        try {
            // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
            fr = new FileReader(new File(nomFichier));
            fr.close();
            fr = null;
        }catch(Exception f){ System.out.println("ConfigHistoFile : erreur d'ouverture de fichier " + f);}
    }


    /////////////////////////////////////////
    private void copyFile(ArrayList fileToCopy, writeTextFile fileToWrite) {
        try {
            // LE FICHIER EXISTE : on le copie
            for (int i=0; i<fileToCopy.size(); i++)
                fileToWrite.uneLigne((String)fileToCopy.get(i));
        }catch(Exception e){System.out.println("ConfigHistoFile : copyFile erreur "+e);}
    }

}