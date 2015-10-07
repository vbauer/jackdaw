# Jackdaw [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Jackdaw-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1655) [![Build Status](https://travis-ci.org/vbauer/jackdaw.svg)](https://travis-ci.org/vbauer/jackdaw) [![Maven](https://img.shields.io/github/tag/vbauer/jackdaw.svg?label=maven)](https://jitpack.io/#vbauer/jackdaw)

<img align="right" style="margin-left: 15px" width="300" height="243" src="jackdaw-misc/jackdaw.png" />

**Jackdaw** is a Java [Annotation Processor](http://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/Processor.html)
which allows to simplify Java/Android development and prevents writing of tedious code.

Jackdaw was inspired by [Lombok](http://projectlombok.org) project, but in comparison with Lombok:

* it does not need to have an extra plugin in IDE
* it does not modify the existing source code


## Features

**Jackdaw** supports the following compile time annotations:

<ul>
    <li><a href="#jadapter">@JAdapter</a></li>
    <li><a href="#jbean">@JBean</a></li>
    <li><a href="#jbuilder">@JBuilder</a></li>
    <li><a href="#jclassdescriptor">@JClassDescriptor</a></li>
    <li><a href="#jcomparator">@JComparator</a></li>
    <li><a href="#jfactorymethod">@JFactoryMethod</a></li>
    <li><a href="#jfunction">@JFunction</a></li>
    <li><a href="#jmessage">@JMessage</a></li>
    <li><a href="#jpredicate">@JPredicate</a></li>
    <li><a href="#jrepeatable">@JRepeatable</a></li>
    <li><a href="#jservice">@JService</a></li>
    <li><a href="#jsupplier">@JSupplier</a></li>
</ul>


## Setup

**Jackdaw** uses [JitPack.io](https://jitpack.io) for distribution, so
you need to configure JitPack's Maven repository to fetch artifacts (dependencies).

It is necessary to make dependency on `jackdaw-core`.
This module contains compile time annotations which will be used to give a hints for APT.
Module `jackdaw-apt` contains annotation processor and all correlated logic.

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependencies>
    <dependency>
        <groupId>com.github.vbauer.jackdaw</groupId>
        <artifactId>jackdaw-core</artifactId>
        <version>${jackdaw.version}</version>
    </dependency>
</dependencies>
```

After that, you need to configure `maven-compiler-plugin`:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven.compiler.plugin.version}</version>
            <configuration>
                <forceJavacCompilerUse>true</forceJavacCompilerUse>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>com.github.vbauer.jackdaw</groupId>
                    <artifactId>jackdaw-apt</artifactId>
                    <version>${jackdaw.version}</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

### Gradle

Add custom repository:

```groovy
repositories {
    mavenCentral()
    maven {
        url 'https://jitpack.io'
    }
}
```

Add `provided` scope to prevent `jackdaw-apt`:
```groovy
configurations {
    provided
}

sourceSets {
    main.compileClasspath += configurations.provided
    test.compileClasspath += configurations.provided
    test.runtimeClasspath += configurations.provided
}
```

Specify needed dependencies:
```
dependencies {
    compile 'com.github.vbauer.jackdaw:jackdaw-core:1.0.6'
    provided 'com.github.vbauer.jackdaw:jackdaw-apt:1.0.6'
}
```


## Configuration

Available parameters for annotation processor:

* **addSuppressWarningsAnnotation** - Add `@SuppressWarnings("all")` annotation on all generated classes to prevent unnecessary issues of IDE inspections (default value is `true`).
* **addGeneratedAnnotation** - Add `@Generated` annotation on all generated classes to have possibility skip execution of static code analysis (default value is `true`).
* **addGeneratedDate** - Add `date` parameter to `@Generated` annotation. It is also necessary to switch on `addGeneratedAnnotation` parameter (default value is `false`).

Example configuration for maven-compiler-plugin:
```xml
<configuration>
    <compilerArgs>
        <arg>-AaddGeneratedAnnotation=true</arg>
        <arg>-AaddGeneratedDate=true</arg>
        <arg>-AaddSuppressWarningsAnnotation=false</arg>
    </compilerArgs>
</configuration>
```


## Annotations

Some things that you need to know before exploring an examples:

* Code style in the below examples was changed to minimize text in README.
* Some annotations and imports were also removed to simplify understanding.
* All generated classes will have the same package as original classes.


### @JAdapter

**@JAdapter** allows to create class with empty method implementations using some interface or class.
Using generated class, you can override only needed methods (like in Swing, ex: [MouseAdapter](http://docs.oracle.com/javase/7/docs/api/java/awt/event/MouseAdapter.html)).

Original class `MouseListener`:
```java
@JAdapter
public interface MouseListener {
    void onMove(int x, int y);
    void onPressed(int button);
}
```
Generated class `MouseListenerAdapter`:
```java
public class MouseListenerAdapter implements MouseListener {
    public void onMove(final int x, final int y) {
    }
    public void onPresses(final int button) {
    }
}
```


### @JBean
**@JBean** generates some boilerplate code that is normally associated with simple POJOs (Plain Old Java Objects) and beans:

* getters for all non-static/private fields,
* setters for all non-static/private/final fields,
* and copy constructors from super class

Original class `AbstractUserModel`:
```java
@JBean
public abstract class AbstractUserModel {
    protected int id;
    protected String username;
    protected String password;
    protected boolean admin;
}
```
Generated class `User`:
```java
public class User extends AbstractUserModel {
    public User() { super(); }

    public void setId(final int id) { this.id = id; }
    public void getId() { return id; }
    
    public String getUsername() { return username; }
    public void setUsername(final String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(final String password) { this.password = password; }
    
    public boolean isAdmin() { return admin; }
    public void setAdmin(final boolean admin) { this.admin = admin; }
}
```

Prefix 'Abstract' and postfix 'Model' will be removed if they are presented.


### @JBuilder
The **@JBuilder** annotation produces complex builder APIs for your classes.

Original class `Company`:
```java
@JBuilder
public class Company {
    private int id;
    private String name;
    private Set<String> descriptions;

    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public Set<String> getDescriptions() { return descriptions; }
    public void setDescriptions(final Set<String> descriptions) { this.descriptions = descriptions; }
}
```
Generated class `CompanyBuilder`:
```java
public class CompanyBuilder {
    private int id;
    private String name;
    private Set<String> descriptions;

    public static CompanyBuilder create() {
        return new CompanyBuilder();
    }
    public CompanyBuilder id(final int id) {
        this.id = id;
        return this;
    }
    public CompanyBuilder name(final String name) {
        this.name = name;
        return this;
    }
    public CompanyBuilder descriptions(final Set<String> descriptions) {
        this.descriptions = descriptions;
        return this;
    }
    public Company build() {
        final Company object = new Company();
        object.setId(id);
        object.setName(name);
        object.setListed(listed);
        object.setDescriptions(descriptions);
        return object;
    }
}
```

`@JBuilder` lets you automatically produce the code required to have your class be instantiable with code such as:
```java
CompanyBuilder.create()
    .id(1)
    .name("John Smith")
    .descriptions(Collections.singleton("Good guy"))
    .build()
```


### @JClassDescriptor
Sometimes it is necessary to use reflection, so it will be useful to have some string constants.
**@JClassDescriptor** generates it for you easily.

Available parameters:

* **fields** - generate information about fields (default is `true`).
* **methods** - generate information about methods (default is `true`).

Original class `Company`:
```java
@JClassDescriptor
public class Company {
    private int id;
    private String name;

    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }
}
```
Generated class `CompanyClassDescriptor`:
```java
public final class CompanyClassDescriptor {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";

    public static final String METHOD_ID = "getId";
    public static final String METHOD_SET_ID = "setId";
    public static final String METHOD_NAME = "getName";
    public static final String METHOD_SET_NAME = "setName";

    private CompanyClassDescriptor() {
        throw new UnsupportedOperationException();
    }
}
```

### @JComparator
To generated safe and well-coded comparator, you have to write a lot of boilerplate code.
**@JComparator** annotation allows to simplify this situation.
To generate reverse order comparator, use parameter `reverse`.
By default, all generated comparators are null-safe. Use `nullable` parameter to generate not null-safe comparators.

There are several ways to generate comparator or group of comparators.
It depends on the annotation location:

* **annotation on field** - generate comparator only for one specified field.
* **annotation on method without args** - generate comparator using method with empty list of arguments and non-void return-value.
* **annotation on class** - generate comparators using 2 previous strategies (for all fields and simple methods).

Original class `Company`:
```java
public class Company {
    @JComparator(nullable = false) private String name;
    private long revenue;
    
    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    @JComparator public long getRevenue() { return revenue; }
    public void setRevenue(final long revenue) { this.revenue = revenue; }
}
```
Generated class `CompanyComparators`:
```java
public final class CompanyComparators {
    public static final Comparator<Company> NAME = new Comparator<Company>() {
        public int compare(final Company o1, final Company o2) {
            final String v1 = o1.getName();
            final String v2 = o2.getName();
            return v1.compareTo(v2);
        }
    };
    public static final Comparator<Company> REVENUE = new Comparator<Company>() {
        public int compare(final Company o1, final Company o2) {
            final Long v1 = o1 == null ? null : o1.getRevenue();
            final Long v2 = o2 == null ? null : o2.getRevenue();
            if (v1 == v2) {
                return 0;
            } else if (v1 == null) {
                return -1;
            } else if (v2 == null) {
                return 1;
            }
            return v1.compareTo(v2);
        }
    };
    private CompanyComparators() {
        throw new UnsupportedOperationException();
    }
}
```


### @JFactoryMethod
**@JFactoryMethod** allows to use pattern *Factory Method* for object instantiation.
To use this annotation it is necessary to have setters and default constructor in original class.

Available parameters:

* **method** - factory method name (default value is `"create"`).
* **all** - use all fields of class in factory method (default value is `true`).
* **arguments** - use only specified fields in factory method (it is an empty array by default).

Original class `Company`:
```java
@JFactoryMethod
public class Company {
    private int id;
    private String name;

    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }
}
```
Generated class `CompanyFactory`:
```java
public final class CompanyFactory {
    private CompanyFactory() {
        throw new UnsupportedOperationException();
    }
    public static Company create(final int id, final String name) {
        final Company object = new Company();
        object.setId(id);
        object.setName(name);
        return object;
    }
}
```

### @JFunction

The **@JFunction** annotation generates `Function` implementation to use functional-way for programming.
You can specify different function interfaces for implementation generation (`JFunctionType`):

* **JAVA** - functions from Java 8 (`java.util.function.Function`)
* **GUAVA** - Guava functions (`com.google.common.base.Function`)

There are several ways to generate function or group of functions.
It depends on the annotation location:

* **annotation on field** - generate function only for one specified field.
* **annotation on method without args** - generate function using method with empty list of arguments and non-void return-value.
* **annotation on class** - generate functions using 2 previous strategies (for all fields and simple methods).

Original class `Company`:
```java
public class Company {
    @JFunction private int id;
    
    public int getId() { return id; }
    public void setId(final int id) { this.id = id; }
}
```
Generated class `CompanyFunctions`:
```java
public final class CompanyFunctions {
    public static final Function<Company, Integer> ID = new Function<Company, Integer>() {
        public Integer apply(final Company input) {
            return (input != null) ? input.getId() : null;
        }
    };
}
```


### @JMessage
The **@JMessage** annotation does not generate any additional code, instead of this it prints information in logs during project compiling.
It could be useful to make some really meaningful notes for you or your team, instead of using TODOs in comments.

Available parameters:

* **value** - List of notes, that will be logged.
* **type** - Logging level (default value is `Diagnostic.Kind.MANDATORY_WARNING`).
* **details** - Add information about annotated element with note message (default value is `false`).
* **after** - Show message only after the given date. It could be useful to specify deadline. Supported formats:
    * yyyy-MM-dd
    * yyyy/MM/dd
    * dd-MM-yyyy
    * dd/MM/yyyy
* **before** - Show message only before the given date. It could be useful in some cases. Supported formats are them same as in previous parameter.

Example:
```java
@JMessage({
    "Do not forget to remove this class in the next release",
    "MouseListener interface will be used instead of it"
})
public abstract class AbstractMouseListener implements MouseListener {
    // Some piece of code.
}
```

Part of compilation output:
```
[INFO] --- maven-processor-plugin:2.2.4:process (process) @ jackdaw-sample ---
[WARNING] diagnostic: warning: Do not forget to remove this class in the next release
[WARNING] diagnostic: warning: MouseListener interface will be used instead of it
```

This feature could be also useful in pair with CI servers (detect `[WARNING]` and make some additional actions).


### @JPredicate

The **@JPredicate** annotation generates `Predicate` implementation to use functional-way for programming.
You can specify different predicate interfaces for implementation generation (`JPredicateType`):

* **JAVA** - predicates from Java 8 (`java.util.function.Predicate`)
* **GUAVA** - Guava predicates (`com.google.common.base.Predicate`)
* **COMMONS** - predicates from Apache Commons Collections (`org.apache.commons.collections.Predicate`)

There are several ways to generate predicate or group of predicates.
It depends on the annotation location:

* **annotation on field** - generate predicate only for one specified field.
* **annotation on method without args** - generate predicate using method with empty list of arguments and non-void return-value.
* **annotation on class** - generate predicate using 2 previous strategies (for all fields and simple methods).

Original class `Company`:
```java
public class Company {
    @JPredicate(reverse = true) private boolean listed;
}
```
Generated class `CompanyPredicates`:
```java
public final class CompanyPredicates {
    public static final Predicate<Company> LISTED = new Predicate<Company>() {
        public boolean apply(final Company input) {
            return !input.isListed();
        }
    };
}
```
To generate reverse predicate, use parameter `reverse`.
By default, all generated predicates are null-safe. Use `nullable` parameter to generate not null-safe predicates.


### @JRepeatable

There are some situations where you want to apply the same annotation to a declaration or type use.
As of the Java SE 8 release, repeating annotations enable you to do this.
If you don't/can't use Java 8, then **@JRepeatable** helps you to resolve this problem using extra list-annotation.

Original annotation `@Role`:
```java
@JRepeatable
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Role {
}
```
Generated annotation `@RoleList`:
```java
@Retention(java.lang.annotation.RetentionPolicy.CLASS)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface RoleList {
    Role[] value();
}
```


### @JService

Java annotation processors and other systems use [ServiceLoader](http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html)
to register implementations of well-known types using META-INF metadata.
However, it is easy for a developer to forget to update or correctly specify the service descriptors.
Metadata will be generated for any class annotated with **@JService**.

Example:
```java
public interface BaseType {}

@JService(BaseType.class)
public class TypeA implements BaseType {}

@JService(BaseType.class)
public class TypeB implements BaseType {}
```
Generated file `META-INF/services/BaseType`:
```java
TypeA
TypeB
```


### @JSupplier

The **@JSupplier** annotation generates `Supplier` implementation to use functional-way for programming.
You can specify different supplier interfaces for implementation generation (`JSupplierType`):

* **GUAVA** - Guava suppliers (`com.google.common.base.Supplier`)
* **JAVA** - suppliers from Java 8 (`java.util.function.Supplier`)

There are several ways to generate supplier or group of suppliers.
It depends on the annotation location:

* **annotation on field** - generate supplier only for one specified field.
* **annotation on method without args** - generate supplier using method with empty list of arguments and non-void return-value.
* **annotation on class** - generate suppliers using 2 previous strategies (for all fields and simple methods).

Original class `Company`:
```java
public class Company {
    @JSupplier public Integer id;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
```
Generated class `CompanySuppliers`:
```java
public final class CompanySuppliers {
    public static Supplier<Integer> getId(final Company o) {
        return new Supplier<Integer>() {
            public Integer get() {
                return o.getId();
            }
        };
    }
}
```


## Extensions

**Jackdaw** is based on APT processor which executes different code generators.
Each code generator should implement interface [CodeGenerator](jackdaw-apt/src/main/java/com/github/vbauer/jackdaw/code/base/CodeGenerator.java)
(or extends from [BaseCodeGenerator](jackdaw-apt/src/main/java/com/github/vbauer/jackdaw/code/base/BaseCodeGenerator.java) / [GeneratedCodeGenerator](jackdaw-apt/src/main/java/com/github/vbauer/jackdaw/code/base/GeneratedCodeGenerator.java)).

Signature of CodeGenerator interface:
```java
public interface CodeGenerator {

    Class<? extends Annotation> getAnnotation();

    void generate(CodeGeneratorContext context) throws Exception;

    void onStart() throws Exception;

    void onFinish() throws Exception;

}
```

All generators are loaded by Java [ServiceLoader](http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html) mechanism,
so you can add your custom generator in few steps:

* Create new generator class. It must have no-argument constructor.
* Create file META-INF/services/com.github.vbauer.jackdaw.code.base.CodeGenerator and put canonical name of you class in this file.

That's all!


## Building from source

Jackdaw uses Maven for its build. 
To build the complete project with all modules (including example project), run

```bash
mvn clean install
```

from the root of the project directory.


## Might also like

* [jconditions](https://github.com/vbauer/jconditions) - Extra conditional annotations for JUnit.
* [caesar](https://github.com/vbauer/caesar) - Library that allows to create async beans from sync beans.
* [houdini](https://github.com/vbauer/houdini) - Type conversion system for Spring framework.
* [herald](https://github.com/vbauer/herald) - Log annotation for logging frameworks.
* [commons-vfs2-cifs](https://github.com/vbauer/commons-vfs2-cifs) - SMB/CIFS provider for Commons VFS.
* [avconv4java](https://github.com/vbauer/avconv4java) - Java interface to avconv tool.


## License

Copyright 2015 Vladislav Bauer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See [LICENSE](LICENSE) file for details.
