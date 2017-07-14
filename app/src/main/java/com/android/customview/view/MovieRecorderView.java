package com.android.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.customview.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by litonghui on 2017/7/13.
 */

public class MovieRecorderView extends LinearLayout implements MediaRecorder.OnErrorListener {

    private SurfaceView mSurfaceView;
    private ProgressBar mProgressBar;
    private SurfaceHolder mSurfaceHolder;//Surface 抽象接口
    private Camera mCamera;
    private int mWidth;
    private int mHeight;
    private int mTimeCount;
    private boolean isOpenCamera;
    private int mRecodeMaxTime;
    private File mRecordFile = null;

    private MediaRecorder mMediaRecorder;
    private Timer mTimer;
    private OnRecordFinishListener mOnRecordFinishListener;


    public MovieRecorderView(Context context) {
        this(context,null);
    }

    public MovieRecorderView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MovieRecorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MovieRecorderView, defStyleAttr, 0);
        mWidth = a.getInteger(R.styleable.MovieRecorderView_m_width, 320);
        mHeight = a.getInteger(R.styleable.MovieRecorderView_m_height, 240);
        isOpenCamera = a.getBoolean(R.styleable.MovieRecorderView_is_open_camera, true);
        mRecodeMaxTime = a.getInteger(R.styleable.MovieRecorderView_record_max_time, 10);

        LayoutInflater.from(context).inflate(R.layout.movie_recoder_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(mRecodeMaxTime);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        //noinspection deprecation
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        a.recycle();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            initCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera)
                return;
            freeCameraResource();
        }
    }

    private void initCamera() {
        if (null != mCamera)
            freeCameraResource();
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null) {
            return;
        }
        setCameraParams();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.set("orientation", "portrait");
            mCamera.setParameters(params);
        }
    }

    private void freeCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }

    }

    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + "lth/video/");//录制视频的保存地址
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mRecordFile = File.createTempFile("recording", ".mp4", vecordDir);// mp4格式的录制的视频文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecord() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        mMediaRecorder.setVideoSize(mWidth, mHeight);
        mMediaRecorder.setVideoEncodingBitRate(1024 * 1024 * 100);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }


    public void record(OnRecordFinishListener listener) {
        this.mOnRecordFinishListener = listener;
        createRecordDir();
        if (!isOpenCamera)
            initCamera();
        initRecord();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimeCount++;
                mProgressBar.setProgress(mTimeCount);
                if (mTimeCount == mRecodeMaxTime) {
                    stop();
                    if (mOnRecordFinishListener != null)
                        mOnRecordFinishListener.onRecordFinish();
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    private void stopRecord() {
        mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            mMediaRecorder.stop();
        }
    }

    private void releaseRecord() {
        if (mMediaRecorder != null)
            mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public int getTimeCount() {
        return mTimeCount;
    }

    //返回录制的视频文件
    public File getmVecordFile() {
        return mRecordFile;
    }
    /**
     * 录制完成回调接口
     */
    public interface OnRecordFinishListener {
        public void onRecordFinish();
    }

}
