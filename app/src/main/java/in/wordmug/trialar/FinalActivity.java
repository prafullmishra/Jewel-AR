package in.wordmug.trialar;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

import java.util.ArrayList;
import java.util.List;

public class FinalActivity extends AppCompatActivity {

    CameraView cameraView;
    Overlay overlay;

    boolean isBusy;
    FirebaseVisionFaceDetector detector;
    FirebaseVisionFaceDetectorOptions options;

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        cameraView  = findViewById(R.id.camera);
        overlay     = findViewById(R.id.overlay);

        cameraView.setFacing(Facing.BACK);
        cameraView.setCropOutput(true);
        cameraView.start();

        options = new FirebaseVisionFaceDetectorOptions.Builder()
                .enableTracking()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                //.setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();

        detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                if(!isBusy && frame.getData()!=null && frame.getData().length>0)
                {
                    isBusy = true;
                    FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                            .setRotation(getRotation(frame.getRotation()))
                            .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                            .setHeight(frame.getSize().getHeight())
                            .setWidth(frame.getSize().getWidth())
                            .build();

                    Log.i("FRAME SIZE", frame.getSize().getWidth()+","+frame.getSize().getHeight());
                    Log.i("VIEW SIZE", cameraView.getWidth()+","+cameraView.getHeight());
                    Log.i("OVERLAY SIZE", overlay.getWidth()+","+overlay.getHeight());
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
                                                        //List<FirebaseVisionPoint> points = face.getContour(FirebaseVisionFaceLandmark.LEFT_EYE).getPoints();
                                                        FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                                        FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                                                        FirebaseVisionFaceLandmark nose = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
                                                        if (leftEar != null && rightEar!=null) {
                                                            List<FirebaseVisionPoint> points = new ArrayList<>();
                                                            points.add(leftEar.getPosition());
                                                            points.add(rightEar.getPosition());
                                                            points.add(nose.getPosition());
                                                            overlay.setPoints(points);
                                                        }
                                                        //overlay.setPoints(points);
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

    private int getRotation(int val)
    {
        int result;
        switch (val) {
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
        return result;
    }

    public void toggleCam(View view) {
        cameraView.toggleFacing();
    }
}
