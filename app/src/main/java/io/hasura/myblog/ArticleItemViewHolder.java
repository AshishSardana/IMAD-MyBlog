package io.hasura.myblog;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ashish.sardana on 16-04-2017.
 */

public class ArticleItemViewHolder extends RecyclerView.ViewHolder{
    TextView articleName;
    CardView cardView;

    public ArticleItemViewHolder(View itemView) {
        super(itemView);

        articleName = (TextView) itemView.findViewById(R.id.articleName);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
    }

}
