package tk.diffusehyperion.SimpleSnake.activities;

import android.app.Activity;
import android.os.Bundle;

import tk.diffusehyperion.SimpleSnake.R;
import tk.diffusehyperion.SimpleSnake.engine.SnakeEngine;
import tk.diffusehyperion.SimpleSnake.views.GameView;

public class GameActivity extends Activity {

    private SnakeEngine engine;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameView gameView = findViewById(R.id.gameView);

        Bundle settings = getIntent().getExtras();
        int sizeScale = settings.getInt("size");
        int speed = settings.getInt("speed");
        int apple = settings.getInt("apple");

        this.engine = gameView.engine;
        engine.initGame(sizeScale + 1, (5 - (speed)) * 50, (apple + 1) * 0.4F);
        engine.run();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        engine.handler.removeCallbacks(engine);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
