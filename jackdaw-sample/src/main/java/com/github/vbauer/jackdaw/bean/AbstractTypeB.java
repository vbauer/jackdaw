package com.github.vbauer.jackdaw.bean;

import com.github.vbauer.jackdaw.annotation.JBean;
import com.github.vbauer.jackdaw.annotation.JService;

/**
 * @author Vladislav Bauer
 */

@JBean
@JService(BaseType.class)
public class AbstractTypeB implements BaseType {
    protected TypeA type;
}
