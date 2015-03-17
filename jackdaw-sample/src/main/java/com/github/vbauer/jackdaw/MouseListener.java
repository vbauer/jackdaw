package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.annotation.JMessage;

/**
 * @author Vladislav Bauer
 */

@JAdapter
@JMessage(value = "Just a simple mouse listener interface", details = true)
public interface MouseListener {

    void onClick();

    void onMove(@JMessage("asd") int x, int y);

    Void press(int button);

    int getX();

    Integer getY();

    boolean isPressed(int button);

}
