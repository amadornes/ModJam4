package com.amadornes.tbircme.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {
    
    public static String capitalize(String string) {
    
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
    
    public static String convertPacketToString(String packet) {
    
        String s = "";
        
        StringTokenizer st = new StringTokenizer(packet, ".");
        while (st.hasMoreTokens()) {
            s += capitalize(st.nextToken());
        }
        
        return s;
    }
    
    public static String join(int from, int to, String... strings) {
    
        String[] strs = Arrays.copyOfRange(strings, from, to);
        String str = "";
        for (String s : strs)
            str += s + " ";
        return str.trim();
    }
    
    public static String join(String... strings) {
    
        String str = "";
        for (String s : strings)
            str += s + " ";
        return str.trim();
    }
    
    public static String[] exceptionToString(Exception ex) {
    
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ex.printStackTrace(ps);
        ps.flush();
        ps.close();
        byte[] bytes = baos.toByteArray();
        try {
            baos.close();
        } catch (Exception e) {
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
        List<String> data = new ArrayList<String>();
        String l = null;
        try {
            while ((l = br.readLine()) != null) {
                data.add(l);
            }
        } catch (Exception e) {
        }
        try {
            br.close();
        } catch (Exception e) {
        }
        try {
            bais.close();
        } catch (Exception e) {
        }
        String[] str = data.toArray(new String[data.size()]);
        data.clear();
        return str;
    }
    
}
