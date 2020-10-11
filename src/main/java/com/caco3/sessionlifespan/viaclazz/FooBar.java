package com.caco3.sessionlifespan.viaclazz;

import java.util.Objects;

class FooBar {
    private String baz;

    public FooBar() {
    }

    public FooBar(String baz) {
        this.baz = baz;
    }

    public String getBaz() {
        return baz;
    }

    public void setBaz(String baz) {
        this.baz = baz;
    }

    @Override
    public String toString() {
        return "FooBar{" +
                "name='" + baz + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FooBar fooBar = (FooBar) o;
        return Objects.equals(baz, fooBar.baz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baz);
    }
}
