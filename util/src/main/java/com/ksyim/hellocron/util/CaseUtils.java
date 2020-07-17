package com.ksyim.hellocron.util;

public class CaseUtils {

    static public String pascalToKebab(String s) {
        return s.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }
}
