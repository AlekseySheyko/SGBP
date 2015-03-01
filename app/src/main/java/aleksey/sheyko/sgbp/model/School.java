package aleksey.sheyko.sgbp.model;

import com.orm.SugarRecord;

public class School extends SugarRecord<School> {
    int id;
    String name;

    public School() {
    }

    public School(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
