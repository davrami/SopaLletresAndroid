package dam2.sopalletresandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class  Utility {

     public static String comprovaPosicio(int posicio, ArrayList<Paraula> paraules, ArrayList<Integer> posicionsMarcades, JocDbHelper db, int id){
         boolean paraulaCompletada  = false;
         String result= "";

         for (int v : posicionsMarcades) {
             System.out.println("v"+ v);
         }


         for (Paraula pa : paraules) {
             //System.out.println(pa.getNom());
             for (Paraula.Lletra l : pa.lletres) {
                 //System.out.println(l.getString() +" posición: "+l.getPosicion());
                 if(l.getPosicion()==posicio){
                     System.out.println(l.getPosicion()+" "+posicio);

                     result = "NoCompletada";
                     paraulaCompletada = comprovaParaulaCompletada(pa, posicionsMarcades);
                     if(paraulaCompletada) {

                         if(PrincipalActivity.paraulasCompletadas.indexOf(pa) == -1){
                             db.updatePuntuacioSuma(id,new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date()),10);
                             PrincipalActivity.paraulasCompletadas.add(pa);
                             result="Completada";
                         }else{
                             result="repetida";
                         }

                     }
                 }
             }
         }

         return result;
     }

     //comprueba si las posiciones seleccionadas corresponden a una palabra
     public static boolean comprovaParaulaCompletada(Paraula p, ArrayList<Integer> posicionsMarcades){

         boolean result = true;
         int count = 0;

         for (Paraula.Lletra l : p.lletres) {
             System.out.println("1.p"+p.getNom()+" l "+l.getString()+" "+posicionsMarcades.indexOf(l.getPosicion()));

             if(posicionsMarcades.indexOf(l.getPosicion()) < 0){
                 result= false;
             }else{
                 count++;
             }
             System.out.println("2.p"+p.getNom()+" l "+l.getString()+" "+posicionsMarcades.indexOf(l.getPosicion())+ " "+count+" "+posicionsMarcades.size());

         }
         return result && count == posicionsMarcades.size();
     }

    public static void sumaPunts(Paraula p, ArrayList<Paraula> paraulasCompletadas, JocDbHelper db, int id){
            System.out.println("no existe");
            db.updatePuntuacioSuma(id,new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date()),10);
    }




}
