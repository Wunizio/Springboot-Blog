package lu.cnfpc.blog.exception;

public class BlogUserNotFoundException extends RuntimeException {
    public BlogUserNotFoundException(String message){
        super(message);
    }
}
