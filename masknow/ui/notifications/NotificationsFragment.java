package com.booknbooks.maskinfo.ui.notifications;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.booknbooks.maskinfo.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    ConstraintLayout expandableView;
    Button arrowBtn;

    ConstraintLayout expandableView2;
    Button arrowBtn2;

    ConstraintLayout expandableView3;
    Button arrowBtn3;

    ConstraintLayout expandableView4;
    Button arrowBtn4;


    //결과를 띄어줄 TextView
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;

    Elements elements;
    Elements elements2;

    String content1;
    String content2;
    String content3;
    String content4;
    String content5;
    String content6;

//    Document doc = null;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        textView1 = (TextView) view.findViewById(R.id.textBox1);
        textView2 = (TextView) view.findViewById(R.id.textBox2);
        textView3 = (TextView) view.findViewById(R.id.textBox3);
        textView4 = (TextView) view.findViewById(R.id.textBox4);
        textView5 = (TextView) view.findViewById(R.id.textBox5);
        textView6 = (TextView) view.findViewById(R.id.textBox6);

        //AsyncTask 객체 생성
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Document doc = Jsoup.connect("http ://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=11&ncvContSeq=&contSeq=&board_id=&gubun=").get(); //질병관리본부
                    elements = doc.select("dd");  //태그:a, class:num, 첫번째
                    elements2 = doc.select("p");

                    content1 = elements.get(0).ownText();
                    content2 = elements.get(2).ownText();
                    content3 = elements.get(4).ownText();
                    content4 = elements.get(6).ownText();
                    content5 = elements2.get(1).ownText();

                    content6 = elements2.get(0).ownText();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                textView1.setText(String.valueOf(content1));
                textView2.setText(String.valueOf(content2));
                textView3.setText(String.valueOf(content3));
                textView4.setText(String.valueOf(content4));
                textView5.setText("(+" + String.valueOf(content5) + ")");

                textView6.setText("※ " + String.valueOf(content6));

            }



        }.execute();







        // 이미지 슬라이드
        ImageSlider imageSlider = view.findViewById(R.id.slider);
        ImageSlider imageSlider2 = view.findViewById(R.id.slider2);
        ImageSlider imageSlider3 = view.findViewById(R.id.slider3);
        ImageSlider imageSlider4 = view.findViewById(R.id.slider4);


        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.help1, null));
        slideModels.add(new SlideModel(R.drawable.help2, null));
        slideModels.add(new SlideModel(R.drawable.help3, null));
        slideModels.add(new SlideModel(R.drawable.help4, null));
        slideModels.add(new SlideModel(R.drawable.help5, null));
        slideModels.add(new SlideModel(R.drawable.help6, null));

        List<SlideModel> slideModels2 = new ArrayList<>();
        slideModels2.add(new SlideModel(R.drawable.distance1, null));
        slideModels2.add(new SlideModel(R.drawable.distance2, null));
        slideModels2.add(new SlideModel(R.drawable.distance3, null));
        slideModels2.add(new SlideModel(R.drawable.distance4, null));

        List<SlideModel> slideModels3 = new ArrayList<>();
        slideModels3.add(new SlideModel(R.drawable.mask1, null));
        slideModels3.add(new SlideModel(R.drawable.mask2, null));
        slideModels3.add(new SlideModel(R.drawable.mask3, null));
        slideModels3.add(new SlideModel(R.drawable.mask4, null));

        List<SlideModel> slideModels4 = new ArrayList<>();
        slideModels4.add(new SlideModel(R.drawable.mind1, null));
        slideModels4.add(new SlideModel(R.drawable.mind2, null));
        slideModels4.add(new SlideModel(R.drawable.mind3, null));
        slideModels4.add(new SlideModel(R.drawable.mind4, null));
        slideModels4.add(new SlideModel(R.drawable.mind5, null));
        slideModels4.add(new SlideModel(R.drawable.mind6, null));
        slideModels4.add(new SlideModel(R.drawable.mind7, null));
        slideModels4.add(new SlideModel(R.drawable.mind8, null));
        slideModels4.add(new SlideModel(R.drawable.mind9, null));
        slideModels4.add(new SlideModel(R.drawable.mind10, null));
        slideModels4.add(new SlideModel(R.drawable.mind11, null));

        imageSlider.setImageList(slideModels, false);
        imageSlider2.setImageList(slideModels2, false);
        imageSlider3.setImageList(slideModels3, false);
        imageSlider4.setImageList(slideModels4, false);


        expandableView = view.findViewById(R.id.expandableView);
        arrowBtn = view.findViewById(R.id.arrowBtn);

        expandableView2 = view.findViewById(R.id.expandableView2);
        arrowBtn2 = view.findViewById(R.id.arrowBtn2);

        expandableView3 = view.findViewById(R.id.expandableView3);
        arrowBtn3 = view.findViewById(R.id.arrowBtn3);

        expandableView4 = view.findViewById(R.id.expandableView4);
        arrowBtn4 = view.findViewById(R.id.arrowBtn4);


        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE) {
                    //TransitionManager.beginDelayedTransition();
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.up);
                } else {
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.down);

                }
            }
        });

        arrowBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView2.getVisibility()==View.GONE) {
                    //TransitionManager.beginDelayedTransition();
                    expandableView2.setVisibility(View.VISIBLE);
                    arrowBtn2.setBackgroundResource(R.drawable.up);
                } else {
                    expandableView2.setVisibility(View.GONE);
                    arrowBtn2.setBackgroundResource(R.drawable.down);

                }
            }
        });

        arrowBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView3.getVisibility()==View.GONE) {
                    //TransitionManager.beginDelayedTransition();
                    expandableView3.setVisibility(View.VISIBLE);
                    arrowBtn3.setBackgroundResource(R.drawable.up);
                } else {
                    expandableView3.setVisibility(View.GONE);
                    arrowBtn3.setBackgroundResource(R.drawable.down);

                }
            }
        });

        arrowBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView4.getVisibility()==View.GONE) {
                    //TransitionManager.beginDelayedTransition();
                    expandableView4.setVisibility(View.VISIBLE);
                    arrowBtn4.setBackgroundResource(R.drawable.up);
                } else {
                    expandableView4.setVisibility(View.GONE);
                    arrowBtn4.setBackgroundResource(R.drawable.down);

                }
            }
        });


    }


}