package in.til.yogeshkumar.timesanimation.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import in.til.yogeshkumar.timesanimation.R;
import in.til.yogeshkumar.timesanimation.Utils.Utils;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Activity context;

    public RecyclerViewAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        updateViewHolderHeight(holder, position);
    }

    private void updateViewHolderHeight(@NonNull ViewHolder holder, int position) {
        int height = getCardHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        layoutParams.height = height;
        holder.itemView.setLayoutParams(layoutParams);
        holder.cardView.setCardElevation(Utils.getDimensionFromDp(4, context));
        if (position == 0) {
            holder.guidelineStart.setGuidelinePercent(0.0f);
            holder.guidelineEnd.setGuidelinePercent(1.0f);
        }

    }

    private int getCardHeight() {
        return (Utils.getScreenHeight(context) * 65) / 100;
    }

    @Override
    public int getItemCount() {
        return 10; //I should not do this
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ConstraintLayout constraintLayout;
        Guideline guidelineStart;
        Guideline guidelineEnd;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            constraintLayout = itemView.findViewById(R.id.card_view_container);
            guidelineStart = constraintLayout.findViewById(R.id.guidelineStart);
            guidelineEnd = constraintLayout.findViewById(R.id.guidelineEnd);
        }
    }

}
