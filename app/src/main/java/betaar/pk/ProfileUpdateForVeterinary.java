package betaar.pk;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import betaar.pk.Adapters.SpinnerListingAdapter;
import betaar.pk.Config.API;
import betaar.pk.Preferences.Prefs;
import betaar.pk.VolleyLibraryFiles.AppSingleton;
import betaar.pk.utils.Utility;

public class ProfileUpdateForVeterinary extends AppCompatActivity {

    ScrollView scroll;
    Spinner sp_select_qualification;
    LinearLayout ll_when_lad;
    LinearLayout ll_when_dvm;
    LinearLayout ll_dynamic_layout;
    LinearLayout ll_to_inflat;
    RelativeLayout fb_admore;
    LinearLayout ll_main_check;
    RelativeLayout rl_update_profile;

    CheckBox cb_dairy, cb_pet, cb_equine, cb_bird, cb_other;
    LinearLayout ll_inner_cb_dairy, ll_inner_cb_pet, ll_inner_cb_equine, ll_inner_cb_bird, ll_inner_cb_other;

    LinearLayout ll_cb_treatment_nutritionist, ll_cb_breed_surgeon;
    CheckBox cb_treatment, cb_nutritionist;
    CheckBox cb_breeding, cb_surgeon;

    Button bt_document_copi_dvm_mphile;
    Button bt_document_copies_for_experience;

    Button bt_document_copies_common_study;

    LinearLayout ll_inflate_document_for_first;
    LinearLayout ll_inflate_document_for_mphil_document;
    LinearLayout ll_inflate_document_for_experience;

    TextView tv_expertise;
    TextView tv_specialization;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Uri imageUri = null;
    Bitmap bitmap1;

    private int indicatorForGallary = -1;

    EditText et_top_year_of_passing, et_top_instituation_name;
    EditText et_rvmp_mpil_dvm;
    EditText et_deparments_mpil_dvm, et_instituation_name_mpil_dvm, et_passing_year_mpil_dvm;

    ArrayList<String> topDocumentsArray;
    ArrayList<String> mPhileDocuments;

    CheckBox cb_dairy_cow, cb_dairy_buffalo, cb_dairy_sheep, cb_dairy_goat, cb_dairy_camel;
    CheckBox cb_pet_dog, cb_pet_cat, cb_pet_rabbit;
    CheckBox cb_equine_horse, cb_equine_donkey, cb_equine_mule;
    CheckBox cb_bird_ostrich, cb_bird_fancy, cb_bird_game, cb_bird_poultry;
    CheckBox cb_other_fish, cb_other_lion, cb_other_deer, cb_other_monkey, cb_other_other;

    ArrayList<TempDataClass> experineceViewList;
    ArrayList<JSONObject> data;
    JSONObject jODairy;
    JSONObject jsonObjectDiploma;
    JSONObject jsonObjectDairy;
    JSONObject jsonObjectPets;
    JSONObject jsonObjectEquine;
    JSONObject jsonObjectBirds;
    JSONObject jsonObjectOther;
    JSONObject jsonObjectMphile;
    JSONObject jsonObjectSpecialist;
    JSONObject jsonObjectExperience;

