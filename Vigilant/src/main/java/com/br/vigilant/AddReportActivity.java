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

import com.br.adapter.AdapterCategoriesList;
import com.br.utils.LocationHandler;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Berhell on 08/07/14.
 */
public class AddReportActivity extends Activity {

    private static Context context;
    public static AdapterCategoriesList adapter_categories;
    public static List<ParseObject> categories_list;
    private ParseObject actual_category;
    LocationHandler locationHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ProblemCategory");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categories, ParseException e) {
                if (!categories.isEmpty()) {
                    AddReportActivity.adapter_categories = new AdapterCategoriesList(AddReportActivity.context, categories);
                    AddReportActivity.categories_list = categories;
                    Log.d("Cat", "Cat result: " + categories);
                } else {
                    Log.d("score", "Error: ");
                }
            }
        });


        AddReportActivity.context = getApplicationContext();

        locationHandler = LocationHandler.getInstance();

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(locationHandler.getActualLatitude(),
                    locationHandler.getActualLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_add_report);

        if (addresses.size() > 0){
            Log.d("adress: ",addresses.get(0).getAddressLine(0)+addresses.get(0).getAddressLine(1));
            EditText editText_adress = (EditText) findViewById(R.id.address_report_input_edittext);
            editText_adress.setText(addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getAddressLine(1));
        }
        else {
            EditText editText_adress = (EditText) findViewById(R.id.address_report_input_edittext);
            editText_adress.setText("Could you type the name of these street?");
        }






        // Find the last picture
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

// Put it in the image view
        if (cursor.moveToFirst()) {
            final ImageView imageView = (ImageView) findViewById(R.id.photo_report_display_imageview);
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                imageView.setImageBitmap(bm);
            }
        }
        cursor.close();
    }

    public void categoryButtonClicked(View view) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Which category?");

////Implementaçao estática das categorias para o adapterå
//        String[] categories_names = new String[]{"Airport-ic_airport", "Animals-ic_animals", "Environment and Pollution-ic_environment",
//                "Public Lighting-ic_lighting", "Public Property-ic_property", "Public Transport-ic_transport", "Road and Street Conditions-ic_road",
//                "Trees-ic_tree", "Secutiry-ic_security", "Rubbish-ic_rubbish", "Other-ic_others", "Culture, Sports and Leisure-ic_culture", "Hospitals and Health Care-ic_hospital",
//                "Family and Social Assistance-ic_family", "Public Service-ic_service"};

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

    public void addNewReport(View v){
        ParseObject new_report = new ParseObject("Problem");
        new_report.put("description",findViewById(R.id.description_report_input_edittext));
        new_report.put("unsolvedBy", ParseUser.getCurrentUser().getObjectId());

    }
}
