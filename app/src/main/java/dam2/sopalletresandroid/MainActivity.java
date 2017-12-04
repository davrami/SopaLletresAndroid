package dam2.sopalletresandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected static final String EXTRA_MISSATGE = "dam2.sopadelletresandroid";
    protected Button buttonGoPrincipal; //Declare button in view
    public GridView topListData;
    public GridView topListPunts;
    public ArrayAdapter<String> Adapter;
    public JocDbHelper jDB = new JocDbHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topListPunts = (GridView) findViewById(R.id.topListPunts);
        ArrayList punts = (ArrayList) jDB.getDataPuntuacio()[1];
        topListData = (GridView) findViewById(R.id.topListData);
        ArrayList datas = (ArrayList) jDB.getDataPuntuacio()[0];
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        topListData.setAdapter(Adapter);
        topListPunts.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, punts));


        buttonGoPrincipal = (Button) findViewById(R.id.btPrincipal); //Link button in view with event
        buttonGoPrincipal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("info", "btprincipal");

                obrirActivity("principal");
            }
        });

    }

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
}