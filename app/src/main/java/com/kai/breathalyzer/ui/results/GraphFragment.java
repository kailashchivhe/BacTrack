package com.kai.breathalyzer.ui.results;

import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.kai.breathalyzer.R;
import com.kai.breathalyzer.databinding.FragmentGraphBinding;
import com.kai.breathalyzer.ui.test.TestViewModel;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    FragmentGraphBinding binding;
    GraphViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //TODO make list fetch call and init observer
    }

    private void setData(){
        binding.progressCircular.setVisibility(View.GONE);
        binding.anyChart.setVisibility( View.VISIBLE );
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("10/15/2022", 0.4));
        data.add(new ValueDataEntry("10/16/2022", 0.1));
        data.add(new ValueDataEntry("10/17/2022", 0.16));
        data.add(new ValueDataEntry("10/18/2022", 0.5));
        data.add(new ValueDataEntry("10/19/2022", 0.12));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

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