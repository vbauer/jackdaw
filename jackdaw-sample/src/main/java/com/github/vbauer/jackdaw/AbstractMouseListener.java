package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JAdapter;

/**
 * @author Vladislav Bauer
 */

@JAdapter
public abstract class AbstractMouseListener implements MouseListener {

    private int x;
    private int y;


    public AbstractMouseListener() {
        this(0, 0);
    }

    public AbstractMouseListener(final int x, final int y) {
        this.x = x;
        this.y = y;
    }


    protected abstract void testMouse();

    @Override
    public int getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }

}
