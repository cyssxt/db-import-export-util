package com.cyssxt.excel.core;

import java.io.IOException;

public interface Decorter<T> {
    T get() throws IOException;
}
