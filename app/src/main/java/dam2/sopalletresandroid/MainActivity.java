package dam2.sopalletresandroid;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        GridView gridview = (GridView) findViewById(R.id.greedlayout);

        String[] TextsLayout = new String[]{"A","B","A","B","A","B","A","B","A","B","A","B","A","B","A","B"};
        gridview.setAdapter(new TextViewAdapter(this,TextsLayout));
        //gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position + "-" +parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e("E",""+parent.getItemAtPosition(position).toString());
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }

        });
        /*gridview.setOnTouchListener(new GridView.OnTouchListener(){
            float initialX, initialY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        v.performClick();

                        Log.e("TAG", "Action was DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e("TAG", "Action was MOVE"+event.getX());
                        break;

                    case MotionEvent.ACTION_UP:
                        float finalX = event.getX();
                        float finalY = event.getY();
                        v.performClick();
                        Log.e("TAG", "Action was UP");

                        if (initialX < finalX) {
                            Log.e("TAG", "Left to Right swipe performed");
                            v.performClick();
                        }

                        if (initialX > finalX) {
                            Log.e("TAG", "Right to Left swipe performed");
                            v.performClick();
                        }

                        if (initialY < finalY) {
                            Log.e("TAG", "Up to Down swipe performed");
                        }

                        if (initialY > finalY) {
                            Log.e("TAG", "Down to Up swipe performed");
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        Log.e("TAG","Action was CANCEL");
                        break;

                    case MotionEvent.ACTION_OUTSIDE:
                        Log.e("TAG", "Movement occurred outside bounds of current screen element");
                        break;

                }
                return true;
        }
            @Override
            public boolean performClick() {
                // tried adding this method to get rid of warning, but didn't work
                super.performClick();
                return true;
            }
        });*/
    }

    /*private String TAG = AppCompatActivity.class.getSimpleName();
    float initialX, initialY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //


        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                Log.e("TAG", "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("TAG", "Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                Log.e("TAG", "Action was UP");

                if (initialX < finalX) {
                    Log.e("TAG", "Left to Right swipe performed");
                }

                if (initialX > finalX) {
                    Log.e("TAG", "Right to Left swipe performed");
                }

                if (initialY < finalY) {
                    Log.e("TAG", "Up to Down swipe performed");
                }

                if (initialY > finalY) {
                    Log.e("TAG", "Down to Up swipe performed");
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                Log.e("TAG","Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.e("TAG", "Movement occurred outside bounds of current screen element");
                break;

        }
        return super.onTouchEvent(event);
    }*/

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
    }*/
}
