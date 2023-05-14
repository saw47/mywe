package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.saw47.mywe.R;
import com.github.saw47.mywe.databinding.FragmentAddPageBinding;
import com.github.saw47.mywe.databinding.FragmentMainBinding;
import com.github.saw47.mywe.databinding.FragmentMainItemBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import listener.ItemClickListener;
import object.Note;
import ui.MainItemFragment;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<Note> data;
    private ItemClickListener clickListener;
    private final FragmentMainItemBinding binding;

    NoteAdapter(Context context, List<Note> data) {
        this.data = data;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.binding = FragmentMainItemBinding.inflate(inflater);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Note note;

        public ViewHolder(View view) {
            super(view);
        }

        public void bind(Note note, FragmentMainItemBinding binding) {
            this.note = note;
            binding.text.setText(note.getTextNote());
            binding.deadLine.setText(String.valueOf(note.getDeadLine()));
            /*
            private final int number;
            private final String textNote;
            private final Long deadLine;
            private final int delayReminder;
            private final boolean important;
             */
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new ViewHolder(binding.getRoot());
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Note note = data.get(position);
        viewHolder.bind(note, binding);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }


}
