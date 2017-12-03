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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
                myWebView.loadUrl("file:///android_asset/Reglas del juego.html");
                break;
        }

    }

    ///// MAIN PROGRAM
    public Button buttonGoMain;
    public Button btReset;
    public GridView gvTauler; //grid lletres
    public String[] arrayMots; //llista paraules de xml
    public ArrayList<String> mots;
    public ArrayList<String> llegenda;

    public GridView gvParaules; //llista paraules
    public ArrayList<Paraula> llistaParaules;
    public static ArrayList<Paraula> paraulasCompletadas;
    public ArrayList<String> nomsContactes;

    public Integer puntuacion;
    public static ArrayList<Integer> letrasMarcadas;
    public String[] abc = new String[]{"A","B", "C", "D", "E","F", "G", "H", "I", "J","K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    public ArrayAdapter<String> Adapter;

    JocDbHelper jDB;//SQL DATABASE
    long idDatabase = 0;//Id_PARTIDA
    //public ArrayAdapter<String> AdapterParaules;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                100);

        String [] paraulesXML = getResources().getStringArray(R.array.mots); //recupera de arrays.xml les paraules
        nomsContactes = new ArrayList<String>();

        jDB = new JocDbHelper(this);//Obre acces a la base de dades SQL
        idDatabase = jDB.insertPuntuacio(new SimpleDateFormat("yyy/MM/dd HH:mm:ss").format(new Date()), 0);

        obtenirContactes(); //obtiene nombres contactos , se ejecuta en el overray de los permisos si estos están concedidos

        llistaParaules = new ArrayList<Paraula>();
        paraulasCompletadas = new ArrayList<Paraula>();

        mots = new ArrayList<String>();//lista de paraulas (contactos + xml)
        //introduce los nombres contactos como palabras para la sopa de letras

        int sizeMots = 40; //Establim la cuantitat de noms que agafa

        if(nomsContactes.size() > 0){
            for (String nom : nomsContactes){
                if(mots.size() < sizeMots && nom.split(" ")[0].length()<8){
                    //Afegit l'split per evitar que agafi noms complets amb cognoms
                    //Només n'agafa si són més curts de 8 caracters
                    System.out.println("CONTACT");

                    mots.add(nom.split(" ")[0]);
                }
            }
        }
        if(mots.size()<sizeMots){ //si no hay suficientes nombres mete palabras del xml
            for (String p : paraulesXML){
                if(mots.size()  < sizeMots){
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

                    String result = Utility.comprovaPosicio(position, llistaParaules, letrasMarcadas, jDB, (int)idDatabase);

                    view.setBackgroundColor(getResources().getColor(android.R.color.tertiary_text_dark));

                    if (result == "Completada") {
                        System.out.println("paraula COMPLETADA");
                        // TODO marcar la palabra de abajo
                        // Todo sumar puntos
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
                }else{
                    view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    System.out.println(position); //borrar a array de posiciones marcadas
                    System.out.println( letrasMarcadas.remove(letrasMarcadas.indexOf(position)));
                }
            }
        });
    }


    //GENERA el adapter y inserta en el gridview
    public void generateGridTauler() {
        buttonGoMain.setVisibility(View.GONE); // hide button generate
        String[] array = generaArrayLLetres2();
        Adapter = new ArrayAdapter<String>(this,R.layout.layout_sopa, array);
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
            System.out.println("Mot------------->"+paraula);
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

    //GENERA un array DIFERENTS POSICIONS
    public String[] generaArrayLLetres2(){

        String[] result = new String[100];
        int x = 0,pos;
        Random rnd = new Random();
        llegenda = new ArrayList<>();
        for (int i = 0 ; i < 100;i+=10 ){
            pos = rnd.nextInt(mots.size());
            String paraula = mots.get(pos);
            Paraula p = new Paraula(paraula, i/10);
            llistaParaules.add(i/10, p);
            result = colocaLletra(result,paraula,i/10);
            llegenda.add(paraula);
            x++;
        }
        result = ompleForats(result);

        for (int i = 0 ; i < 100;i++ ){
            System.out.println(result[i]);
        }

        //GENERA lista paraules grid inferior
        gvParaules = (GridView) findViewById(R.id.gvParaules);
        ArrayAdapter<String> AdapterParaules = new ArrayAdapter<String>(this, R.layout.layout_centered, llegenda);
        gvParaules.setAdapter(AdapterParaules);

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
    public String[] colocaLletra(String[] array, String paraula, int index){
        //int randomNum = rand.nextInt((max - min) + 1) + min;
        int n, allargada;
        String cadena = "";
        String[] fragments;
        Random rnd = new Random();
        boolean insert = false;
        Paraula.Lletra l;

        do {
            n = rnd.nextInt((3 - 1) + 1) + 1;
            allargada=paraula.length()-1;
            Log.i("pos",""+n);
            outerswitch:
            switch (n){
                //Horizontal
                case 1:
                    do {
                        n = rnd.nextInt((99) + 1);
                    }while ((n+allargada)>99);

                    System.out.println("La n-----------------------> "+n);
                    System.out.println("La paraula-----------------> "+paraula);

                    for (int x = 0;x<paraula.length();x++){
                        if ((n+x)%10==9 || array[n+x] != null){
                            break outerswitch;
                        }else{
                            System.out.println("La n-----------------------> Sí");
                        }
                    }

                    fragments = paraula.split("(?!^)");
                    for (int x = 0;x<paraula.length();x++){
                        l = new Paraula.Lletra();
                        array[x+n] = fragments[x];
                        l.setString(fragments[x]);
                        l.setPosicion(x+n);
                        llistaParaules.get(index).lletres.add(l);
                    }
                    insert = true;
                    break;
                //Vertical
                case 2:
                    do {
                        n = rnd.nextInt((99) + 1);
                    }while (n+(allargada*10)>99);

                    System.out.println("La n-----------------------> "+n);
                    System.out.println("La paraula-----------------> "+paraula);

                    for (int x = 0;x<(paraula.length()*10);x+=10){
                        if ((n+x)%10==9 || array[n+x] != null){
                            break outerswitch;
                        }else{
                            System.out.println("La n-----------------------> Sí");
                        }
                    }
                    fragments = paraula.split("(?!^)");
                    for (int x = 0;x<paraula.length();x++){
                        l = new Paraula.Lletra();
                        array[(x*10)+n] = fragments[x];
                        l.setString(fragments[x]);
                        l.setPosicion((x*10)+n);
                        llistaParaules.get(index).lletres.add(l);
                    }
                    insert = true;
                    break;
                //Diagonal
                case 3:
                    do {
                        n = rnd.nextInt((99) + 1);
                    }while (n+(allargada*11)>99);

                    System.out.println("La n-----------------------> "+n);
                    System.out.println("La paraula-----------------> "+paraula);

                    for (int x = 0;x<(paraula.length()*11);x+=11){
                        if ((n+x)%10==9 || array[n+x] != null){
                            break outerswitch;
                        }else{
                            System.out.println("La n-----------------------> Sí "+paraula);
                        }
                    }
                    fragments = paraula.split("(?!^)");
                    for (int x = 0;x<paraula.length();x++){
                        l = new Paraula.Lletra();
                        array[n+(x*11)] = fragments[x];
                        l.setString(fragments[x]);
                        l.setPosicion(n+(x*11));
                        llistaParaules.get(index).lletres.add(l);
                    }
                    insert = true;
                    break;
            }
        }while (!insert);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        //Code 100 es respecte a l'accés als contactes
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                obtenirContactes();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display contactnames", Toast.LENGTH_SHORT).show();
            }
        }
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
