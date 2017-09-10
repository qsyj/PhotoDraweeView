package me.relex.photodraweeview;

/**
 * @author wangql
 * @email wangql@leleyuntech.com
 * @date 2017/9/8 17:20
 */
public class Config {
    private boolean mTapToRetryEnabled;
    public static final int STATE_DOWNLOAD_FAILURE = 1;
    public static final int STATE_DOWNLOAD_ING = 2;
    public static final int STATE_DOWNLOAD_SUCCESS = 0;
    public static final int STATE_DOWNLOAD_INITIAL = -1;
    /**
     * 加载状态 0加载成功
     */
    private int dowaloadState=STATE_DOWNLOAD_SUCCESS;
    public boolean isTapToRetryEnabled() {
        return mTapToRetryEnabled;
    }

    public void setTapToRetryEnabled(boolean tapToRetryEnabled) {
        mTapToRetryEnabled = tapToRetryEnabled;
    }

    public void setDowaloadFailure() {
        dowaloadState = STATE_DOWNLOAD_FAILURE;
    }
    public void setDowaloading() {
        dowaloadState = STATE_DOWNLOAD_ING;
    }
    public void setDowaloadSuccess() {
        dowaloadState = STATE_DOWNLOAD_SUCCESS;
    }

    public boolean isDownloadSuccess() {
        return dowaloadState == STATE_DOWNLOAD_SUCCESS;
    }

    public boolean isDownloadFailure() {
        return dowaloadState == STATE_DOWNLOAD_FAILURE;
    }
    public boolean isDownloadLoading() {
        return dowaloadState == STATE_DOWNLOAD_ING;
    }
}
