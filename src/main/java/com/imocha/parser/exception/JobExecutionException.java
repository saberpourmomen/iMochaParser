package com.imocha.parser.exception;

public class JobExecutionException extends RuntimeException{
    public JobExecutionException(String message,Throwable e){
        super(message,e);
    }
}
