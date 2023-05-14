package ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.FragmentAddPageBinding;

import viewmodel.ViewModelMain;

public class AddItemFragment extends Fragment {

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
        return binding.getRoot();
    }

    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}