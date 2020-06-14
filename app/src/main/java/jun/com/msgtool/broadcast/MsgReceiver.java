package jun.com.msgtool.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
import jun.com.msgtool.app.Myapplication;
import jun.com.msgtool.bean.PhoneNumber;
import org.xutils.ex.DbException;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;
import static jun.com.msgtool.app.Myapplication.content_like;
import static jun.com.msgtool.app.Myapplication.db;

public class MsgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(x.app(), "收到短信", Toast.LENGTH_SHORT).show();
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object o : pdus) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) o);
            // 获取是谁发的
            String address = message.getOriginatingAddress();
            // 获取短信内容
            String messageBody = message.getMessageBody();
            Toast.makeText(x.app(), "来自:" + address + "内容:" + messageBody, Toast.LENGTH_SHORT).show();
            if (Myapplication.isPlay) {
                //转发
                if (messageBody.contains(content_like)) {
                    try {
                        sendMsg(messageBody);
                    } catch (DbException e) {
                        Toast.makeText(x.app(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void sendMsg(String content) throws DbException {
        List<PhoneNumber> phoneNum = db.selector(PhoneNumber.class).findAll();
        if (phoneNum != null && phoneNum.size() > 0) {
            for (PhoneNumber number : phoneNum) {
                sendMsg(number.getNumber(), content);
            }
        } else {
            Toast.makeText(x.app(), "手机号列表为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMsg(String phoneNum, String content) {
        if (!phoneNum.equals("")) {
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(content);
            for (int i = 0; i < strings.size(); i++) {
                manager.sendTextMessage(phoneNum, null, content, null, null);
            }
            Toast.makeText(x.app(), phoneNum + "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(x.app(), "手机号为空", Toast.LENGTH_SHORT).show();
        }
    }


}
