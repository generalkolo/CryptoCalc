package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semanientreprise.steemitcalc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Custom.CustomItemClickListener;
import Model.topFiveCoins;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GeneralKolo on 2018/03/21.
 */

public class topFiveCoinsAdapter extends RecyclerView.Adapter<topFiveCoinsAdapter.topFiveCoinsViewHolder> {

    private List<topFiveCoins> arrayList;
    private Context context;
    CustomItemClickListener listener;

    public topFiveCoinsAdapter(List<topFiveCoins> arrayList, Context context, CustomItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public topFiveCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topfivecoins_layout, null);
        final topFiveCoinsViewHolder topFiveCoinsViewHolder = new topFiveCoinsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, topFiveCoinsViewHolder.getAdapterPosition());
            }
        });

        return topFiveCoinsViewHolder;
    }

    @Override
    public void onBindViewHolder(topFiveCoinsViewHolder holder, int position) {
        holder.coinName.setText(arrayList.get(position).getName());
        if(!arrayList.get(position).getImageUrl().isEmpty()){
            Picasso.with(context).load(arrayList.get(position).getImageUrl())
                    .resize(200,200)
                    .into(holder.coinImage);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class topFiveCoinsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.coin_image) ImageView coinImage;
        @BindView(R.id.coin_name) TextView coinName;

        public topFiveCoinsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
