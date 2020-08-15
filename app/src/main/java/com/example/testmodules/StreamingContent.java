package com.example.testmodules;

import java.io.IOException;
import java.io.OutputStream;

public interface StreamingContent {
    void writeTo(OutputStream var1) throws IOException;
}