package dam2.sopalletresandroid;

import android.content.Intent;
import java.util.ArrayList;

//clase controladora de les paraules
public class Paraula {
    String nom;
    public ArrayList<Lletra> lletres = new ArrayList<Lletra>();

    public String getNom() {
        return nom;
    }

    public Paraula(String nom) {
        super();
        this.nom = nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Paraula() {
        ArrayList<Lletra> lletres = new ArrayList<Lletra>();
    }

    public ArrayList<Lletra> getLletres() {
        return lletres;
    }

    public void setLletres(ArrayList<Lletra> lletres) {
        this.lletres = lletres;
    }


    //Clase controladora de Lletras de paraulas
    public static class Lletra {
        public Integer posicion;

        public String string;

        public Integer getPosicion() {
            return posicion;
        }

        public void setPosicion(Integer posicion) {
            this.posicion = posicion;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}

