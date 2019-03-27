package io.nuls.plugin.util;

import io.nuls.sdk.core.utils.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String getPropery(String name) {
        return System.getProperty(name);
    }

    public static byte[] readBytes(Path path){
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Object[] parseArgs(String argStr){
        if(StringUtils.isBlank(argStr)){
            return null;
        }
        String[] splits = argStr.split(",");
        if(splits.length > 0){
            List<Object> args = new ArrayList<>();
            for(String str : splits){
                int index = str.indexOf(" ");
                String type = str.substring(0,index);
                String value = str.substring(3);
                /*String[] arr = str.split(" ");
                String type = arr[0];
                String value = arr[1];*/
                System.out.println("type:"+type +"--> value:"+value);
                args.add(evaluateArgType(type,value));
            }
            return args.toArray();
        }
        return null;
    }
    private static Object evaluateArgType(String type,String value){
        switch (type){
            case "-I":
                return Integer.parseInt(value);
            case "-L":
                return Long.valueOf(value);
            case "-S":
                return Short.valueOf(value);
            case "-C":
                return Character.valueOf(value.charAt(0));
            case "-Z":
                return Boolean.valueOf(value);
            case "-B":
                return Byte.valueOf(value);
            case "-D":
                return Double.valueOf(value);
            case "-F":
                return Float.valueOf(value);
            case "-T":
                return value;
            default: return value;
        }
    }
}
