package ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;


import java.util.List;

import db.NoteEntity;
import util.TabPositionState;
import viewmodel.ViewModelMain;

public class MainActivity extends AppCompatActivity {

    private int state;

    private final String TAG = "MW-MA";

    private final static int MAIN_FRAGMENT_DEL_ITEM = 0;
    private final static int MAIN_FRAGMENT_GENERAL = 2;
    private final static int ADD_ITEM_FRAGMENT = 4;
    private final static String nameVariableKey = "FRAGMENT_STATE";
    private final static String STACK = "MainStack";

    private ActivityMainBinding binding;
    private ViewModelMain model;
    private FragmentManager fragmentManager;

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
        model.setFirstTimeTabPosition();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate savedInstanceState == null");
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
                model.tempClick();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "¯\\_(ツ)_/¯  Yet not implemented", Toast.LENGTH_SHORT);
                toast.show();
                //TODO not implemented
            }
        });

        model.isDataChanged.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> entities) {
                model.refreshDataList();
            }
        });

        model.frameClickedSingleLiveEvent.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if (state != MAIN_FRAGMENT_DEL_ITEM)
                {
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
                model.clearTempEntity();
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

    public void fabOnClickHandler()
    {
        switch (state) {
            case MAIN_FRAGMENT_GENERAL:
                Log.d(TAG, "fabOnClickHandler case MAIN_FRAGMENT");
                state = ADD_ITEM_FRAGMENT;
                showAddPageFragmentAttr();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.host_fragment, AddPageFragment.class, null)
                        .addToBackStack(STACK)
                        .commit();
                break;
            case ADD_ITEM_FRAGMENT:
                Log.d(TAG, "fabOnClickHandler case ADD_ITEM_FRAGMENT");
                model.saveNoteOnClick();
                state = MAIN_FRAGMENT_GENERAL;
                showMainFragmentAttr();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.host_fragment, MainFragment.class, null)
                        .commit();
                break;
            case MAIN_FRAGMENT_DEL_ITEM:
                Log.d(TAG, "fabOnClickHandler case MAIN_FRAGMENT_DEL_ITEM");
                model.deleteNotes();
                state = MAIN_FRAGMENT_GENERAL;
                showMainFragmentAttr();
                model.clearTempEntity();
                break;
        }
    }


    public void showAddPageFragmentAttr(){
        binding.fab.setImageResource(R.drawable.ic_baseline_done_outline_24);
        binding.menuButton.hide();
        binding.tabsFrameLayout.setVisibility(View.GONE);
        //binding.typeSomethingTextView.setVisibility(View.VISIBLE);
    }
    public void showMainFragmentAttr(){
        binding.fab.setImageResource(R.drawable.ic_baseline_add_24);
        binding.menuButton.show();
        //binding.typeSomethingTextView.setVisibility(View.GONE);
        binding.tabsFrameLayout.setVisibility(View.VISIBLE);
    }

    public void showDeleteItemFragmentAttr(){
        binding.fab.setImageResource(R.drawable.ic_baseline_delete_24);
        binding.menuButton.hide();
        binding.tabsFrameLayout.setVisibility(View.GONE);
    }
}