package com.br.vigilant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.br.adapter.AdapterCategoriesList;
import com.br.utils.CacheObject;
import com.br.utils.LocationHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Berhell on 08/07/14.
 */
public class AddReportActivity extends Activity {

    private static Context context;
    private ParseObject actual_category = null;
    private LocationHandler location_handler;
    private Bitmap last_photo;
    AdapterCategoriesList adapter_categories;
    List<ParseObject> categories_list;
    ParseObject actual_problem_status;
    List<Address> addresses = null;

    public static File cacheDir;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> categories_problem = ParseQuery.getQuery("ProblemCategory");
        categories_problem.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categories, ParseException e) {
                if (!categories.isEmpty()) {
                    adapter_categories = new AdapterCategoriesList(AddReportActivity.context, categories);
                    categories_list = categories;
                    Collections.sort(categories_list, new CustomComparator());
                    Log.d("Cat", "Cat result: " + categories);

                } else {
                    Log.d("score", "Error: " + e);
                }
            }
        });

        //get the object with the status OPENED
        ParseQuery<ParseObject> status_objects = ParseQuery.getQuery("ProblemStatus");
        status_objects.whereEqualTo("objectId", "5uwuPDhv3c");
        status_objects.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (!parseObjects.isEmpty()) {
                    actual_problem_status = parseObjects.get(0);
                } else {
                    Log.d("score", "Error: " + e);
                }
            }
        });


        AddReportActivity.context = getApplicationContext();

        location_handler = LocationHandler.getInstance();

        //get the address based on coordinate
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            addresses = gcd.getFromLocation(location_handler.getActualLatitude(),
                    location_handler.getActualLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set view
        setContentView(R.layout.activity_add_report);

        //set the Address based on coordinate
        if (addresses.size() > 0) {
            Log.d("adress: ", addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1)+ addresses.get(0).getAddressLine(2));
            EditText editText_adress = (EditText) findViewById(R.id.address_report_input_edittext);
            editText_adress.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1)+ ", " +addresses.get(0).getAddressLine(2));
        } else {
            EditText editText_adress = (EditText) findViewById(R.id.address_report_input_edittext);
            editText_adress.setText("Could you type the name of these street?");
        }

        //get the last picture taken
        final Cursor cursor = this.findLastPicture();
        // Put it in the image view
        if (cursor.moveToFirst()) {
            final ImageView imageView = (ImageView) findViewById(R.id.photo_report_display_imageview);
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                imageView.setImageBitmap(bm);
                last_photo = bm;
            }
        }
        cursor.close();
    }

    public void categoryButtonClicked(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Which category?");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );

        builderSingle.setAdapter(adapter_categories,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView categoryName = (TextView) findViewById(R.id.add_report_category_name);
                        categoryName.setText(categories_list.get(which).get("name").toString());

                        ImageView categoryImage = (ImageView) findViewById(R.id.add_report_category_image);
                        Resources res = context.getResources();
                        int resID = res.getIdentifier(categories_list.get(which).get("categoryLogo").toString(),
                                "drawable", context.getPackageName());
                        Drawable drawable = res.getDrawable(resID);
                        categoryImage.setImageDrawable(drawable);
                        actual_category = categories_list.get(which);

                        dialog.dismiss();
                    }
                }
        );
        builderSingle.show();
    }

    public void addNewReport(View v) {

        boolean isValid = true;

        String category_id = null;
        //Get fields
        EditText description_field = (EditText) findViewById(R.id.description_report_input_edittext);
        EditText address_field = (EditText) findViewById(R.id.address_report_input_edittext);
        if (actual_category != null) {
            category_id = actual_category.getObjectId();
        }
        ParseGeoPoint location = new ParseGeoPoint(location_handler.getActualLatitude(), location_handler.getActualLongitude());

        //Validate form
        if (description_field.getText().toString() == "" || description_field == null)
            isValid = false;
        if (address_field.getText().toString() == "" || address_field == null)
            isValid = false;
        if (category_id == null)
            isValid = false;
        if (location == null)
            isValid = false;
        if (category_id == null)
            isValid = false;

        if (isValid) {
            Toast.makeText(context, "Saving your report", Toast.LENGTH_SHORT).show();

            //Compressing and manking a byte stream with the last photo
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            last_photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] data = stream.toByteArray();
            ParseFile image_file = new ParseFile(data);

            ParseObject new_report = new ParseObject("Problem");
            new_report.put("description", description_field.getText().toString());
            new_report.put("address", address_field.getText().toString());
            new_report.put("problemCategory", actual_category);
            new_report.put("statusObject", actual_problem_status);
            new_report.put("coordinate", location);
            new_report.put("userObject", ParseUser.getCurrentUser());
            new_report.put("image", image_file);
            new_report.put("innapropriate", false);
            new_report.put("city", addresses.get(0).getAddressLine(1).toString());
            new_report.put("country", addresses.get(0).getAddressLine(2).toString());
            new_report.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.d("Tag", "exception " + e);
                    }
                }
            });
            finish();
        } else {
            Toast.makeText(context, "Fill all the fields, please", Toast.LENGTH_SHORT).show();

        }
    }

    public Cursor findLastPicture() {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        return cursor;
    }

    public class CustomComparator implements Comparator<ParseObject> {
        @Override
        public int compare(ParseObject o1, ParseObject o2) {
            return o1.get("name").toString().compareTo(o2.get("name").toString());
        }
    }
}
