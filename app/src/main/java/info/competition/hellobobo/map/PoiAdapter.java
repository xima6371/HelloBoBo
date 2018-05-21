package info.competition.hellobobo.map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.competition.hellobobo.R;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.ViewHolder> {
    private List<PoiTip> mPois = new ArrayList<>();
    private Context mContext;

    public PoiAdapter(Context context) {
        mContext = context;

    }

    public void addResultTips(List<PoiTip> pois) {
        mPois.clear();
        mPois.addAll(pois);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PoiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.frag_map_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdapter.ViewHolder holder, int position) {
        PoiTip poiTip = mPois.get(position);
        holder.tvDistance.setText(poiTip.getDistance());
        holder.tvPoiName.setText(poiTip.getPoiName());
        holder.tvAddress.setText(poiTip.getAddress());
    }

    @Override
    public int getItemCount() {
        return mPois.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDistance;
        private TextView tvPoiName;
        private TextView tvAddress;


        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPoiName = itemView.findViewById(R.id.tv_poi_name);
            tvDistance = itemView.findViewById(R.id.tv_distance);
        }
    }
}
