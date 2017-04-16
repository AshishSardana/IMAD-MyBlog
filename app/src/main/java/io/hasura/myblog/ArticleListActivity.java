package io.hasura.myblog;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArticleListAdapter articleListAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        articleListAdapter = new ArticleListAdapter();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //Step 1 - Create an adapter
        recyclerView.setAdapter(articleListAdapter);
        //Set 2 - set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Articles");
        progressDialog.show();

        ApiManager.getApiInterface().getArticles()
                .enqueue(new Callback<List<Article>>() {
                    @Override
                    public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            articleListAdapter.setData(response.body());
                        }
                        else{
                            //Showing toast instead of alert
                            progressDialog.dismiss();
                            Toast.makeText(ArticleListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Article>> call, Throwable t) {
                        //Showing toast instead of alert
                        progressDialog.dismiss();
                        Toast.makeText(ArticleListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class ArticleListAdapter extends  RecyclerView.Adapter<ArticleItemViewHolder> {

        /*//Mock data
        String [] articleList = {
                "article1", "article2", "article3", "article4", "article5"
        };
        */

        List<Article> articleList = new ArrayList<>();
        @Override
        public ArticleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflating the view = layout_article.xml
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_article, parent, false);
            return new ArticleItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ArticleItemViewHolder holder, final int position) {
            //Set the data i.e name of the article as per its position
            holder.articleName.setText(articleList.get(position).getHeading());
            holder.cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(ArticleListActivity.this, "Article Clicked: " +articleList.get(position).getHeading(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            //How many items we have in the list
            return articleList.size();
        }
        public void setData(List<Article> data){
            this.articleList = data;
            //To notify the adapter and recycle the adapter view
            this.notifyDataSetChanged();
        }
    }
}
