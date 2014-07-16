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

public class ThreadAnime extends Thread implements com.marsouin.constants.Centre {

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
                yield();
        }catch(Exception e){}
        while (!stop){
            try {
                sleep(ANIME_DELAY);
            }catch(Exception e){System.out.println("ThreadAnime : "+e);}
            System.out.println("-=< animation >=-");
            try{
                yield();
            }catch(Exception e){}
           
            try{
                yield();
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
