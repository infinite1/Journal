package com.example.journal3;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder> {

    private Cursor mMediaStoreCursor;
    private final Activity mAcitivty;
    private OnClickThumbListener mOnClickThumbListener;

    public interface OnClickThumbListener {
        void  onClickImage (Uri imageUri);
        void  onClickVideo (Uri videoUri);

    }

    public MediaStoreAdapter(Activity mAcitivty)
    {
        this.mAcitivty = mAcitivty;
        this.mOnClickThumbListener=(OnClickThumbListener)mAcitivty;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_image_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Bitmap bitmap = getBitmapFromMediaStore(position);
//        if (bitmap != null)
//        {
//            holder.getmImageView().setImageBitmap(bitmap);
//        }
        Glide.with(mAcitivty).load(getUriFromMediaStore(position))
                .centerCrop()
                .override(96,96)
                .into(holder.getmImageView());

    }

    @Override
    public int getItemCount() {
        return (mMediaStoreCursor == null) ? 0 : mMediaStoreCursor.getCount();
    }







    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mImageView;

        public ViewHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.mediastoreImageView);
            mImageView.setOnClickListener(this);
        }

        public ImageView getmImageView() {
            return mImageView;
        }

        @Override
        public void onClick(View v) {
            getOnClickUri(getAdapterPosition());
        }
    }






    private Cursor swapCursor(Cursor cursor) {
        if (mMediaStoreCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mMediaStoreCursor;
        this.mMediaStoreCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor oldCusor = swapCursor(cursor);
        if (oldCusor != null) {
            oldCusor.close();
        }
    }

    private Bitmap getBitmapFromMediaStore(int position) {
        int idIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

        mMediaStoreCursor.moveToPosition(position);
        switch(mMediaStoreCursor.getInt(mediaTypeIndex)) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                return MediaStore.Images.Thumbnails.getThumbnail(
                        mAcitivty.getContentResolver(), mMediaStoreCursor.getLong(idIndex),
                        MediaStore.Images.Thumbnails.MICRO_KIND, null);
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                return MediaStore.Video.Thumbnails.getThumbnail(
                        mAcitivty.getContentResolver(), mMediaStoreCursor.getLong(idIndex),
                        MediaStore.Video.Thumbnails.MICRO_KIND, null);

                default:
                return null;

        }
    }

    private Uri getUriFromMediaStore (int position)
    {
        int dataIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        mMediaStoreCursor.moveToPosition(position);

        String dataString = mMediaStoreCursor.getString(dataIndex);
        Uri mediaUri = Uri.parse("file://" + dataString);

        return mediaUri;
    }

    private void getOnClickUri(int position)
    {
        int mediaTypeIndex = mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
        int dataIndex=mMediaStoreCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

        String dataString=mMediaStoreCursor.getString(dataIndex);
        Uri mediaUri=Uri.parse("file://" + dataString);
        mMediaStoreCursor.moveToPosition(position);
        switch(mMediaStoreCursor.getInt(mediaTypeIndex))
        {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:

                mOnClickThumbListener.onClickImage(mediaUri);
                break;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                mOnClickThumbListener.onClickVideo(mediaUri);
                break;
            default:
        }
    }

}
