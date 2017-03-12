package cc.cuichen.gps_alarm;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private TextView locView;
    private EditText edit_long;
    private EditText edit_lati;
    private EditText edit_dis;
    private Button button_set, map_button;
    private double longitude, latitude;
    private double go_long, go_lati, go_dis;
    private boolean vibratored = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initLocation();
        locationClient.startLocation();
    }

    private void initView() {
        locView = (TextView) findViewById(R.id.locView);
        edit_lati = (EditText) findViewById(R.id.edit_lati);
        edit_long = (EditText) findViewById(R.id.edit_long);
        edit_dis = (EditText) findViewById(R.id.edit_dis);
        button_set = (Button) findViewById(R.id.button_set);
        map_button = (Button) findViewById(R.id.map_button);
        button_set.setOnClickListener(MainActivity.this);
        map_button.setOnClickListener(MainActivity.this);
    }

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                longitude = loc.getLongitude();
                latitude = loc.getLatitude();
                if (!vibratored && Distance(longitude, latitude, go_long, go_lati) <= go_dis) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(5000);
                    vibratored = true;
                }
                locView.setText("经度:" + longitude + "\n纬度:" + latitude);
            } else {
                locView.setText("定位失败");
            }
        }
    };

    public void set_alarm() {
        go_long = Double.parseDouble(edit_long.getText().toString());
        go_lati = Double.parseDouble(edit_lati.getText().toString());
        go_dis = Double.parseDouble(edit_dis.getText().toString());
        vibratored = false;
    }

    public static double Distance(double long1, double lat1, double long2, double lat2) {
        double a, b, R;
        R = 6378137;
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    public void joke() {
        Toast.makeText(MainActivity.this, "功能尚未实现，敬请期待！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_set :
                set_alarm();
                break;
            case R.id.map_button :
                joke();
        }
    }

}
