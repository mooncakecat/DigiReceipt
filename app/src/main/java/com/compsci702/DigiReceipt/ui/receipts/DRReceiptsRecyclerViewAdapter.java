package com.compsci702.DigiReceipt.ui.receipts;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.ui.view.DRSquareImageView;
import com.compsci702.DigiReceipt.util.DRImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} for displaying receipts
 */
class DRReceiptsRecyclerViewAdapter extends RecyclerView.Adapter<DRReceiptsRecyclerViewAdapter.ViewHolder> {

    @NonNull private final List<DRReceipt> mReceiptsList;
    @NonNull private final Context mContext;
    @NonNull private final AdapterListener mAdapterListener;

    interface AdapterListener {
        void onReceiptSelected();
    }

    DRReceiptsRecyclerViewAdapter(@NonNull Context context, @NonNull List<DRReceipt> receiptsList,
                                  @NonNull AdapterListener adapterListener) {
        mContext = context;
        mReceiptsList = receiptsList;
        mAdapterListener = adapterListener;
    }

    @Override public DRReceiptsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_receipt_thumbnail, parent, false);
        return new DRReceiptsRecyclerViewAdapter.ViewHolder(mContext, view, mAdapterListener);
    }

    @Override public void onBindViewHolder(DRReceiptsRecyclerViewAdapter.ViewHolder holder, int position) {
        final DRReceipt receipt = mReceiptsList.get(position);
        holder.updateView(receipt);
    }

    @Override public int getItemCount() {
        return mReceiptsList.size();
    }


	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     * view holder
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AdapterListener mAdapterListener;
        Context mContext;

        @BindView(R.id.receipt_thumbnail_imageview) DRSquareImageView mThumbnailView;

        ViewHolder(@NonNull Context context, @NonNull View view, @NonNull AdapterListener adapterListener) {
            super(view);
            mContext = context;
            mAdapterListener = adapterListener;

            ButterKnife.bind(this, view);
        }

        public void updateView(@NonNull DRReceipt receipt) {
          String thumbnailURL = DRImageUtil.getImageUrl(receipt.getFilename());
          Glide.with(mContext).load(Uri.parse(thumbnailURL)).asBitmap().centerCrop().into(mThumbnailView);
        }

        @Override public void onClick(View view) {
            mAdapterListener.onReceiptSelected();
        }
    }
}
