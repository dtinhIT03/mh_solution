package org.example.mhcommon.core.extension;

@FunctionalInterface
public interface SupplierThrowable<T> {
    T get() throws Exception;
}
