package com.example.demojwt.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class VsResponseUtil {
    public static ResponseEntity<RestData<?>> success(HttpStatus status,Object data){
        RestData<?> response=new RestData<>(data);
        return new ResponseEntity<>(response,status);
    }
    public static ResponseEntity<RestData<?>> success(Object data){
        return success(HttpStatus.OK,data);
    }

    public static ResponseEntity<RestData<?>> success(MultiValueMap<String,String>header,HttpStatus status, Object data){
        RestData<?> response=new RestData<>(data);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.addAll(header);
        return ResponseEntity.ok().headers(httpHeaders).body(response);
    }
        public static ResponseEntity<RestData<?>> error(HttpStatus status,Object message){
        return new ResponseEntity<>(RestData.error(status.value(),message),status);
    }

}
