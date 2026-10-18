package ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.saw47.mywe.databinding.FragmentAddPageBinding;

import util.InputManager;
import viewmodel.ViewModelMain;

public class AddPageFragment extends Fragment {
    private FragmentAddPageBinding binding;
    private ViewModelMain model;

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

        if (model.tempNote != null) {
            binding.textFieldValue.setText(model.tempNote.getTextNote());
            binding.isSportNoteSwitch.setChecked(model.tempNote.getIsSportNote());
        }

        binding.isSportNoteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.sportFlag = isChecked;
            model.tempText = binding.textFieldValue.getText().toString();
        });

        binding.addReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.tempReminderTextValue = (isChecked) ? binding.textFieldValue.getText().toString() : null;
        });


        binding.textFieldValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                model.tempText = binding.textFieldValue.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        InputManager.hideKeyboard(this.getContext());
        //model.createCalendarEvent();
        super.onStop();
    }
}