/*
 * ThreadAnime.java
 *
 * Created on 1 octobre 2002, 11:35
 */

/*
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package visu;

public class ThreadAnime extends Thread implements constants.centre {

    private boolean stop = true;
    private CanvasMap mere=null;
    /** Creates a new instance of ThreadAnime */
    public ThreadAnime(CanvasMap m) {
        mere = m;
        stop=true;
    }

    public void run() {

        mere.repaint();
        try{
                this.yield();
        }catch(Exception e){}
        while (!stop){
            try {
                this.sleep(ANIME_DELAY);
            }catch(Exception e){System.out.println("ThreadAnime : "+e);}
            System.out.println("-=< animation >=-");
            try{
                this.yield();
            }catch(Exception e){}
           
            try{
                this.yield();
            }catch(Exception e){}

        }

    }

    public void start() {
        if (!stop) System.out.println("ThreadAnime : Erreur au demarrage; thread d�ja actif ");
        else {
            stop=false;
            super.start();
        }
    }

    public void arret() {
        if (stop) System.out.println("ThreadAnime : Erreur a l'arret; thread d�ja mort ");
        else stop = true;
    }

}
