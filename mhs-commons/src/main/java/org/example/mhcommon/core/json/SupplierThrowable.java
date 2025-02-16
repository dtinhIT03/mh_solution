package org.example.mhcommon.core.json;

@FunctionalInterface
public interface SupplierThrowable<T> {
    T get() throws Exception;
}
