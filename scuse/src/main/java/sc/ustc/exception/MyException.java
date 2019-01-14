package sc.ustc.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 */
@Getter
@Setter
public class MyException extends Exception {

    private String message;

    public MyException(String message) {
        this.message = message;
    }
}
