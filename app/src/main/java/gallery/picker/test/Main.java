package gallery.picker.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import gallery.picker.ImagePicker;


/**
 * Created at 2016/4/11.
 *
 * @author YinLanShan
 */
public class Main extends Activity implements View.OnClickListener {
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(-1);
        for(int i = 1; i <= 9; i++)
            adapter.add(i);
        Spinner sp = (Spinner)findViewById(R.id.max);
        sp.setAdapter(adapter);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(sp);

            // Set popupWindow height to 500px
            int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    200, getResources().getDisplayMetrics());
            popupWindow.setHeight(height);
        }
        catch (Exception e) {
            // silently fail...
        }

        findViewById(R.id.pick).setOnClickListener(this);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ImagePicker.class);
        Spinner sp = (Spinner)findViewById(R.id.max);
        Integer max = (Integer) sp.getSelectedItem();
        intent.putExtra(ImagePicker.EXTRA_MAX, max);
        CheckBox cb = (CheckBox) findViewById(R.id.camera);
        intent.putExtra(ImagePicker.EXTRA_WITH_CAMERA, cb.isChecked());
        File output = new File(Environment.getExternalStorageDirectory(), "TEMP.jpg");
        intent.putExtra(ImagePicker.EXTRA_PHOTO_OUTPUT, output.getAbsolutePath());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK) {
            ArrayList<String> list = data.getStringArrayListExtra(ImagePicker.EXTRA_RESULT);
            mAdapter.clear();
            mAdapter.addAll(list);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File output = new File(Environment.getExternalStorageDirectory(), "TEMP.jpg");
        if (output.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(output.getPath(), options);
            Log.d("Test", String.format("Size: %d X %d",
                    options.outWidth, options.outHeight));
            output.delete();
        }
    }
}
