package ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.github.saw47.mywe.databinding.FragmentAddPageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import util.InputManager;
import viewmodel.ViewModelMain;

public class AddPageFragment extends Fragment {

    private FragmentAddPageBinding binding;
    private ViewModelMain model;
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

        InputManager.showKeyboard(this.requireContext(), binding.textFieldValue);  //TODO this is not work

        if (model.tempNote != null) {
            binding.textFieldValue.setText(model.tempNote.getTextNote());
            binding.isSportNoteSwitch.setChecked(model.tempNote.getIsSportNote());
        }

        binding.isSportNoteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.sportFlag = isChecked;
            model.tempText = binding.textFieldValue.getText().toString();
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
}