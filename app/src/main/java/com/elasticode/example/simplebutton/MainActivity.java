package com.elasticode.example.simplebutton;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.elasticode.ElastiCode;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends ActionBarActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = new Button(this);
        btn.setText("OK");

        // -------Remove before production-----
        ElastiCode.devModeWithLogging();
        // ------------------------------------
        ElastiCode.startSession("", new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                if((Boolean)data){
                    ElastiCode.defineCase("First Button", 3);

                    switch (ElastiCode.stateIndexForCase("First Button")){
                        case 1:
                            btn.setBackgroundColor(Color.YELLOW);
                            break;
                        case 2:
                            btn.setBackgroundColor(Color.RED);
                            break;
                        case 0:
                        default:
                            btn.setBackgroundColor(Color.BLUE);
                            break;
                    }

                    RelativeLayout rl = (RelativeLayout)findViewById(R.id.mainLayout);
                    rl.addView(btn);

                    // Take a snap show of the screen after finish loading
                    doAfterLayout(rl, new Runnable() {
                        @Override
                        public void run() {
                            ElastiCode.takeSnapShot("First Button", MainActivity.this);
                        }
                    });

                }else{

                }
            }
        }, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs a piece of code after the next layout run
     */
    public static void doAfterLayout(final View view,final Runnable runnable){
        final ViewTreeObserver.OnGlobalLayoutListener listener=new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override public void onGlobalLayout(){
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                runnable.run();
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

}
