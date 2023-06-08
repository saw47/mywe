package ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.ActivityMainBinding;


import viewmodel.ViewModelMain;

public class MainActivity extends AppCompatActivity {

    private int state;

    private final static int MAIN_FRAGMENT = 2;
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
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d("NAV", "onCreate savedInstanceState == null");
            state = MAIN_FRAGMENT;
            showMainFragmentAttr();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.host_fragment, MainFragment.class, null)
                    .commit();
        }

        binding.fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (state) {
                    case MAIN_FRAGMENT:
                        Log.d("NAV", "onClick case MAIN_FRAGMENT");
                        state = ADD_ITEM_FRAGMENT;
                        showAddPageFragmentAttr();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.host_fragment, AddItemFragment.class, null)
                                .addToBackStack(STACK)
                                .commit();
                        break;
                    case ADD_ITEM_FRAGMENT:
                        Log.d("NAV", "onClick case ADD_ITEM_FRAGMENT");
                        state = MAIN_FRAGMENT;
                        showMainFragmentAttr();
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.host_fragment, MainFragment.class, null)
                                .addToBackStack(STACK)
                                .commit();
                        break;
                }
            }
        });
        setContentView(binding.getRoot());
    }

    public void showAddPageFragmentAttr(){
        binding.fab.setImageResource(R.drawable.ic_baseline_done_outline_24);
        binding.menuButton.hide();
        binding.tabsFrameLayout.setVisibility(View.GONE);
        binding.typeSomethingTextView.setVisibility(View.VISIBLE);
    }
    public void showMainFragmentAttr(){
        binding.fab.setImageResource(R.drawable.ic_baseline_add_24);
        binding.menuButton.show();
        binding.typeSomethingTextView.setVisibility(View.GONE);
        binding.tabsFrameLayout.setVisibility(View.VISIBLE);
    }
}