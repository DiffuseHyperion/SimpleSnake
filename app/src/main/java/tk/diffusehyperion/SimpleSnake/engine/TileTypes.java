package tk.diffusehyperion.SimpleSnake.engine;

import android.graphics.Color;

public enum TileTypes {
    EMPTY(Color.GRAY),
    SNAKEHEAD(0xFFFFFFFF),
    SNAKEBODY(0xFFDDDDDD),
    APPLE(Color.RED),
    WALL(Color.DKGRAY);

    private final int colour;

    TileTypes(int colour) {
        this.colour = colour;
    }

    public int getColour() {
        return colour;
    }
}
