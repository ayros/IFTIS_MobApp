package com.ayros.iftis_mobapp.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ayros.iftis_mobapp.Data;
import com.ayros.iftis_mobapp.R;
import com.ayros.iftis_mobapp.db.DatabaseCallback;
import com.ayros.iftis_mobapp.model.News;
import com.ayros.iftis_mobapp.model.Student;
import com.ayros.iftis_mobapp.network.DownloadCallback;
import com.ayros.iftis_mobapp.network.NetworkFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment implements DatabaseCallback<News> {

    private static final String ARG_STUDENT = "user";

    private Student mStudent;
    private List<News> news;

    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mAdapter;
    private SimpleDateFormat spf;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStudent = (Student)getArguments().getSerializable(ARG_STUDENT);
        }
        news = new ArrayList<>();
        updateDb();
        spf = new SimpleDateFormat("dd MMM HH:mm");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mNewsRecyclerView = v.findViewById(R.id.news_view);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }

    private void updateUI(){
        mAdapter = new NewsAdapter(news);
        mNewsRecyclerView.setAdapter(mAdapter);
    }

    private void updateDb(){
        NewsDataBaseAction action = new NewsDataBaseAction(getActivity(),this);
        Data.getInstance(getActivity()).getData(action);
    }

    @Override
    public void finished(List<News> news) {
        this.news.addAll(news);
        updateUI();
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public News mNews;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mDescriptionTextView;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.news_date);
            mDescriptionTextView = (TextView)itemView.findViewById(R.id.news_description);
        }

        public void bindNews(News news){
            mNews = news;
            mTitleTextView.setText(mNews.getTitle());
            mDateTextView.setText(spf.format(mNews.getTime()));
            mDescriptionTextView.setText(mNews.getDescription());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), mNews.getTitle() + "clicked!", Toast.LENGTH_SHORT).show();
        }
    }


    private class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{

        private List<News> mNewsList;

        public NewsAdapter(List<News> news){
            mNewsList = news;
        }
        @NonNull
        @Override
        public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.news_list_layout,viewGroup,false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsHolder newsHolder, int position) {
            News event = mNewsList.get(mNewsList.size() - position-1);
            newsHolder.bindNews(event);
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

}
