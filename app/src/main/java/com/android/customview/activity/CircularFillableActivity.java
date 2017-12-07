package com.android.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.widget.SeekBar;

import com.android.customview.R;
import com.android.customview.view.CircularFillableLoaders;

/**
 * Created by litonghui on 2017/10/11.
 */

public class CircularFillableActivity extends Activity{

    private CircularFillableLoaders circularFillableLoaders;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);

        circularFillableLoaders = (CircularFillableLoaders) findViewById(R.id.circularFillableLoaders);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.v("lth","progress:"+progress);
                circularFillableLoaders.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /*// PROGRESS
        ((DiscreteSeekBar) findViewById(R.id.seekBarProgress)).setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                circularFillableLoaders.setProgress(value);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        // BORDER
        ((DiscreteSeekBar) findViewById(R.id.seekBarBorderWidth)).setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                circularFillableLoaders.setBorderWidth(value * getResources().getDisplayMetrics().density);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });

        // AMPLITUDE
        ((DiscreteSeekBar) findViewById(R.id.seekBarAmplitude)).setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                circularFillableLoaders.setAmplitudeRatio((float) value / 1000);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });

        // COLOR
        ((LobsterShadeSlider) findViewById(R.id.shadeslider)).addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {
                circularFillableLoaders.setColor(color);
            }

            @Override
            public void onColorSelected(@ColorInt int color) {
            }
        });*/
    }
}
