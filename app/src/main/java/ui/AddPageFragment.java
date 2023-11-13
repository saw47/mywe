package ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.saw47.mywe.databinding.FragmentAddPageBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import java.util.Calendar;
import java.util.Date;
import util.InputManager;
import viewmodel.ViewModelMain;

public class AddPageFragment extends Fragment {

    private FragmentAddPageBinding binding;
    private ViewModelMain model;
    private MaterialTimePicker timePicker;
    private MaterialDatePicker<Long> datePicker;
    private final String TAG = "MW-APF";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(ViewModelMain.class);
        binding = FragmentAddPageBinding.inflate(inflater, container, false);
        InputManager.showKeyboard(this.requireContext(), binding.textFieldValue);

        model.addReminderEvent.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged (Object o) {
                if (Boolean.TRUE.equals(model.addReminderEvent.getValue()))
                {
                    String title = binding.textFieldValue.getText().toString();
                    datePicker.show(getParentFragmentManager(), "DatePicker");
                    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(selection));
                            timePickerDialog(cal, title);
                        }
                    });

                }
            }
        });

        if (model.tempNote != null) {
            binding.textFieldValue.setText(model.tempNote.getTextNote());
            binding.isSportNoteSwitch.setChecked(model.tempNote.getIsSportNote());
        }

        binding.isSportNoteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.sportFlag = isChecked;
            model.tempText = binding.textFieldValue.getText().toString();
        });

        binding.addReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.addReminderFlag = isChecked;
            pickerInstance(isChecked);
        });


        binding.textFieldValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                model.tempText = binding.textFieldValue.getText().toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        InputManager.hideKeyboard(this.getContext()); //TODO this is not work
        super.onStop();
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

    private void timePickerDialog(Calendar cal, String title) {
        timePicker.show(getParentFragmentManager(), "TimePicker");
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

    private void calendarEvent(Calendar cal, String title) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}