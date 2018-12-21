package sc.ustc.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class IdBean {
    private String name;
    private String column;

    public IdBean() {}

    public IdBean(String idName, String idColumn) {
        this.name = idName;
        this.column = idColumn;
    }
}
