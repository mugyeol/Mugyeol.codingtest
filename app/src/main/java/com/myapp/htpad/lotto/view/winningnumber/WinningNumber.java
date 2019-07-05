package com.myapp.htpad.lotto.view.winningnumber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.view.splash.SplashActivity;

import java.util.ArrayList;

public class WinningNumber extends AppCompatActivity {
    private static final String DEEPLINK_PATH = "myscheme://myhost/";
    private static final String TAG = "위닝넘버";
    private int[] mResourceSet = {R.id.wnum1,R.id.wnum2,R.id.wnum3,R.id.wnum4,R.id.wnum5,R.id.wnum6,R.id.wnum7};
    @BindView(R.id.winningHeader)TextView header;
    @BindView(R.id.winningExit)AppCompatImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_number);
        ButterKnife.bind(this);
        Uri data = getIntent().getData();
        int drwNo = 0;
        if (data!=null) {
            String param = data.getQueryParameter("drwNo");
            drwNo = Integer.parseInt(param);
        }
        String title = drwNo+"회차 1등 당첨 번호 입니다";
        header.setText(title);
        SplashActivity.mApiService.getWinningData(drwNo).enqueue(new Callback<LottoModel>() {
            @Override
            public void onResponse(Call<LottoModel> call, Response<LottoModel> response) {
                if (response.body()!=null){
                    if (response.body().getReturnValue().equals(getString(R.string.returnsuccess))){
                        LottoModel lottoModel = response.body();
                        ArrayList<Integer> arrayList = new ArrayList();
                        arrayList.add(lottoModel.getDrwtNo1());
                        arrayList.add(lottoModel.getDrwtNo2());
                        arrayList.add(lottoModel.getDrwtNo3());
                        arrayList.add(lottoModel.getDrwtNo4());
                        arrayList.add(lottoModel.getDrwtNo5());
                        arrayList.add(lottoModel.getDrwtNo6());
                        arrayList.add(lottoModel.getBnusNo());

                        for (int i=0; i<arrayList.size(); i++){
                            TextView textView = (TextView)findViewById(mResourceSet[i]);
                            textView.setText(String.valueOf(arrayList.get(i)));
                            textView.setBackground(getDrawable(R.drawable.round));
                        }
                    }else{

                        Log.d(TAG,getString(R.string.returnvaluefail));
                        header.setText(getString(R.string.nodata));
                    }
                }else {
                    Log.d(TAG,getString(R.string.responsenull));
                    header.setText(getString(R.string.nodata));
                }
            }

            @Override
            public void onFailure(Call<LottoModel> call, Throwable t) {
                Log.d(TAG,""+t);

                header.setText(getString(R.string.nodata));

            }
        });
        }
        @OnClick(R.id.winningExit)
        void exit(){
        finish();
        }



    }