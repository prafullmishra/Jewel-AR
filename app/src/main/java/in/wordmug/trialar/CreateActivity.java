package in.wordmug.trialar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;

public class CreateActivity extends AppCompatActivity {

    CameraView cameraView;
    ImageView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        cameraView = findViewById(R.id.preview);
        result      = findViewById(R.id.result);
        cameraView.setFacing(Facing.BACK);
        cameraView.setCropOutput(true);
        cameraView.start();

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                CameraUtils.decodeBitmap(jpeg, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(final Bitmap bitmap) {
                        int [] allpixels = new int [bitmap.getHeight() * bitmap.getWidth()];
                        bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

                        for(int i=0;i<allpixels.length;i++)
                        {
                            if(allpixels[i] == Color.GREEN)
                            {
                                allpixels[i] = Color.TRANSPARENT;
                            }
                        }
                        bitmap.setPixels(allpixels,0,bitmap.getWidth(),0, 0, bitmap.getWidth(),bitmap.getHeight());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.setImageBitmap(bitmap);
                                bitmap.recycle();
                                result.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraView.stop();
    }

    public void go(View view) {
        cameraView.capturePicture();
    }
}
