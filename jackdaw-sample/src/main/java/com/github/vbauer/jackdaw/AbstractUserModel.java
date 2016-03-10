package com.github.vbauer.jackdaw;

import com.github.vbauer.jackdaw.annotation.JBean;

/**
 * @author Vladislav Bauer
 */

@JBean
@RoleList({
    @Role("user"),
    @Role("moderator"),
    @Role("administrator")
})
public abstract class AbstractUserModel {

    public static final String DEFAULT_PASSWORD = "qwerty";

    protected int id;
    protected String username;
    protected String password;
    protected Boolean activated;
    protected boolean admin;

    protected int getId() {
        return id;
    }

}
