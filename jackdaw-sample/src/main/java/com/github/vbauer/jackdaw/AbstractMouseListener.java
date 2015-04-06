package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.annotation.JClassDescriptor;
import com.github.vbauer.jackdaw.annotation.JMessage;

import javax.tools.Diagnostic;

/**
 * @author Vladislav Bauer
 */

@JMessage(
    type = Diagnostic.Kind.NOTE,
    value = {
        "Do not forget to remove this class in the next release",
        "MouseListener interface will be used instead of it"
    }
)
@JAdapter
@JClassDescriptor
public abstract class AbstractMouseListener implements MouseListener {

    private int x;
    private int y;


    @JMessage(value = "DEADLINE!", showAfter = "22/03/2015", type = Diagnostic.Kind.NOTE)
    public AbstractMouseListener() {
        this(0, 0);
    }

    public AbstractMouseListener(final int x, final int y) {
        this.x = x;
        this.y = y;
    }


    @SuppressWarnings("unused")
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
