package com.kai.breathalyzer.ui.results;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.kai.breathalyzer.databinding.FragmentGraphBinding;
import com.kai.breathalyzer.model.UserHistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphFragment extends Fragment {

    FragmentGraphBinding binding;
    GraphViewModel viewModel;
    SharedPreferences sharedPreferences;
    String jwtToken;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE);
        jwtToken = sharedPreferences.getString("jwtToken", "");
        id = sharedPreferences.getString("id", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGraphBinding.inflate( inflater,container,false );
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(GraphViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getData(jwtToken);
        viewModel.getUserHistoryMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserHistory>>() {
            @Override
            public void onChanged(@Nullable List<UserHistory> userHistories) {
                if( userHistories != null && userHistories.size() > 0 ) {
                    setData(userHistories);
                }
                else{
                    Toast.makeText( getContext(), "No Data Available", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    private void setData(List<UserHistory> userHistories ) {
        binding.progressCircular.setVisibility(View.GONE);
        binding.anyChart.setVisibility( View.VISIBLE );
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for(UserHistory userHistory : userHistories ){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = inputFormat.parse(userHistory.getDate());
                String formattedDate = outputFormat.format(date);
                data.add(new ValueDataEntry(formattedDate, Double.parseDouble(userHistory.getMeasurements())));
            }
            catch (ParseException exception){
                exception.printStackTrace();
            }
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Alcohol Test Results");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("Result");

        binding.anyChart.setChart(cartesian);
    }
}