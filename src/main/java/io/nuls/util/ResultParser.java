package io.nuls.util;

import io.nuls.sdk.core.model.Result;

import java.util.Map;

public class ResultParser {

    public static Map<String,Object> parseResult(Result result){
        if(result.isSuccess()){
            return (Map<String,Object>)result.getData();
        }
        return null;
    }
}
