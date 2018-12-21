package sc.ustc.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class EntityMapper {

    private String className;
    private String table;
    private IdBean id;
    private List<PropertyBean> properties;

}
