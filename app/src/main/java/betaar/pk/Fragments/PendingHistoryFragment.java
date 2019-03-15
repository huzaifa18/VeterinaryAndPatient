package betaar.pk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import betaar.pk.Adapters.HistoryAdapter;
import betaar.pk.Adapters.ImageListingAdapter;
import betaar.pk.Models.FarmSolutionData;
import betaar.pk.R;

public class PendingHistoryFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HistoryAdapter adapter;
    private ArrayList<FarmSolutionData> list;

    public PendingHistoryFragment() {
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

        recyclerView.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < 20; i++) {

            list.add(new FarmSolutionData("Item "));
            Log.e("TAG", "the array size is: " + list.size());
        }

        adapter = new HistoryAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

    }



}
