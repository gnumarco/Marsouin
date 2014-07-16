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
package com.marsouin.visu;

import com.marsouin.data.ReadTextFile;
import com.marsouin.data.WriteTextFile;
import static com.marsouin.data.ReadTextFile.decomposeLigneEnMots;
import static com.marsouin.data.ReadTextFile.decomposeLigneEnMotsSansEspaces;
import java.io.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.getProperty;
import java.util.ArrayList;
import static java.util.Calendar.getInstance;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showInputDialog;

public class ConfigHistoFile implements com.marsouin.constants.Centre, com.marsouin.constants.Balise {

    private final boolean dBug = true;
    private final boolean dBug1 = false;

    private String nomFichier = null;

    public ConfigHistoFile() {
        // trouver le fichier g�n�ral
        trouverFichier();
    }

    private void trouverFichier() {
        // prouve l'existance du fichier g�n�ral, ou le cr�e !
        nomFichier = CONFIG_FILE;
        FileReader fr;
        try {
            // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
            File homeDir, configDir, configFile;
            String userHome = getProperty("user.home");
            try {
                homeDir = new File(userHome);
                if (!homeDir.isDirectory()) {
                    throw new Exception("homeDir is no dir");
                }
            } catch (Exception e) {
                System.out.println("ConfigHistoFile : impossible de trouver HomeDirectory " + e);
                e.printStackTrace();
                throw e;
            }

            try {
                configDir = new File(homeDir, CONFIG_DIR);
                if (dBug1) {
                    System.out.println("repertoire configuration...");
                }
                if (configDir.mkdir()) {
                    System.out.println("cr�ation de " + configDir.getAbsolutePath());
                }
                if (dBug1) {
                    System.out.println("entr�e dans le repertoire de configuration...");
                }
            } catch (Exception e) {
                System.out.println("ConfigHistoFile : Probleme au repertoire de configuration...");
                e.printStackTrace();
                throw e;
            }

            try {
                configFile = new File(configDir, CONFIG_FILE);
                if (dBug1) {
                    System.out.println("fichier configuration...");
                }
                // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
                try {
                    fr = new FileReader(configFile.getAbsolutePath());
                    fr.close();
                } catch (IOException e) { // si lengthfichier n'extiste pas
                    System.out.println("cr�ation du fichier de configuration...");
                    try {
                        WriteTextFile nouvo = new WriteTextFile(configFile.getAbsolutePath());
                        this.ecrireEnteteGeneral(nouvo);
                        nouvo.fermer();
                        System.out.println("");
                    } catch (Exception f) {
                        System.out.println("ConfigHistoFile : configFile : erreur de creation" + f);
                        f.printStackTrace();
                    }
                }
                nomFichier = configFile.getAbsolutePath();
                if (dBug1) {
                    System.out.println("ok fichier de configuration...");
                }
            } catch (Exception e) {
                System.out.println("ConfigHistoFile : Probleme au fichier de configuration...");
                e.printStackTrace();
                throw e;
            }
            configFile = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ecrireEnteteGeneral(WriteTextFile ecrit) {
        ecrit.uneLigne(" # <-> Nouveau Fichier de config : " + nomFichier);
        ecrit.uneLigne(" # <-> Date : " + getInstance().getTime().toString());
    }

    public void appliquerConfig(String nomConfig, FrmConf frm) {
        ArrayList listeNoms, cfg = null;
        int num = -1;
        try {
            trouverFichier();
            listeNoms = listerNomConfig();
            if (!listeNoms.contains(nomConfig)) {
                throw new Exception();
            }

            cfg = decouperConfig(nomConfig);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void supprimerConfig(String nomConfig) {
        ArrayList<String> maLigne, oldFile;
        String ligne;
        int debut = -1, fin = -1, num = -1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num = 0;
            while (num < oldFile.size()) {
                ligne = (String) oldFile.get(num);
                maLigne = decomposeLigneEnMots(ligne);
                num++;
                if ((maLigne != null) && (maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    debut = num - 1;
                    ligne = (String) oldFile.get(num);
                    maLigne = decomposeLigneEnMots(ligne);
                    num++;
                    while ((maLigne != null) && (!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMots(ligne);
                        num++;
                    }
                    if (!maLigne.contains(nomConfig)) {
                        // pas la bonne config : laisser filer
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMots(ligne);
                        num++;
                    } else {
                        // la bonne config
                        while ((maLigne != null) && (!maLigne.contains(END_CONFIG))) {
                            ligne = (String) oldFile.get(num);
                            maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                            num++;
                        }
                        if ((maLigne != null) && (maLigne.contains(END_CONFIG))) {
                            fin = num - 1;
                            // arreter :
                            num = oldFile.size() + 10;
                        }
                    }
                }
            }

            if ((num == oldFile.size() + 10) & ((debut > -1) & (fin > -1))) {
                System.out.println("config trouv�e");
            } else {
                throw new Exception("config inconnue !#? ");
            }

            for (num = 0; num < (fin - debut + 1); num++) {
                oldFile.remove(debut);
            }

            WriteTextFile fichier = new WriteTextFile(nomFichier);
            copyFile(oldFile, fichier);
            fichier.fermer();
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : supprimerConfig erreur " + e);
        }

    }

    public void setCommentaire(String nomConf, String comment) {

        ArrayList<String> maLigne, oldFile = null;
        String ligne;

        int num = -1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num = 0;
            while (num < oldFile.size()) {
                ligne = (String) oldFile.get(num);
                maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                num++;
                if ((maLigne != null) && (maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    while ((maLigne != null) && (!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                        num++;
                    }
                    if (!maLigne.contains(nomConf)) {
                        // mauvaise pioche on continue
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                        num++;
                    } else {
                        // la bonne config
                        while ((maLigne != null) && (!maLigne.contains(COMMENTAIRE_CONFIG))) {
                            ligne = (String) oldFile.get(num);
                            maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                            num++;
                        }
                        if ((maLigne != null) && (maLigne.contains(COMMENTAIRE_CONFIG))) {
                            // remplacer lengthcommentaire
                            oldFile.set(num, comment);
                            num++;
                            // supprimer les lignes de commentaire anciennes
                            ligne = (String) oldFile.get(num);
                            maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                            while ((maLigne != null) && (!maLigne.contains(END_COMMENTAIRE_CONFIG))) {
                                oldFile.remove(num);
                                ligne = (String) oldFile.get(num);
                                maLigne = decomposeLigneEnMotsSansEspaces(ligne);
                            }
                            if ((maLigne != null) && (maLigne.contains(END_COMMENTAIRE_CONFIG))) // arreter :
                            {
                                num = oldFile.size() + 10;
                            } else {
                                throw new Exception(" balise fin de commentaire non trouv�e");
                            }
                        }
                    }
                }
            }
            if (num == oldFile.size() + 10) {
                System.out.println("commentaire remplac�");
            } else {
                throw new Exception("commentaire inconnu !#? ");
            }
            WriteTextFile fichier = new WriteTextFile(nomFichier);
            copyFile(oldFile, fichier);
            fichier.fermer();
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : setCommentaire erreur " + e);
        }

    }

    public void renommerConfig(String oldName, String newName) {
        ArrayList<String> maLigne, oldFile;
        String ligne;
        int num = -1;
        try {
            trouverFichier();
            oldFile = this.getOldFile(nomFichier);
            num = 0;
            while (num < oldFile.size()) {
                ligne = (String) oldFile.get(num);
                maLigne = decomposeLigneEnMots(ligne);
                num++;
                if ((maLigne != null) && (maLigne.contains(NEW_CONFIG))) {
                    // une conf trouv�e
                    ligne = (String) oldFile.get(num);
                    maLigne = decomposeLigneEnMots(ligne);
                    num++;
                    while ((maLigne != null) && (!maLigne.contains(NOM_CONFIG))) {
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMots(ligne);
                        num++;
                    }
                    if (!maLigne.contains(oldName)) {
                        // pas la bonne config : laisser filer
                        ligne = (String) oldFile.get(num);
                        maLigne = decomposeLigneEnMots(ligne);
                        num++;
                    } else {
                        // la bonne config
                        num--;
                        oldFile.set(num, (NOM_CONFIG + " " + newName));
                        num = oldFile.size() + 10;
                    }
                }
            }

            if ((num == oldFile.size() + 10)) {
                System.out.println("config trouv�e");
            } else {
                throw new Exception("config inconnue !#? ");
            }

            WriteTextFile fichier = new WriteTextFile(nomFichier);
            copyFile(oldFile, fichier);
            fichier.fermer();
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : renommerConfig erreur " + e);
        }

    }

    private ArrayList decouperConfig(String nom) throws Exception {
        ReadTextFile rtf = null;
        ArrayList<String> tab, ligne;
        ArrayList<ArrayList<String>> ret;
        int i = -1, lng = 0;
        int numero = this.listerNomConfig().indexOf(nom);
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new ReadTextFile(nomFichier);
            atteindreConfig(numero, rtf);

            ret = new ArrayList<>();
            ligne = rtf.decomposeLigneEnMots();
            while ((ligne != null) && (!ligne.contains(END_CONFIG))) {

                if (ligne.contains(CHECKBOXC) | ((ligne.contains(LIMITC)) | ((ligne.contains(FLAGAFFICH))
                        | (ligne.contains(COMBI_GC))))) {
                    tab = new ArrayList<>();

                    if (ligne.contains(CHECKBOXC)) {
                        tab.add(CHECKBOXC);
                        tab.add(ligne.get(ligne.indexOf(CHECKBOXC) + 1));
                    } else if (ligne.contains(LIMITC)) {
                        tab.add(LIMITC);
                        tab.add(ligne.get(ligne.indexOf(LIMITC) + 1));
                    } else if (ligne.contains(FLAGAFFICH)) {
                        tab.add(FLAGAFFICH);
                        tab.add(ligne.get(ligne.indexOf(FLAGAFFICH) + 1));
                    } else if (ligne.contains(COMBI_GC)) {
                        tab.add(COMBI_GC);
                        tab.add(ligne.get(ligne.indexOf(COMBI_GC) + 1));
                    }

                    lng = parseInt((String) tab.get(1));
                    if (dBug) {
                        System.out.println("cfghf lng : " + lng);
                    }

                    ligne = rtf.decomposeLigneEnMots();
                    for (i = 0; i < lng; i++) {
                        tab.add((String) ligne.get(i));
                        if (dBug1) {
                            System.out.println("cfghf " + (String) ligne.get(i));
                        }
                    }
                    ret.add(tab);
                }
                ligne = rtf.decomposeLigneEnMots();
            }
            if (dBug) {
                System.out.println("balisefin");
            }
            rtf.fermer();
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : decouperConfig erreur " + e);
            e.printStackTrace();
            rtf.fermer();
            throw e;
        }
        return ret;
    }

    private void atteindreConfig(int num, ReadTextFile rtf) throws Exception {
        ArrayList ligne = rtf.decomposeLigneEnMots();
        int i = -1;
        while (ligne != null) {
            if (!ligne.contains(NEW_CONFIG)) //chercher encore
            {
                ligne = rtf.decomposeLigneEnMots();
            } else {                                         //config trouv�e
                i++;
                if (i == num) // C EST LA BONNE CONFIG !
                {
                    ligne = null;
                } else // pas la bonne config
                {
                    ligne = rtf.decomposeLigneEnMots();
                }
            }
        }
        if (i != num) {
            throw new Exception("mauvaise position : not found");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    public void ajouterConfig(String nomConfig, String commentaire, Memory m, int id) {
        // cote frm visu et data carte
        WriteTextFile fichier = null;
        // trouver le fichier general
        trouverFichier();
        // on memorise le contenu du fichier existant:
        ArrayList fichierInitial = this.getOldFile(nomFichier);
        try {
            fichier = new WriteTextFile(nomFichier);
            this.ajouter(fichier, nomConfig, commentaire,
                    m.getFlagAffich(id));
            this.copyFile(fichierInitial, fichier);
            fichier.fermer();
            System.out.println(" Configuration actuelle enregistree dans " + nomFichier);
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : erreur a l'enregistrement " + e);
        }
    }

    private void ajouter(WriteTextFile fichier, String nomConfig, String commentaire,
            int[] flagAff) {

        try {
            int i;
            String maligne;

            this.ecrireEnteteConfig(fichier);

            fichier.uneLigne(NOM_CONFIG + " " + nomConfig);
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
            for (i = 0; i < flagAff.length; i++) {
                maligne = maligne + " " + Integer.toString(flagAff[i]);
            }
            fichier.uneLigne(maligne);
            fichier.uneLigne();
            this.ecrirePiedConfig(fichier);
        } catch (Exception f) {
            System.out.println(f);
            f.printStackTrace();
        }
    }

    private void ecrireEnteteConfig(WriteTextFile ecrit) {
        ecrit.uneLigne(NEW_CONFIG);
        ecrit.uneLigne(" # <-> Date : " + getInstance().getTime().toString());
    }

    private void ecrirePiedConfig(WriteTextFile ecrit) {
        ecrit.uneLigne(" # <-> Auteur : " + getProperty("user.home"));
        ecrit.uneLigne(END_CONFIG);
        ecrit.uneLigne();
    }

    public ArrayList getOldFile(String fileName) {

        FileReader fr = null;
        ArrayList ret = new ArrayList();
        // recopier :
        try {
            com.marsouin.data.ReadTextFile rtf = new com.marsouin.data.ReadTextFile(fileName);
            String ln = rtf.lectureIntegraleLigne();
            while (ln != null) {
                ret.add(ln);
                ln = rtf.lectureIntegraleLigne();
            }
            rtf.fermer();
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : initFile recopy " + e);
            e.printStackTrace();
        }
        return ret;
    }

    public void importerDepuis(String source) {
        try {
            trouverFichier();
            trouverFichier(source);
            ArrayList<String> oldConfig = getOldFile(nomFichier);
            ArrayList<String> src = getOldFile(source);

            // verifier les doublons
            int s = 0;
            ArrayList<String> listeTotale = this.listerNomConfig();
            ArrayList<String> ligne;
            String ret, lenom;
            JOptionPane JO = new JOptionPane("ATTENTION", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null);

            try {
                for (s = 0; s < src.size(); s++) {
                    ligne = decomposeLigneEnMots(((String) src.get(s)));
                    if (ligne.contains(NOM_CONFIG)) {
                        lenom = ligne.get(ligne.indexOf(NOM_CONFIG + 1));
                        while ((listeTotale.contains(lenom)) | (lenom.lastIndexOf(" ") != -1)) {
                            // changer de nom
                            JO.setInitialValue(lenom + "_" + (new File(source)).getName());
                            ret = showInputDialog(this, (lenom + " existe, entrez un nouveau nom SANS ESPACES !"));
                            lenom = ret;
                        }
                        // effecuter le changement
                        src.add(s, (NOM_CONFIG + " " + lenom));
                        listeTotale.add(lenom);
                    }
                }
            } catch (Exception e) {
            }

            WriteTextFile fichier;
            fichier = new WriteTextFile(nomFichier);
            this.ecrireEnteteGeneral(fichier);

            fichier.uneLigne(NEW_IMPORT_CONFIG);
            this.copyFile(src, fichier);
            fichier.uneLigne(END_IMPORT_CONFIG);

            this.copyFile(oldConfig, fichier);

            fichier.fermer();

        } catch (Exception e) {
            System.out.println(" ConfigHistoFile : erreur d'importation! " + e);
        }
    }

    public void exporterVers(String fileName) {
        try {
            this.trouverFichier();
            WriteTextFile fichier = new WriteTextFile(fileName);
            this.copyFile(getOldFile(nomFichier), fichier);
            fichier.fermer();
        } catch (Exception e) {
            System.out.println(" ConfigHistoFile : erreur d'exportation! " + e);
        }
    }

    public ArrayList listerNomConfig() {

        ArrayList<String> liste = new ArrayList<>();
        ReadTextFile rtf;
        ArrayList<String> ligne;

        this.trouverFichier();
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new ReadTextFile(nomFichier);
            ligne = rtf.decomposeLigneEnMots();
            while (ligne != null) {
                if (ligne.contains(NEW_CONFIG)) {
                    ligne = rtf.decomposeLigneEnMots();
                    while ((ligne != null) && (!ligne.contains(END_CONFIG))) {
                        if (ligne.contains(NOM_CONFIG)) {
                            liste.add(ligne.get(ligne.indexOf(NOM_CONFIG) + 1));
                            ligne = null;
                        } else {
                            ligne = rtf.decomposeLigneEnMots();
                        }
                    }
                }
                ligne = rtf.decomposeLigneEnMots();
            }
            rtf.fermer();
            if (liste.isEmpty()) {
                System.out.println(" aucune configuration trouvee");
                throw new Exception("NO_CONFIG");
            }
        } catch (Exception e) {
            if (!e.getMessage().equalsIgnoreCase("NO_CONFIG")) {
                System.out.println("ConfigHistoFile : listerNomConfig erreur " + e);
                e.printStackTrace();
            }
        }
        return liste;
    }

    public ArrayList listerCommentaireConfig() {

        ArrayList liste = new ArrayList();
        ReadTextFile rtf;
        ArrayList ligne ;
        String maLigne, comment = null;
        this.trouverFichier();
        try {
            // LE FICHIER EXISTE : on le parcours
            rtf = new ReadTextFile(nomFichier);
            ligne = rtf.decomposeLigneEnMots();
            while (ligne != null) {
                if (ligne.contains(NEW_CONFIG)) {
                    ligne = rtf.decomposeLigneEnMots();
                    while ((ligne != null) && (!ligne.contains(COMMENTAIRE_CONFIG))) {
                        ligne = rtf.decomposeLigneEnMots();
                    }
                    // le commentaire demarre la ligne suivante
                    maLigne = rtf.lectureIntegraleLigne();
                    ligne = decomposeLigneEnMots(maLigne);
                    comment = "";
                    if ((ligne != null) && (!ligne.contains(END_COMMENTAIRE_CONFIG))) {
                        comment = maLigne;
                        maLigne = rtf.lectureIntegraleLigne();
                        ligne = decomposeLigneEnMots(maLigne);
                    }
                    while ((ligne != null) && (!ligne.contains(END_COMMENTAIRE_CONFIG))) {
                        comment = comment + '\n' + maLigne;
                        maLigne = rtf.lectureIntegraleLigne();
                        ligne = decomposeLigneEnMots(maLigne);
                    }
                    liste.add(comment);
                    ligne = null;
                } else {
                    ligne = rtf.decomposeLigneEnMots();
                }
            }

            rtf.fermer();
            if (liste.isEmpty()) {
                liste = null;
                throw new Exception();
            }

        } catch (Exception e) {
            System.out.println("ConfigHistoFile : listerCommConfig erreur " + e);
            e.printStackTrace();
        }
        return liste;
    }

    ///////////////////////////////////////////////////////////////
    private void trouverFichier(String nomFichier) throws Exception {
        FileReader fr;
        try {
            // POUR VERIFIER QUE LE FICHIER EXISTE : on essaie de le lire
            fr = new FileReader(new File(nomFichier));
            fr.close();
        } catch (IOException f) {
            System.out.println("ConfigHistoFile : erreur d'ouverture de fichier " + f);
        }
    }

    /////////////////////////////////////////
    private void copyFile(ArrayList fileToCopy, WriteTextFile fileToWrite) {
        try {
            for (Object fileToCopy1 : fileToCopy) {
                fileToWrite.uneLigne((String) fileToCopy1);
            }
        } catch (Exception e) {
            System.out.println("ConfigHistoFile : copyFile erreur " + e);
        }
    }

}
