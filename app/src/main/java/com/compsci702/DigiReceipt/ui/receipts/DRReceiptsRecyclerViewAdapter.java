package com.compsci702.DigiReceipt.ui.receipts;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class DRReceiptsRecyclerViewAdapter extends RecyclerView.Adapter<DRReceiptsRecyclerViewAdapter.ViewHolder> {

    @NonNull private final List<DRReceipt> mReceiptsList;
    @NonNull private final Context mContext;
    @NonNull private final AdapterListener mAdapterListener;

	public interface AdapterListener {
        void onReceiptSelected(String receiptFilename);
    }

    public DRReceiptsRecyclerViewAdapter(@NonNull Context context, @NonNull List<DRReceipt> receiptsList,
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

        @NonNull AdapterListener mAdapterListener;
        @NonNull Context mContext;
        @Nullable DRReceipt mReceipt;

        @BindView(R.id.receipt_thumbnail_imageview) DRSquareImageView mThumbnailView;

        ViewHolder(@NonNull Context context, @NonNull View view, @NonNull AdapterListener adapterListener) {
            super(view);
            mContext = context;
            mAdapterListener = adapterListener;

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void updateView(@NonNull DRReceipt receipt) {
          	mReceipt = receipt;
			String thumbnailUrl = receipt.getFilename();
          	Glide.with(mContext).load(Uri.parse(thumbnailUrl)).asBitmap().centerCrop().into(mThumbnailView);
        }

        @Override public void onClick(View view) {
          if (mReceipt == null) throw new IllegalStateException("onClick() called on null image");
            mAdapterListener.onReceiptSelected(mReceipt.getFilename());
        }
    }
}
