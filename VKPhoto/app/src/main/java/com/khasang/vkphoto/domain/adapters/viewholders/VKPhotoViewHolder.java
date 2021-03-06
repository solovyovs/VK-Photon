package com.khasang.vkphoto.domain.adapters.viewholders;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SelectableHolder;
import com.khasang.vkphoto.R;
import com.khasang.vkphoto.presentation.model.Photo;
import com.khasang.vkphoto.presentation.presenter.album.VKAlbumPresenter;
import com.khasang.vkphoto.util.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

@Deprecated
public class VKPhotoViewHolder implements SelectableHolder, View.OnLongClickListener, View.OnClickListener {
    final private ImageView imageView;
    final private CheckBox photoSelectedCheckBox;
    private boolean selectable;
    private Photo photo;
    private VKAlbumPresenter vkAlbumPresenter;
    private MultiSelector multiSelector;
    private int adapterPosition;

    public VKPhotoViewHolder(View view, MultiSelector multiSelector, VKAlbumPresenter vkAlbumPresenter) {
        imageView = (ImageView) view.findViewById(R.id.iv_photo);
        this.multiSelector = multiSelector;
        this.vkAlbumPresenter = vkAlbumPresenter;
        photoSelectedCheckBox = (CheckBox) view.findViewById(R.id.cb_photo_selected);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        photoSelectedCheckBox.setOnClickListener(this);
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public void setSelectable(boolean b) {
        Logger.d("sel" + adapterPosition + " " + b);
        selectable = b;
        if (selectable) {
            photoSelectedCheckBox.setVisibility(View.VISIBLE);
        } else {
            photoSelectedCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isActivated() {
        return photoSelectedCheckBox.isChecked();
    }

    @Override
    public void setActivated(boolean b) {
        photoSelectedCheckBox.setChecked(b);
    }

    @Override
    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    @Override
    public long getItemId() {
        return -1;
    }


    @Override
    public void onClick(View v) {
        Logger.d(String.valueOf(adapterPosition));
        if (multiSelector.isSelectable()) {
            Logger.d(String.valueOf(this.hashCode()));
//            multiSelector.bindHolder(this, adapterPosition, -1);
            multiSelector.tapSelection(this);
            vkAlbumPresenter.checkActionModeFinish(multiSelector);
        } else {
            Logger.d("onClick");

        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!multiSelector.isSelectable()) {
            Logger.d(String.valueOf(this.hashCode()));
            Logger.d(photo.filePath);
            Logger.d(String.valueOf(adapterPosition));
//            multiSelector.bindHolder(this, adapterPosition, -1);
            multiSelector.setSelectable(true);
            multiSelector.setSelected(this, true);
            vkAlbumPresenter.selectPhoto(multiSelector, (AppCompatActivity) v.getContext(), true);
            Logger.d("" + adapterPosition + " " + isSelectable());
            return true;
        }
        return false;
    }

    public void loadPhoto(final Photo photo) {
        Picasso.with(imageView.getContext()).load(photo.getUrlToMaxPhoto()).error(R.drawable.vk_share_send_button_background).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Logger.d(photo.toString() + " loaded successfully");
                VKPhotoViewHolder.this.photo = photo;
            }

            @Override
            public void onError() {
            }
        });
    }
}
