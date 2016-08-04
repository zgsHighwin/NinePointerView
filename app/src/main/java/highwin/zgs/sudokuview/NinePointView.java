package highwin.zgs.sudokuview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NinePointView extends View {
    private Paint mBackgroundPaint;
    private Paint mShowCirclePaint;
    private Paint mLinePaint;
    private Paint mChooseCirclePaint;
    private Paint mLinePathPaint;
    private boolean[] mLinkedCircle;

    public NinePointView(Context context) {
        this(context, null);
    }

    public NinePointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NinePointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private int strokeWidth = 10;

    private void initPaint() {

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(20);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);

        mShowCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShowCirclePaint.setDither(true);
        mShowCirclePaint.setStyle(Paint.Style.STROKE);
        mShowCirclePaint.setStrokeWidth(strokeWidth);
        mShowCirclePaint.setColor(Color.BLACK);
        mShowCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mShowCirclePaint.setStrokeJoin(Paint.Join.ROUND);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setDither(true);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(strokeWidth);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);

        mChooseCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChooseCirclePaint.setDither(true);
        mChooseCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mChooseCirclePaint.setStrokeWidth(strokeWidth);
        mChooseCirclePaint.setColor(Color.BLUE);
        mChooseCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mChooseCirclePaint.setStrokeJoin(Paint.Join.ROUND);

        mLinePathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePathPaint.setDither(true);
        mLinePathPaint.setStyle(Paint.Style.STROKE);
        mLinePathPaint.setStrokeWidth(strokeWidth);
        mLinePathPaint.setStrokeWidth(30);
        mLinePathPaint.setColor(Color.RED);
        mLinePathPaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePathPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int realWidth = 0;
        int realHeight = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            realWidth = widthSize;
        } else {
            realWidth = 50;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = heightSize;
        } else {
            realHeight = 50;
        }

        //取最大值作为最为图形最外侧的面积
        int max = Math.max(realWidth, realHeight);
        setMeasuredDimension(max, max);

    }

    //几乘几的图形
    private int baseNumber = 3;

    private boolean[][] mIsLInkedCircle = new boolean[3][3];
    private List<Boolean[][]> mPathList = new ArrayList<>();

    public int getBaseNumber() {
        return baseNumber;
    }

    public void setBaseNumber(int baseNumber) {
        this.baseNumber = baseNumber;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF backGroundRectF = new RectF(0, 0, getWidth(), getHeight());
        mBackgroundPaint.setShader(new SweepGradient(getWidth() / 2, getHeight() / 2, new int[]{Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW}, null));
        canvas.drawRoundRect(backGroundRectF, 5, 5, mBackgroundPaint);
        int eachWidth = getWidth() / baseNumber; //每个部分占据的高度
        int eachHeight = getHeight() / baseNumber;   //每个部分占据的高度

        //显示圆的半径
        int showCircleRadius = eachWidth / 4;
        //绘制显示圆
        for (int i = 0; i < baseNumber; i++) {
            for (int j = 0; j < baseNumber; j++) {
                int xCenter = eachWidth / 2 * (j + 1) + eachWidth / 2 * j; //圆心x坐标
                int yCenter = eachHeight / 2 * (i + 1) + eachHeight / 2 * i; //圆心y坐标
                if (mPointX > xCenter - showCircleRadius && mPointX < xCenter + showCircleRadius
                        && mPointY > yCenter - showCircleRadius && mPointY < yCenter + showCircleRadius) {
                    mIsLInkedCircle[i][j] = true;
                    Log.d("NinePointView", "    i:  " + i + "   j:  " + j + "   true");
                }
                Log.d("NinePointView", "    i:  " + i + "   j:  " + j + "   false");
                canvas.drawCircle(xCenter, yCenter, showCircleRadius, mShowCirclePaint);
            }
        }

        // 实心小圆的半径
        int chooseCircleRadius = showCircleRadius / 2;
        //画三个选中的实心小圆
        for (int i = 0; i < baseNumber; i++) {
            for (int j = 0; j < baseNumber; j++) {
                if (mIsLInkedCircle[i][j]) {
                    canvas.drawCircle(eachWidth / 2 * (j + 1) + eachWidth / 2 * j, eachHeight / 2 * (i + 1) + eachHeight / 2 * i, chooseCircleRadius, mChooseCirclePaint);
                    continue;
                }
            }
        }


        //画横向三条直线
        for (int i = 0; i < baseNumber - 1; i++) {
            mLinePaint.setShader(new LinearGradient(strokeWidth, eachHeight * (i + 1), getWidth() - strokeWidth,
                    eachHeight * (i + 1),
                    new int[]{Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW},
                    new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f}, Shader.TileMode.MIRROR));

            canvas.drawLine(strokeWidth, eachHeight * (i + 1), getWidth() - strokeWidth, eachHeight * (i + 1), mLinePaint);
        }

        //画纵向三条直线
        for (int i = 0; i < baseNumber - 1; i++) {
            mLinePaint.setShader(new LinearGradient(eachWidth * (i + 1), 0, eachWidth * (i + 1), getHeight(),
                    new int[]{Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW},
                    new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f}, Shader.TileMode.MIRROR));
            canvas.drawLine(eachWidth * (i + 1), strokeWidth, eachWidth * (i + 1), getHeight() - strokeWidth, mLinePaint);
        }


        for (int i = 0; i < mIsLInkedCircle.length; i++) {
            for (int j = 0; j < mIsLInkedCircle[i].length; j++) {
                Log.d("NinePointView", "mIsLInkedCircle[i][j]:" + mIsLInkedCircle[i][j]);
            }
        }

        canvas.drawLine(getStartX(), getStartY(), mMoveX, mMoveY, mLinePathPaint);
    }

    private float getStartY() {
        return 0;
    }

    private float getStartX() {
        return 0;
    }

    //第一次点下去的坐标
    private int mPointX;
    private int mPointY;

    //移动的坐标
    private int mMoveX;
    private int mMoveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointX = (int) event.getX();
                mPointY = (int) event.getY();
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveX = (int) event.getX();
                mMoveY = (int) event.getY();

                break;

            case MotionEvent.ACTION_UP:

                break;

        }
        return true;
    }
}
