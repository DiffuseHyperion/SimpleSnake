package tk.diffusehyperion.SimpleSnake.views;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import tk.diffusehyperion.SimpleSnake.engine.Directions;
import tk.diffusehyperion.SimpleSnake.engine.SnakeEngine;

public class GameView extends View {

    public SnakeEngine engine;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        setFocusable(true);
        requestFocus();
        setFocusableInTouchMode(true);

        engine = new SnakeEngine(context, this);
        new Thread(() -> {
            Instrumentation inst = new Instrumentation();
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        }).start();
        // for some reason, onKeyDown wont be triggered on the first press. this is a hacky solution but it works lol
    }

    @Override
    protected void onDraw(Canvas canvas) {
        engine.draw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                engine.setDirection(Directions.UP);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                engine.setDirection(Directions.DOWN);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                engine.setDirection(Directions.LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                engine.setDirection(Directions.RIGHT);
                return true;
            default:
                return false;
        }
    }

}
