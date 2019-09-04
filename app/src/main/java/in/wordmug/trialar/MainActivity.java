package in.wordmug.trialar;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsic;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileOutputStream;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {

//    private Camera mCamera;
//    private CameraPreview mPreview;
//    private FaceDetector detector;
//    private Camera.PreviewCallback callback;
//    private ImageView small;

    boolean isBusy = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        small   = findViewById(R.id.small);
//        detector = new FaceDetector.Builder(this).setTrackingEnabled(false).setLandmarkType(FaceDetector.NO_LANDMARKS).setMode(FaceDetector.ACCURATE_MODE).build();
//
//        callback = new Camera.PreviewCallback() {
//            @Override
//            public void onPreviewFrame(byte[] data, Camera camera) {
//                Log.i("data is",data.length+"");
//
//               if(!isBusy)
//               {
//                   isBusy = true;
//                   Bitmap bitmap = Bitmap.createBitmap(mPreview.getWidth(),mPreview.getHeight(), Bitmap.Config.ARGB_8888);
//                   Allocation bmdata = render(mPreview.getWidth(), mPreview.getHeight(), data);
//                   bmdata.copyTo(bitmap);
//
//                   if(bitmap!=null)
//                   {
//                       getSupportActionBar().setSubtitle("empty not");
//                       Log.i("evaluating","now");
////                       Frame frame = new Frame.Builder().setBitmap(bitmap).build();
////                       SparseArray<Face> faces = detector.detect(frame);
////                       Log.i("FACEs",faces.size()+"");
//                       //getSupportActionBar().setTitle("faces - "+faces.size());
//                        //small.setImageBitmap(bitmap);
//                       bitmap.recycle();
//                   }
//                   else
//                   {
//                       getSupportActionBar().setSubtitle("null bitmap");
//                   }
//                   //isBusy = false;
//               }
//            }
//        };
//
//        mCamera = getCameraInstance();
//        mPreview = new CameraPreview(this, mCamera, callback);
//
//
//        FrameLayout cameraView = (FrameLayout) findViewById(R.id.camera_preview);
//        cameraView.addView(mPreview);
//
//
//        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {
//
//            @Override
//            public void onPictureTaken(byte[] data, Camera camera) {
////                Log.i("CAPTURED","NOW");
////                mCamera.startPreview();
////                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
////
////                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
////                SparseArray<Face> faces = detector.detect(frame);
////                Log.i("FACEs",faces.size()+"");
////
////                bitmap.recycle();
//
//            }
//        };
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(detector.isOperational())
//                {
//                    try
//                    {
//                        //mCamera.startPreview();
//                        //mCamera.takePicture(null,null,mPicture);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//                handler.postDelayed(this,1000);
//            }
//        },3000);

    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        detector.release();
//    }
//
//    private Allocation render(int width, int height, byte[] nv21)
//    {
//        RenderScript rs = RenderScript.create(this);
//        ScriptIntrinsicYuvToRGB yuvToRGB = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
//
//        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
//        Allocation in = Allocation.createTyped(rs,yuvType.create(), Allocation.USAGE_SCRIPT);
//
//        Type.Builder rgbType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
//        Allocation out = Allocation.createTyped(rs,rgbType.create(), Allocation.USAGE_SCRIPT);
//        in.copyFrom(nv21);
//        yuvToRGB.setInput(in);
//        yuvToRGB.forEach(out);
//        return out;
//    }
//
//    /** Check if this device has a camera */
//    private boolean checkCameraHardware(Context context) {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//            // this device has a camera
//            return true;
//        } else {
//            // no camera on this device
//            return false;
//        }
//    }
//
//    /** A safe way to get an instance of the Camera object. */
//    public static Camera getCameraInstance(){
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        }
//        catch (Exception e){
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }

}
