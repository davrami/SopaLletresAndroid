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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PrincipalActivity extends AppCompatActivity {

    public Button buttonGoMain;
    public Button btReset;
    public GridView gvTauler;
    public List<Integer> letrasMarcadas;
    public String[] abc = new String[]{"A","B", "C", "D", "E","F", "G", "H", "I", "J","K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T","U", "V", "W", "X", "Y", "Z"};
    ArrayAdapter<String> Adapter;

    //private TextView tvText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //tvText = (TextView) findViewById(R.id.tvPrincipal);


        Intent intent = getIntent();
        String missatge = intent.getStringExtra(MainActivity.EXTRA_MISSATGE); // get data inside intent last view
        Log.i("info", missatge);
        //tauler
        gvTauler = (GridView) findViewById(R.id.gvTauler);
        final List<Integer> letrasMarcadas = new ArrayList<>();
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

                for(Integer item : letrasMarcadas){
                    //System.out.println(item);
                    System.out.println();
                    gvTauler.getChildAt(item).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }

               /* final int size = gvTauler.getChildCount();
                for(int i = 0; i < size; i++) {
                    ViewGroup gridChild = (ViewGroup) mGridView.getChildAt(i);
                    int childSize = gridChild.getChildCount();
                    for(int k = 0; k < childSize; k++) {
                        if( gridChild.getChildAt(k) instanceof TextView ) {
                            gridChild.getChildAt(k).setVisibility(View.GONE);
                        }
                    }
                }*/



            }
        });

        gvTauler.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("info", parent.toString()+ view+ position+id  );
                Log.i("info", view.toString()  );
                Log.i("info", String.valueOf(position));
                Log.i("info", String.valueOf(id ) );

                System.out.println( abc[position]);
                letrasMarcadas.add(position);

                view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        });
    }


    public void generateGridTauler() {
        buttonGoMain.setVisibility(View.GONE); // hide button generate

        Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item, abc);
        gvTauler.setAdapter(Adapter);


    }


}
