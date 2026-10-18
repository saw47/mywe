package ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.ActivityMainBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;
import java.util.Date;

import viewmodel.ViewModelMain;

public class MainActivity extends AppCompatActivity {

    private final static int MAIN_FRAGMENT_DEL_ITEM = 0;
    private final static int MAIN_FRAGMENT_GENERAL = 2;
    private final static int ADD_ITEM_FRgit AGMENT = 4;
    private final static String nameVariableKey = "FRAGMENT_STATE";
    private final static String STACK = "MainStack";
    private final String TAG = "MW-MA";
    private int state;
    private ActivityMainBinding binding;
    private ViewModelMain model;
    private FragmentManager fragmentManager;
    private MaterialTimePicker timePicker;
    private MaterialDatePicker<Long> datePicker;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(nameVariableKey, state);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        state = savedInstanceState.getInt(nameVariableKey);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(this).get(ViewModelMain.class);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            state = MAIN_FRAGMENT_GENERAL;
            showMainFragmentAttr();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.host_fragment, MainFragment.class, null)
                    .commit();
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fabOnClickHandler();
            }
        });

        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuButtonClickHandler();
            }
        });


        model.frameClickedSingleLiveEvent.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (state != MAIN_FRAGMENT_DEL_ITEM) {
                    fabOnClickHandler();
                }
            }
        });

        model.noteIsSelectedEvent.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (Boolean.TRUE.equals(model.noteIsSelectedEvent.getValue())) {
                    state = MAIN_FRAGMENT_DEL_ITEM;
                    showDeleteItemFragmentAttr();
                } else {
                    state = MAIN_FRAGMENT_GENERAL;
                    showMainFragmentAttr();
                }
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                model.setTabPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //TODO not implemented
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //TODO not implemented
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        switch (state) {
            case MAIN_FRAGMENT_GENERAL:
                model.clearTempEntity();
                super.onBackPressed();
                this.finish();
                break;
            case ADD_ITEM_FRAGMENT:
                model.saveNoteAction();
                calendarEventHandler();
                state = MAIN_FRAGMENT_GENERAL;
                fragmentManager.popBackStack();
                showMainFragmentAttr();
                break;
            case MAIN_FRAGMENT_DEL_ITEM:
                model.clearTempEntity();
                state = MAIN_FRAGMENT_GENERAL;
                showMainFragmentAttr();
                break;
        }
    }

    private void calendarEventHandler() {
        if (model.tempReminderTextValue != null && !model.tempReminderTextValue.isEmpty()) {
            pickerInstance(true);
            String title = model.tempReminderTextValue;
            datePicker.show(fragmentManager, "DatePicker");
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(selection));
                    timePickerDialog(cal, title);
                }
            });
        }
        model.clearReminderBuffer();
    }

    public void menuButtonClickHandler() {
        switch (state) {
            case MAIN_FRAGMENT_GENERAL:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case ADD_ITEM_FRAGMENT:
                model.saveNoteAction();
                calendarEventHandler();
                state = MAIN_FRAGMENT_GENERAL;
                showMainFragmentAttr();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.host_fragment, MainFragment.class, null)
                        .commit();
                break;
        }
    }

    public void fabOnClickHandler() {
        switch (state) {
            case MAIN_FRAGMENT_GENERAL:
                state = ADD_ITEM_FRAGMENT;
                showAddPageFragmentAttr();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.host_fragment, AddPageFragment.class, null)
                        .addToBackStack(STACK)
                        .commit();
                break;
            case MAIN_FRAGMENT_DEL_ITEM:
                model.deleteNotes();
                state = MAIN_FRAGMENT_GENERAL;
                showMainFragmentAttr();
                model.clearTempEntity();
                break;
        }
    }

    private void timePickerDialog(Calendar cal, String title) {
        timePicker.show(fragmentManager, "TimePicker");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.HOUR, timePicker.getHour());
                cal.set(Calendar.MINUTE, timePicker.getMinute());
                calendarEvent(cal, title);
                pickerInstance(false);
            }
        });
    }

    private void pickerInstance(Boolean isChecked) {
        if (isChecked) {
            datePicker = MaterialDatePicker.Builder
                    .datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            timePicker = new MaterialTimePicker.Builder().build();
        } else {
            datePicker = null;
            timePicker = null;
        }
    }

    private void calendarEvent(Calendar cal, String title) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
        intent.putExtra("title", title);
        startActivity(intent);
    }


    public void showAddPageFragmentAttr() {
        binding.fab.hide();
        binding.menuButton.setImageResource(R.drawable.ic_baseline_done_outline_24);
        binding.tabsFrameLayout.setVisibility(View.GONE);
    }

    public void showMainFragmentAttr() {
        binding.fab.show();
        binding.fab.setImageResource(R.drawable.ic_baseline_add_24);
        binding.menuButton.show();
        binding.menuButton.setImageResource(R.drawable.ic_baseline_menu_24);
        binding.tabsFrameLayout.setVisibility(View.VISIBLE);
    }

    public void showDeleteItemFragmentAttr() {
        binding.fab.show();
        binding.fab.setImageResource(R.drawable.ic_baseline_delete_24);
        binding.menuButton.hide();
        binding.tabsFrameLayout.setVisibility(View.GONE);
    }
}