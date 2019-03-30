package demo;

public class myExceptionDemo extends Exception {
    public enum ExceptionType{

    }
    //无参构造方法
    public myExceptionDemo(){
        super();
    }

    //有参的构造方法
    public myExceptionDemo(String message){
        super(message);
    }

    // 用指定的详细信息和原因构造一个新的异常
    public myExceptionDemo(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public myExceptionDemo(Throwable cause) {
        super(cause);
    }
}
