package com.github.vbauer.jackdaw.bean;

import com.github.vbauer.jackdaw.annotation.JAdapter;
import com.github.vbauer.jackdaw.annotation.JBean;
import com.github.vbauer.jackdaw.annotation.JBuilder;
import com.github.vbauer.jackdaw.annotation.JClassDescriptor;
import com.github.vbauer.jackdaw.annotation.JComparator;
import com.github.vbauer.jackdaw.annotation.JFactoryMethod;
import com.github.vbauer.jackdaw.annotation.JFunction;
import com.github.vbauer.jackdaw.annotation.JIgnore;
import com.github.vbauer.jackdaw.annotation.JPredicate;
import com.github.vbauer.jackdaw.annotation.JService;
import com.github.vbauer.jackdaw.annotation.JSupplier;

/**
 * @author Vladislav Bauer
 */
@JIgnore
@JAdapter
@JBean
@JBuilder
@JClassDescriptor
@JComparator
@JFactoryMethod
@JFunction
@JPredicate
@JService(Object.class)
@JSupplier
public abstract class IgnoredBeanModel {
}
