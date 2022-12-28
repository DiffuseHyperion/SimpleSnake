package tk.diffusehyperion.SimpleSnake.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import tk.diffusehyperion.SimpleSnake.R;

public class MainActivity extends Activity {

    // sharedprefs are used to save config values between games

    private SharedPreferences sharedPref;
    private SeekBar appleBar;
    private SeekBar sizeBar;
    private SeekBar speedBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Button startButton = findViewById(R.id.startButton);

        appleBar = findViewById(R.id.appleBar);
        sizeBar = findViewById(R.id.sizeBar);
        speedBar = findViewById(R.id.speedBar);

        appleBar.setProgress(sharedPref.getInt("apple", 1));
        sizeBar.setProgress(sharedPref.getInt("size", 1));
        speedBar.setProgress(sharedPref.getInt("speed", 2));

        initListener(appleBar, "apple");
        initListener(sizeBar, "size");
        initListener(speedBar, "speed");

        startButton.setOnClickListener((listener) -> startGame());
    }

    private void initListener(SeekBar bar, String key) {
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(key, progress);
                    editor.apply();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);

        int appleBarProg = appleBar.getProgress();
        int sizeBarProg = sizeBar.getProgress();
        int speedBarProg = speedBar.getProgress();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("apple", appleBarProg);
        editor.putInt("size", sizeBarProg);
        editor.putInt("speed", speedBarProg);
        editor.apply();

        intent.putExtra("apple", appleBarProg);
        intent.putExtra("size", sizeBarProg);
        intent.putExtra("speed", speedBarProg);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
