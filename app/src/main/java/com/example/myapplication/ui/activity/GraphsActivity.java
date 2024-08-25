package com.example.myapplication.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.DailyUsage;
import com.example.myapplication.models.UserCountPerMonth;
import com.example.myapplication.utils.DateUtils;
import com.example.myapplication.viewmodel.UserSessionViewModel;
import com.example.myapplication.viewmodel.UserViewModel;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphsActivity extends AppCompatActivity {
    private BarChart newUsersPerMonthBarchart,dailyUsageBarchart;
    private UserViewModel usersViewModel;
    private UserSessionViewModel userSessionViewModel;
    Spinner weekSelector,monthSelector;
    private TextView tvNoResultsFound,yAxisLabelDailyUsageGraph,yAxisLabelNewUsersGraph;
    ImageView searchOffIcon;
    LinearLayout noResultsContainer;
    private LinearLayout noUsersContainer;
    private TextView tvNoUsersFound;
    private ImageView ivSearchOffIconUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graphs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_graphs), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setActionBar();
        setupViewModel();
        setupMonthSelector();
        initWeekSelector();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupMonthSelector() {

        List<String> monthOptions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Date month = DateUtils.getDateMonthsAgo(i);
            String formattedMonth = DateUtils.formatMonth(month);
            monthOptions.add(formattedMonth);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, monthOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSelector.setAdapter(adapter);

        monthSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                displayUserCountForMonth(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                displayUserCountForMonth(0);
            }
        });
    }

    private void initWeekSelector() {

        List<String> weekOptions = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            Date[] weekRange = DateUtils.getWeekRange(i);
            weekOptions.add(DateUtils.formatWeekRange(weekRange[0], weekRange[1]));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, weekOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelector.setAdapter(adapter);

        weekSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayUsageForWeek(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                displayUsageForWeek(0);
            }
        });
    }


    private void displayUserCountForMonth(int monthsAgo) {
        Date monthStart = DateUtils.getMonthStartDate(monthsAgo);
        Date monthEnd = DateUtils.getMonthEndDate(monthsAgo);

        long startDate = monthStart.getTime();
        long endDate = monthEnd.getTime();

        usersViewModel.getUserCountPerMonth(startDate, endDate).observe(this, userCounts -> {
            if (userCounts == null || userCounts.isEmpty()) {
                newUsersPerMonthBarchart.clearChart();
                showNoUsers();
            } else {
                hideNoUsers();
                populateBarChartUsersPerMonth(userCounts);
            }
        });
    }

    private void initViews(){
        monthSelector = findViewById(R.id.monthSelector);
        weekSelector = findViewById(R.id.weekSelector);
        newUsersPerMonthBarchart = findViewById(R.id.newUsersPerMonthBarchart);
        dailyUsageBarchart = findViewById(R.id.dailyUsageBarchart);
        weekSelector = findViewById(R.id.weekSelector);
        tvNoResultsFound = findViewById(R.id.tvNoResultsFound);
        searchOffIcon = findViewById(R.id.ivSearchOffIcon);
        noResultsContainer = findViewById(R.id.noResultsContainer);
        yAxisLabelDailyUsageGraph = findViewById(R.id.yAxisLabelDailyUsageGraph);
        noUsersContainer = findViewById(R.id.noUsersContainer);
        tvNoUsersFound = findViewById(R.id.tvNoUsersFound);
        ivSearchOffIconUsers = findViewById(R.id.ivSearchOffIconUsers);
        yAxisLabelNewUsersGraph = findViewById(R.id.yAxisLabelNewUsersGraph);
    }


    private void setupViewModel() {
        usersViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);
    }

    private void showNoUsers() {
        noUsersContainer.setVisibility(View.VISIBLE);
        ivSearchOffIconUsers.setVisibility(View.VISIBLE);
        tvNoUsersFound.setVisibility(View.VISIBLE);
        newUsersPerMonthBarchart.setVisibility(View.GONE);
        yAxisLabelNewUsersGraph.setVisibility(View.GONE);

    }

    private void hideNoUsers() {
        noUsersContainer.setVisibility(View.GONE);
        ivSearchOffIconUsers.setVisibility(View.GONE);
        tvNoUsersFound.setVisibility(View.GONE);
        newUsersPerMonthBarchart.setVisibility(View.VISIBLE);
        yAxisLabelNewUsersGraph.setVisibility(View.VISIBLE);
    }

    private void showNoResults(){
        noResultsContainer.setVisibility(View.VISIBLE);
        searchOffIcon.setVisibility(View.VISIBLE);
        tvNoResultsFound.setVisibility(View.VISIBLE);
        dailyUsageBarchart.setVisibility(View.GONE);
        yAxisLabelDailyUsageGraph.setVisibility(View.GONE);
    }

    private void hideNoResults(){
        noResultsContainer.setVisibility(View.GONE);
        searchOffIcon.setVisibility(View.GONE);
        tvNoResultsFound.setVisibility(View.GONE);
        dailyUsageBarchart.setVisibility(View.VISIBLE);
        yAxisLabelDailyUsageGraph.setVisibility(View.VISIBLE);
    }

    private void displayUsageForWeek(int weeksBefore){
        Date[] weekRange = DateUtils.getWeekRange(weeksBefore);
        long startDate = weekRange[0].getTime();
        long endDate = weekRange[1].getTime();

        userSessionViewModel.getDailyUsageForWeek(startDate, endDate).observe(this, dailyUsages -> {

            if (dailyUsages.isEmpty()) {
                dailyUsageBarchart.clearChart();
                showNoResults();
            } else {
                hideNoResults();
                populateBarChartDailyUsage(dailyUsages);
            }
        });
    }

    private void populateBarChartDailyUsage(List<DailyUsage> dailyUsages){
        dailyUsageBarchart.clearChart();

        for (DailyUsage usage : dailyUsages) {

            float minutesSpent = (float) usage.getTotalDuration() / 1000 / 60;

            dailyUsageBarchart.addBar(new BarModel(usage.getSessionDate(), minutesSpent, Constants.BAR_COLOR_USAGE ));
        }

        dailyUsageBarchart.startAnimation();
    }

    private void populateBarChartUsersPerMonth(List<UserCountPerMonth> userCounts) {
        if (userCounts == null || userCounts.isEmpty()) {
            newUsersPerMonthBarchart.clearChart();
            showNoUsers();
            return;
        }

        hideNoUsers();

        for (UserCountPerMonth userCount : userCounts) {

            String monthLabel = DateUtils.formatMonth(userCount.getMonth());

            BarModel barModel = new BarModel(monthLabel, userCount.getUserCount(), Constants.BAR_COLOR_USERS);

            newUsersPerMonthBarchart.addBar(barModel);
        }

        newUsersPerMonthBarchart.startAnimation();
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setTitle(Constants.EMPTY_STRING);


            LinearLayout customActionBarView = createCustomActionBarView();

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.END | Gravity.CENTER_VERTICAL
            );

            actionBar.setCustomView(customActionBarView, params);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    private LinearLayout createCustomActionBarView() {

        TextView textView = new TextView(this);
        textView.setText(R.string.graphs);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setGravity(Gravity.END);

        ImageView imageView = getImageView();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        return linearLayout;
    }

    private @NonNull ImageView getImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.baseline_people_24);


        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.setMargins(16, 0, 0, 0);

        imageView.setLayoutParams(imageParams);
        return imageView;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}