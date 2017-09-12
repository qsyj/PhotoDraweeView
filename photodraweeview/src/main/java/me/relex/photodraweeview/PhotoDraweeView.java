package me.relex.photodraweeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import static com.facebook.drawee.drawable.ScalingUtils.ScaleType.CENTER;

public class PhotoDraweeView extends SimpleDraweeView implements IAttacher {

    private Attacher mAttacher;

    private boolean mEnableDraweeMatrix = true;
    private DownLoadListener mDownLoadListener;
    private Config mConfig;

    public PhotoDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public PhotoDraweeView(Context context) {
        super(context);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        if (mConfig == null) {
            mConfig = new Config();
        }
        if (mAttacher == null || mAttacher.getDraweeView() == null) {
            mAttacher = new Attacher(this);
        }
    }
    public Attacher getAttacher() {
        return mAttacher;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
//        Log.e(getClass().getSimpleName(), "onTouchEvent b:" + b);
        return b;
    }

    @Override
    public void setController(DraweeController draweeController) {
        super.setController(draweeController);
    }

    @Override protected void onDraw(@NonNull Canvas canvas) {
        int saveCount = canvas.save();
        if (mEnableDraweeMatrix) {
            canvas.concat(mAttacher.getDrawMatrix());
        }
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override protected void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    @Override protected void onDetachedFromWindow() {
        mAttacher.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    @Override public float getMinimumScale() {
        return mAttacher.getMinimumScale();
    }

    @Override public float getMediumScale() {
        return mAttacher.getMediumScale();
    }

    @Override public float getMaximumScale() {
        return mAttacher.getMaximumScale();
    }

    @Override public void setMinimumScale(float minimumScale) {
        mAttacher.setMinimumScale(minimumScale);
    }

    @Override public void setMediumScale(float mediumScale) {
        mAttacher.setMediumScale(mediumScale);
    }

    @Override public void setMaximumScale(float maximumScale) {
        mAttacher.setMaximumScale(maximumScale);
    }

    @Override public float getScale() {
        return mAttacher.getScale();
    }

    @Override public void setScale(float scale) {
        mAttacher.setScale(scale);
    }

    @Override public void setScale(float scale, boolean animate) {
        mAttacher.setScale(scale, animate);
    }

    @Override public void setScale(float scale, float focalX, float focalY, boolean animate) {
        mAttacher.setScale(scale, focalX, focalY, animate);
    }

    @Override public void setOrientation(@Attacher.OrientationMode int orientation) {
        mAttacher.setOrientation(orientation);
    }

    @Override public void setZoomTransitionDuration(long duration) {
        mAttacher.setZoomTransitionDuration(duration);
    }

    @Override public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener listener) {
        mAttacher.setOnDoubleTapListener(listener);
    }

    @Override public void setOnScaleChangeListener(OnScaleChangeListener listener) {
        mAttacher.setOnScaleChangeListener(listener);
    }

    @Override public void setOnLongClickListener(OnLongClickListener listener) {
        mAttacher.setOnLongClickListener(listener);
    }

    @Override public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override public void setOnViewTapListener(OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override public OnPhotoTapListener getOnPhotoTapListener() {
        return mAttacher.getOnPhotoTapListener();
    }

    @Override public OnViewTapListener getOnViewTapListener() {
        return mAttacher.getOnViewTapListener();
    }

    @Override public void update(int imageInfoWidth, int imageInfoHeight) {
        mAttacher.update(imageInfoWidth, imageInfoHeight);
    }

    public boolean isEnableDraweeMatrix() {
        return mEnableDraweeMatrix;
    }

    public void setEnableDraweeMatrix(boolean enableDraweeMatrix) {
        mEnableDraweeMatrix = enableDraweeMatrix;
    }

    public Config getConfig() {
        return mConfig;
    }

    private void setDownLoadListener(DownLoadListener listener) {
        mDownLoadListener = listener;
    }

    public void setPhotoImageRequest(final ImageRequest imageRequest, final int failResourceId, final ScalingUtils.ScaleType failScaleType, int progressResourceId, ScalingUtils.ScaleType loadingScaleType, boolean tapToRetryEnabled, final DownLoadListener listener) {
        final Uri uri = imageRequest.getSourceUri();
        setDownLoadListener(listener);
        mEnableDraweeMatrix = false;
        GenericDraweeHierarchy hierarchy=getHierarchy();
        if (failResourceId >= 0) {
            hierarchy.setFailureImage(failResourceId, failScaleType);
            if (tapToRetryEnabled) {
                hierarchy.setRetryImage(failResourceId,failScaleType);
            }

        }
        if (progressResourceId >= 0) {
            if (loadingScaleType == null) {
                loadingScaleType = CENTER;
            }
            hierarchy.setProgressBarImage(progressResourceId,loadingScaleType);
        }

        mConfig.setTapToRetryEnabled(tapToRetryEnabled);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setCallerContext(getContext())
                .setTapToRetryEnabled(tapToRetryEnabled)
                .setImageRequest(imageRequest)
                .setOldController(getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {
                        setOnTouchListener(null);
                        super.onSubmit(id, callerContext);
                        mConfig.setDowaloading();
                    }

                    @Override public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        mConfig.setDowaloadFailure();
//                        getHierarchy().setActualImageScaleType(failScaleType);
//                        setImageURI(Uri.parse("res://sadf/"+failResourceId));
                        mEnableDraweeMatrix = false;
                        if (mDownLoadListener != null) {
                            mDownLoadListener.onFailure(PhotoDraweeView.this,id,throwable);
                        }
                    }

                    @Override public void onFinalImageSet(String id, ImageInfo imageInfo,
                                                          Animatable animatable) {//图片加载成功
                        setOnTouchListener(mAttacher);
                        super.onFinalImageSet(id, imageInfo, animatable);
                        mConfig.setDowaloadSuccess();
                        mEnableDraweeMatrix = true;
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                        if (mDownLoadListener != null) {
                            mDownLoadListener.onFinalImageSet(PhotoDraweeView.this,uri,id,imageInfo,animatable);
                        }
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        super.onIntermediateImageFailed(id, throwable);
                        mEnableDraweeMatrix = false;
                        if (mDownLoadListener != null) {
                            mDownLoadListener.onIntermediateImageFailed(PhotoDraweeView.this,id,throwable);
                        }
                    }

                    @Override public void onIntermediateImageSet(String id, ImageInfo imageInfo) {//图片设置渐进式
                        setOnTouchListener(mAttacher);
                        super.onIntermediateImageSet(id, imageInfo);
                        mEnableDraweeMatrix = true;
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                        if (mDownLoadListener != null) {
                            mDownLoadListener.onIntermediateImageSet(PhotoDraweeView.this,uri,id,imageInfo);
                        }
                    }
                })
                .build();

        setController(controller);
    }
    public void setPhotoUri(final Uri uri, final int failResourceId , final ScalingUtils.ScaleType failScaleType, int progressResourceId,
                            ScalingUtils.ScaleType loadingScaleType, boolean tapToRetryEnabled, final DownLoadListener listener) {
        ImageRequest imageRequest= ImageRequestBuilder.newBuilderWithSource(uri).build();
        setPhotoImageRequest(imageRequest,failResourceId,failScaleType,progressResourceId,loadingScaleType,tapToRetryEnabled,listener);
    }
    public void setPhotoUri(String url,int failResourceId ,int progressResourceId,boolean tapToRetryEnabled,final DownLoadListener listener) {
        setPhotoUri(Uri.parse(url),failResourceId,null,progressResourceId,null,tapToRetryEnabled,listener);
    }
    public void setPhotoUri(String url,int failResourceId ,ScalingUtils.ScaleType failScaleType,int progressResourceId,ScalingUtils.ScaleType loadingScaleType,boolean tapToRetryEnabled,final DownLoadListener listener) {
        setPhotoUri(Uri.parse(url),failResourceId,failScaleType,progressResourceId,loadingScaleType,tapToRetryEnabled,listener);
    }
    public void setPhotoUri(Uri uri) {
        setPhotoUri(uri, getContext());
    }

    public void setPhotoUri(Uri uri, @Nullable Context context) {
        setPhotoUri(uri,-1,null,-1,null,false,null);
    }

    public boolean isNeedDrag(int dx , int dy) {
        if (mAttacher==null)
            return false;
        return mAttacher.isNeedDrag(dx,dy);
    }
    public boolean startOnTouch(MotionEvent event) {
        if (mAttacher==null)
            return false;
        mAttacher.refreshLastTouchY();
        return mAttacher.onTouch(this,event);
    }
    public boolean isDragging() {
        return mAttacher.isDragging();
    }

    public interface  DownLoadListener{

        void onFinalImageSet(PhotoDraweeView view, Uri uri, String id, ImageInfo imageInfo, Animatable animatable) ;
        void onIntermediateImageSet(PhotoDraweeView view, Uri uri, String id, ImageInfo imageInfo) ;

        void onFailure(PhotoDraweeView view, String id, Throwable throwable);
        void onIntermediateImageFailed(PhotoDraweeView view, String id, Throwable throwable);
    }
}
