package in.til.yogeshkumar.timesanimation.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.til.yogeshkumar.timesanimation.R;

/**
 * Created by Yogesh Kumar.
 */
public class FullScreenAdapter extends RecyclerViewAdapter {

    public FullScreenAdapter(Activity context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_fill, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
