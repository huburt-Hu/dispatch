package com.example.app.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Class description
 * 芝麻信用积分图标盘
 *
 * @author ex-huxibing552
 * @date 2016-11-30 16:38
 */
public class ZhiMaCreditChartView extends View {

    private Context mContext;

    private int mWidth;//view默认宽度
    private int mHeight;//view默认高度

    private int maxNum;//最大数值
    private int startAngle;//开始角度
    private int sweepAngle;//扫过角度

    private int sweepInWidth;//内弧宽度
    private int sweepOutWidth;//外弧宽度
    private int gap;//内外弧间隙

    private int radius;//圆心

    private Paint arcPaint;
    private Paint shaderArcPaint;
    private Paint indicatorPaint;
    private Paint textPaint;

    private int currentNum;

    public ZhiMaCreditChartView(Context context) {
        this(context, null);
    }

    public ZhiMaCreditChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZhiMaCreditChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initPaint();
    }

    private void init(Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZhiMaCreditChartView);
        maxNum = a.getInt(R.styleable.ZhiMaCreditChartView_maxMum, 750);
        startAngle = a.getInt(R.styleable.ZhiMaCreditChartView_startAngle, 160);
        sweepAngle = a.getInt(R.styleable.ZhiMaCreditChartView_sweepAngle, 220);
        a.recycle();
        //初始化
        mContext = context.getApplicationContext();
        mWidth = dp2px(200);
        mHeight = mWidth * 4 / 5;
        sweepInWidth = dp2px(8);
        sweepOutWidth = dp2px(3);
        gap = dp2px(10);

        setBackgroundColor(calculateColor(0));
    }

    private void initPaint() {
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        arcPaint.setDither(true);//防抖动
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(0xffffffff);

        shaderArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(0xffffffff);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //提供wrap_content模式下的默认宽高
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //支持padding属性，即在绘制时考虑padding
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        mHeight = getHeight() - paddingTop - paddingBottom;
        mWidth = getWidth() - paddingLeft - paddingRight;

        radius = Math.min(mWidth / 4, mHeight / 2);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight * 2 / 3);
        drawRound(canvas);//画内外圆弧
        drawScale(canvas);//画刻度
        drawIndicator(canvas);//画进度值
        drawCenterText(canvas);//画中间文字
        canvas.restore();
    }

    private void drawRound(Canvas canvas) {
        //内弧
        arcPaint.setAlpha(0x40);
        arcPaint.setStrokeWidth(sweepInWidth);
        RectF r = new RectF(-radius, -radius, radius, radius);
        canvas.drawArc(r, startAngle, sweepAngle, false, arcPaint);
        //外弧
        arcPaint.setStrokeWidth(sweepOutWidth);
        RectF r2 = new RectF(-radius - gap, -radius - gap, radius + gap, radius + gap);
        canvas.drawArc(r2, startAngle, sweepAngle, false, arcPaint);
    }

    private String[] text = {"较差", "中等", "良好", "优秀", "极好"};

    private void drawScale(Canvas canvas) {
        canvas.save();
        float angle = sweepAngle / 30;//刻度间隔
        canvas.rotate(-270 + startAngle);//将起始刻度点转到正上方（270）
        for (int i = 0; i <= 30; i++) {
            arcPaint.setStrokeWidth(dp2px(2));
            if (i % 6 == 0) {
                arcPaint.setAlpha(0x70);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2 + dp2px(1), arcPaint);
                drawText(canvas, String.valueOf(i * maxNum / 30));
            } else {
                arcPaint.setAlpha(0x50);
                canvas.drawLine(0, -radius - sweepInWidth / 2, 0, -radius + sweepInWidth / 2, arcPaint);
            }
            if (i == 3 | i == 9 || i == 15 || i == 21 || i == 27) {
                arcPaint.setAlpha(0x50);
                drawText(canvas, text[(i - 3) / 6]);
            }
            canvas.rotate(angle);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, String text) {
        textPaint.setTextSize(sp2px(8));
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, -textWidth / 2, -radius + dp2px(15), textPaint);
    }

    private int[] indicatorColor = {0xffffffff, 0x00ffffff, 0x99ffffff, 0xffffffff};//颜色梯度"白色，透明，半透明，白色"

    private void drawIndicator(Canvas canvas) {
        shaderArcPaint.setStyle(Paint.Style.STROKE);
        int sweep;
        if (currentNum <= maxNum) {
            sweep = (int) ((float) currentNum / (float) maxNum * sweepAngle);
        } else {
            sweep = sweepAngle;
        }
        if (sweep == 0) {//若sweep为0，相当于360
            sweep = 1;
        }
        Log.i("tag", "sweep:" + sweep);
        shaderArcPaint.setStrokeWidth(sweepOutWidth);
        SweepGradient shader = new SweepGradient(0, 0, indicatorColor, null);//颜色梯度渐变
        shaderArcPaint.setShader(shader);
        int w = dp2px(10);
        RectF r = new RectF(-radius - w, -radius - w, radius + w, radius + w);
        canvas.drawArc(r, startAngle, sweep, false, shaderArcPaint);
        float x = (float) ((radius + dp2px(10)) * Math.cos(Math.toRadians(startAngle + sweep)));
        float y = (float) ((radius + dp2px(10)) * Math.sin(Math.toRadians(startAngle + sweep)));
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setColor(0xffffffff);
        indicatorPaint.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID));//圆点边缘模糊
        canvas.drawCircle(x, y, dp2px(3), indicatorPaint);
    }

    private void drawCenterText(Canvas canvas) {
        ;
        textPaint.setTextSize(radius / 2);
        canvas.drawText(String.valueOf(currentNum), -textPaint.measureText(String.valueOf(currentNum)) / 2, 0, textPaint);
        textPaint.setTextSize(radius / 4);
        String content = "";
        if (currentNum < maxNum / 5) {
            content = "信用较差";
        } else if (currentNum >= maxNum / 5 && currentNum < maxNum * 2 / 5) {
            content = "信用中等";
        } else if (currentNum >= maxNum * 2 / 5 && currentNum < maxNum * 3 / 5) {
            content = "信用良好";
        } else if (currentNum >= maxNum * 3 / 5 && currentNum < maxNum * 4 / 5) {
            content = "信用优秀";
        } else if (currentNum >= maxNum * 4 / 5) {
            content = "信用极好";
        }
        Rect r = new Rect();
        textPaint.getTextBounds(content, 0, content.length(), r);
        canvas.drawText(content, -r.width() / 2, r.height() + 20, textPaint);
    }

    public void setCurrentNumAnim(int num) {
        float duration = (float) Math.abs(num - currentNum) / maxNum * 1500 + 500;
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentNum", num);//currentNum的属性动画，需要提供getter，setter
        anim.setDuration((long) Math.min(duration, 2000));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//获取当前currentNum值
                int color = calculateColor(value);
                setBackgroundColor(color);
            }
        });
        anim.start();
    }

    private int calculateColor(int value) {
        ArgbEvaluator evaluator = new ArgbEvaluator();//颜色估值器
        float fraction;
        int color;
        if (value <= maxNum / 2) {
            fraction = (float) value / (maxNum / 2);
            color = (int) evaluator.evaluate(fraction, 0xffff6347, 0xffff8c00);//红到橙
        } else if (value <= maxNum) {
            fraction = ((float) value - maxNum / 2) / (maxNum / 2);
            color = (int) evaluator.evaluate(fraction, 0xffff8c00, 0xff00ced1);//橙到蓝
        } else {
            color = 0xff00ced1;
        }
        return color;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public void setMaxNum(int num) {
        this.maxNum = num;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int px2sp(float pxValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
