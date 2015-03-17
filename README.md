
# Jackdaw [![Build Status](https://travis-ci.org/vbauer/jackdaw.svg)](https://travis-ci.org/vbauer/jackdaw) [![Maven](https://img.shields.io/github/tag/vbauer/jackdaw.svg?label=maven)](https://jitpack.io/#vbauer/jackdaw)

<img align="right" style="margin-left: 15px" width="300" height="243" src="jackdaw-misc/jackdaw.png" />

**Jackdaw** is a Java [Annotation Processor](http://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/Processor.html) which
allows to simplify development without using reflections and prevents writing of tedious code.

Jackdaw was inspired by [Lombok](http://projectlombok.org) project, but in comparison with Lombok:

* it does not need to have an extra plugin in IDE
* it does not modify an existing source code


## Features

**Jackdaw** support the following compile time annotations:

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
</ul>


## Setup

**Jackdaw** uses [JitPack.io](https://jitpack.io) for distribution, so
you need to configure JitPack's Maven repository to fetch artifacts (dependencies).

Also, it is necessary to make dependency on `jackdaw-core`.
This module contains compile time annotations which will be used to give a hints for APT.

### Maven

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependencies>
    <dependency>
        <groupId>com.github.vbauer</groupId>
        <artifactId>jackdaw-core</artifactId>
        <version>${jackdaw.version}</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    compile 'com.github.vbauer:jackdaw-core:1.0.0'
}
```


## Configuration

### Maven

You can use any other APT plugin for Maven instead of `maven-processor-plugin`.
It is just an example configuration.

```xml
<build>
    <plugins>

        <!-- Disable annotation processors during normal compilation -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven.compiler.plugin.version}</version>
            <configuration>
                <compilerArgument>-proc:none</compilerArgument>
            </configuration>
        </plugin>

        <plugin>
            <groupId>org.bsc.maven</groupId>
            <artifactId>maven-processor-plugin</artifactId>
            <version>${maven.processor.plugin.version}</version>
            <executions>
                <execution>
                    <id>process</id>
                    <goals>
                        <goal>process</goal>
                    </goals>
                    <phase>generate-sources</phase>
                    <configuration>
                        <encoding>${file.encoding}</encoding>
                        <processors>
                            <processor>com.github.vbauer.jackdaw.JackdawProcessor</processor>
                        </processors>
                        <optionMap>
                            <!--<addGeneratedAnnotation>false</addGeneratedAnnotation>-->
                            <!--<addGeneratedDate>true</addGeneratedDate>-->
                        </optionMap>
                    </configuration>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>com.github.vbauer</groupId>
                    <artifactId>jackdaw-apt</artifactId>
                    <version>${jackdaw.version}</version>
                </dependency>
            </dependencies>
        </plugin>

    </plugins>
</build>
```

### Gradle

**TODO:** Add section.


## Annotations

Some things that you need to know before exploring an examples:

* Code style in the below examples was changed to minimize text in README.
* Some annotations and imports were also removed to simplify understanding.
* All generated classes will have the same package as original classes.


### @JAdapter

`@JAdapter` allows to create class with empty method implementations using some interface or class.
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
@JBean generates some boilerplate code that is normally associated with simple POJOs (Plain Old Java Objects) and beans:

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
The `@Builder` annotation produces complex builder APIs for your classes.

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

`@Builder` lets you automatically produce the code required to have your class be instantiable with code such as:
```java
CompanyBuilder.create()
    .id(1)
    .name("John Smith")
    .descriptions(Collections.singleton("Good guy"))
    .build()
```


### @JClassDescriptor
Sometimes it is necessary to use reflection, so it will be useful to have some string constants.
`@JClassDescriptor` generates it for you easily.

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
To generated safe well-coded comparator, you have to write a lot of boilerplate code.
`@JComparator` annotation allows to simplify this situation.
It is also possible to generate reverse order comparator using parameter `reverse`.

Original class `Company`:
```java
public class Company {
    @JComparator
    private String name;
}
```
Generated class `CompanyComparators`:
```java
public final class CompanyComparators {
    public static final Comparator<Company> NAME = new Comparator<Company>() {
        public int compare(final Company o1, final Company o2) {
            final String v1 = o1 == null ? null : o1.getName();
            final String v2 = o2 == null ? null : o2.getName();
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
@JFactoryMethod allows to use pattern *Factory Method* for object instantiation.
To use this annotation it is necessary to have setters and default constructor in original class.

**Available parameters:**

* **method** - factory method name (default value is "create").
* **all** - use all fields of class in factory method (default value is `false`).
* **arguments** - use only specified fields in factory method (is is an empty array by default).

Original class `Company`:
```java
@JFactoryMethod(all = true)
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
Original class:
```java
public class Company {
    @JFunction private int id;
}
```
Generated class:
```java
public final class CompanyFunctions {
    public static final Function<Company, Integer> ID = new Function<Company, Integer>() {
        public Integer apply(final Company input) {
            return input.getId();
        }
    };
}
```


### @JMessage
**TODO:** Finish section.

### @JPredicate
Original class:
```java
public class Company {
    @JPredicate(reverse = true)
    private boolean listed;
}
```
Generated class:
```java
public final class CompanyPredicates {
    public static final Predicate<Company> LISTED = new Predicate<Company>() {
        public boolean apply(final Company input) {
            return !input.isListed();
        }
    };
}
```


### @JRepeatable
Original annotation `Role`:
```java
@JRepeatable
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Role {
}
```
Generated annotation `RoleList`:
```java
@Retention(java.lang.annotation.RetentionPolicy.CLASS)
@Target(java.lang.annotation.ElementType.TYPE)
public @interface RoleList {
    Role[] value();
}
```


## Building from source

MapStruct uses Maven for its build. To build the complete project run

```bash
mvn clean install
```

from the root of the project directory.


## Might also like

* [caesar](https://github.com/vbauer/caesar) - Library that allows to create async beans from sync beans.
* [houdini](https://github.com/vbauer/houdini) - Type conversion system for Spring framework.
* [herald](https://github.com/vbauer/herald) - Logging annotation for Spring framework.
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
