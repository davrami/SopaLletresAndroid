package dam2.sopalletresandroid;

import java.util.ArrayList;

public abstract class  Utility {

     public static String comprovaPosicio(int posicio, ArrayList<Paraula> paraules, ArrayList<Integer> posicionsMarcades){
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
                         PrincipalActivity.paraulasCompletadas.add(pa);
                     }
                 }
             }
         }
         if(paraulaCompletada){
             //System.out.println("paraula COMPLETADA");
             result="Completada";
         }else{
             //System.out.println("paraula NO completada");
         }
         return result;
     }

     //comprueba si las posiciones seleccionadas corresponden a una palabra
     public static boolean comprovaParaulaCompletada(Paraula p, ArrayList<Integer> posicionsMarcades){

         boolean result = true;
         int count = 0;

         for (Paraula.Lletra l : p.lletres) {
             System.out.println("p"+p.getNom()+" l "+l.getString()+" "+posicionsMarcades.indexOf(l.getPosicion()));

             if(posicionsMarcades.indexOf(l.getPosicion()) < 0){
                 result= false;
             }else{
                 count++;
             }
             System.out.println("p"+p.getNom()+" l "+l.getString()+" "+posicionsMarcades.indexOf(l.getPosicion()));

         }
         return result && count == posicionsMarcades.size();
     }

    public static void sumaPunts(Paraula p, ArrayList<Paraula> paraulasCompletadas){
        if(paraulasCompletadas.indexOf(p)>0){
            System.out.println("no existe");
        }


    }

}
