package com.zuk.ireader.ui.fragment.novel;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zuk.ireader.R;
import com.zuk.ireader.ui.activity.novel.NovelBookListActivity;
import com.zuk.ireader.ui.activity.novel.NovelDownloadActivity;
import com.zuk.ireader.ui.activity.web.NovelSiteWebActivity;

/**
 * Created by ksymac on 2019/1/29.
 */

public class NovelTabMore extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("TabMore____onCreateView");
        return inflater.inflate(R.layout.novel_tab_more, container, false);
    }


    TableRow more_privacy_policy;
    TableRow more_vercheck;
    TableRow more_feedback;
    TableRow more_sortcom;
    TableRow more_appreview;
    TableRow more_adclear;
    TableRow more_boxview;
    TableRow more_boxlimitclear;
    TableRow more_download;

    private Context mContext;

    TextView txtDetail;
    ToggleButton swtDetail;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //System.out.println("TabMore____onActivityCreated");
        mContext =(getActivity());
        more_privacy_policy = (TableRow) this.getView().findViewById(R.id.more_privacy_policy);
        more_vercheck = (TableRow) this.getView().findViewById(R.id.more_vercheck);
        more_feedback = (TableRow) this.getView().findViewById(R.id.more_feedback);
        more_sortcom = (TableRow) this.getView().findViewById(R.id.more_sortcom);
        more_appreview = (TableRow) this.getView().findViewById(R.id.more_appreview);

        more_adclear = (TableRow) this.getView().findViewById(R.id.more_adclear);
        more_boxview = (TableRow) this.getView().findViewById(R.id.more_boxview);
        more_boxlimitclear = (TableRow) this.getView().findViewById(R.id.more_boxlimitclear);
        more_download = (TableRow) this.getView().findViewById(R.id.more_download);

        more_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPrivacyPolicy();
            }
        });
        more_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDownloadView();
            }
        });
        more_vercheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayWeb();
            }
        });
        more_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
        more_sortcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoSortComany();
            }
        });
        more_boxview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movetoBoxView();
            }
        });

        more_appreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayWeb();
            }
        });

        more_adclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity mainactivity = (MainActivity)getActivity();
//                mainactivity.buyADClearItem();
            }
        });
        more_boxlimitclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity mainactivity = (MainActivity)getActivity();
//                mainactivity.buyBoxLimitClear();
            }
        });
    }


    public void sendMail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        String aEmailList[] = { "svalbard.k@gmail.com" };
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "携帯小説について");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");//Html.fromHtml(body.toString())
        try{
            startActivity(emailIntent);
        } catch (ActivityNotFoundException ex){
            Toast.makeText(mContext.getApplicationContext(), "No activity found", Toast.LENGTH_LONG).show(); //Display an error message
        }
    }
    public void openPlayWeb() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.zuk.ireader"));
            startActivity(intent);
        } catch (Exception e) { //google play app is not installed
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.zuk.ireader"));
            startActivity(intent);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void movetoSortComany() {
//        MainActivity mainactivity = (MainActivity)getActivity();
//        mainactivity.ShowCompanySort(ComCode.TAB_MORE);
    }
    public void movetoBoxView() {
//        MainActivity mainactivity = (MainActivity)getActivity();
//        mainactivity.ShowBoxView(ComCode.TAB_MORE);
    }

    public void openDownloadView(){
        Intent intent = new Intent(getContext(), NovelDownloadActivity.class);
        startActivity(intent);
    }
    public void openPrivacyPolicy(){
        NovelSiteWebActivity.startActivity(this.getContext(), "http://ksyapp.com/privacy/novelreader.html");
    }
}

