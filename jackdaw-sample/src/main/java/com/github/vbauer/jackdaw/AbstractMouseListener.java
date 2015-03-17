package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.annotation.JClassDescriptor;
import com.github.vbauer.jackdaw.annotation.JMessage;

/**
 * @author Vladislav Bauer
 */

@JAdapter
@JClassDescriptor
public abstract class AbstractMouseListener implements MouseListener {

    private int x;
    private int y;


    @JMessage("Default constructor")
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
