package com.caco3.sessionlifespan.viainterface;

import java.util.Objects;

public class FooBarImpl implements FooBar {
    private String baz;

    public FooBarImpl() {
    }

    public FooBarImpl(String name) {
        this.baz = name;
    }

    public void setBaz(String baz) {
        this.baz = baz;
    }

    public String getBaz() {
        return baz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FooBarImpl that = (FooBarImpl) o;
        return Objects.equals(baz, that.baz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baz);
    }

    @Override
    public String toString() {
        return "FooBarInterfaceImpl{" +
                "name='" + baz + '\'' +
                '}';
    }
}
