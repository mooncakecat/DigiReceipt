package com.compsci702.DigiReceipt.ui.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.compsci702.DigiReceipt.R;
import com.compsci702.DigiReceipt.ui.model.DRReceipt;
import com.compsci702.DigiReceipt.util.DRImageUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * PagerAdapter for displaying an image in {@link DRImageFragment}
 */
class DRImagePagerAdapter extends PagerAdapter {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * AdapterListener
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	interface AdapterListener {
	}

  	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 	 * internal fields
 	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@NonNull private final List<? extends DRReceipt> mReceiptsList;

	@NonNull private final Context mContext;

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
   * constructor
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	DRImagePagerAdapter(@NonNull Context context, @NonNull List<? extends DRReceipt> receipts) {
		mContext = context;
		mReceiptsList = receipts;
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	* base class overrides
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	@Override public int getCount() {
		return mReceiptsList.size();
	}

	@Override public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override public Object instantiateItem(final ViewGroup container, int position) {
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View imageLayout = layoutInflater.inflate(R.layout.row_image, container, false);

		ButterKnife.bind(this, imageLayout);

		final DRReceipt receipt = mReceiptsList.get(position);

		ViewHolder viewHolder = new ViewHolder(imageLayout, container);
		viewHolder.updateView(receipt);

		return imageLayout;
	}

	@Override public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * view holder
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	static class ViewHolder extends RecyclerView.ViewHolder {

		@NonNull private final View mLayout;

		@BindView(R.id.imageview) ImageView mImageView;

		ViewHolder(@NonNull View imageLayout, @NonNull ViewGroup container) {
			super(imageLayout);
			ButterKnife.bind(this, imageLayout);
			new PhotoViewAttacher(mImageView);
			container.addView(imageLayout);
			mLayout = imageLayout;
		}

		void updateView(DRReceipt receipt) {
			String imageURL = receipt.getFilename();
			Glide.with(mLayout.getContext()).load(Uri.parse(imageURL)).fitCenter().into(mImageView);
		}
	}
}
