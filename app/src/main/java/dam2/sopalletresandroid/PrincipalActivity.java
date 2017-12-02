package dam2.sopalletresandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class PrincipalActivity extends AppCompatActivity {

    public Button buttonGoMain;
    public Button btReset;
    public GridView gvTauler; //grid lletres
    public String[] arrayMots; //llista paraules de xml
    public ArrayList<String> mots;

    public GridView gvParaules; //llista paraules
    public ArrayList<Paraula> llistaParaules;
    public static ArrayList<Paraula> paraulasCompletadas;
    public ArrayList<String> nomsContactes;

    public Integer puntuacion;
    public static ArrayList<Integer> letrasMarcadas;
    public String[] abc = new String[]{"A","B", "C", "D", "E","F", "G", "H", "I", "J","K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    public ArrayAdapter<String> Adapter;
    //public ArrayAdapter<String> AdapterParaules;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        String [] paraulesXML = getResources().getStringArray(R.array.mots); //recupera de arrays.xml les paraules
        nomsContactes = new ArrayList<String>();
        obtenirContactes(); //obtiene nombres contactos

        llistaParaules = new ArrayList<Paraula>();
        paraulasCompletadas = new ArrayList<Paraula>();

        mots = new ArrayList<String>();//lista de paraulas (contactos + xml)
        //introduce los nombres contactos como palabras para la sopa de letras

        if(nomsContactes.size() > 0){
            for (String nom : nomsContactes){
                if(mots.size() < 10){
                    System.out.println("CONTACT");

                    mots.add(nom);
                }
            }
        }
        if(mots.size()<10){ //si no hay suficientes nombres mete palabras del xml
            for (String p : paraulesXML){
                if(mots.size()  < 10){
                    System.out.println("XML");

                    mots.add(p);
                }
            }
        }

        int i = 0;
        /*for (String arrayMot : arrayMots) {
            System.out.println(arrayMot.toString()+i);
            i++;
        }*/

        Intent intent = getIntent();
        String missatge = intent.getStringExtra(MainActivity.EXTRA_MISSATGE); // get data inside intent last view
        Log.i("info", missatge);

        //tauler sopa lletras
        gvTauler = (GridView) findViewById(R.id.gvTauler); //grid de lletres
        gvParaules = (GridView) findViewById(R.id.gvParaules); //grid paraules

        final ArrayList<Integer> letrasMarcadas = new ArrayList<>();
        btReset = (Button) findViewById(R.id.btReset);
        buttonGoMain = (Button) findViewById(R.id.btGoMain);


        buttonGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "generating...");
                generateGridTauler();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "reset");
                for(Integer item : letrasMarcadas ){
                    //System.out.println(item);
                    System.out.println();
                    gvTauler.getChildAt(item).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    for (Paraula pc: paraulasCompletadas){
                        for (Paraula.Lletra l : pc.lletres) {
                            TextView tParaula = (TextView) gvTauler.getChildAt(l.getPosicion());
                            tParaula.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        }
                    }
                }
                letrasMarcadas.clear();
            }
        });

        gvTauler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(letrasMarcadas.indexOf(position) < 0) { //si no esta marcada previamente
                    letrasMarcadas.add(position); //añadir a array de posiciones marcadas

                    //Log.i("info", parent.toString()+ view+ position+id  );
                    //Log.i("infoView", view.toString()  );
                    Log.i("infoPosition", String.valueOf(position));
                    Log.i("infoId", parent.getItemAtPosition(position).toString());

                    String result = Utility.comprovaPosicio(position, llistaParaules, letrasMarcadas);

                    view.setBackgroundColor(getResources().getColor(android.R.color.tertiary_text_dark));

                    if (result == "Completada") {
                        System.out.println("paraula COMPLETADA");
                        Paraula p = paraulasCompletadas.get(paraulasCompletadas.size() - 1);
                        TextView tParaula = (TextView) gvParaules.getChildAt(p.getIndex());
                        tParaula.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        btReset.callOnClick();

                        // Todo sumar puntos
                        Utility.sumaPunts( p, paraulasCompletadas);

                        Toast toast = Toast.makeText(getApplicationContext(), "Bien Hecho!", Toast.LENGTH_SHORT);
                        toast.show();

                    } else {
                        System.out.println("paraula No Completada");
                    }
                /*if (comprovaParaula(parent, letrasMarcadas)){
                    btReset.callOnClick();
                }*/
                }
            }
        });
    }


    //GENERA el adapter y inserta en el gridview
    public void generateGridTauler() {
        buttonGoMain.setVisibility(View.GONE); // hide button generate
        String[] array = generaArrayLLetres();
        Adapter = new ArrayAdapter<String>(this,android.R.layout.test_list_item, array);
        gvTauler.setAdapter(Adapter);

    }

    public String[] generaArrayLLetres() {

        String result = "";
        int q = 0;
        for (int i = 0; i <= 99 && q <= 9; i += 10) {
            String paraula = mots.get(q);
            String fila = "";

            for (int l = 0; l < 10; l++) { //genera una fila de lletras random  = "kjfdshufdsdjas"
                Random r = new Random();
                int randLetra = r.nextInt(26);
                fila = fila.concat(abc[randLetra]);
            }

            //decide donde puede colocarse la parabra y hace substring "asdMESAasdasd"
            int limitH = 10 - (paraula.length() - 1);
            Random r = new Random();
            int rand = r.nextInt(limitH);
            System.out.println(rand);
            Paraula p = new Paraula(paraula, q);
            llistaParaules.add(p);

            fila = fila.substring(0, rand) + paraula + fila.substring(rand + paraula.length());

            for (int z = 0; z < paraula.length(); z++) {
                //System.out.println( "q"+q +" i"+i+" z"+z +" rand"+rand+" limit"+(limitH)+" pos"+(limitH+z)+" paraula"+paraula+" sub"+paraula.substring(z,z+1)+" finalpos"+(limitH + z+ i+ rand -1));
                Paraula.Lletra l = new Paraula.Lletra();
                l.setString(paraula.substring(z, z + 1));
                l.setPosicion(z+ i+ rand);
                p.lletres.add(l);
            }
            result = result.concat(fila); //string resultant de concatenar lletres random y paraules
            q++;
        }


        //GENERA lista paraules grid inferior
        gvParaules = (GridView) findViewById(R.id.gvParaules);
        ArrayAdapter<String> AdapterParaules = new ArrayAdapter<String>(this, android.R.layout.test_list_item, mots);
        gvParaules.setAdapter(AdapterParaules);

        //PRINT paraules lletres y sus posiciones
        for (Paraula pa : llistaParaules) {
            System.out.println(pa.getNom());
            for (Paraula.Lletra l : pa.lletres) {
                System.out.println(l.getString() +" posición: "+l.getPosicion());
            }
        }

        return result.split("(?!^)");//retorna un string concatenado de todas las letras
    }

    //int randomNum = rand.nextInt((max - min) + 1) + min;

    public String[] colocaLletra(String[] array, String paraula){
        int n;
        String cadena = "";
        Random rnd = new Random();

        n = rnd.nextInt((1 - 3) + 1) + 1;
        switch (n){
            //Horitzontal
            case 1:
                n = rnd.nextInt((0 - 99) + 1);
                for (int x = 0;x<paraula.length();x++){
                    if (n+paraula.length()%10<paraula.length() || array[n+x].length() != 0){
                        array = colocaLletra(array, paraula);
                    }else{
                        for (x = 0;x<paraula.length();x++){
                            array[x+n] = paraula.split("(?!^)")[x];
                        }
                    }
                }
                break;
            //Vertical
            case 2:
                break;
            //Diagonal
            case 3:
                break;
        }

        return array;
    }

    public Boolean comprovaParaula(AdapterView<?> parent, ArrayList<Integer> posicions){
        String paraula = "";

        for (Integer item : posicions ){
            paraula += parent.getItemAtPosition(item).toString();
        }

        if (Arrays.asList(arrayMots).contains(paraula)){
            System.out.println(paraula+" Existeix");
            return true;
        }else
            return false;
    }


    public void obtenirContactes() {
        Cursor phones = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
        System.out.println("asdasd");

        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
           // Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
            if(name != null && name.length()>1){
                nomsContactes.add(name.toUpperCase());
            }
           //System.out.println(name.toUpperCase());
        }
        phones.close();
    }

}