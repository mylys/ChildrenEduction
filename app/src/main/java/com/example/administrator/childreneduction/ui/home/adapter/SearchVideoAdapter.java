package com.example.administrator.childreneduction.ui.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.childreneduction.R;
import com.example.administrator.childreneduction.bmob.VedioTable;
import com.example.administrator.childreneduction.ui.listener.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/29.
 */

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.SVViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<VedioTable> mTables;

    private OnClickListener mOnClickListener;

    public SearchVideoAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void refresh(List<VedioTable> tables) {
        mTables = (ArrayList<VedioTable>) tables;
    }

    public void addData(ArrayList<VedioTable> table) {
        mTables.addAll(table);
    }


    public ArrayList<VedioTable> getTables() {
        return mTables;
    }

    @Override
    public SVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.adapter_search_video, parent, false);
        inflate.setOnClickListener(this);
        return new SVViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final SVViewHolder holder, int position) {
        holder.itemView.setTag(position);
        VedioTable vedioTable = mTables.get(position);
        holder.mTvAdaUser.setText("用户：" + vedioTable.getU_name());
        holder.mTvAdaTitle.setText(vedioTable.getV_title());
        holder.mTvAdaLabel.setText("标签："+vedioTable.getV_label());
        holder.mTvAdaTime.setText("时间："+vedioTable.getCreatedAt());

        Glide.with(mContext)
                .load(vedioTable.getU_url())
                .into(holder.mCivAdaSearviHead);

        String url = vedioTable.getV_url();
        if (url != null) {
            Observable.just(url)
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull String s) throws Exception {
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(s, new HashMap<String, String>());
                            Bitmap time = mmr.getFrameAtTime();
                            return time;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(@NonNull Bitmap s) throws Exception {
                            holder.mImgAdapterVideoBackground.setImageBitmap(s);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mTables != null ? mTables.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.setOnClickListener(v, (Integer) v.getTag());
        }
    }

    /**
     * @param onClickListener
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    class SVViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView mCivAdaSearviHead;
        private TextView mTvAdaUser;
        private TextView mTvAdaTitle;
        private TextView mTvAdaLabel;
        private TextView mTvAdaTime;
        private ImageView mImgAdapterVideoBackground;

        public SVViewHolder(View itemView) {
            super(itemView);
            mCivAdaSearviHead = (CircleImageView) itemView.findViewById(R.id.civ_ada_searvi_head);
            mTvAdaUser = (TextView) itemView.findViewById(R.id.tv_ada_user);
            mTvAdaTitle = (TextView) itemView.findViewById(R.id.tv_ada_title);
            mTvAdaLabel = (TextView) itemView.findViewById(R.id.tv_ada_label);
            mTvAdaTime = (TextView) itemView.findViewById(R.id.tv_ada_time);
            mImgAdapterVideoBackground = (ImageView) itemView.findViewById(R.id.img_adapter_video_background);
        }
    }
}
