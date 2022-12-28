package tk.diffusehyperion.SimpleSnake.engine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tk.diffusehyperion.SimpleSnake.activities.MainActivity;

public class SnakeEngine implements Runnable{

    private final List<List<Tile>> tiles = new ArrayList<>();
    private int widthTiles = 0;
    private int heightTiles = 0;
    private int tileSize = 0;
    private List<Tile> snakeList = new ArrayList<>();
    private final Paint tilePaint = new Paint();
    private final View view;
    private final Context context;
    public Directions direction = Directions.NONE;
    public Directions nextDirection = Directions.NONE;
    public int score = 0;
    public final Handler handler = new android.os.Handler(Looper.getMainLooper());
    private int speed;

    public SnakeEngine(Context context, View view) {
        this.context = context;
        this.view = view;
        // handles tile positioning and size, and walls
    }

    public void initGame(int sizeScale, int speed, float percentageApples) {
        this.speed = speed;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        List<Integer> gcdList = new ArrayList<>();
        for (int i = 1; i <= width && i <= height; i++) {
            if (width % i == 0 && height % i == 0) {
                gcdList.add(i);
            }
        }

        tileSize = gcdList.get(gcdList.size() - sizeScale);
        widthTiles = width / tileSize;
        heightTiles = height / tileSize;
        for (int y = 0; y < heightTiles; y++) {
            List<Tile> row = new ArrayList<>();
            for (int x = 0; x < widthTiles; x++) {
                if (x == 0 || x == widthTiles - 1 || y == 0 || y == heightTiles - 1) {
                    row.add(new Tile(x, y, TileTypes.WALL));
                } else {
                    row.add(new Tile(x, y, TileTypes.EMPTY));
                }
            }
            tiles.add(row);
        }
        tilePaint.setStyle(Paint.Style.FILL);

        // handles apples and snake starting pos
        int totalTiles = widthTiles * heightTiles;
        //float percentToBeApples = 0.5F;
        for (int i = 0; i < (percentageApples/ 100F) * totalTiles; i++) {
            generateApple();
        }

        Tile snakeTile = getTile(widthTiles / 2, heightTiles / 2);
        snakeTile.setType(TileTypes.SNAKEHEAD);
        snakeList.add(snakeTile);
    }

    public void generateApple() {
        Random r = new Random();
        int rndWidth = r.nextInt(widthTiles);
        int rndHeight = r.nextInt(heightTiles);
        Tile tile = getTile(rndWidth, rndHeight);
        if (tile.type != TileTypes.EMPTY) {
            generateApple();
            // retry since its not in empty spot
        } else {
            tile.setType(TileTypes.APPLE);
        }
    }

    public void draw(Canvas canvas) {
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tilePaint.setColor(tile.getColour());
                canvas.drawRect(tile.x * tileSize, tile.y * tileSize, (tile.x + 1)  * tileSize, (tile.y + 1) * tileSize, tilePaint);
            }
        }
    }

    @Override
    public void run() {
        // nextDirection is used to buffer the next direction. this also helps prevents double inputs, which can cause the snake to move into itself.
        if (nextDirection != Directions.NONE) {
            direction = nextDirection;
            nextDirection = Directions.NONE;
        }
        if (direction == Directions.NONE) {
            handler.postDelayed(this, speed);
            return;
        }
        Tile oldHead = snakeList.get(snakeList.size() - 1);
        oldHead.setType(TileTypes.SNAKEBODY);
        Tile newHead = null;
        switch (direction) {
            case UP:
                newHead = getTile(oldHead.x, oldHead.y - 1);
                break;
            case DOWN:
                newHead = getTile(oldHead.x, oldHead.y + 1);
                break;
            case LEFT:
                newHead = getTile(oldHead.x - 1, oldHead.y);
                break;
            case RIGHT:
                newHead = getTile(oldHead.x + 1, oldHead.y);
                break;
            case NONE:
                newHead = oldHead;
                break;
        }
        if (newHead.type == TileTypes.WALL || newHead.type == TileTypes.SNAKEBODY) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            handler.removeCallbacks(this);
        } else {
            if (newHead.type == TileTypes.APPLE) {
                score++;
                generateApple();
            }
            newHead.setType(TileTypes.SNAKEHEAD);
            snakeList.add(newHead);
            if (snakeList.size() > score + 3) {
                Tile lastBody = snakeList.get(0);
                lastBody.setType(TileTypes.EMPTY);
                snakeList.remove(lastBody);
            }
            view.invalidate();
            handler.postDelayed(this, speed);
        }
    }

    public Tile getTile(int x, int y) {
        return tiles.get(y).get(x);
    }

    public void setDirection(Directions dir) {
        switch (dir) {
            case UP:
                if (this.direction == Directions.DOWN) {
                    return;
                }
                break;
            case DOWN:
                if (this.direction == Directions.UP) {
                    return;
                }
                break;
            case LEFT:
                if (this.direction == Directions.RIGHT) {
                    return;
                }
                break;
            case RIGHT:
                if (this.direction == Directions.LEFT) {
                    return;
                }
                break;
        }
        this.nextDirection = dir;
    }

    public static class Tile {
        private final int x;
        private final int y;
        private TileTypes type;

        public Tile(int x, int y, TileTypes type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public void setType(TileTypes type) {
            this.type = type;
        }

        public int getColour() {
            return type.getColour();
        }
    }
}
