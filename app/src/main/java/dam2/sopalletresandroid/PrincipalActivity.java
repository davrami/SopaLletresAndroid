package dam2.sopalletresandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class PrincipalActivity extends AppCompatActivity {


    ////OPTIONS MENU
    protected static final String EXTRA_MISSATGE = "dam2.sopadelletresandroid";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                obrirActivity("main");
                return true;
            case R.id.principal:
                obrirActivity("principal");
                return true;
            case R.id.ajuda:
                obrirActivity("ajuda");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void obrirActivity(String view) {

        Log.i("info", view);
        String classe = "";

        switch (view) {
            case "main":
                setContentView(R.layout.activity_main);
                Intent intentMain = new Intent(this, MainActivity.class);
                intentMain.putExtra(EXTRA_MISSATGE, "go to main");
                startActivity(intentMain);

                break;
            case "principal":
                setContentView(R.layout.activity_principal);
                Intent intentPrincipal = new Intent(this, PrincipalActivity.class);
                intentPrincipal.putExtra(EXTRA_MISSATGE, "go to principal");
                startActivity(intentPrincipal);

                break;

            case "ajuda":
                setContentView(R.layout.activity_help);
                WebView myWebView = (WebView) findViewById(R.id.webviewAjuda);
                WebSettings webSettings = myWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                myWebView.loadUrl("http://www.ludoteka.com/sopa-de-letras.html");
                break;
        }

    }

    ///// MAIN PROGRAM
    public Button buttonGoMain;
    public Button btReset;
    public GridView gvTauler; //grid lletres
    public String[] arrayMots; //llista paraules de xml
    public GridView gvParaules; //llista paraules
    public String[][][] paraulesColocades ;
    public ArrayList<Paraula> llistaParaules = new ArrayList<Paraula>();


    public static ArrayList<Integer> letrasMarcadas;
    public String[] abc = new String[]{"A","B", "C", "D", "E","F", "G", "H", "I", "J","K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    public ArrayAdapter<String> Adapter;
    public ArrayAdapter<String> AdapterParaules;

    //private TextView tvText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //tvText = (TextView) findViewById(R.id.tvPrincipal);

        ArrayList<Paraula> llistaParaules = new ArrayList<Paraula>();
        arrayMots = getResources().getStringArray(R.array.mots); //recupera de arrays.xml les paraules
        for (String arrayMot : arrayMots) {
            System.out.println(arrayMot.toString());
        }
        String[][][] paraulesColocades =  new String[20][20][20];
        Intent intent = getIntent();
        String missatge = intent.getStringExtra(MainActivity.EXTRA_MISSATGE); // get data inside intent last view
        Log.i("info", missatge);
        //tauler
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
                    gvTauler.getChildAt(item).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                letrasMarcadas.clear();
            }
        });

        gvTauler.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("info", parent.toString()+ view+ position+id  );
                Log.i("infoView", view.toString()  );
                Log.i("infoPosition", String.valueOf(position));
                Log.i("infoId", parent.getItemAtPosition(position).toString() );
                //Log.i("infoId", String.valueOf(id ) );
                //System.out.println( abc[position]);

                letrasMarcadas.add(position);
                view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                if (comprovaParaula(parent, letrasMarcadas)){
                    btReset.callOnClick();
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
        for (int i = 0; i <= 99 && q <= 9; i += 9) {
            String paraula = arrayMots[q];
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
            Paraula p = new Paraula(paraula);
            llistaParaules.add(p);

            fila = fila.substring(0, rand) + paraula + fila.substring(rand + paraula.length());

            for (int z = 0; z < paraula.length(); z++) {
                //System.out.println( "q"+q +" i"+i+" z"+z +" pos"+(limitH+z)+" paraula"+paraula+" sub"+paraula.substring(z,z+1));
                Paraula.Lletra l = new Paraula.Lletra();
                l.setString(paraula.substring(z, z + 1));
                l.setPosicion(limitH + z+ i+ rand -1);
                p.lletres.add(l);
            }

            result = result.concat(fila); //string resultant de concatenar lletres random y paraules
            q++;


        }
        //GENERA lista paraules grid inferior
        gvParaules = (GridView) findViewById(R.id.gvParaules);
        ArrayAdapter<String> AdapterParaules = new ArrayAdapter<String>(this, android.R.layout.test_list_item, arrayMots);
        gvParaules.setAdapter(AdapterParaules);

        for (Paraula pa : llistaParaules) {
            System.out.println(pa.getNom());

            for (Paraula.Lletra l : pa.lletres) {
                System.out.println(l.getString() +" posición: "+l.getPosicion());
            }
        }

        return result.split("(?!^)");

    }


    //GENERA un array DIFERENTS POSICIONS
    public String[] generaArrayLLetres2(){

        String[] result = new String[100];
        int x = 0;
        for (int i = 0 ; i < 100;i+=20 ){
            String paraula = arrayMots[x];
            result = colocaLletra(result,paraula);
            result = ompleForats(result);
            x++;
        }

        for (int i = 0 ; i < 100;i++ ){
            System.out.println(result[i]);
        }

        return result;
    }

    //OMPLE els espais en BLANC de l'array
    public String[] ompleForats(String[] array){

        Random rnd = new Random();

        for (int x = 0; x<array.length;x++){
            if (array[x] == null ){
                array[x] = Character.toString((char) (rnd.nextInt((90 - 65) + 1) + 65));
            }
        }

        return array;
    }

    //COLOCA les paraules ALEATORIAMENT
    public String[] colocaLletra(String[] array, String paraula){
        //int randomNum = rand.nextInt((max - min) + 1) + min;
        int n;
        String cadena = "";
        String[] fragments;
        Random rnd = new Random();

        n = rnd.nextInt((3 - 1) + 1) + 1;
        Log.i("pos",""+n);
        switch (n){
            //Horizontal
            case 1:
                do {
                    n = rnd.nextInt((99) + 1);
                }while (n+paraula.length()>99);

                System.out.println("La n-----------------------> "+n);
                System.out.println("La paraula-----------------> "+paraula);

                for (int x = 0;x<paraula.length();x++){
                    if ((n+paraula.length())%10<paraula.length()){
                        array = colocaLletra(array, paraula);
                        break;
                    }else{
                        System.out.println("La n-----------------------> Sí");
                    }
                }
                fragments = paraula.split("(?!^)");
                for (int x = 0;x<paraula.length();x++){
                    array[x+n] = fragments[x];
                }

                break;
            //Vertical
            case 2:
                do {
                    n = rnd.nextInt((99) + 1);
                }while (n+(paraula.length()*10)>99);

                System.out.println("La n-----------------------> "+n);
                System.out.println("La paraula-----------------> "+paraula);

                for (int x = 0;x<(paraula.length()*10);x+=10){
                    if ((n+(paraula.length()*10))%10<paraula.length()){
                        array = colocaLletra(array, paraula);
                        break;
                    }else{
                        System.out.println("La n-----------------------> Sí");
                    }
                }
                fragments = paraula.split("(?!^)");
                for (int x = 0;x<(paraula.length()*10);x+=10){
                    array[x+n] = fragments[x/10];
                }
                break;
            //Diagonal
            case 3:
                do {
                    n = rnd.nextInt((99) + 1);
                }while (n+(paraula.length()*11)>99);

                System.out.println("La n-----------------------> "+n);
                System.out.println("La paraula-----------------> "+paraula);

                for (int x = 0;x<(paraula.length()*11);x+=11){
                    if ((n+(paraula.length()*11))%10<paraula.length()){
                        array = colocaLletra(array, paraula);
                        break;
                    }else{
                        System.out.println("La n-----------------------> Sí "+paraula);
                    }
                }
                fragments = paraula.split("(?!^)");
                for (int x = 0;x<(paraula.length()*11);x+=11){
                    array[x+n] = fragments[x/11];
                }
                break;
        }

        return array;
    }

    //Comprova que la paraula existeixi dins l'array de respostes
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
}
