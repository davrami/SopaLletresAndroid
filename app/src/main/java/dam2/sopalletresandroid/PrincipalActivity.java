package dam2.sopalletresandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
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

    public Button buttonGoMain;
    public Button btReset;
    public GridView gvTauler;
    public String[] arrayMots;

    public static ArrayList<Integer> letrasMarcadas;
    public String[] abc = new String[]{"A","B", "C", "D", "E","F", "G", "H", "I", "J","K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    ArrayAdapter<String> Adapter;

    //private TextView tvText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //tvText = (TextView) findViewById(R.id.tvPrincipal);

        arrayMots = getResources().getStringArray(R.array.mots);
        /*for (String arrayMot : arrayMots) {
            System.out.println(arrayMot.toString());
        }*/

        Intent intent = getIntent();
        String missatge = intent.getStringExtra(MainActivity.EXTRA_MISSATGE); // get data inside intent last view
        Log.i("info", missatge);

        //tauler
        gvTauler = (GridView) findViewById(R.id.gvTauler);
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
        Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item, array);
        gvTauler.setAdapter(Adapter);
    }

    //test
    public String[] generaArrayLLetres(){

        String result ="";
        int x = 0;
        for (int i = 0 ; i < 100;i+=9 ){
            String paraula = arrayMots[x];
            result = result.concat(paraula);
            x++;
        }

        return result.split("(?!^)");
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
}
