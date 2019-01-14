package sc.ustc.interceptor;

public interface Interceptor {

    boolean preAction();

    void afterAction() throws Exception;

    default void afterAction(String action, String result) throws Exception {
    }
}
