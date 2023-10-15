package ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.saw47.mywe.databinding.FragmentMainBinding;

import java.util.List;

import rw.adapter.NoteAdapter;
import object.Note;
import rw.helper.NoteItemTouchHelperCallback;
import util.TabPositionState;
import viewmodel.ViewModelMain;

public class MainFragment extends Fragment {

    private final String TAG = "MW-MF";

    private FragmentMainBinding binding;
    private ViewModelMain model;
    private NoteAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = new ViewModelProvider(requireActivity()).get(ViewModelMain.class);
        binding = FragmentMainBinding.inflate(inflater, container, false);

        mAdapter = new NoteAdapter(this.getContext(), model.data, model);
        binding.mainRv.setItemAnimator(null);
        binding.mainRv.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new NoteItemTouchHelperCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.mainRv);

        model.data.observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.submitList(notes);
                binding.mainRv.setAdapter(mAdapter);
            }
        });

        model.tabStateChangeEvent.observe(getViewLifecycleOwner(), new Observer<TabPositionState>() {
            @Override
            public void onChanged(TabPositionState tabPositionState) {
                mAdapter.submitList(null);
                binding.mainRv.setAdapter(mAdapter);
            }
        });


        model.noteIsSelectedEvent.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged (Object o) {
                if (Boolean.FALSE.equals(model.noteIsSelectedEvent.getValue()))
                {
                    binding.mainRv.setAdapter(mAdapter);
                    mAdapter.submitList(null);
                    mAdapter.submitList(model.data.getValue());
                }
            }
        });

        model.noteIsDeletedEvent.observe(getViewLifecycleOwner(), new Observer() {
            @Override
            public void onChanged (Object o) {
                if (Boolean.TRUE.equals(model.noteIsDeletedEvent.getValue()))
                {
                    mAdapter.submitList(null);
                    binding.mainRv.setAdapter(mAdapter);
                    model.clearDeleteNotesLiveEvent();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}