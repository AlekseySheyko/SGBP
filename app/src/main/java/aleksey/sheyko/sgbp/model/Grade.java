package aleksey.sheyko.sgbp.model;

import com.orm.SugarRecord;

public class Grade extends SugarRecord<Grade> {
    int schoolId;
    String name;

    public Grade() {
    }

    public Grade(int schoolId, String name) {
        this.schoolId = schoolId;
        this.name = name;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }
}
