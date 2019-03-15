package betaar.pk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import betaar.pk.Adapters.HistoryAdapter;
import betaar.pk.Adapters.ImageListingAdapter;
import betaar.pk.Adapters.ListingAdapterCheckupRequest;
import betaar.pk.CheckupRequestsListing;
import betaar.pk.Config.API;
import betaar.pk.Models.FarmSolutionData;
import betaar.pk.Models.RequestModelVet;
import betaar.pk.Preferences.Prefs;
import betaar.pk.R;
import betaar.pk.VolleyLibraryFiles.AppSingleton;

public class RequestsHistoryFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HistoryAdapter adapter;
    private ArrayList<FarmSolutionData> list;
    public ArrayList<RequestModelVet> requestData;
    ListingAdapterCheckupRequest dataAdapter;

    ImageView progress_logo;
    Animation rotate;

    public RequestsHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_history, container, false);

        initialize();

        return view;
    }

    private void initialize() {

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_history);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        list = new ArrayList<FarmSolutionData>();
        requestData = new ArrayList<RequestModelVet>();

        progress_logo = (ImageView) view.findViewById(R.id.progress_logo);
        progress_logo.bringToFront();
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        progress_logo.setAnimation(rotate);

        recyclerView.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < 20; i++) {

            list.add(new FarmSolutionData("Item "));
            Log.e("TAG", "the array size is: " + list.size());
        }

        dataAdapter = new ListingAdapterCheckupRequest(getActivity(), requestData);
        recyclerView.setAdapter(dataAdapter);
        
        /*adapter = new HistoryAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);*/

        checkUpRequests();

    }

    private void checkUpRequests(){

        // Tag used to cancel the request
        String cancel_req_tag = "My Products";
        //show pregress here

        progress_logo.setVisibility(View.VISIBLE);
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        progress_logo.setAnimation(rotate);


        StringRequest strReq = new StringRequest(Request.Method.POST, API.Get_requests, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Get Pending Requests Response: " + response.toString());

                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);

                try {

                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONArray visits = jObj.getJSONArray("visits");

                        for (int i = 0; i < visits.length(); i++){

                            JSONObject obj = visits.getJSONObject(i);

                            String visit_id = obj.getString("visit_id");
                            String user_id_sender = obj.getString("user_id_sender");
                            String user_id_receiver = obj.getString("user_id_receiver");
                            String immediate_visit = obj.getString("immediate_visit");
                            String date_of_visit = obj.getString("date_of_visit");
                            String time_of_visit = obj.getString("time_of_visit");
                            String user_address = obj.getString("user_address");
                            String user_lat = obj.getString("user_lat");
                            String user_lng = obj.getString("user_lng");
                            String reason_of_visit = obj.getString("reason_of_visit");
                            String no_of_animals = obj.getString("no_of_animals");
                            String speciality_id = obj.getString("speciality_id");
                            String protocol = obj.getString("protocol");
                            String visit_started_at = obj.getString("visit_started_at");
                            String visit_ended_at = obj.getString("visit_ended_at");
                            String created_at = obj.getString("created_at");

                            JSONObject obj1 = obj.getJSONObject("receiver");

                            String name = obj1.getString("name");
                            String phone = obj1.getString("phone");

                            JSONObject obj2 = obj.getJSONObject("category");
                            String category_name = obj2.getString("name");

                            JSONObject obj3 = obj.getJSONObject("sub_category");
                            String sub_category_name = obj3.getString("name");

                            requestData.add(new RequestModelVet(visit_id,user_id_sender,user_id_receiver,immediate_visit,date_of_visit,time_of_visit,
                                    user_address,user_lat,user_lng,reason_of_visit,no_of_animals,speciality_id,protocol,visit_started_at,visit_ended_at,
                                    created_at,name,phone,category_name,sub_category_name));

                        }

                        dataAdapter = new ListingAdapterCheckupRequest(getActivity(), requestData,"client");
                        recyclerView.setAdapter(dataAdapter);

                        String message = jObj.getString("msg");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        boolean exist = jObj.getBoolean("exist");

                        if (exist){
                            String message = jObj.getString("msg");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else {

                            API.logoutService(getActivity());

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                progress_logo.clearAnimation();
                progress_logo.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", API.KEY);
                params.put("user_id", Prefs.getUserIDFromPref(getActivity()));
                params.put("type", "completed");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    
}
