package jun.com.msgtool.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "PhoneNumber")
public class PhoneNumber {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "Number")
    private String Number;
    @Column(name = "teacher")
    private String teacher;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return teacher + Number;
    }
}
