package com.eyecare;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyPerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DailyPerformanceFragment extends Fragment {

    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyPerformanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyPerformanceFragment newInstance(String param1, String param2) {
        DailyPerformanceFragment fragment = new DailyPerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DailyPerformanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_performance, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PerformanceItem> list = new ArrayList<>();
        list.add(new PerformanceItem("Track Animal", "20 Exercises • 20 min", 0));
        list.add(new PerformanceItem("Eye Games", "20 Exercises • 20 min", 0));
        list.add(new PerformanceItem("Eye Test", "20 Exercises • 20 min", 9));
        list.add(new PerformanceItem("Focus Duration", "20 Exercises • 20 min", 0));
        list.add(new PerformanceItem("Screen Breaks", "20 Exercises • 20 min", 0));

        recyclerView.setAdapter(new PerformanceAdapter(list));

        return view;
    }
}