package sc.ustc.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JDBCConfigMapper {

    private String driver;
    private String url;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "JDBCConfigMapper{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
