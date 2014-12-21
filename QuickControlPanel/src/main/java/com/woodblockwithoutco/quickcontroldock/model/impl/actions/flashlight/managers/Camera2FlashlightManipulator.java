package com.woodblockwithoutco.quickcontroldock.model.impl.actions.flashlight.managers;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.Toast;

import com.woodblockwithoutco.quickcontroldock.R;

import java.util.Arrays;
import java.util.List;

@TargetApi(VERSION_CODES.LOLLIPOP)
public class Camera2FlashlightManipulator implements FlashlightManipulator {

    private Context mContext;

    private SurfaceTexture mSurfaceTexture;
    private Handler mHandler;

    private boolean mPendingRequest = false;

    private CameraDevice mDevice;
    private CameraCaptureSession mSession;

    public Camera2FlashlightManipulator(Context context, TextureView surfaceView) {
        mPendingRequest = false;
        mHandler = surfaceView.getHandler();
        surfaceView.setSurfaceTextureListener(new SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurfaceTexture = surface;
                if(mPendingRequest) {
                    mPendingRequest = false;
                    turnFlashlightOn();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mSurfaceTexture = null;
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                mSurfaceTexture = surface;
                if(mPendingRequest) {
                    mPendingRequest = false;
                    turnFlashlightOn();
                }
            }
        });
        mContext = context;
    }

    @Override
    public void turnFlashlightOn() {
        if(mSurfaceTexture == null) {
            mPendingRequest = true;
            return;
        }

        try {
            CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIds = manager.getCameraIdList();
            for(String id : cameraIds) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);
                Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if(flashAvailable) {
                    manager.openCamera(id, new CameraStateCallback(), mHandler);
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Toast.makeText(mContext, R.string.camera_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void turnFlashlightOff() {
        if(mSession != null) mSession.close();
        if(mDevice != null) mDevice.close();
        mPendingRequest = false;
    }

    protected class CameraStateCallback extends StateCallback {

        @Override
        public void onOpened(CameraDevice camera) {
            mDevice = camera;
            try {
                Surface surface = new Surface(mSurfaceTexture);
                List<Surface> surfaces = Arrays.asList(surface);
                mDevice.createCaptureSession(surfaces, new CameraSessionCallback(), mHandler);
            } catch (CameraAccessException e) {
                Toast.makeText(mContext, R.string.camera_error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            mDevice = null;
            Toast.makeText(mContext, mContext.getString(R.string.camera_error_code) + error, Toast.LENGTH_LONG).show();
        }
    }

    protected class CameraSessionCallback extends CameraCaptureSession.StateCallback {

        @Override
        public void onConfigured(CameraCaptureSession session) {
            mSession = session;
            try {
                Builder builder = mDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                Surface surface = new Surface(mSurfaceTexture);
                builder.addTarget(surface);
                session.setRepeatingRequest(builder.build(), null, mHandler);
            } catch (CameraAccessException e) {
                session.close();
                Toast.makeText(mContext, R.string.camera_session_error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
            session.close();
            Toast.makeText(mContext, R.string.camera_session_error, Toast.LENGTH_LONG).show();
        }
    }
}
