package edu.rutgers.ece453.rupool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhu_z on 2017/12/16.
 *
 *
 */

public class AdapterRecyclerViewMainFragment extends RecyclerView.Adapter<AdapterRecyclerViewMainFragment.ViewHolder> {

    private List<PoolActivity> mData;
    private OnItemClickListener mOnItemClickListener;

    AdapterRecyclerViewMainFragment(List<PoolActivity> data, OnItemClickListener onItemClickListener) {
        mData = data;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 可能有问题
                mOnItemClickListener.onItemClick(mData.get((int) v.getTag()));
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoolActivity poolActivity = mData.get(position);
        holder.view.setTag(position);
        holder.textViewDateMonth.setText(DataTimeUtil.getMonth(poolActivity.getDate()));
        holder.textViewDateDay.setText(DataTimeUtil.getDay(poolActivity.getDate()));
        holder.textViewStartPoint.setText(poolActivity.getStartPoint());
        holder.textViewDestination.setText(poolActivity.getDestiName());
        holder.textViewDescription.setText(poolActivity.getDescription());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface OnItemClickListener {
        void onItemClick(PoolActivity poolActivity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textViewDateMonth;
        TextView textViewDateDay;
        TextView textViewStartPoint;
        TextView textViewDestination;
        TextView textViewDescription;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textViewDateMonth = itemView.findViewById(R.id.Date_Month);
            textViewDateDay = itemView.findViewById(R.id.Date_Day);
            textViewStartPoint = itemView.findViewById(R.id.start_point);
            textViewDestination = itemView.findViewById(R.id.item_destination);
            textViewDescription = itemView.findViewById(R.id.description);
        }
    }

}
