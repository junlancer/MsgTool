package jun.com.msgtool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import jun.com.msgtool.R;
import jun.com.msgtool.app.Myapplication;
import jun.com.msgtool.bean.PhoneNumber;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static jun.com.msgtool.app.Myapplication.content_like;
import static jun.com.msgtool.app.Myapplication.db;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button st;
    private Button add;
    private TextView status;
    private EditText editText;
    private ArrayAdapter<PhoneNumber> adapter;
    private List<PhoneNumber> phoneNum = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            bindView();
        } catch (DbException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindView() throws DbException {
        listView = findViewById(R.id.list_view_item);
        st = findViewById(R.id.st);
        add = findViewById(R.id.add);
        status = findViewById(R.id.status);
        editText = findViewById(R.id.editText);
        // 给数据datas
        getNewAdapter();
        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始并发执行通知发短信
                Myapplication.isPlay = !Myapplication.isPlay;
                if (Myapplication.isPlay)
                    status.setText("正在运行,请不要退出和息屏");
                else status.setText("请设置永久亮屏,启动后不要退出和锁屏");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加手机号
                List<PhoneNumber> numbers = new ArrayList<>();
                PhoneNumber number = new PhoneNumber();
                //提取后11位
                String phoneText = editText.getText().toString();
                String teacherName = phoneText.substring(0, phoneText.length() - 11);
                String phoneNumText = phoneText.substring(phoneText.length() - 11);
                //添加的时候注意用前缀来区分老师:  宋杰12345678901
                if (phoneNumText.equals("") || !validateMobilePhone(phoneNumText)) {
                    if (phoneNumText.contains("set-"))
                        content_like = phoneNumText.replace("set", "");
                    else Toast.makeText(MainActivity.this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
                } else {
                    number.setNumber(editText.getText().toString());
                    number.setTeacher(teacherName);
                    numbers.add(number);
                    try {
                        db.save(numbers);
                        getNewAdapter();
                    } catch (DbException e) {
                        Toast.makeText(MainActivity.this, "存储报错", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "长按删除当前手机号", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PhoneNumber number = (PhoneNumber) adapterView.getItemAtPosition(i);//获取选择项的值
                List<PhoneNumber> delList = new ArrayList<>();
                delList.add(number);
                try {
                    db.delete(delList);
                    Toast.makeText(MainActivity.this, "删除" + number.getNumber(), Toast.LENGTH_SHORT).show();
                    getNewAdapter();

                } catch (DbException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    //刷新列表
    private void getNewAdapter() throws DbException {
        phoneNum = db.selector(PhoneNumber.class).findAll();
        if (phoneNum == null)
            phoneNum = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, phoneNum);
        listView.setAdapter(adapter);
        add.setText("ADD(" + phoneNum.size() + ")");
    }

    //校验手机号
    private static boolean validateMobilePhone(String in) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(in).matches();
    }


    //动态权限申请
}
