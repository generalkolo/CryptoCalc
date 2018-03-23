package com.semanientreprise.steemitcalc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.semanientreprise.steemitcalc.R;

public class splash extends AppCompatActivity implements Animation.AnimationListener{
        private ImageView iv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash_activity);
            AnimateView();
        }

        private void AnimateView() {
            iv = (ImageView) findViewById(R.id.splash_img);
            final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.splash_anim);
            iv.startAnimation(an);
            an.setAnimationListener(this);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            iv.setImageResource(R.drawable.steemitcalc);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent();
            intent.setClass(splash.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
