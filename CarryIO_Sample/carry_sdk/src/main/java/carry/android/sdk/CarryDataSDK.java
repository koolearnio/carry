package carry.android.sdk;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import carry.android.sdk.view.ViewFetcher;

/**
 * Created by leixun on 17/5/2.
 */
public class CarryDataSDK {
    private static final String TAG = "CarryDataSDK";
    private static CarryDataSDK sInstance = null;
    static String sPackageName;
    static String sProjectId;
    private static final Object sInstanceLock = new Object();
    private CarryConfig kdConfig;
    private ViewFetcher viewFetcher;
    private Application application;

    public static String getVersion() {
        return "1.0.0";
    }

    private CarryDataSDK() {
    }


    public CarryDataSDK(CarryConfig kdConfig) {

    }

    public CarryDataSDK(Application application, String token, double sampling) {

    }

    public void startWithConfiguration(Application application, CarryConfig kdConfig){
        this.application = application;
        this.kdConfig = kdConfig;
    }

    public static CarryDataSDK getInstance() {
        if(sInstance == null){
            synchronized(CarryDataSDK.class) {
                if (null == sInstance) {
                    Log.i("CarryDataSDK", "CarryDataSDK 还未初始化");
                    return new CarryDataSDK();
                }
            }
        }
        return sInstance;
    }

    public ViewFetcher getViewFecther(){
        if(viewFetcher == null){
            viewFetcher = new ViewFetcher(application);
        }
        return viewFetcher;
    }


    public CarryDataSDK setPageName(Activity activity, String name) {
        return this;
    }

}
