package creek.student.finalproject;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private int maxRow;
    private int level = 1;
    private int margin = 450;
    private int row = R.id.tableRow1;
    private int count = 0;
    private boolean isPlaying = true;
    private static boolean goingUp = true;
    private Handler mHandler;
    private ImageView img;
    private ConstraintLayout constraintLayout;
    private ArrayList<Block> blocks = new ArrayList<>();
    private Player player;

    public void blockArray(){
        for(int i=0;i<100;i++){
            blocks.add(new Block((BlockType )))
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ActionBar actionBar = getSupportActionBar();
        maxRow = R.id.tableRow5;
        assert actionBar != null;
        actionBar.hide();
        player = new Player(R.id.drill, this);
        img = player.getImage().getImageView();
        constraintLayout = findViewById(R.id.constraintLayout8);
        mHandler = new Handler();
        start();

    }

    public static boolean isGoingUp(){
        return goingUp;
    }
    public static void changeGoingUp(){
        goingUp= true;
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try{ move(); }
            finally {
                if (count >= 286 * 5 + 5000 + 93) stop();
                else mHandler.postDelayed(mStatusChecker, 1);
            }
        }
    };

    void start() {
        mStatusChecker.run();
    }
    void stop() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void move() {

        imageMove();

        //player = new Player(R.id.drill, this);
        for(int i = R.id.imageView; i <= R.id.imageView9; i++){
            Block block = new Block(i, this);
            player.update();
            block.update();

            if(player.intersected(block)) {
                Log.e("Hit", i + " was hit!");
                //
                //block.hit();
            }
        }
        count++;
        if(margin == 0 && row == maxRow && goingUp) { goingUp = false; }
        //else if(margin == 640 && row == R.id.tableRow1 && !goingUp) { stop(); }
        if(margin == 0 && goingUp){
            margin = 286;
            row = row + 1;
        }
        if(margin == 286 && !goingUp){
            margin = 0;
            row = row - 1;
        }
        changeConstraint();
    }

    private void changeConstraint(){
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        if(goingUp) {
            margin--;
            constraintSet.connect(row, ConstraintSet.TOP, R.id.constraintLayout8, ConstraintSet.TOP, margin);
        }
        else {
            margin++;
            if(row == R.id.tableRow1)
                constraintSet.connect(row, ConstraintSet.TOP, R.id.constraintLayout8, ConstraintSet.TOP, margin);
            else
                constraintSet.connect(row, ConstraintSet.TOP, row - 1, ConstraintSet.TOP, margin);
        }
        constraintSet.applyTo(constraintLayout);
    }

    public void pause(View v)
    {
        if(isPlaying){
            isPlaying = false;

            stop();
        }
        else{
            start();
        }
    }

    private void imageMove(){

        img.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        //if(StartPT.x + event.getX() - DownPT.x >= width || StartPT.x + event.getX() - DownPT.x <= 0){
                            //break;
                        //}
                        StartPT.set(img.getX(), img.getY());
                        img.setX((int)(StartPT.x + event.getX() - DownPT.x));
                        StartPT.set( img.getX(), img.getY() );
                        break;
                    case MotionEvent.ACTION_DOWN :
                        DownPT.set( event.getX(), event.getY() );
                        StartPT.set( img.getX(), img.getY() );
                        break;
                    case MotionEvent.ACTION_UP :
                        // Nothing have to do
                        break;
                    default :
                        break;
                }

            return true;
            }

        });}
}
