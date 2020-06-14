package jun.com.msgtool.app;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import static jun.com.msgtool.utils.DBHelp.daoConfig;

//记得在xml里面注册
public class Myapplication extends Application {
    public static boolean isPlay = false;
    public static String content_like = "不做为交易依据";
    public static DbManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        try {
            db = x.getDb(daoConfig);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

    }
}
