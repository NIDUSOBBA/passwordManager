package org.example.exception;


public class ServiceExceptionHandler {

    public boolean validSplitResponse(String[] split, int length, String message){
        if (split.length != length){
            System.out.println(message);
            return true;
        }else {
            return false;
        }
    }
}
