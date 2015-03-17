package com.github.vbauer.jackdaw.model;

import com.github.vbauer.jackdaw.annotation.JBean;

/**
 * @author Vladislav Bauer
 */

@JBean
public abstract class AbstractUserModel {

    protected int id;
    protected String username;
    protected String password;
    protected Boolean activated;
    protected boolean admin;

    protected int getId() {
        return id;
    }

}
