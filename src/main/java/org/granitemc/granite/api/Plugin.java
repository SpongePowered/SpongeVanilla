package org.granitemc.granite.api;

public @interface Plugin {
    public String name();

    public String id();

    public String version();

    public Class<?> setupClass() default DummySetupClass.class;

}
