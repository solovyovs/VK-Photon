package com.khasang.vkphoto.domain.adapters;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.khasang.vkphoto.R;
import com.khasang.vkphoto.domain.adapters.viewholders.VkCommentsViewHolder;
import com.khasang.vkphoto.presentation.model.Comment;
import com.khasang.vkphoto.presentation.model.VkProfile;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 06.03.2016.
 */
public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<VkCommentsViewHolder> {
    public CommentRecyclerViewAdapter() {
    }

    private List<Comment> comments;
    private List<VkProfile> profiles;

    public CommentRecyclerViewAdapter(List<Comment> comments, List<VkProfile> profiles) {
        this.comments = comments;
        this.profiles = profiles;
    }

    @Override
    public VkCommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new VkCommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VkCommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        VkProfile profile = null;
        for (VkProfile temp : profiles) {
            if (temp.id == comment.from_id) {
                profile = temp;
                break;
            }
        }
        if (profile != null) {
            holder.name.setText(profile.first_name + " " + profile.last_name);
            final ImageView imageView = holder.userImage;
            Glide.with(imageView.getContext()).load(profile.photo_100).asBitmap().fitCenter().into(
                    new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        holder.text.setText(userReplayed(comment.text));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(comment.date * 1000L));
        holder.date.setText(dateString);

        if (comment.likes > 0) {
            holder.commentsLikes.setText(String.valueOf(comment.likes));
            holder.isCommentLikes.setVisibility(View.VISIBLE);
        }
    }

    private String userReplayed(String text) {
        Pattern p = Pattern.compile("\\[id(.+)\\|.+\\].+");
        Matcher matcher = p.matcher(text);
        if (matcher.matches()) {
            Matcher m = p.matcher(text);
            int userId = 0;
            if (m.find()) {
                userId = Integer.valueOf(m.group(1));
            }
            for (VkProfile temp : profiles) {
                if (temp.id == userId) {
                    return text.replaceAll("\\[id(.+)\\|.+\\]", "replied to " + temp.first_name);
                }
            }
        }
        return text;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setData(List<Comment> commentsList, List<VkProfile> profiles) {
        this.comments = commentsList;
        this.profiles = profiles;
        notifyDataSetChanged();
    }
}
