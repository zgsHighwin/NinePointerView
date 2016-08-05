package highwin.zgs.sudokuview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private SeekBar mSb;
    private NinePointView mNpv;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSb = ((SeekBar) findViewById(R.id.sb));
        mNpv = ((NinePointView) findViewById(R.id.npv));
        mSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mNpv.setBaseNumber(i);
                mNpv.clear();
                mTv.setText("总数" + mNpv.getTotalCount() + "\t单前选中" + 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mTv = (TextView) findViewById(R.id.tv);

        mNpv.setOnNinePointViewChangeListener(new NinePointView.OnNinePointViewChangeListener() {
            @Override
            public void onChange(LinkedList<NinePointView.PointerMessage> pointerMessages, int total, int selectCount) {
                mTv.setText("总数" + total + "\t单前选中" + selectCount);
            }
        });
    }

    public void click(View v) {
        mTv.setText("总数" + mNpv.getTotalCount() + "\t单前选中" + 0);

        mNpv.clear();
    }
}
