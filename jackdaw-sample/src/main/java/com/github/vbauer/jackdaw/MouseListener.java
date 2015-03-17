package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JAdapter;

/**
 * @author Vladislav Bauer
 */

@JAdapter
public interface MouseListener {

    void onClick();

    void onMove(int x, int y);

    Void press(int button);

    int getX();

    Integer getY();

    boolean isPressed(int button);

}
