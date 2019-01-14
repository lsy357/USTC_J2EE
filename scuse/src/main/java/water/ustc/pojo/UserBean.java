package water.ustc.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter
public class UserBean implements Serializable {
    private Integer userId;
    private String userName;
    private String userPassword;

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
