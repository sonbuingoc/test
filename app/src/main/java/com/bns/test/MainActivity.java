package com.bns.test;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView[] dots;
    LinearLayout sliderDotspanel;
    private int dotscount;
    ViewPager viewpager;
    TextView textView;
    TextView tvLoading;
    int i = 100;
    ShowLoading showLoading;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        textView = findViewById(R.id.tv);
        tvLoading = findViewById(R.id.tv_loading);


        showLoading = new ShowLoading();
        showLoading.execute();

//        System.out.println("ngocson: "+textView.getMaxLines());
//        textView.setMaxLines(3);
//        System.out.println("ngocson: "+textView.getText().length());
//        textView.setText(textView.getText().subSequence(0, textView.getText().length() - 12) + "... " + "See more");

//        System.out.println("ngocson: "+textView.getLineCount());
        makeTextViewResizable(textView, 5, "See more", true);
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img1));
        list.add(new Photo(R.drawable.img2));
        list.add(new Photo(R.drawable.img3));
        list.add(new Photo(R.drawable.img1));
        list.add(new Photo(R.drawable.img1));
        viewpager = (ViewPager) findViewById(R.id.photos_viewpager);
        PagerAdapter adapter = new PhotosAdapter(this, list);
        viewpager.setAdapter(adapter);

        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_default));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
//            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
            sliderDotspanel.addView(dots[i], params);

        }


        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_selected));


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_default));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_selected));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);

                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
//                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() -1);
//                    text = tv.getText().subSequence(0, lineEndIndex- expandText.length()+1) + " " + expandText;
                    return;
                }
//                System.out.println("ngocson: "+lineEndIndex);
//                System.out.println("ngocson: "+tv.length());
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable((tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private SpannableStringBuilder addClickablePartTextViewResizable(final String strSpanned, final TextView tv,
                                                                     final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false), str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    public class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
            textView.setHighlightColor(Color.TRANSPARENT);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#66FFFFFF"));

        }

        @Override
        public void onClick(View widget) {
            Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
        }
    }

    private class ShowLoading extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            while (true) {
                i = i + 100;
                publishProgress(i);
                if (i == 300) {
                    i = 0;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);

            if (i == 100) {
                tvLoading.setText("Loading.");
            } else {
                if (i == 200) {
                    tvLoading.setText("Loading..");
                } else {
                    tvLoading.setText("Loading...");
                }
            }
//            if (i % 300 == 0) {
//                tvLoading.setText("Loading.");
//            } else if (i % 200 == 0) {
//                tvLoading.setText("Loading..");
//            } else if (i % 100 == 0) {
//                tvLoading.setText("Loading...");
//            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLoading.cancel(true);
    }
}