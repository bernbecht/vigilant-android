package com.br.vigilant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.br.adapter.AdapterCategoriesList;
import com.br.adapter.AdapterMoreFeaturesList;
import com.br.utils.ConnectionUtils;
import com.br.utils.DatePicker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Berhell on 10/07/14.
 */
public class ReportDescriptionActivity extends Activity {

    private ParseObject problem;
    private ParseObject category;
    private ParseObject status;
    private List<ParseObject> botheredList;
    private ParseUser user;
    public Context context;
    private boolean isBothered = false;
    private boolean isBotheredInit = false;
    //variable for change the Content early the Cloud response
    private boolean isSolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        Log.d("test", "objectId: " + objectId);
        try {
            getAllProblemsFromCloud(objectId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        context = getApplicationContext();

        setContentView(R.layout.activity_report_description);


        if (problem != null) {
            TextView address = (TextView) findViewById(R.id.textview_address_reportDescription);
//            TextView country = (TextView) findViewById(R.id.textview_country_reportDescription);
            TextView createdAt = (TextView) findViewById(R.id.textview_date_reportDescription);
            TextView problemStatusField = (TextView) findViewById(R.id.textview_status_reportDescription);
            ImageView problemImageField = (ImageView) findViewById(R.id.imageview_problem_reportDescription);
            ImageView categoryImageField = (ImageView) findViewById(R.id.imageview_category_reportDescription);
            TextView categoryNameField = (TextView) findViewById(R.id.textview_category_reportDescription);
            TextView botheredCounter = (TextView) findViewById(R.id.textview_bothered_reportDescription);
            TextView nicknameField = (TextView) findViewById(R.id.textview_nickname_reportDescription);
            TextView descriptionField = (TextView) findViewById(R.id.textview_description_reportDescription);
            LinearLayout problemStatusContainer = (LinearLayout) findViewById(R.id.linearLayout_status_reportDescription);
            Button botheredButton = (Button) findViewById(R.id.button_bothered_reportDescription);


            address.setText(problem.get("address").toString());
//            country.setText(problem.get("address").toString());

            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            createdAt.setText(df.format(problem.getCreatedAt()));

            //set category logo
            Resources res = context.getResources();
            int resID = res.getIdentifier(category.get("categoryLogo").toString(),
                    "drawable",
                    context.getPackageName());
            Drawable drawable = res.getDrawable(resID);
            categoryImageField.setImageDrawable(drawable);

            categoryNameField.setText(category.get("name").toString());
            nicknameField.setText(user.get("nickname").toString());
            descriptionField.setText(problem.get("description").toString());

            //set problem image
            ParseFile pf = (ParseFile) problem.get("image");
            byte[] blob = new byte[0];
            try {
                blob = pf.getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            problemImageField.setImageBitmap(bmp);

            problemStatusField.setText(status.get("name").toString() + " " + df.format(status.getUpdatedAt()));
            if (isSolved) {
                problemStatusContainer.setBackgroundColor(getResources().getColor(R.color.green_dark));
            } else {
                problemStatusContainer.setBackgroundColor(getResources().getColor(R.color.red_dark));
            }

            if (botheredList != null) {
                botheredCounter.setText(botheredList.size() + " bothered");
            } else {
                botheredCounter.setText("0 bothered");
            }

            if (isBotheredInit) {
                botheredButton.setBackground(getResources().getDrawable(R.drawable.bothered_button_clicked));
                botheredButton.setPadding(14,7,14,7);
                botheredButton.setText(getResources().getText(R.string.button_bothered_clicked_reportDescription));
                isBothered = true;
            }
        }
    }

    //TODO: (PERSISTENCE) it should be a persistence and not a new request
    public void getAllProblemsFromCloud(String objectId) throws ParseException {
        List<ParseObject> problemsResult;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Problem");
        query.include("userObject");
        query.include("statusObject");
        query.include("problemCategory");
        query.whereEqualTo("objectId", objectId);
        problemsResult = query.find();
        if (!problemsResult.isEmpty()) {
            problem = problemsResult.get(0);
            category = (ParseObject) problem.get("problemCategory");
            user = (ParseUser) problem.get("userObject");
            status = (ParseObject) problem.get("statusObject");

            //set the variable for problem's status for Actvity changes
            if (status.get("name").equals("solved")) {
                isSolved = true;
            } else {
                isSolved = false;
            }

            Log.d("test", "desc report: " + problem.getObjectId());
            Log.d("teste", "prob desc: " + problem.get("description"));
            Log.d("teste", "prob desc: " + ((ParseUser) problem.get("userObject")).getObjectId());
            Log.d("teste", "prob desc: " + ((ParseObject) problem.get("statusObject")).getObjectId());
            Log.d("teste", "prob desc: " + category.getObjectId());

            List<ParseObject> botheredResults;
            query = ParseQuery.getQuery("ProblemSupport");
            query.include("userObject");
            query.include("problemObject");
            query.whereEqualTo("problemObject", problem);
            botheredResults = query.find();
            if (!botheredResults.isEmpty()) {
                botheredList = botheredResults;
                Log.d("teste", "bothered: " + botheredList.size());
                for (int i = 0; i < botheredList.size(); i++) {
                    ParseUser u = (ParseUser) botheredList.get(i).get("userObject");
                    ParseObject p = (ParseObject) botheredList.get(i).get("problemObject");
                    Log.d("teste", "isBother user ID: " + ParseUser.getCurrentUser().getObjectId());
                    Log.d("teste", "isBother problem ID: " + problem.getObjectId());
                    Log.d("teste", "isBother actual problem user ID and problemID: " + u.getObjectId() + " " + p.getObjectId());
                    if (new String(u.getObjectId()).equals(ParseUser.getCurrentUser().getObjectId())
                            && new String(p.getObjectId()).equals(problem.getObjectId())) {
                        isBotheredInit = true;
                        Log.d("teste", "isBother : " + isBothered);
                    }
                }
                Log.d("teste", "isBother outside: " + isBothered);
            }
        }
    }

    public void addBothered(View v) {
        if(ConnectionUtils.detectConnection(this)){
            Button botheredButton = (Button) findViewById(R.id.button_bothered_reportDescription);
            TextView botheredLabel = (TextView) findViewById(R.id.textview_bothered_reportDescription);
            String botheredLabelContent = botheredLabel.getText().toString();
            String botheredLabelArray[] = botheredLabelContent.split(" ");
            int botheredNumber = Integer.parseInt(botheredLabelArray[0]);

            if (!isBothered) {
                botheredNumber++;
                botheredLabel.setText(botheredNumber + " bothered");
                botheredButton.setBackground(getResources().getDrawable(R.drawable.bothered_button_clicked));
                botheredButton.setPadding(14,7,14,7);
                botheredButton.setText(getResources().getText(R.string.button_bothered_clicked_reportDescription));

                this.isBothered = true;
            } else {
                botheredNumber--;
                botheredLabel.setText(botheredNumber + " bothered");
                botheredButton.setBackground(getResources().getDrawable(R.drawable.bothered_button_normal));
                botheredButton.setPadding(14,7,14,7);
                botheredButton.setText(getResources().getText(R.string.button_bothered_normal_reportDescription));
                isBothered = false;
            }
        }else{
            Toast.makeText(context, "You are without connection. Try it again later.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void showMoreFeaturesDialog(View v) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                this);


        String[] listCategories = {"This problem is solved", "Share on Facebook", "Report this as inappropriate"};
        Log.d("teste", "status problem: " + status.get("name"));
        if (isSolved) {
            listCategories[0] = "This problem is not solved";
            Log.d("teste", "status problem: " + status.get("name"));
        }

        AdapterMoreFeaturesList adapter_categories = new AdapterMoreFeaturesList(this.context,
                listCategories);

        builderSingle.setAdapter(adapter_categories,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!status.get("name").equals("solved")) {
                            switch (which) {
                                case 0:
                                    Log.d("teste", "Solved");
                                    if(ConnectionUtils.detectConnection(context)){
                                        setProblemStatusCloud();
                                        Toast.makeText(context, "Thank you", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "You are without connection. Try it again later.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    Toast.makeText(context, "Sharing...", Toast.LENGTH_SHORT).show();
                                    Log.d("teste", "Facebook");
                                    break;
                                case 2:

                                    Log.d("teste", "Report");
                                    if(ConnectionUtils.detectConnection(context)){
                                        setInappropriate();
                                        Toast.makeText(context, "Reporting...", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "You are without connection. Try it again later.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        } else {
                            switch (which) {
                                case 0:
                                    Log.d("teste", "UnSolved");
                                    if(ConnectionUtils.detectConnection(context)){
                                        setProblemStatusCloud();
                                        Toast.makeText(context, "Thank you", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "You are without connection. Try it again later.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    Toast.makeText(context, "Sharing...", Toast.LENGTH_SHORT).show();
                                    Log.d("teste", "Facebook");
                                    break;
                                case 2:

                                    Log.d("teste", "Report");
                                    if(ConnectionUtils.detectConnection(context)){
                                        setInappropriate();
                                        Toast.makeText(context, "Reporting...", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context, "You are without connection. Try it again later.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        }

                        dialog.dismiss();
                    }
                }
        );
        builderSingle.show();
    }

    private void setProblemStatusCloud() {

        String table = "ProblemStatus";
        String attributeNameColumn;
        final String userStatusColumn;
        final String dateStatusColumn;

        //if it is not solved
        if (!isSolved) {
            attributeNameColumn = "solved";
            userStatusColumn = "solvedBy";
            dateStatusColumn = "solvedAt";
            this.isSolved = true;
        } else {
            attributeNameColumn = "unsolved";
            userStatusColumn = "usolvedBy";
            dateStatusColumn = "unsolvedAt";
            this.isSolved = false;
        }

        setStatusLabel();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(table);
        query.whereEqualTo("name", attributeNameColumn);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (!results.isEmpty()) {
                    Log.d("teste", "Solving");
                    ParseObject problemAux = problem;
                    problemAux.put("statusObject", results.get(0));
                    Log.d("teste", "Status: " + results.get(0).get("name"));
                    Log.d("teste", "ObjectID: " + problemAux.getObjectId());
                    problemAux.put(userStatusColumn, ParseUser.getCurrentUser());
                    problemAux.put(dateStatusColumn, DatePicker.getActualDateType());
                    problemAux.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d("teste", "error :" + e);
                            }
                        }
                    });
                } else {
                }
            }
        });
    }

    private void setStatusLabel() {
        LinearLayout problemStatusContainer = (LinearLayout) findViewById(R.id.linearLayout_status_reportDescription);
        TextView problemStatusField = (TextView) findViewById(R.id.textview_status_reportDescription);
        if (isSolved) {
            problemStatusContainer.setBackgroundColor(getResources().getColor(R.color.green_dark));
            problemStatusField.setText("solved" + " " + DatePicker.getActualDate());
        } else {
            problemStatusContainer.setBackgroundColor(getResources().getColor(R.color.red_dark));
            problemStatusField.setText("unsolved" + " " + DatePicker.getActualDate());
        }
    }

    private void setIsBotheredCloud(){
        if (!isBotheredInit && isBothered) {
            this.isBothered = true;
            ParseObject botheredObject = new ParseObject("ProblemSupport");
            botheredObject.put("userObject", ParseUser.getCurrentUser());
            botheredObject.put("problemObject", problem);
            botheredObject.saveInBackground();
        } else if (isBotheredInit && !isBothered) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ProblemSupport");
            query.whereEqualTo("userObject", ParseUser.getCurrentUser());

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("ProblemSupport");
            query2.whereEqualTo("problemObject", problem);

            List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
            queries.add(query);
            queries.add(query2);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
            mainQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    if (!results.isEmpty()) {
                        ParseObject botheredObject = results.get(0);
                        botheredObject.deleteInBackground();
                    }
                }
            });
        }
    }

    private void setInappropriate(){
        ParseObject inappropriateObject = new ParseObject("Innapropriate");
        inappropriateObject.put("userObject", ParseUser.getCurrentUser());
        inappropriateObject.put("problemObject", problem);
        inappropriateObject.saveInBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsBotheredCloud();
    }

}

