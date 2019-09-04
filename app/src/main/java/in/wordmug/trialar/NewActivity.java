package in.wordmug.trialar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;

import java.io.IOException;
import java.util.List;

public class NewActivity extends AppCompatActivity {

    CameraView cameraView;
    FrameLayout frameLayout;
    ImageView lear,rear,eye,neck;
    GraphicOverlay overlay;
    Canvas canvas;

    FirebaseVisionFaceDetectorOptions options;
    FirebaseVisionFaceDetector detector;
    boolean isBusy = false;

    float getCorrect(float px)
    {
        return px;
        //return px/((float) getResources().getDisplayMetrics().densityDpi/DisplayMetrics.DENSITY_DEFAULT);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        cameraView  = findViewById(R.id.cameraView);
        frameLayout = findViewById(R.id.container);
        lear        = findViewById(R.id.lear);
        rear        = findViewById(R.id.rear);
        eye         = findViewById(R.id.eye);
        neck        = findViewById(R.id.neck);
        overlay     = findViewById(R.id.overlay);

        cameraView.setFacing(Facing.BACK);
        cameraView.setCropOutput(true);
        cameraView.start();

        float dip = 30f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        options = new FirebaseVisionFaceDetectorOptions.Builder()
                .enableTracking()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();

        detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);


        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull final Frame frame) {
                if(!isBusy)
                {
                    int result;
                    switch (frame.getRotation()) {
                        case 0:
                            result = FirebaseVisionImageMetadata.ROTATION_0;
                            break;
                        case 90:
                            result = FirebaseVisionImageMetadata.ROTATION_90;
                            break;
                        case 180:
                            result = FirebaseVisionImageMetadata.ROTATION_180;
                            break;
                        case 270:
                            result = FirebaseVisionImageMetadata.ROTATION_270;
                            break;
                        default:
                            result = FirebaseVisionImageMetadata.ROTATION_0;
                    }

                    //Log.i("frame rotation",frame.getRotation()+" // "+frame.getFormat());
                    isBusy = true;
                    FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                            .setRotation(result)
                            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                            .setHeight(frame.getSize().getHeight())
                            .setWidth(frame.getSize().getWidth())
                            .build();

                    //Log.i("info",frameLayout.getWidth()+"x"+frameLayout.getHeight()+"   "+frame.getSize().getWidth()+"x"+frame.getSize().getHeight());


                    FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(frame.getData(), metadata);

                    Task<List<FirebaseVisionFace>> resultx =
                            detector.detectInImage(image)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<List<FirebaseVisionFace>>() {
                                                @Override
                                                public void onSuccess(List<FirebaseVisionFace> faces) {
                                                    // Task completed successfully
                                                    //Log.i("faces",faces.size()+"");
                                                    if(faces.size()>0)
                                                    {
                                                        FirebaseVisionFace face = faces.get(0);

                                                        FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                                        if (leftEar != null) {
                                                            FirebaseVisionPoint learPos = leftEar.getPosition();
                                                            lear.setY(learPos.getY());
                                                            lear.setX(learPos.getX());
                                                        }

                                                        FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                                                        if (rightEar != null) {
                                                            FirebaseVisionPoint rearPos = rightEar.getPosition();
                                                            rear.setY(rearPos.getY());
                                                            rear.setX(rearPos.getX());
                                                        }

                                                        //FirebaseVisionFaceLandmark cheek = face.getLandmark(FirebaseVisionFaceLandmark.)

                                                        List<FirebaseVisionPoint> facePoints = face.getContour(FirebaseVisionFaceContour.FACE).getPoints();
                                                        if(facePoints.size()>0)
                                                        {
                                                            neck.setX(facePoints.get(28).getX());
                                                            neck.setY(facePoints.get(18).getY()+40);
                                                        }


                                                    }
                                                }
                                            })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                    Log.i("faces","FAILED");
                                                    e.printStackTrace();
                                                }
                                            });
                    isBusy = false;
                }
            }
        });
    }

    public void switchCam(View view) {
        if(cameraView.getFacing() == Facing.BACK)
        {
            cameraView.setFacing(Facing.FRONT);
        }
        else
        {
            cameraView.setFacing(Facing.BACK);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.stop();
        try {
            detector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
