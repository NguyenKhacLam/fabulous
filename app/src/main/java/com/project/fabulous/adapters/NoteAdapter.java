package com.project.fabulous.adapters;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.fabulous.models.Note;
import com.project.fabulous.R;


import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.HolderNote> {

    private LayoutInflater layoutInflater;
    private ArrayList<Note> data;
    private ArrayList<Note> dataFull;
    private OnClickNoteListener listener;

    public NoteAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setListener(OnClickNoteListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<Note> data) {
        this.data = data;
        this.dataFull = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HolderNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_note, parent, false);
        return new HolderNote(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNote holder, int position) {
        final Note note = data.get(position);
        holder.bindView(note);
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickNote(note, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onClickLongNote(note);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public Filter getFilter(){ return dataFilter; };

    public Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filterUsers = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filterUsers.addAll(dataFull);
            }else {
                String filterPattern = constraint.toString().trim();

                for (Note note : dataFull) {
                    if (note.getTitle().toLowerCase().contains(filterPattern)){
                        filterUsers.add(note);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterUsers;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class HolderNote extends RecyclerView.ViewHolder {
        private TextView title, subtitle, date;
        private LinearLayout layoutNote;
        private ImageView imageView;

        public HolderNote(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubTitle);
            date = itemView.findViewById(R.id.textDatetime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageView = itemView.findViewById(R.id.imgNoteCard);
        }

        private void bindView(Note note) {
            title.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()){
                subtitle.setVisibility(View.GONE);
            }else {
                subtitle.setText(note.getSubtitle());
            }
            date.setText(note.getDatetime());

            if (note.getImageUrl() != null && !note.getImageUrl().isEmpty()){
                imageView.setVisibility(View.VISIBLE);
                Glide.with(imageView).load(note.getImageUrl()).into(imageView);
            }

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
                if (note.getColor().equals("#000000")){
                    title.setTextColor(Color.WHITE);
                    subtitle.setTextColor(Color.WHITE);
                    date.setTextColor(Color.WHITE);
                }
            }else {
                gradientDrawable.setColor(Color.parseColor("#ebebeb"));
            }
        }
    }

    public interface OnClickNoteListener {
        void onClickNote(Note note, int position);

        void onClickLongNote(Note note);
    }

}
