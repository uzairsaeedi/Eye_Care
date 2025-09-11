package com.eyetest.eyecare.eyesighttest;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private static final int REQ_CAMERA = 101;

    private PreviewView previewView;
    private FrameLayout overlayContainer;
    private ImageView ivPreviewThumb, btnShutter, btnSave, btnUndo, ivSwitchCamera, ivCloseCam;
    private View flashOverlay;
    private RecyclerView rvStickers;
    private ImageView ivAllStickers;

    private ImageView ivCapturedFull;

    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;

    private final List<Integer> allStickers = new ArrayList<>();
    private final List<View> addedStickers = new ArrayList<>();

    private StickerAdapter stickerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        overlayContainer = findViewById(R.id.overlayContainer);
        ivPreviewThumb = findViewById(R.id.ivPreviewThumb);
        btnShutter = findViewById(R.id.btnShutter);
        btnSave = findViewById(R.id.btnSave);
        btnUndo = findViewById(R.id.btnUndo);
        rvStickers = findViewById(R.id.rvStickers);
        ivAllStickers = findViewById(R.id.ivAllStickers);
        ivSwitchCamera = findViewById(R.id.ivSwitchCamera);
        ivCloseCam = findViewById(R.id.ivCloseCam);
        flashOverlay = findViewById(R.id.flashOverlay);

        cameraExecutor = Executors.newSingleThreadExecutor();

        rvStickers.setVisibility(View.GONE);
        ivAllStickers.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnUndo.setVisibility(View.GONE);
        ivPreviewThumb.setVisibility(View.GONE);

        allStickers.addAll(Arrays.asList(
                R.mipmap.glasses1_foreground, R.mipmap.glasses2_foreground, R.mipmap.glasses3_foreground,
                R.mipmap.glasses4_foreground, R.mipmap.glasses5_foreground, R.mipmap.glasses6_foreground,
                R.mipmap.glasses7_foreground, R.mipmap.glasses8_foreground
        ));

        stickerAdapter = new StickerAdapter(allStickers, resId -> addStickerToOverlay(resId));
        rvStickers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStickers.setAdapter(stickerAdapter);

        ivAllStickers.setOnClickListener(v -> {
            boolean anyVisible = (rvStickers.getVisibility() == View.VISIBLE);
            if (anyVisible) {
                rvStickers.setVisibility(View.GONE);
            } else {
                rvStickers.setVisibility(View.VISIBLE);
            }
        });

        btnShutter.setOnClickListener(v -> {
            playShutterAnimation();
            takePhoto();
        });

        btnUndo.setOnClickListener(v -> {
            if (!addedStickers.isEmpty()) {
                View last = addedStickers.remove(addedStickers.size() - 1);
                overlayContainer.removeView(last);
                btnUndo.setVisibility(addedStickers.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(v -> saveEditedImage());

        ivSwitchCamera.setOnClickListener(v -> {
            lensFacing = (lensFacing == CameraSelector.LENS_FACING_BACK) ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
            startCamera();
        });

        ivCloseCam.setOnClickListener(v -> {
            if (ivCapturedFull != null) {
                resetToPreview();
            } else {
                finish();
            }
        });


        overlayContainer.setClipToPadding(false);
        overlayContainer.setClipChildren(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, REQ_CAMERA);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreviewAndCapture(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera provider error", e);
                Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreviewAndCapture(@NonNull ProcessCameraProvider cameraProvider) {
        try {
            cameraProvider.unbindAll();

            Preview preview = new Preview.Builder().build();
            imageCapture = new ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build();

            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build();

            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        } catch (Exception e) {
            Log.e(TAG, "bindPreviewAndCapture error", e);
        }
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override public void onCaptureSuccess(@NonNull ImageProxy image) {
                try {
                    Bitmap bmp = imageProxyToBitmap(image);
                    image.close();
                    if (bmp != null) {
                        runOnUiThread(() -> showCapturedImage(bmp));
                    } else {
                        runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Capture conversion failed", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "capture->bitmap error", e);
                    image.close();
                    runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Capture failed", Toast.LENGTH_SHORT).show());
                }
            }

            @Override public void onError(@NonNull ImageCaptureException exception) {
                Log.e(TAG, "ImageCapture error: " + exception.getMessage(), exception);
                runOnUiThread(() -> Toast.makeText(CameraActivity.this, "Capture error: " + exception.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private Bitmap imageProxyToBitmap(@NonNull ImageProxy image) {
        try {
            if (image.getFormat() == ImageFormat.JPEG) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }

            ImageProxy.PlaneProxy[] planes = image.getPlanes();
            ByteBuffer yBuffer = planes[0].getBuffer();
            ByteBuffer uBuffer = planes[1].getBuffer();
            ByteBuffer vBuffer = planes[2].getBuffer();

            int ySize = yBuffer.remaining();
            int uSize = uBuffer.remaining();
            int vSize = vBuffer.remaining();

            byte[] yBytes = new byte[ySize]; yBuffer.get(yBytes);
            byte[] uBytes = new byte[uSize]; uBuffer.get(uBytes);
            byte[] vBytes = new byte[vSize]; vBuffer.get(vBytes);

            byte[] nv21 = new byte[ySize + uSize + vSize];
            System.arraycopy(yBytes, 0, nv21, 0, ySize);

            int uvPos = ySize;
            int pixelStride = planes[1].getPixelStride();

            if (pixelStride == 1) {
                int rowStride = planes[1].getRowStride();
                for (int row = 0; row < image.getHeight() / 2; row++) {
                    int rowOffset = row * rowStride;
                    for (int col = 0; col < image.getWidth() / 2; col++) {
                        int uIndex = rowOffset + col * pixelStride;
                        int vIndex = rowOffset + col * pixelStride;
                        byte v = vBytes[vIndex];
                        byte u = uBytes[uIndex];
                        nv21[uvPos++] = v;
                        nv21[uvPos++] = u;
                    }
                }
            } else {
                int min = Math.min(vBytes.length, uBytes.length);
                for (int i = 0; i < min; i++) {
                    nv21[uvPos++] = vBytes[i];
                    nv21[uvPos++] = uBytes[i];
                }
            }

            YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean ok = yuvImage.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 90, out);
            if (!ok) Log.w(TAG, "YuvImage compressToJpeg returned false");
            byte[] jpegBytes = out.toByteArray();
            out.close();
            return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
        } catch (Exception e) {
            Log.e(TAG, "imageProxyToBitmap failed: " + e.getMessage(), e);
            return null;
        }
    }

    private void showCapturedImage(@NonNull Bitmap bmp) {
        try {
            previewView.setVisibility(View.GONE);

            overlayContainer.removeAllViews();
            overlayContainer.setBackgroundColor(Color.BLACK);

            ivCapturedFull = new ImageView(this);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            ivCapturedFull.setLayoutParams(lp);
            ivCapturedFull.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivCapturedFull.setImageBitmap(bmp);

            overlayContainer.addView(ivCapturedFull, 0);

            ivAllStickers.setVisibility(View.VISIBLE);
            rvStickers.setVisibility(View.GONE);
            ivSwitchCamera.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            btnUndo.setVisibility(addedStickers.isEmpty() ? View.GONE : View.VISIBLE);

            btnShutter.setVisibility(View.GONE);

            ivPreviewThumb.setImageBitmap(Bitmap.createScaledBitmap(bmp, 160, 160, true));
            ivPreviewThumb.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.e(TAG, "showCapturedImage error", e);
        }
    }

    private void playShutterAnimation() {
        btnShutter.animate().scaleX(0.85f).scaleY(0.85f).setDuration(100).withEndAction(() ->
                btnShutter.animate().scaleX(1f).scaleY(1f).setDuration(150).start()).start();

        flashOverlay.setAlpha(0f);
        flashOverlay.setVisibility(View.VISIBLE);
        flashOverlay.animate().alpha(1f).setDuration(80).withEndAction(() ->
                flashOverlay.animate().alpha(0f).setDuration(220).withEndAction(() ->
                        flashOverlay.setVisibility(View.GONE)).start()).start();
    }

    private void addStickerToOverlay(int stickerResId) {
        if (ivCapturedFull == null) {
            Toast.makeText(this, "Take a photo first to add stickers", Toast.LENGTH_SHORT).show();
            return;
        }

        for (View v : addedStickers) {
            overlayContainer.removeView(v);
        }
        addedStickers.clear();

        final StickerView sv = new StickerView(this);
        sv.setImageResource(stickerResId);

        sv.setBackgroundColor(Color.TRANSPARENT);

        try {
            if (sv instanceof ImageView) {
                ((ImageView) sv).setScaleType(ImageView.ScaleType.FIT_CENTER);
                ((ImageView) sv).setAdjustViewBounds(true);
            }
        } catch (Exception ignored) {}

        int overlayW = overlayContainer.getWidth();
        int overlayH = overlayContainer.getHeight();
        int size = Math.min(overlayW, overlayH) / 2;
        if (size <= 0) size = 200;

        int left = overlayW / 2 - size / 2;
        int top = overlayH / 2 - size / 2;

        int padding = dpToPx(8);
        left = Math.max(padding, Math.min(left, Math.max(padding, overlayW - size - padding)));
        top = Math.max(padding, Math.min(top, Math.max(padding, overlayH - size - padding)));

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        lp.leftMargin = left;
        lp.topMargin = top;

        overlayContainer.addView(sv, lp);

        sv.setScaleX(0.6f);
        sv.setScaleY(0.6f);
        sv.setAlpha(0f);
        sv.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(220)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        addedStickers.add(sv);
        btnUndo.setVisibility(View.VISIBLE);
    }


    private void saveEditedImage() {
        if (overlayContainer.getWidth() == 0 || overlayContainer.getHeight() == 0) {
            Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap result = Bitmap.createBitmap(overlayContainer.getWidth(), overlayContainer.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        overlayContainer.draw(canvas);

        String filename = "eyecare_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) values.put(MediaStore.Images.Media.IS_PENDING, 1);

        Uri uri = null;
        try {
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri == null) throw new Exception("Failed to create MediaStore entry");
            OutputStream out = getContentResolver().openOutputStream(uri);
            result.compress(Bitmap.CompressFormat.JPEG, 92, out);
            if (out != null) out.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                getContentResolver().update(uri, values, null, null);
            }
            Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();

            resetToPreview();
        } catch (Exception e) {
            Log.e(TAG, "saveEditedImage failed", e);
            Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            if (uri != null) getContentResolver().delete(uri, null, null);
        }
    }

    private void resetToPreview() {
        overlayContainer.removeAllViews();
        overlayContainer.setBackgroundColor(Color.TRANSPARENT);
        ivCapturedFull = null;
        addedStickers.clear();

        rvStickers.setVisibility(View.GONE);
        ivAllStickers.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnUndo.setVisibility(View.GONE);

        ivSwitchCamera.setVisibility(View.VISIBLE);
        btnShutter.setVisibility(View.VISIBLE);
        previewView.setVisibility(View.VISIBLE);
        startCamera();
    }

    // dp utility
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null && !cameraExecutor.isShutdown()) cameraExecutor.shutdown();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraExecutor != null && !cameraExecutor.isShutdown()) {
            cameraExecutor.shutdownNow();
            cameraExecutor = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraExecutor == null || cameraExecutor.isShutdown()) {
            cameraExecutor = Executors.newSingleThreadExecutor();
        }
        if (ivCapturedFull == null) {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