    ImageView progress_logo;
    Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update_for_veterinary);

        init();
        spinnerQualificationSelectListener();

        addMoreClickHandler();
        checkCnageListnerForDairy();
        checkCnageListnerForPets();
        checkCnageListnerForEquine();
        checkCnageListnerForBirds();
        checkCnageListnerForOther();
        gettingDocumentsFromGallary();
        btDocumentsForMphil();
        updateButtonClickHandler();
    }

    private void init() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        scroll = (ScrollView) findViewById(R.id.scroll);
        sp_select_qualification = (Spinner) findViewById(R.id.sp_select_qualification);
        ll_when_lad = (LinearLayout) findViewById(R.id.ll_when_lad);
        ll_when_dvm = (LinearLayout) findViewById(R.id.ll_when_dvm);
        ll_to_inflat = (LinearLayout) findViewById(R.id.ll_to_inflat);
        ll_dynamic_layout = (LinearLayout) findViewById(R.id.ll_dynamic_layout);
        fb_admore = (RelativeLayout) findViewById(R.id.fb_admore);

        ll_main_check = (LinearLayout) findViewById(R.id.ll_main_check);

        cb_dairy = (CheckBox) findViewById(R.id.cb_dairy);
        cb_pet = (CheckBox) findViewById(R.id.cb_pet);
        cb_equine = (CheckBox) findViewById(R.id.cb_equine);
        cb_bird = (CheckBox) findViewById(R.id.cb_bird);
        cb_other = (CheckBox) findViewById(R.id.cb_other);

        ll_inner_cb_dairy = (LinearLayout) findViewById(R.id.ll_inner_cb_dairy);
        ll_inner_cb_pet = (LinearLayout) findViewById(R.id.ll_inner_cb_pet);
        ll_inner_cb_equine = (LinearLayout) findViewById(R.id.ll_inner_cb_equine);
        ll_inner_cb_bird = (LinearLayout) findViewById(R.id.ll_inner_cb_bird);
        ll_inner_cb_other = (LinearLayout) findViewById(R.id.ll_inner_cb_other);

        ll_cb_treatment_nutritionist = (LinearLayout) findViewById(R.id.ll_cb_treatment_nutritionist);
        ll_cb_breed_surgeon = (LinearLayout) findViewById(R.id.ll_cb_breed_surgeon);
        cb_treatment = (CheckBox) findViewById(R.id.cb_treatment);
        cb_nutritionist = (CheckBox) findViewById(R.id.cb_nutritionist);
        cb_breeding = (CheckBox) findViewById(R.id.cb_breeding);
        cb_surgeon = (CheckBox) findViewById(R.id.cb_surgeon);

        rl_update_profile = (RelativeLayout) findViewById(R.id.rl_update_profile);
        rl_update_profile.setVisibility(View.GONE);

        bt_document_copi_dvm_mphile = (Button) findViewById(R.id.bt_document_copi_dvm_mphile);

        bt_document_copies_common_study = (Button) findViewById(R.id.bt_document_copies_common_study);
        bt_document_copies_for_experience = (Button) findViewById(R.id.bt_document_copies_for_experience);

        ll_inflate_document_for_first = (LinearLayout) findViewById(R.id.ll_inflate_document_for_first);
        ll_inflate_document_for_mphil_document = (LinearLayout) findViewById(R.id.ll_inflate_document_for_mphil_document);
        ll_inflate_document_for_experience = (LinearLayout) findViewById(R.id.ll_inflate_document_for_experience);

        SpinnerListingAdapter subCategory = new SpinnerListingAdapter(getApplicationContext(), Arrays.qualification_type);
        sp_select_qualification.setAdapter(subCategory);

        /*ArrayAdapter adapter = ArrayAdapter.createFromResource(ProfileUpdateForVeterinary.this,
                R.array.qualification_type, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_select_qualification.setAdapter(adapter);*/

        ll_inflate_document_for_first.removeAllViews();
        ll_inflate_document_for_mphil_document.removeAllViews();
        ll_inflate_document_for_experience.removeAllViews();

        ll_to_inflat.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.layout_add_more_for_veterinary_update_profile, null);

        LinearLayout ll_inflate_document_for_experienceInner = (LinearLayout) rowView.findViewById(R.id.ll_inflate_document_for_experience);

        ll_to_inflat.addView(rowView, ll_to_inflat.getChildCount());
        ImageView im_remove_view = (ImageView) rowView.findViewById(R.id.im_remove_view);
        Button bt_document_copies_for_experience = (Button) rowView.findViewById(R.id.bt_document_copies_for_experience);
        im_remove_view.setVisibility(View.GONE);

        ll_inflate_document_for_experienceInner.removeAllViews();

        int indecator = ll_to_inflat.getChildCount();
        indecator = indecator + 2;
        Log.e("TAG", "the count of view in experience: " + indecator);
        btDynamicButtonForExperienceDocuments(bt_document_copies_for_experience, indecator);

        et_top_year_of_passing = (EditText) findViewById(R.id.et_top_year_of_passing);
        et_top_instituation_name = (EditText) findViewById(R.id.et_top_instituation_name);
        et_rvmp_mpil_dvm = (EditText) findViewById(R.id.et_rvmp_mpil_dvm);
        et_deparments_mpil_dvm = (EditText) findViewById(R.id.et_deparments_mpil_dvm);
        et_instituation_name_mpil_dvm = (EditText) findViewById(R.id.et_instituation_name_mpil_dvm);
        et_passing_year_mpil_dvm = (EditText) findViewById(R.id.et_passing_year_mpil_dvm);

        topDocumentsArray = new ArrayList<>();
        mPhileDocuments = new ArrayList<>();

        cb_dairy_cow = (CheckBox) findViewById(R.id.cb_dairy_cow);
        cb_dairy_buffalo = (CheckBox) findViewById(R.id.cb_dairy_buffalo);
        cb_dairy_sheep = (CheckBox) findViewById(R.id.cb_dairy_sheep);
        cb_dairy_goat = (CheckBox) findViewById(R.id.cb_dairy_goat);
        cb_dairy_camel = (CheckBox) findViewById(R.id.cb_dairy_camel);

        cb_pet_dog = (CheckBox) findViewById(R.id.cb_pet_dog);
        cb_pet_cat = (CheckBox) findViewById(R.id.cb_pet_cat);
        cb_pet_rabbit = (CheckBox) findViewById(R.id.cb_pet_Rabbit);

        cb_equine_horse = (CheckBox) findViewById(R.id.cb_equine_horse);
        cb_equine_donkey = (CheckBox) findViewById(R.id.cb_equine_donkey);
        cb_equine_mule = (CheckBox) findViewById(R.id.cb_equine_mule);

        cb_bird_ostrich = (CheckBox) findViewById(R.id.cb_bird_ostrich);
        cb_bird_fancy = (CheckBox) findViewById(R.id.cb_bird_fancy);
        cb_bird_game = (CheckBox) findViewById(R.id.cb_bird_game);
        cb_bird_poultry = (CheckBox) findViewById(R.id.cb_bird_poultry);

        cb_other_fish = (CheckBox) findViewById(R.id.cb_other_fish);
        cb_other_lion = (CheckBox) findViewById(R.id.cb_other_lion);
        cb_other_deer = (CheckBox) findViewById(R.id.cb_other_deer);
        cb_other_monkey = (CheckBox) findViewById(R.id.cb_other_monkey);
        cb_other_other = (CheckBox) findViewById(R.id.cb_other_other);

        tv_expertise = (TextView) findViewById(R.id.tv_expertise);
        tv_specialization = (TextView) findViewById(R.id.tv_specialization);

        experineceViewList = new ArrayList<>();
        data = new ArrayList<>();

        progress_logo = (ImageView) findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

    }

    private void spinnerQualificationSelectListener() {

        sp_select_qualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {

                    ll_when_dvm.setVisibility(View.GONE);
                    ll_when_lad.setVisibility(View.GONE);
                    ll_dynamic_layout.setVisibility(View.GONE);
                    ll_main_check.setVisibility(View.GONE);
                    rl_update_profile.setVisibility(View.GONE);
                }
                if (i == 1) {

                    ll_when_dvm.setVisibility(View.GONE);
                    ll_when_lad.setVisibility(View.VISIBLE);
                    ll_dynamic_layout.setVisibility(View.VISIBLE);
                    ll_main_check.setVisibility(View.VISIBLE);

                    ll_cb_treatment_nutritionist.setVisibility(View.GONE);
                    cb_surgeon.setVisibility(View.GONE);
                    cb_breeding.setChecked(true);
                    rl_update_profile.setVisibility(View.VISIBLE);
                    et_rvmp_mpil_dvm.setVisibility(View.GONE);


                    //for specialities check
                    if (cb_treatment.isChecked()) {

                        cb_treatment.setChecked(false);
                    }
                    if (cb_breeding.isChecked()) {
                        cb_breeding.setChecked(true);
                    }
                    if (cb_nutritionist.isChecked()) {
                        cb_nutritionist.setChecked(false);
                    }
                    if (cb_surgeon.isChecked()) {
                        cb_surgeon.setChecked(false);
                    }

                }
                if (i == 2) {

                    ll_when_dvm.setVisibility(View.VISIBLE);
                    ll_when_lad.setVisibility(View.VISIBLE);
                    ll_dynamic_layout.setVisibility(View.VISIBLE);
                    ll_main_check.setVisibility(View.VISIBLE);

                    ll_cb_treatment_nutritionist.setVisibility(View.VISIBLE);
                    cb_surgeon.setVisibility(View.VISIBLE);
                    cb_breeding.setChecked(false);

                    rl_update_profile.setVisibility(View.VISIBLE);
                    et_rvmp_mpil_dvm.setVisibility(View.VISIBLE);

                    if (cb_treatment.isChecked()) {

                        cb_treatment.setChecked(false);
                    }
                    if (cb_breeding.isChecked()) {
                        cb_breeding.setChecked(false);
                    }
                    if (cb_nutritionist.isChecked()) {
                        cb_nutritionist.setChecked(false);
                    }
                    if (cb_surgeon.isChecked()) {
                        cb_surgeon.setChecked(false);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addMoreClickHandler() {

        fb_admore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.layout_add_more_for_veterinary_update_profile, null);

                LinearLayout ll_inflate_document_for_experienceInner = (LinearLayout) rowView.findViewById(R.id.ll_inflate_document_for_experience);
                ll_to_inflat.addView(rowView, ll_to_inflat.getChildCount());
                Button bt_document_copies_for_experience = (Button) rowView.findViewById(R.id.bt_document_copies_for_experience);

                ll_inflate_document_for_experienceInner.removeAllViews();
                deleteingView(rowView);

                int indecator = ll_to_inflat.getChildCount();
                indecator = indecator + 2;
                Log.e("TAG", "the count of view in experience: " + indecator);
                btDynamicButtonForExperienceDocuments(bt_document_copies_for_experience, indecator);


            }
        });
    }

    private void deleteingView(final View myView) {

        Log.e("TAG", "Image button Clicked: " + ll_to_inflat.getChildCount());
        for (int i = 0; i < ll_to_inflat.getChildCount(); i++) {
            final View timingView = ll_to_inflat.getChildAt(i);
            ImageView im_remove_view = (ImageView) timingView.findViewById(R.id.im_remove_view);

            im_remove_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parent = (ViewGroup) timingView.getParent();
                    parent.removeView(timingView);

                    //ll_to_inflat.removeView((View) (myView).getParent());
                }
            });
        }

    }

    private void checkCnageListnerForDairy() {

        cb_dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_dairy.isChecked()) {
                    ll_inner_cb_dairy.setVisibility(View.VISIBLE);
                    Animation slidToRight = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_right);
                    ll_inner_cb_dairy.startAnimation(slidToRight);

                } else {

                    Animation slidToLeft = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_left);
                    ll_inner_cb_dairy.startAnimation(slidToLeft);
                    slidToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_inner_cb_dairy.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
    }


    private void checkCnageListnerForPets() {

        cb_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_pet.isChecked()) {
                    ll_inner_cb_pet.setVisibility(View.VISIBLE);
                    Animation slidToRight = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_right);
                    ll_inner_cb_pet.startAnimation(slidToRight);

                } else {

                    Animation slidToLeft = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_left);
                    ll_inner_cb_pet.startAnimation(slidToLeft);
                    slidToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_inner_cb_pet.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
    }

    private void checkCnageListnerForEquine() {

        cb_equine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_equine.isChecked()) {
                    ll_inner_cb_equine.setVisibility(View.VISIBLE);
                    Animation slidToRight = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_right);
                    ll_inner_cb_equine.startAnimation(slidToRight);

                } else {

                    Animation slidToLeft = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_left);
                    ll_inner_cb_equine.startAnimation(slidToLeft);
                    slidToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_inner_cb_equine.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
    }

    private void checkCnageListnerForBirds() {

        cb_bird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_bird.isChecked()) {
                    ll_inner_cb_bird.setVisibility(View.VISIBLE);
                    Animation slidToRight = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_right);
                    ll_inner_cb_bird.startAnimation(slidToRight);

                } else {

                    Animation slidToLeft = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_left);
                    ll_inner_cb_bird.startAnimation(slidToLeft);
                    slidToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_inner_cb_bird.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
    }

    private void checkCnageListnerForOther() {

        cb_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_other.isChecked()) {
                    ll_inner_cb_other.setVisibility(View.VISIBLE);
                    Animation slidToRight = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_right);
                    ll_inner_cb_other.startAnimation(slidToRight);

                } else {

                    Animation slidToLeft = AnimationUtils.loadAnimation(ProfileUpdateForVeterinary.this, R.anim.slide_to_left);
                    ll_inner_cb_other.startAnimation(slidToLeft);
                    slidToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            ll_inner_cb_other.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        });
    }

    private void gettingDocumentsFromGallary() {

        bt_document_copies_common_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean result = Utility.checkPermission(ProfileUpdateForVeterinary.this);
                if (result) {


                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_dialog_for_getting_photo);

                    Button bt_dialog_from_gallary = (Button) dialog.findViewById(R.id.bt_dialog_from_gallary);
                    Button bt_dialog_from_camera = (Button) dialog.findViewById(R.id.bt_dialog_from_camera);

                    bt_dialog_from_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            cameraIntent(1); //to detect that the view is from top document button view

                        }
                    });

                    bt_dialog_from_gallary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            indicatorForGallary = 1;
                            galleryIntent();
                        }
                    });


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();


                }

            }
        });
    }

    private void cameraIntent(final int detector) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        String fileName = getFileName(imageUri);
        if (detector == 1) {
            addViewFileName(fileName, imageUri.toString());
        }
        if (detector == 2) {
            addViewFileNameForMphile(fileName, imageUri.toString());

        }
        if (detector > 2) {
            addFileNameForDynamicViewsForExperience(fileName, imageUri.toString(), detector);

        }
        Log.e("TAG", "the upload file name is: " + fileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAg", "The Request code is: " + requestCode);

        //  if (resultCode == Activity.RESULT_OK) {
        if (requestCode == SELECT_FILE) {

            if (data != null) {
                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (data != null) {
                onCaptureImageResult(data);
            }
        }

    }

    //selecting image from galary
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            imageUri = data.getData();
            String fileName = getFileName(imageUri);
            if (indicatorForGallary == 1) {
                addViewFileName(fileName, imageUri.toString());
            }
            if (indicatorForGallary == 2) {

                addViewFileNameForMphile(fileName, imageUri.toString());
            }
            if (indicatorForGallary > 2) {
                addFileNameForDynamicViewsForExperience(fileName, imageUri.toString(), indicatorForGallary);
            }
            Log.e("TAG", "the upload file name is: " + fileName);

            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //pmdcImageFrameLayout.setVisibility(View.VISIBLE);
                //pmdc_select_picture_layout.setVisibility(View.GONE);
                //iv_pmdc.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //getting image form camera
    private void onCaptureImageResult(Intent data) {

        try {

            bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            //pmdcImageFrameLayout.setVisibility(View.VISIBLE);
            //pmdc_select_picture_layout.setVisibility(View.GONE);
            //iv_pmdc.setImageBitmap(bitmap1);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void addViewFileName(String fileName, String imageURi) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.layout_for_add_file_name
                , null);

        TextView tv_image_uri = (TextView) rowView.findViewById(R.id.tv_image_uri);
        tv_image_uri.setText(imageURi);
        TextView tv_file_name = (TextView) rowView.findViewById(R.id.tv_file_name);
        tv_file_name.setText(fileName);

        Log.e("TAG", "the file uri is: " + imageURi);

        ll_inflate_document_for_first.addView(rowView, ll_inflate_document_for_first.getChildCount());

        deletingImageFileName(rowView);
        btPreviewImage(rowView);
    }

    private void deletingImageFileName(final View myView) {

        for (int i = 0; i < ll_inflate_document_for_first.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_first.getChildAt(i);
            ImageView im_remove_view = (ImageView) rootView.findViewById(R.id.iv_crose_file_name);

            im_remove_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parent = (ViewGroup) rootView.getParent();
                    parent.removeView(rootView);

                    //ll_to_inflat.removeView((View) (myView).getParent());
                }
            });
        }
    }

    private void btPreviewImage(final View previewView) {

        for (int i = 0; i < ll_inflate_document_for_first.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_first.getChildAt(i);
            Button d_view_button_preview = (Button) rootView.findViewById(R.id.d_view_button_preview);

            d_view_button_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tv_image_uri = (TextView) rootView.findViewById(R.id.tv_image_uri);
                    TextView tv_file_name = (TextView) rootView.findViewById(R.id.tv_file_name);
                    Log.e("TAg", "the image page is: " + tv_image_uri.getText().toString());


                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_image_preview);

                    TextView tv_dialog_image_name = (TextView) dialog.findViewById(R.id.tv_dialog_image_name);
                    ImageView iv_dialog_preview_image = (ImageView) dialog.findViewById(R.id.iv_dialog_preview_image);

                    imageUri = Uri.parse(tv_image_uri.getText().toString());

                    tv_dialog_image_name.setText(tv_file_name.getText().toString());
                    iv_dialog_preview_image.setImageURI(imageUri);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();

                }
            });
        }

    }

    private void btDocumentsForMphil() {
        bt_document_copi_dvm_mphile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean result = Utility.checkPermission(ProfileUpdateForVeterinary.this);
                if (result) {

                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_dialog_for_getting_photo);

                    Button bt_dialog_from_gallary = (Button) dialog.findViewById(R.id.bt_dialog_from_gallary);
                    Button bt_dialog_from_camera = (Button) dialog.findViewById(R.id.bt_dialog_from_camera);

                    bt_dialog_from_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            cameraIntent(2);

                        }
                    });

                    bt_dialog_from_gallary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            indicatorForGallary = 2;
                            galleryIntent();
                        }
                    });


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();


                }
            }
        });
    }

    private void addViewFileNameForMphile(String fileName, String imageURi) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.layout_for_add_file_name_mphil
                , null);

        TextView tv_image_uri_mphil = (TextView) rowView.findViewById(R.id.tv_image_uri_mphil);
        tv_image_uri_mphil.setText(imageURi);
        TextView tv_file_name_mphil = (TextView) rowView.findViewById(R.id.tv_file_name_mphil);
        tv_file_name_mphil.setText(fileName);

        ll_inflate_document_for_mphil_document.addView(rowView, ll_inflate_document_for_mphil_document.getChildCount());

        deletingImageFileNameForMphil(rowView);
        btPreviewImageMphile(rowView);
    }

    private void deletingImageFileNameForMphil(final View myView) {

        for (int i = 0; i < ll_inflate_document_for_mphil_document.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_mphil_document.getChildAt(i);
            ImageView im_remove_view = (ImageView) rootView.findViewById(R.id.iv_crose_file_name_mphil);

            im_remove_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parent = (ViewGroup) rootView.getParent();
                    parent.removeView(rootView);


                }
            });
        }
    }


    private void btPreviewImageMphile(final View previewView) {

        for (int i = 0; i < ll_inflate_document_for_mphil_document.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_mphil_document.getChildAt(i);
            Button d_view_button_preview_mphil = (Button) rootView.findViewById(R.id.d_view_button_preview_mphil);

            d_view_button_preview_mphil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tv_image_uri_mphil = (TextView) rootView.findViewById(R.id.tv_image_uri_mphil);
                    TextView tv_file_name_mphil = (TextView) rootView.findViewById(R.id.tv_file_name_mphil);
                    Log.e("TAg", "the image page is: " + tv_image_uri_mphil.getText().toString());


                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_image_preview);

                    TextView tv_dialog_image_name = (TextView) dialog.findViewById(R.id.tv_dialog_image_name);
                    ImageView iv_dialog_preview_image = (ImageView) dialog.findViewById(R.id.iv_dialog_preview_image);

                    imageUri = Uri.parse(tv_image_uri_mphil.getText().toString());

                    tv_dialog_image_name.setText(tv_file_name_mphil.getText().toString());
                    iv_dialog_preview_image.setImageURI(imageUri);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();

                }
            });
        }
    }

    private void btDynamicButtonForExperienceDocuments(Button btExpereiceDocuments, final int detector) {
        btExpereiceDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean result = Utility.checkPermission(ProfileUpdateForVeterinary.this);
                if (result) {

                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_dialog_for_getting_photo);

                    Button bt_dialog_from_gallary = (Button) dialog.findViewById(R.id.bt_dialog_from_gallary);
                    Button bt_dialog_from_camera = (Button) dialog.findViewById(R.id.bt_dialog_from_camera);

                    bt_dialog_from_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            cameraIntent(detector);

                        }
                    });

                    bt_dialog_from_gallary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            indicatorForGallary = detector;
                            galleryIntent();
                        }
                    });


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();


                }
            }
        });
    }

    private void addFileNameForDynamicViewsForExperience(String fileName, String imageURi, int viewNumber) {


        viewNumber = viewNumber - 3;

        final View parentViwe = ll_to_inflat.getChildAt(viewNumber);
        Log.e("TAg", "view positino is: " + parentViwe);

        LinearLayout ll_inflate_document_for_experience_inner = (LinearLayout) parentViwe.findViewById(R.id.ll_inflate_document_for_experience);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.layout_for_add_file_name_experience
                , null);
        TextView tv_image_uri_experience = (TextView) rowView.findViewById(R.id.tv_image_uri_experience);
        tv_image_uri_experience.setText(imageURi);
        TextView tv_file_name_experience = (TextView) rowView.findViewById(R.id.tv_file_name_experience);
        tv_file_name_experience.setText(fileName);


        ll_inflate_document_for_experience_inner.addView(rowView);

        deletingImageFileNameForExperience(rowView);
        btPreviewImageExperience(rowView);

    }

    private void deletingImageFileNameForExperience(final View myView) {

        LinearLayout ll_inflate_document_for_experience_inner = (LinearLayout) myView.findViewById(R.id.ll_inflate_document_for_experience);

        for (int i = 0; i < ll_inflate_document_for_experience_inner.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_experience_inner.getChildAt(i);
            ImageView im_remove_view = (ImageView) rootView.findViewById(R.id.iv_crose_file_name_experience);

            im_remove_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup parent = (ViewGroup) rootView.getParent();
                    parent.removeView(rootView);

                }
            });
        }
    }


    private void btPreviewImageExperience(final View previewView) {

        LinearLayout ll_inflate_document_for_experience_inner = (LinearLayout) previewView.findViewById(R.id.ll_inflate_document_for_experience);

        for (int i = 0; i < ll_inflate_document_for_experience_inner.getChildCount(); i++) {
            final View rootView = ll_inflate_document_for_experience_inner.getChildAt(i);
            Button d_view_button_preview_mphil = (Button) rootView.findViewById(R.id.d_view_button_preview_experience);

            d_view_button_preview_mphil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView tv_image_uri_mphil = (TextView) rootView.findViewById(R.id.tv_image_uri_experience);
                    TextView tv_file_name_mphil = (TextView) rootView.findViewById(R.id.tv_file_name_experience);
                    Log.e("TAg", "the image page is: " + tv_image_uri_mphil.getText().toString());


                    final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_image_preview);

                    TextView tv_dialog_image_name = (TextView) dialog.findViewById(R.id.tv_dialog_image_name);
                    ImageView iv_dialog_preview_image = (ImageView) dialog.findViewById(R.id.iv_dialog_preview_image);

                    imageUri = Uri.parse(tv_image_uri_mphil.getText().toString());

                    tv_dialog_image_name.setText(tv_file_name_mphil.getText().toString());
                    iv_dialog_preview_image.setImageURI(imageUri);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    dialog.show();

                }
            });
        }
    }

    private void updateButtonClickHandler() {
        rl_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("TAG", "Button Click!");

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                if (experineceViewList.size() > 0) {
                    experineceViewList.clear();
                }

                int theExperienceViewCount = ll_to_inflat.getChildCount();
                EditText et_experience_from = null;
                EditText et_experience_to = null;
                EditText et_organization_name = null;
                Log.e("TAg", "the experience View count are: " + theExperienceViewCount);

                for (int i = 0; i < theExperienceViewCount; i++) {

                    final View parantView = ll_to_inflat.getChildAt(i);
                    et_experience_from = (EditText) parantView.findViewById(R.id.et_experience_from);
                    et_experience_to = (EditText) parantView.findViewById(R.id.et_experience_to);
                    et_organization_name = (EditText) parantView.findViewById(R.id.et_organization_name);

                    String expFrom = et_experience_from.getText().toString();
                    String expTo = et_experience_to.getText().toString();
                    String expORG_Name = et_organization_name.getText().toString();

                        Log.e("TAg", "the experience from is: " + expFrom);
                        Log.e("TAg", "the experience expTo is: " + expTo);
                       Log.e("TAg", "the experience expORG_Name is: " + expORG_Name);

                    String et_exprience_from_is_not_nice = et_experience_from.getText().toString();

                    ArrayList<String> urisForImages = new ArrayList<>();

                    LinearLayout ll_inflate_document_for_experience = (LinearLayout) parantView.findViewById(R.id.ll_inflate_document_for_experience);

                    int chiledCountForExperience = ll_inflate_document_for_experience.getChildCount();
                        Log.e("TAg", "the child count for top documents are: " + chiledCountForExperience);

                    if (chiledCountForExperience > 0) {

                        for (int j = 0; j < chiledCountForExperience; j++) {

                            final View mViews = ll_inflate_document_for_experience.getChildAt(j);
                            TextView tv_image_uri_experience = (TextView) mViews.findViewById(R.id.tv_image_uri_experience);
                            //         Log.e("TAg", "the image uri is: " + tv_image_uri_experience.getText().toString());
                            urisForImages.add(tv_image_uri_experience.getText().toString());

                        }
                    }//end of for loop for experience documents

                    experineceViewList.add(new TempDataClass(expTo, expFrom, expORG_Name, urisForImages));

                }//end of experience dynamic views

                ArrayList<HashMap<String, String>> precticeDataRecord = new ArrayList<>();

                String topPassingYeaer = et_top_year_of_passing.getText().toString();
                String topInstituation = et_top_instituation_name.getText().toString();
                String toRVMP_or_SVMP = et_rvmp_mpil_dvm.getText().toString();

                String mPhileDemartment = et_deparments_mpil_dvm.getText().toString();
                String mPhilePassingYear = et_passing_year_mpil_dvm.getText().toString();
                String mPhileInstituation = et_instituation_name_mpil_dvm.getText().toString();

                Log.e("TAG", "OKAY");

                if (et_top_year_of_passing.getText().toString().length() == 0) {

                    scroll.smoothScrollTo(0, ll_when_lad.getTop());
                    et_top_year_of_passing.setError("Passing Year Must not be Empty!");
                    et_top_year_of_passing.setAnimation(animShake);

                } else if (et_top_year_of_passing.getText().toString().length() != 4) {

                    scroll.smoothScrollTo(0, ll_when_lad.getTop());
                    et_top_year_of_passing.setError("At least 4 Characters are to be entered!");
                    et_top_year_of_passing.setAnimation(animShake);

                } else if (et_top_instituation_name.getText().length() == 0) {

                    scroll.smoothScrollTo(0, ll_when_lad.getTop());
                    et_top_instituation_name.setError("Institution Name Must not be Empty!");
                    et_top_instituation_name.setAnimation(animShake);

                } else if (sp_select_qualification.getSelectedItemId() == 2 && et_rvmp_mpil_dvm.getText().length() == 0) {

                    scroll.smoothScrollTo(0, ll_when_lad.getTop());
                    et_rvmp_mpil_dvm.setError("This field must not be empty!");
                    et_rvmp_mpil_dvm.setAnimation(animShake);

                } else if (!(cb_dairy.isChecked() || cb_pet.isChecked() || cb_equine.isChecked() || cb_bird.isChecked() || cb_other.isChecked())) {

                    scroll.smoothScrollTo(0, tv_expertise.getTop());
                    tv_expertise.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Expertise!", Toast.LENGTH_SHORT).show();
                    tv_expertise.setAnimation(animShake);

                } else if (cb_dairy.isChecked() && !(cb_dairy_cow.isChecked() || cb_dairy_buffalo.isChecked() || cb_dairy_sheep.isChecked() || cb_dairy_goat.isChecked() || cb_dairy_camel.isChecked())) {

                    scroll.smoothScrollTo(0, cb_dairy.getTop());
                    cb_dairy.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Dairy Expertise!", Toast.LENGTH_SHORT).show();
                    cb_dairy.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                } else if (cb_pet.isChecked() && !(cb_pet_dog.isChecked() || cb_pet_cat.isChecked() || cb_pet_rabbit.isChecked())) {

                    scroll.smoothScrollTo(0, cb_pet.getTop());
                    cb_pet.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Pet Expertise!", Toast.LENGTH_SHORT).show();
                    cb_pet.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                } else if (cb_equine.isChecked() && !(cb_equine_horse.isChecked() || cb_equine_donkey.isChecked() || cb_equine_mule.isChecked())) {

                    scroll.smoothScrollTo(0, cb_equine.getTop());
                    cb_equine.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Equine Expertise!", Toast.LENGTH_SHORT).show();
                    cb_equine.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                } else if (cb_bird.isChecked() && !(cb_bird_ostrich.isChecked() || cb_bird_poultry.isChecked() || cb_bird_fancy.isChecked() || cb_bird_game.isChecked())) {

                    scroll.smoothScrollTo(0, cb_bird.getTop());
                    cb_bird.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Bird Expertise!", Toast.LENGTH_SHORT).show();
                    cb_bird.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                } else if (cb_other.isChecked() && !(cb_other_fish.isChecked() || cb_other_lion.isChecked() || cb_other_deer.isChecked() || cb_other_monkey.isChecked() || cb_other_other.isChecked())) {

                    scroll.smoothScrollTo(0, cb_other.getTop());
                    cb_other.setError("Please Select Any Expertise!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Other Category Expertise!", Toast.LENGTH_SHORT).show();
                    cb_other.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    tv_specialization.setError(null);

                } else if (!(cb_treatment.isChecked() || cb_nutritionist.isChecked() || cb_breeding.isChecked() || cb_surgeon.isChecked())) {

                    scroll.smoothScrollTo(0, tv_specialization.getTop());
                    tv_specialization.setError("Please Select Any Specialization!");
                    Toast.makeText(ProfileUpdateForVeterinary.this, "Please Select Any Specialization!", Toast.LENGTH_SHORT).show();
                    tv_specialization.setAnimation(animShake);

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);

                }

                else if (et_deparments_mpil_dvm.getText().toString().length() == 0 && et_passing_year_mpil_dvm.getText().toString().length() == 0 && et_instituation_name_mpil_dvm.getText().toString().length() == 0) {

                    if (et_deparments_mpil_dvm.getText().toString().length() == 0) {

                        scroll.smoothScrollTo(0, et_deparments_mpil_dvm.getBottom());
                        et_deparments_mpil_dvm.setError("This field can not be empty!");
                        et_deparments_mpil_dvm.setAnimation(animShake);

                    } else if (et_passing_year_mpil_dvm.getText().toString().length() == 0) {

                        scroll.smoothScrollTo(0, et_passing_year_mpil_dvm.getBottom());
                        et_passing_year_mpil_dvm.setError("This field can not be empty!");
                        et_passing_year_mpil_dvm.setAnimation(animShake);

                    } else if (et_instituation_name_mpil_dvm.getText().toString().length() == 0) {

                        scroll.smoothScrollTo(0, et_instituation_name_mpil_dvm.getBottom());
                        et_instituation_name_mpil_dvm.setError("This field can not be empty!");
                        et_instituation_name_mpil_dvm.setAnimation(animShake);

                    }

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                    Log.e("TAG", "et_deparments_mpil_dvm: " + et_deparments_mpil_dvm.getText().toString());
                    Log.e("TAG", "et_passing_year_mpil_dvm: " + et_passing_year_mpil_dvm.getText().toString());
                    Log.e("TAG", "et_deparments_mpil_dvm: " + et_instituation_name_mpil_dvm.getText().toString());

                    if (theExperienceViewCount >= 1 && (et_experience_from.getText().toString().length() == 0 || et_experience_from.getText().toString().length() != 4 || et_experience_to.getText().toString().length() == 0 || et_experience_to.getText().toString().length() != 4 || et_organization_name.getText().toString().length() == 0 || Integer.valueOf(et_experience_from.getText().toString()) > Integer.valueOf(et_experience_to.getText().toString()))) {

                        for (int i = 0; i < theExperienceViewCount; i++) {

                            final View parantView = ll_to_inflat.getChildAt(i);
                            et_experience_from = (EditText) parantView.findViewById(R.id.et_experience_from);
                            et_experience_to = (EditText) parantView.findViewById(R.id.et_experience_to);
                            et_organization_name = (EditText) parantView.findViewById(R.id.et_organization_name);

                            String expFrom = et_experience_from.getText().toString();
                            String expTo = et_experience_to.getText().toString();
                            String expORG_Name = et_organization_name.getText().toString();

                            if (et_experience_from.getText().toString().length() == 0) {

                                //scroll.smoothScrollTo(0, ll_to_inflat.getTop());
                                et_experience_from.setError("This field can not be empty!");
                                ll_to_inflat.setAnimation(animShake);

                            } else if (et_experience_from.getText().toString().length() != 4) {

                                et_experience_from.setError("4 characters required!");
                                ll_to_inflat.setAnimation(animShake);

                            } else if (et_experience_to.getText().toString().length() == 0) {

                                //scroll.smoothScrollTo(0, ll_to_inflat.getTop());
                                et_experience_to.setError("This field can not be empty!");
                                ll_to_inflat.setAnimation(animShake);

                            } else if (et_experience_to.getText().toString().length() != 4) {

                                et_experience_to.setError("4 characters required!");
                                ll_to_inflat.setAnimation(animShake);

                            } else if (et_organization_name.getText().toString().length() == 0) {

                                //scroll.smoothScrollTo(0, ll_to_inflat.getTop());
                                et_organization_name.setError("This field can not be empty!");
                                ll_to_inflat.setAnimation(animShake);

                            } else if (Integer.valueOf(et_experience_from.getText().toString()) > Integer.valueOf(et_experience_to.getText().toString())) {

                                //scroll.smoothScrollTo(0, ll_to_inflat.getTop());
                                et_experience_to.setError("Must be greater than or equal to From!");
                                ll_to_inflat.setAnimation(animShake);

                            }

                            Log.e("TAg", "the experience from is: " + expFrom);
                            Log.e("TAg", "the experience expTo is: " + expTo);
                            Log.e("TAg", "the experience expORG_Name is: " + expORG_Name);

                        }

                        tv_expertise.setError(null);
                        cb_dairy.setError(null);
                        cb_pet.setError(null);
                        cb_equine.setError(null);
                        cb_bird.setError(null);
                        cb_other.setError(null);
                        tv_specialization.setError(null);

                        Log.e("TAG", "Experience NOT OKAY!");

                    }

                } else {

                    tv_expertise.setError(null);
                    cb_dairy.setError(null);
                    cb_pet.setError(null);
                    cb_equine.setError(null);
                    cb_bird.setError(null);
                    cb_other.setError(null);
                    tv_specialization.setError(null);

                    Log.e("TAG", "OKAY! ");

                    //System.gc();

                    if (topDocumentsArray.size() > 0) {
                        topDocumentsArray.clear();
                    }

                    int chiledCountForTopDocuments = ll_inflate_document_for_first.getChildCount();
                    Log.e("TAg", "the child count for top documents are: " + chiledCountForTopDocuments);

                    if (chiledCountForTopDocuments > 0) {

                        for (int i = 0; i < chiledCountForTopDocuments; i++) {

                            final View mViews = ll_inflate_document_for_first.getChildAt(i);
                            TextView tv_image_uri = (TextView) mViews.findViewById(R.id.tv_image_uri);
                            Log.e("TAg", "the image uri is: " + tv_image_uri.getText().toString());
                            topDocumentsArray.add(tv_image_uri.getText().toString());

                        }

                    }//end of for loop for top documents

                    JSONObject jsonObjectD;

                    LinkedHashMap<String, String> dataListSimple = new LinkedHashMap<>();
                    dataListSimple.put("passing_year", topPassingYeaer);
                    dataListSimple.put("instituatio_name", topInstituation);

                    dataListSimple.put("instituatio_RVMP", toRVMP_or_SVMP);

                    Log.e("TAG","passing_year: " + topPassingYeaer);
                    Log.e("TAG","instituatio_name: " + topInstituation);
                    Log.e("TAG","instituatio_RVMP: " + toRVMP_or_SVMP);
        /*if (et_rvmp_mpil_dvm.getVisibility() == view.VISIBLE){
            dataListSimple.put("instituatio_RVMP", toRVMP_or_SVMP);
        }*/
                    //ArrayList<HashMap<String, String>> deta = new ArrayList<>();
                    //deta.add(dataListSimple);

                    jsonObjectDiploma = new JSONObject();

                    //JSONArray jsonArray = new JSONArray(deta);

                    jsonObjectD = new JSONObject(dataListSimple);

                    try {

                        jsonObjectDiploma.put("Diploma", jsonObjectD);

                        //Log.e("TAg", "Diploma: " + jsonObjectD);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //for diary

                    if (cb_dairy.isChecked()) {

                        //LinkedList<HashMap<String, String>> deta1 = new LinkedList<>();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        int test = 0;

                        if (cb_dairy_cow.isChecked()) {
                            String cb_dairy_check = Arrays.getID(Arrays.dairy_categories, cb_dairy_cow.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_dairy_check);
                        }

                        if (cb_dairy_buffalo.isChecked()) {
                            String cb_dairy_check = Arrays.getID(Arrays.dairy_categories, cb_dairy_buffalo.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_dairy_check);
                        }

                        if (cb_dairy_sheep.isChecked()) {
                            String cb_dairy_check = Arrays.getID(Arrays.dairy_categories, cb_dairy_sheep.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_dairy_check);
                        }

                        if (cb_dairy_goat.isChecked()) {
                            String cb_dairy_check = Arrays.getID(Arrays.dairy_categories, cb_dairy_goat.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_dairy_check);
                        }

                        if (cb_dairy_camel.isChecked()) {
                            String cb_dairy_check = Arrays.getID(Arrays.dairy_categories, cb_dairy_camel.getText().toString());
                            dataListSimple2.put(String.valueOf(test), cb_dairy_check);
                        }

                        //deta1.add(dataListSimple2);
                        jsonObjectDairy = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        jODairy = new JSONObject(dataListSimple2);

                        try {
                            jsonObjectDairy.put("Dairy", jODairy);

                            //Log.e("TAg", "Dairy JSON Object: " + jODairy);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        data.clear();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        jsonObjectDairy = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        jODairy = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectDairy.put("Dairy", jODairy);
                            //Log.e("TAg", "Dairy JSON Object: " + jODairy);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //for pet
                    if (cb_pet.isChecked()) {
                        //ArrayList<HashMap<String, String>> deta1 = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        int test = 0;
                        if (cb_pet_dog.isChecked()) {
                            String cb_pet_check = Arrays.getID(Arrays.pets_categories, cb_pet_dog.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_pet_check);
                        }
                        if (cb_pet_cat.isChecked()) {
                            String cb_pet_check = Arrays.getID(Arrays.pets_categories, cb_pet_cat.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_pet_check);
                        }
                        if (cb_pet_rabbit.isChecked()) {
                            String cb_pet_check = Arrays.getID(Arrays.pets_categories, cb_pet_rabbit.getText().toString());
                            dataListSimple2.put(String.valueOf(test), cb_pet_check);
                        }

                        //deta1.add(dataListSimple2);
                        jsonObjectPets = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectPets.put("Pets", jsonObject3);

                            //Log.e("TAg", "Pets JSON Object: " + jsonObject3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        data.clear();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        jsonObjectPets = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectPets.put("Pets", jsonObject3);

                            //Log.e("TAg", "Pets JSON Object: " + jsonObject3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //for equine
                    if (cb_equine.isChecked()) {
                        //ArrayList<HashMap<String, String>> deta1 = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        int test = 0;
                        if (cb_equine.isChecked()) {
                            String cb_equine = Arrays.getID(Arrays.equine_categories, cb_equine_horse.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_equine);
                        }
                        if (cb_equine_donkey.isChecked()) {
                            String cb_equine = Arrays.getID(Arrays.equine_categories, cb_equine_donkey.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_equine);
                        }
                        if (cb_equine.isChecked()) {
                            String cb_equine = Arrays.getID(Arrays.equine_categories, cb_equine_mule.getText().toString());
                            dataListSimple2.put(String.valueOf(test), cb_equine);
                        }

                        //deta1.add(dataListSimple2);
                        jsonObjectEquine = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectEquine.put("Equine", jsonObject3);

                            //Log.e("TAg", "Equine JSON Object: " + forEquine);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        data.clear();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        jsonObjectEquine = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectEquine.put("Equine", jsonObject3);

                            //Log.e("TAg", "Equine JSON Object: " + forEquine);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //for birds
                    if (cb_bird.isChecked()) {
                        //ArrayList<HashMap<String, String>> deta1 = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        int test = 0;
                        if (cb_bird_ostrich.isChecked()) {
                            String cb_birds = Arrays.getID(Arrays.birds_categories, cb_bird_ostrich.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_birds);
                        }
                        if (cb_bird_poultry.isChecked()) {
                            String cb_birds = Arrays.getID(Arrays.birds_categories, cb_bird_poultry.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_birds);
                        }
                        if (cb_bird_fancy.isChecked()) {
                            String cb_birds = Arrays.getID(Arrays.birds_categories, cb_bird_fancy.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_birds);
                        }
                        if (cb_bird_game.isChecked()) {
                            String cb_birds = Arrays.getID(Arrays.birds_categories, cb_bird_game.getText().toString());
                            dataListSimple2.put(String.valueOf(test), cb_birds);
                        }

                        //deta1.add(dataListSimple2);
                        jsonObjectBirds = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectBirds.put("Birds", jsonObject3);

                            //Log.e("TAg", "Birds JSON Object: " + forBirds);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        data.clear();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        jsonObjectBirds = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectBirds.put("Birds", jsonObject3);

                            //Log.e("TAg", "Birds JSON Object: " + forBirds);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //for other
                    if (cb_other.isChecked()) {
                        //ArrayList<HashMap<String, String>> deta1 = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        int test = 0;
                        if (cb_other_fish.isChecked()) {
                            String cb_other = Arrays.getID(Arrays.other_categories, cb_other_fish.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_other);
                        }
                        if (cb_other_lion.isChecked()) {
                            String cb_other = Arrays.getID(Arrays.other_categories, cb_other_lion.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_other);
                        }
                        if (cb_other_deer.isChecked()) {
                            String cb_other = Arrays.getID(Arrays.other_categories, cb_other_deer.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_other);
                        }
                        if (cb_other_monkey.isChecked()) {
                            String cb_other = Arrays.getID(Arrays.other_categories, cb_other_monkey.getText().toString());
                            dataListSimple2.put(String.valueOf(test++), cb_other);
                        }
                        if (cb_other_other.isChecked()) {
                            String cb_other = Arrays.getID(Arrays.other_categories, cb_other_other.getText().toString());
                            dataListSimple2.put(String.valueOf(test), cb_other);
                        }

                        //deta1.add(dataListSimple2);
                        jsonObjectOther = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectOther.put("Other", jsonObject3);

                            //Log.e("TAg", "Other JSON Object: " + forOther);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        data.clear();
                        LinkedHashMap<String, String> dataListSimple2 = new LinkedHashMap<>();
                        jsonObjectOther = new JSONObject();
                        //JSONArray jsonArray2 = new JSONArray(deta1);
                        JSONObject jsonObject3 = new JSONObject(dataListSimple2);
                        try {
                            jsonObjectOther.put("Other", jsonObject3);

                            //Log.e("TAg", "Other JSON Object: " + forOther);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    ArrayList<JSONObject> expertise = new ArrayList<>();
                    expertise.add(jsonObjectDairy);
                    expertise.add(jsonObjectPets);
                    expertise.add(jsonObjectEquine);
                    expertise.add(jsonObjectBirds);
                    expertise.add(jsonObjectOther);

                    //Log.e("TAG", "Expertise: " + expertise);


                    JSONObject jsonObjectSpecialities = new JSONObject();

                    //for specialist cheboxes
                    if (sp_select_qualification.getSelectedItemId() == 1) {

                        //ArrayList<HashMap<String, String>> specialistDataList = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSpecialist = new LinkedHashMap<>();

                        if (cb_breeding.isChecked()) {
                            String cb_treat = Arrays.getID(Arrays.specialities, cb_breeding.getText().toString());
                            dataListSpecialist.put("0", cb_treat);
                        }

                        //specialistDataList.add(dataListSpecialist);
                        jsonObjectSpecialist = new JSONObject();
                        //JSONArray jsonArraySpecialist = new JSONArray(specialistDataList);
                        jsonObjectSpecialities = new JSONObject(dataListSpecialist);
                        try {
                            jsonObjectSpecialist.put("specialities", jsonObjectSpecialities);

                            //Log.e("TAg", "Specialities: " + jsonObjectSpecialities);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (sp_select_qualification.getSelectedItemId() == 2) {
                        //ArrayList<HashMap<String, String>> specialistDataList = new ArrayList<>();
                        LinkedHashMap<String, String> dataListSpecialist = new LinkedHashMap<>();
                        int test = 0;
                        if (cb_treatment.isChecked()) {
                            String cb_treat = Arrays.getID(Arrays.specialities, cb_treatment.getText().toString());
                            dataListSpecialist.put(String.valueOf(test++), cb_treat);
                        }
                        if (cb_nutritionist.isChecked()) {
                            String cb_treat = Arrays.getID(Arrays.specialities, cb_nutritionist.getText().toString());
                            dataListSpecialist.put(String.valueOf(test++), cb_treat);
                        }
                        if (cb_breeding.isChecked()) {
                            String cb_treat = Arrays.getID(Arrays.specialities, cb_breeding.getText().toString());
                            dataListSpecialist.put(String.valueOf(test++), cb_treat);
                        }
                        if (cb_surgeon.isChecked()) {
                            String cb_treat = Arrays.getID(Arrays.specialities, cb_surgeon.getText().toString());
                            dataListSpecialist.put(String.valueOf(test), cb_treat);
                        }

                        //specialistDataList.add(dataListSpecialist);
                        jsonObjectSpecialist = new JSONObject();
                        //JSONArray jsonArraySpecialist = new JSONArray(specialistDataList);
                        jsonObjectSpecialities = new JSONObject(dataListSpecialist);
                        try {
                            jsonObjectSpecialist.put("specialities", jsonObjectSpecialities);

                            //Log.e("TAg", "Specialities: " + jsonObjectSpecialities);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    JSONObject jsonObjectMphil = new JSONObject();

                    //for mPhil
                    if (sp_select_qualification.getSelectedItemId() == 2) {

                        LinkedHashMap<String, String> mPhileData = new LinkedHashMap<>();
                        mPhileData.put("department", mPhileDemartment);
                        mPhileData.put("passing_year", mPhilePassingYear);
                        mPhileData.put("instituation_name", mPhileInstituation);

                        //ArrayList<HashMap<String, String>> arrayMphileDeta = new ArrayList<>();
                        //arrayMphileDeta.add(mPhileData);

                        jsonObjectMphile = new JSONObject();
                        //JSONArray jsonArrayMphile = new JSONArray(arrayMphileDeta);
                        jsonObjectMphil = new JSONObject(mPhileData);
                        try {
                            jsonObjectMphile.put("mPhil", jsonObjectMphil);

                            //Log.e("TAg", "MPhil: " + jsonObjectMphil);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        LinkedHashMap<String, String> mPhileData = new LinkedHashMap<>();
                        mPhileData.put("department", "");
                        mPhileData.put("passing_year", "");
                        mPhileData.put("instituation_name", "");

                        //ArrayList<HashMap<String, String>> arrayMphileDeta = new ArrayList<>();
                        //arrayMphileDeta.add(mPhileData);

                        jsonObjectMphile = new JSONObject();
                        //JSONArray jsonArrayMphile = new JSONArray(arrayMphileDeta);
                        jsonObjectMphil = new JSONObject(mPhileData);
                        try {
                            jsonObjectMphile.put("mPhil", jsonObjectMphil);

                            //Log.e("TAg", "MPhil: " + jsonObjectMphil);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (mPhileDocuments.size() > 0) {
                        mPhileDocuments.clear();
                    }
                    int chiledCountForMphileDocuments = ll_inflate_document_for_mphil_document.getChildCount();
                      //Log.e("TAg", "the child count for top documents are: " + chiledCountForTopDocuments);
                    if (chiledCountForMphileDocuments > 0) {
                        for (int i = 0; i < chiledCountForMphileDocuments; i++) {

                            final View mViews = ll_inflate_document_for_mphil_document.getChildAt(i);
                            TextView tv_image_uri_mphil = (TextView) mViews.findViewById(R.id.tv_image_uri_mphil);
                            //Log.e("TAg", "the image uri is: " + tv_image_uri_mphil.getText().toString());
                            mPhileDocuments.add(tv_image_uri_mphil.getText().toString());
                        }
                    }//end of for loop for mphile documents

                    JSONArray jsonArrayExperience = new JSONArray();

                    //for experience
                    ArrayList<LinkedHashMap<String, String>> deta2 = new ArrayList<>();
                    if (experineceViewList.size() > 0) {
                        for (int s = 0; s < experineceViewList.size(); s++) {
                            LinkedHashMap<String, String> dataListSimple1 = new LinkedHashMap<>();
                            String experienceFrom = experineceViewList.get(s).getdFrom();
                            String experienceTo = experineceViewList.get(s).getdTo();
                            String organizationName = experineceViewList.get(s).getdORGName();
                            dataListSimple1.put("experience_from", experienceFrom);
                            dataListSimple1.put("experience_to", experienceTo);
                            dataListSimple1.put("experience_org_name", organizationName);
                            deta2.add(dataListSimple1);
                        }


                        jsonObjectExperience = new JSONObject();
                        jsonArrayExperience = new JSONArray(deta2);
                        try {
                            jsonObjectExperience.put("Experience", jsonArrayExperience);

                            Log.e("TAg", "Experience: " + jsonArrayExperience);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int x = 0; x < experineceViewList.size(); x++) {

                            ArrayList<String> imageUriListForExperience = new ArrayList<>();

                            ArrayList<String> tempArray = experineceViewList.get(x).getUris();
                            if (tempArray.size() > 0) {
                                for (int s = 0; s < tempArray.size(); s++) {
                                    imageUriListForExperience.add(tempArray.get(s).toString());
                                }
                            }
                            //Log.e("TAG", "Array Size For Documents Experience: " + imageUriListForExperience.size());
                        }

                    }

                    String user_id = Prefs.getUserIDFromPref(ProfileUpdateForVeterinary.this);

                    String diploma_type = "";

                    if (sp_select_qualification.getSelectedItemId() == 1) {
                        diploma_type = "LAD";
                    } else if (sp_select_qualification.getSelectedItemId() == 2) {
                        diploma_type = "DVM";
                    }

                    //Log.e("TAG", "DATA IS OKAY!");

                    //calling service to upload images
                    //upLoadingDataToServer(user_id, diploma_type, jsonObjectD.toString(), expertise.toString(),jsonObjectSpecialities.toString(), jsonObjectMphil.toString() , jsonArrayExperience.toString() , topDocumentsArray, mPhileDocuments, experineceViewList);
                    uploadDataToServer(user_id, diploma_type, jsonObjectD.toString(), expertise.toString(), jsonObjectSpecialities.toString(), jsonObjectMphil.toString(), jsonArrayExperience.toString());

                }

            }//end of overide click buttons
        });
    }

    public class TempDataClass {

        public String getdTo() {
            return dTo;
        }

        public String getdFrom() {
            return dFrom;
        }

        public String getdORGName() {
            return dORGName;
        }

        public ArrayList<String> getUris() {
            return uris;
        }

        String dTo, dFrom, dORGName;
        ArrayList<String> uris;

        public TempDataClass(String dTo, String dFrom, String dORGName, ArrayList<String> uris) {

            this.dTo = dTo;
            this.dFrom = dFrom;
            this.dORGName = dORGName;
            this.uris = uris;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileUpdateForVeterinary.this, DashboardVeterinarian.class));
        finish();
    }

    private void uploadDataToServer(final String user_id, final String diploma_type, final String Diploma, final String Expertise, final String Specialities, final String MPhil, final String Experience) {
        // Tag used to cancel the request
        String cancel_req_tag = "update";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Update_vet, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Update Vet Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("msg");
                        Toast.makeText(ProfileUpdateForVeterinary.this, message, Toast.LENGTH_SHORT).show();
                        showDialogue(message);

                    } else {

                        boolean exist = jObj.getBoolean("exist");

                        if (exist) {
                            String message = jObj.getString("msg");
                            Toast.makeText(ProfileUpdateForVeterinary.this, message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(ProfileUpdateForVeterinary.this);

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Uploading Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

               /* SharedPreferences sharedPreferences  = getSharedPreferences("udid", 0);
                String userUdid = sharedPreferences.getString("udid", "null");*/

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", user_id);
                params.put("diploma_type", diploma_type);
                params.put("Diploma", Diploma);
                params.put("Expertise", Expertise);
                params.put("Specialities", Specialities);
                params.put("MPhil", MPhil);
                params.put("Experience", Experience);

                Log.e("TAG","diploma_type: " + diploma_type);
                Log.e("TAG","Diploma: " + Diploma);
                Log.e("TAG","Expertise: " + Expertise);
                Log.e("TAG","Specialities: " + Specialities);
                Log.e("TAG","MPhil: " + MPhil);
                Log.e("TAG","Experience: " + Experience);

                Log.e("Service", "id: " + user_id +
                        "\n diploma_type: " + diploma_type);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void upLoadingDataToServer(String user_id, String diploma_type, String Diploma, String Expertise, String Specialities, String MPhil, String Experience,
                                      ArrayList<String> topDocumentsImage, ArrayList<String> mPhileDocuments
            , ArrayList<TempDataClass> experineceViewList) {

        Log.e("Service", "id: " + user_id +
                "\n diploma_type: " + diploma_type);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(ProfileUpdateForVeterinary.this, uploadId, API.Update_vet);

            multipartUploadRequest.addParameter("key", API.KEY);//Adding text parameter to the request
            multipartUploadRequest.addParameter("user_id", user_id);
            multipartUploadRequest.addParameter("diploma_type", diploma_type);

            multipartUploadRequest.addParameter("Diploma", Diploma);
            /*for (String diplomaImage : topDocumentsImage){
                multipartUploadRequest.addFileToUpload(diplomaImage, "diplomaImages");
            }*/

            multipartUploadRequest.addParameter("Expertise", Expertise);
            multipartUploadRequest.addParameter("Specialities", Specialities);

            multipartUploadRequest.addParameter("MPhil", MPhil);
            /*for (String mPhileImage : mPhileDocuments){
                multipartUploadRequest.addFileToUpload(mPhileImage, "mPhilImages");
            }*/

            multipartUploadRequest.addParameter("Experience", Experience);

            /*for (TempDataClass experience : experineceViewList){
                String mFrom = experience.getdFrom().toString();
                String mTo = experience.getdTo().toString();
                String mOrganizationName = experience.dORGName.toString();
                ArrayList<String> exprienceImages = experience.getUris();

                multipartUploadRequest.addParameter("experinece_from", mFrom);
                multipartUploadRequest.addParameter("experinece_to", mTo);
                multipartUploadRequest.addParameter("experinece_organiztion", mOrganizationName);

                for (String expImages : exprienceImages){

                    multipartUploadRequest.addFileToUpload(expImages, "experienceImage"); //Adding file
                }
            }*/


            multipartUploadRequest.setMaxRetries(2);
            multipartUploadRequest.startUpload();

        } catch (Exception exc) {
            Toast.makeText(ProfileUpdateForVeterinary.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogue(String msg) {

        final Dialog dialog = new Dialog(ProfileUpdateForVeterinary.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_client_verfication);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.setCancelable(false);
        dialog.show();

        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_messasge = (TextView) dialog.findViewById(R.id.tv_messasge);

        tv_dialog_title.setText("Update SuccessFull!");
        tv_messasge.setText(msg);

        final RelativeLayout bt_proceed = (RelativeLayout) dialog.findViewById(R.id.bt_proceed);
        final TextView tv_proceed = (TextView) dialog.findViewById(R.id.tv_proceed);

        tv_proceed.setText("Ok");

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent i = new Intent(DashboardVeterinarian.this, DashboardVeterinarian.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
            }
        });


    }

}
