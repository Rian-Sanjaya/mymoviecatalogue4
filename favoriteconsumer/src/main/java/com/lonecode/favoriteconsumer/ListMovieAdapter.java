package com.lonecode.favoriteconsumer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListViewHolder> {
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public ListMovieAdapter(ArrayList<Movie> list) {
        this.listMovie = list;
    }

    public ListMovieAdapter(Context context, OnItemClickCallback callback) {
        this.context = context;
        this.onItemClickCallback = callback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }

    public void setData(ArrayList<Movie> movies) {
        listMovie.clear();
        listMovie.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_movie, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(listMovie.get(position));
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDescription;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.img_movie_photo);
            tvName = itemView.findViewById(R.id.txt_movie_name);
            tvDescription = itemView.findViewById(R.id.txt_movie_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickCallback.onItemClicked(listMovie.get(getAdapterPosition()));
                }
            });
        }

        void bind(Movie movie) {
            tvName.setText(movie.getName());
            tvDescription.setText(movie.getDescription());
            Glide.with(context).load(movie.getPosterPath()).into(imgPhoto);
        }
    }
}
