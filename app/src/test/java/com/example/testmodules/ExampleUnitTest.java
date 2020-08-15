package com.example.testmodules;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static String SOURCE_FILE = "/Users/zhangshexin/Desktop/123.pkg";
    private static String TARGET_FILE = "/Users/zhangshexin/Desktop/hehe.zip";
    @Test
    public void addition_isCorrect() {
        try {
            ZipUtil.zip_stored(TARGET_FILE,new File(SOURCE_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}