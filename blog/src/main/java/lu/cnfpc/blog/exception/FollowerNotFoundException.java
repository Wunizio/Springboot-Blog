package lu.cnfpc.blog.exception;

public class FollowerNotFoundException extends RuntimeException{
    public FollowerNotFoundException(String message){
        super(message);
    }
}
