package android.ye.zhbjj.permit;


public interface PermissionCallBack {
    void onGranted(String[] permissions, int[] grantResults);

    void onFailed(String[] permissions, int[] grantResults);

}
