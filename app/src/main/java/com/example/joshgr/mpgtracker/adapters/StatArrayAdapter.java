package com.example.joshgr.mpgtracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.joshgr.mpgtracker.R;
import com.example.joshgr.mpgtracker.data.TripStat;

import java.util.List;

public class StatArrayAdapter extends ArrayAdapter<TripStat> {
    private List<TripStat> mStats;
    private Context mContext;

    public StatArrayAdapter(Context context, int listItemLayoutId, List<TripStat> stats){
        super(context, listItemLayoutId, stats);
        mContext = context;
        mStats = stats;
    }

    @Nullable
    @Override
    public TripStat getItem(int position) {
        return mStats.get(position);
    }

    @Override
    public int getCount() {
        return mStats.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.stat_item, parent, false);

            TextView statName = (TextView) view.findViewById(R.id.stat_name);
            TextView statValue = (TextView) view.findViewById(R.id.stat_value);

            StatViewHolder statViewHolder = new StatViewHolder();
            statViewHolder.StatName = statName;
            statViewHolder.StatValue = statValue;
            view.setTag(statViewHolder);
        }

        StatViewHolder statViewHolder = (StatViewHolder) view.getTag();
        TripStat stat = mStats.get(position);
        statViewHolder.StatName.setText(stat.getStatName());
        statViewHolder.StatValue.setText(stat.getStatValue());
        return view;
    }

}
