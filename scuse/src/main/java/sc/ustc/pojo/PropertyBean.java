package sc.ustc.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PropertyBean {
    private String name;
    private String column;
    private String type;
    private String lazy;

    public PropertyBean() {}

    public PropertyBean(String propertyName, String propertyColumn, String propertyType, String propertyLazy) {
        this.name = propertyName;
        this.column = propertyColumn;
        this.type = propertyType;
        this.lazy = propertyLazy;
    }

}
