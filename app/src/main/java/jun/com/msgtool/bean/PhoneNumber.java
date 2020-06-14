package jun.com.msgtool.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "PhoneNumber")
public class PhoneNumber {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "Number")
    private String Number;

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

    @Override
    public String toString() {
        return "PhoneNumber: " + Number;
    }
}
