package com.bearstouch.android.core;

/**
 * Created by heldervasc on 19/10/14.
 */
public class Result {

   public  enum ResultCode{
       OK,
       SQL_ERROR,
       PERMISSION_DENIED,
       IO_ERROR
   }

   public static ResultCode sqlError(){ return ResultCode.SQL_ERROR; }

   private ResultCode mCode;
   private String mErrorMsg;

   public Result(ResultCode code, String errorMsg){
       mCode=code;
       mErrorMsg=errorMsg;
   }

   public static Result ok(){
       return new Result(ResultCode.OK,"");
   }
    public ResultCode getCode() {
        return mCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public boolean isSuccess(){
        return mCode==ResultCode.OK;
    }

    @Override
    public String toString() {
        return mCode+" - "+mErrorMsg;
    }
}
