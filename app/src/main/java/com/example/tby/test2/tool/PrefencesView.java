package com.example.tby.test2.tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tby.test2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrefencesView extends View {

    private int width, height;

    private float innerRadius, litterRadius, bigRadius;

    private Path weavePath, circlePath;

    private List<Point> points;

    private int weaveHeight, weaveWidth, waterLine, waterOffset, startX,
            waterH;

    private Paint weavePaint, circlePaint, outPaint, centerPaint;

    private Paint paint1, paint2, paint3, paint4, paint5, paint6, paint7,
            paint8, paint9, paint10, paint11, paint12;

    private int[] colors;

    private int whiteColor, selectColor, lastColor;

    private Timer timer;

    private MyTimerTask timerTask;

    private int cx1e, cy1e, cx2e, cy2e, cx3e, cy3e, cx4e, cy4e, cx5e, cy5e,
            cx6e, cy6e;

    private boolean[] isBig1=new boolean[]{false,false,false,false,
            false,false,false,false,
            false,false,false,false};

    private String center;

    private Rect centerR, rect1, rect2, rect3, rect4, rect5, rect6, rect7,
            rect8, rect9, rect10, rect11, rect12, src1, src2, src3, src4, src5,
            src6, src7, src8, src9, src10, src11, src12;

    private RectF dst1, dst2, dst3, dst4, dst5, dst6;

    private int page;
    /*
    * 设置显示的图标
    * */
    private String[]
            strs = new String[] { "国内","国际","社会","体育","科技","军事","财经",
            "敬", "请", "期", "待", "！！！" };;

    private double sinx = 0;

    private Bitmap[] bitmaps;

    private int delta;

    public void setIsBig(String kind){
        if(kind.equals("")){
            for(int i=0;i<isBig1.length;i++){
                isBig1[i]=true;
            }
        return ;
        }
        String kind1[]=kind.split("&");
        for(int i=0;i<kind1.length;i++){

            isBig1[Integer.parseInt(kind1[i])]=true;
        }
        for(int i=0;i<isBig1.length;i++){
            if(isBig1[i])
               doClickEvent(i);
        }

    }
    public String getResult(){
        String result="";
        for(int i=0;i<isBig1.length;i++){
            if(isBig1[i])
                result+=i+"&";
        }
        return result;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            waterOffset += 10;
            if (waterOffset >= weaveWidth) {
                waterOffset = 0;
            }
            if (isStartAnimation) {
                sinx += 0.3;
                curRadius = (int) (litterRadius + (bigRadius - litterRadius)
                        * Math.sin(sinx));
                if (sinx >= Math.PI - 0.5) {
                    isStartAnimation = false;
                    sinx = 0;
                }
            }
            if (isRadiationAnmation) {
                delta += 10;
                if (delta >= 50) {
                    isRadiationAnmation = false;
                    delta = 0;
                }
            }
            postInvalidate();
        };
    };

    public PrefencesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PrefencesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PrefencesView(Context context) {
        super(context);
        init();
    }

    private void init() {
        /*Resources resources = getContext().getResources();
        Drawable btnDrawable = resources.getDrawable(R.drawable.newstitle);
        super.setBackground(btnDrawable);*/
        dst1 = new RectF();
        dst2 = new RectF();
        dst3 = new RectF();
        dst4 = new RectF();
        dst5 = new RectF();
        dst6 = new RectF();
        Resources res = getResources();
        bitmaps = new Bitmap[12];
        /*
        * 设置选中后的图片
        * */
        bitmaps[0] = BitmapFactory.decodeResource(res, R.drawable.china);
        src1 = new Rect(0, 0, bitmaps[0].getWidth(), bitmaps[0].getHeight());
        bitmaps[1] = BitmapFactory.decodeResource(res, R.drawable.internation);
        src2 = new Rect(0, 0, bitmaps[1].getWidth(), bitmaps[1].getHeight());
        bitmaps[2] = BitmapFactory.decodeResource(res, R.drawable.social);
        src3 = new Rect(0, 0, bitmaps[2].getWidth(), bitmaps[2].getHeight());
        bitmaps[3] = BitmapFactory.decodeResource(res, R.drawable.playing);
        src4 = new Rect(0, 0, bitmaps[3].getWidth(), bitmaps[3].getHeight());
        bitmaps[4] = BitmapFactory.decodeResource(res, R.drawable.science);
        src5 = new Rect(0, 0, bitmaps[4].getWidth(), bitmaps[4].getHeight());
        bitmaps[5] = BitmapFactory.decodeResource(res, R.drawable.army);
        src6 = new Rect(0, 0, bitmaps[5].getWidth(), bitmaps[5].getHeight());
        bitmaps[6] = BitmapFactory.decodeResource(res, R.drawable.money);
        src7 = new Rect(0, 0, bitmaps[6].getWidth(), bitmaps[6].getHeight());
        bitmaps[7] = BitmapFactory.decodeResource(res, R.drawable.butn);
        src8 = new Rect(0, 0, bitmaps[7].getWidth(), bitmaps[7].getHeight());
        bitmaps[8] = BitmapFactory.decodeResource(res,R.drawable.butn);
        src9 = new Rect(0, 0, bitmaps[8].getWidth(), bitmaps[8].getHeight());
        bitmaps[9] = BitmapFactory.decodeResource(res, R.drawable.butn);
        src10 = new Rect(0, 0, bitmaps[9].getWidth(), bitmaps[9].getHeight());
        bitmaps[10] = BitmapFactory.decodeResource(res, R.drawable.butn);
        src11 = new Rect(0, 0, bitmaps[10].getWidth(), bitmaps[10].getHeight());
        bitmaps[11] = BitmapFactory.decodeResource(res, R.drawable.butn);
        src12 = new Rect(0, 0, bitmaps[11].getWidth(), bitmaps[11].getHeight());
        colors = new int[] { Color.parseColor("#986b85"),
                Color.parseColor("#5b7496"), Color.parseColor("#7daca6"),
                Color.parseColor("#a5956a"), Color.parseColor("#7b6896"),
                Color.parseColor("#65927d"), Color.parseColor("#b8ba8c"),
                Color.parseColor("#5b7596"), Color.parseColor("#65927d"),
                Color.parseColor("#986b85"), Color.parseColor("#dea937"),
                Color.parseColor("#7daca6") };
        whiteColor = Color.parseColor("#a9a9a9");
        paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint4 = new Paint();
        paint4.setAntiAlias(true);
        paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint6 = new Paint();
        paint6.setAntiAlias(true);
        paint7 = new Paint();
        paint7.setAntiAlias(true);
        paint8 = new Paint();
        paint8.setAntiAlias(true);
        paint9 = new Paint();
        paint9.setAntiAlias(true);
        paint10 = new Paint();
        paint10.setAntiAlias(true);
        paint11 = new Paint();
        paint11.setAntiAlias(true);
        paint12 = new Paint();
        paint12.setAntiAlias(true);
        weavePath = new Path();
        points = new ArrayList<Point>();
        weavePaint = new Paint();
        weavePaint.setAntiAlias(true);
        weavePaint.setStyle(Style.FILL);
        selectColor = Color.BLUE;
        circlePath = new Path();
        timer = new Timer();
        timerTask = new MyTimerTask(handler);
        weavePaint.setStrokeWidth(2);
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Style.STROKE);
        circlePaint.setStrokeWidth(2);
        center = "换一批";
        centerR = new Rect();
        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(Color.GRAY);
        page = 1;


        outPaint = new Paint();
        outPaint.setAntiAlias(true);
        outPaint.setColor(Color.GRAY);
        rect1 = new Rect();
        rect2 = new Rect();
        rect3 = new Rect();
        rect4 = new Rect();
        rect5 = new Rect();
        rect6 = new Rect();
        rect7 = new Rect();
        rect8 = new Rect();
        rect9 = new Rect();
        rect10 = new Rect();
        rect11 = new Rect();
        rect12 = new Rect();
        start();
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
    }

    private void start() {
        timer.schedule(timerTask, 0, 50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft()
                + getPaddingRight();
        height = width;
        innerRadius = (108 * width) / 720;
        waterLine = (int) ((202 * width) / 720 + innerRadius * 4);
        weaveHeight = (int) (innerRadius / 2);
        startX = (int) (width / 2 - innerRadius * 3);
        weaveWidth = (int) (2 * innerRadius);
        waterH = 0;
        litterRadius = (83 * width) / 720;
        bigRadius = (104 * width) / 720;
        cx3e = (int) (width - bigRadius);
        cy3e = width / 2;
        cx6e = (int) bigRadius;
        cy6e = width / 2;
        cx1e = (int) (cx6e + (width / 2 - bigRadius) / 2);
        cy1e = (int) (cy6e - ((width / 2 - bigRadius) / 2)
                * Math.tan(Math.PI / 3));
        cx2e = (int) (cx3e - (width / 2 - bigRadius) / 2);
        cy2e = cy1e;
        cx4e = (int) (cx3e - (width / 2 - bigRadius) / 2);
        cy4e = (int) (cy6e + ((width / 2 - bigRadius) / 2)
                * Math.tan(Math.PI / 3));
        cx5e = cx1e;
        cy5e = cy4e;
        centerPaint.setTextSize((42 * width) / 720);
        centerPaint.getTextBounds(center, 0, center.length(), centerR);
        paint1.setTextSize((28 * width) / 720);
        paint2.setTextSize((28 * width) / 720);
        paint3.setTextSize((28 * width) / 720);
        paint4.setTextSize((28 * width) / 720);
        paint5.setTextSize((28 * width) / 720);
        paint6.setTextSize((28 * width) / 720);
        paint7.setTextSize((28 * width) / 720);
        paint8.setTextSize((28 * width) / 720);
        paint9.setTextSize((28 * width) / 720);
        paint10.setTextSize((28 * width) / 720);
        paint11.setTextSize((28 * width) / 720);
        paint12.setTextSize((28 * width) / 720);
        paint1.getTextBounds(strs[0], 0, strs[0].length(), rect1);
        paint2.getTextBounds(strs[1], 0, strs[1].length(), rect2);
        paint3.getTextBounds(strs[2], 0, strs[2].length(), rect3);
        paint4.getTextBounds(strs[3], 0, strs[3].length(), rect4);
        paint5.getTextBounds(strs[4], 0, strs[4].length(), rect5);
        paint6.getTextBounds(strs[5], 0, strs[5].length(), rect6);
        paint7.getTextBounds(strs[6], 0, strs[6].length(), rect7);
        paint8.getTextBounds(strs[7], 0, strs[7].length(), rect8);
        paint9.getTextBounds(strs[8], 0, strs[8].length(), rect9);
        paint10.getTextBounds(strs[9], 0, strs[9].length(), rect10);
        paint11.getTextBounds(strs[10], 0, strs[10].length(), rect11);
        paint12.getTextBounds(strs[11], 0, strs[11].length(), rect12);
        initPoint();
        setMeasuredDimension(width, height);
    }


    private DrawFilter filter;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(filter);
        weavePath.reset();
        weavePath.moveTo(points.get(0).getX() + waterOffset, points.get(0)
                .getY() - waterH);
        weavePath.quadTo(points.get(1).getX() + waterOffset, points.get(1)
                .getY() - waterH, points.get(2).getX() + waterOffset, points
                .get(2).getY() - waterH);
        weavePath.quadTo(points.get(3).getX() + waterOffset, points.get(3)
                .getY() - waterH, points.get(4).getX() + waterOffset, points
                .get(4).getY() - waterH);
        weavePath.quadTo(points.get(5).getX() + waterOffset, points.get(5)
                .getY() - waterH, points.get(6).getX() + waterOffset, points
                .get(6).getY() - waterH);
        weavePath.quadTo(points.get(7).getX() + waterOffset, points.get(7)
                .getY() - waterH, points.get(8).getX() + waterOffset, points
                .get(8).getY() - waterH);
        weavePath.lineTo(waterOffset + points.get(8).getX(), waterLine);
        weavePath.lineTo(waterOffset + points.get(0).getX(), waterLine);
        weavePath.moveTo(points.get(0).getX() + waterOffset, points.get(0)
                .getY() - waterH);
        weavePath.close();
        weavePaint.setColor(selectColor);
        canvas.drawPath(weavePath, weavePaint);
        canvas.drawText(center, 0, center.length(), width / 2 - centerR.width()
                / 2, width / 2 + centerR.height() / 2, centerPaint);
        canvas.save();
        circlePath.reset();
        circlePath.addCircle(width / 2, height / 2, innerRadius, Direction.CW);
        circlePath.close();
        canvas.clipPath(circlePath, Op.DIFFERENCE);
        canvas.drawColor(whiteColor);
        canvas.drawCircle(width / 2, height / 2, innerRadius + 2, circlePaint);

        if (isRadiationAnmation) {
            dst1.left = (float) (width / 2 - ((width / 2 - cx1e)) / 50 * delta - litterRadius * 1.1);
            dst1.top = (float) (width / 2 - ((width / 2 - cy1e) / 50) * delta - litterRadius * 1.1);
            dst1.right = (float) (dst1.left + litterRadius * 1.1 * 2);
            dst1.bottom = (float) (dst1.top + litterRadius * 1.1 * 2);
            if (isBig1[0] && page == 1) {
                canvas.drawBitmap(bitmaps[0], src1, dst1, paint1);
            } else if (isBig1[6] && page == 2) {
                canvas.drawBitmap(bitmaps[6], src7, dst1, paint1);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx1e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy1e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 1 || index == 7)) {
            dst1.left = cx1e - curRadius;
            dst1.top = cy1e - curRadius;
            dst1.right = cx1e + curRadius;
            dst1.bottom = cy1e + curRadius;
            if (index == 1) {
                canvas.drawBitmap(bitmaps[0], src1, dst1, paint1);
            }
            if (index == 7) {
                canvas.drawBitmap(bitmaps[6], src7, dst1, paint1);
            }
        } else {
            dst1.left = (float) (cx1e - litterRadius * 1.1);
            dst1.top = (float) (cy1e - litterRadius * 1.1);
            dst1.right = (float) (cx1e + litterRadius * 1.1);
            dst1.bottom = (float) (cy1e + litterRadius * 1.1);
            if (isBig1[0] && page == 1) {
                canvas.drawBitmap(bitmaps[0], src1, dst1, paint1);
            } else if (isBig1[6] && page == 2) {
                canvas.drawBitmap(bitmaps[6], src7, dst1, paint1);
            } else {
                canvas.drawCircle(cx1e, cy1e, litterRadius, circlePaint);
            }
        }

        if (isRadiationAnmation) {
            dst2.left = (float) (width / 2 - ((width / 2 - cx2e)) / 50 * delta - litterRadius * 1.1);
            dst2.top = (float) (width / 2 - ((width / 2 - cy2e) / 50) * delta - litterRadius * 1.1);
            dst2.right = (float) (dst2.left + litterRadius * 1.1 * 2);
            dst2.bottom = (float) (dst2.top + litterRadius * 1.1 * 2);
            if (isBig1[1] && page == 1) {
                canvas.drawBitmap(bitmaps[1], src2, dst2, paint2);
            } else if (isBig1[7] && page == 2) {
                canvas.drawBitmap(bitmaps[7], src8, dst2, paint2);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx2e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy2e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 2 || index == 8)) {
            dst2.left = cx2e - curRadius;
            dst2.top = cy2e - curRadius;
            dst2.right = cx2e + curRadius;
            dst2.bottom = cy2e + curRadius;
            if (index == 2) {
                canvas.drawBitmap(bitmaps[1], src2, dst2, paint2);
            }
            if (index == 8) {
                canvas.drawBitmap(bitmaps[7], src8, dst2, paint2);
            }
        } else {
            dst2.left = (float) (cx2e - litterRadius * 1.1);
            dst2.top = (float) (cy2e - litterRadius * 1.1);
            dst2.right = (float) (cx2e + litterRadius * 1.1);
            dst2.bottom = (float) (cy2e + litterRadius * 1.1);
            if (isBig1[1] && page == 1) {
                canvas.drawBitmap(bitmaps[1], src2, dst2, paint2);
            } else if (isBig1[7] && page == 2) {
                canvas.drawBitmap(bitmaps[7], src8, dst2, paint2);
            } else {
                canvas.drawCircle(cx2e, cy2e, litterRadius, circlePaint);
            }
        }

        if (isRadiationAnmation) {
            dst3.left = (float) (width / 2 - ((width / 2 - cx3e)) / 50 * delta - litterRadius * 1.1);
            dst3.top = (float) (width / 2 - ((width / 2 - cy3e) / 50) * delta - litterRadius * 1.1);
            dst3.right = (float) (dst3.left + litterRadius * 1.1 * 2);
            dst3.bottom = (float) (dst3.top + litterRadius * 1.1 * 2);
            if (isBig1[2] && page == 1) {
                canvas.drawBitmap(bitmaps[2], src3, dst3, paint3);
            } else if (isBig1[8] && page == 2) {
                canvas.drawBitmap(bitmaps[9], src9, dst3, paint3);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx3e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy3e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 3 || index == 9)) {
            dst3.left = cx3e - curRadius;
            dst3.top = cy3e - curRadius;
            dst3.right = cx3e + curRadius;
            dst3.bottom = cy3e + curRadius;
            if (index == 3) {
                canvas.drawBitmap(bitmaps[2], src3, dst3, paint3);
            }
            if (index == 9) {
                canvas.drawBitmap(bitmaps[8], src9, dst3, paint3);
            }
        } else {
            dst3.left = (float) (cx3e - litterRadius * 1.1);
            dst3.top = (float) (cy3e - litterRadius * 1.1);
            dst3.right = (float) (cx3e + litterRadius * 1.1);
            dst3.bottom = (float) (cy3e + litterRadius * 1.1);
            if (isBig1[2] && page == 1) {
                canvas.drawBitmap(bitmaps[2], src3, dst3, paint3);
            } else if (isBig1[8] && page == 2) {
                canvas.drawBitmap(bitmaps[8], src9, dst3, paint3);
            } else {
                canvas.drawCircle(cx3e, cy3e, litterRadius, circlePaint);
            }
        }

        if (isRadiationAnmation) {
            dst4.left = (float) (width / 2 - ((width / 2 - cx4e)) / 50 * delta - litterRadius * 1.1);
            dst4.top = (float) (width / 2 - ((width / 2 - cy4e) / 50) * delta - litterRadius * 1.1);
            dst4.right = (float) (dst4.left + litterRadius * 1.1 * 2);
            dst4.bottom = (float) (dst4.top + litterRadius * 1.1 * 2);
            if (isBig1[3] && page == 1) {
                canvas.drawBitmap(bitmaps[3], src4, dst4, paint4);
            } else if (isBig1[9] && page == 2) {
                canvas.drawBitmap(bitmaps[9], src10, dst4, paint4);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx4e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy4e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 4 || index == 10)) {
            dst4.left = cx4e - curRadius;
            dst4.top = cy4e - curRadius;
            dst4.right = cx4e + curRadius;
            dst4.bottom = cy4e + curRadius;
            if (index == 4) {
                canvas.drawBitmap(bitmaps[3], src4, dst4, paint4);
            }
            if (index == 10) {
                canvas.drawBitmap(bitmaps[9], src10, dst4, paint4);
            }
        } else {
            dst4.left = (float) (cx4e - litterRadius * 1.1);
            dst4.top = (float) (cy4e - litterRadius * 1.1);
            dst4.right = (float) (cx4e + litterRadius * 1.1);
            dst4.bottom = (float) (cy4e + litterRadius * 1.1);
            if (isBig1[3] && page == 1) {
                canvas.drawBitmap(bitmaps[3], src4, dst4, paint4);
            } else if (isBig1[9] && page == 2) {
                canvas.drawBitmap(bitmaps[9], src10, dst4, paint4);
            } else {
                canvas.drawCircle(cx4e, cy4e, litterRadius, circlePaint);
            }
        }

        if (isRadiationAnmation) {
            dst5.left = (float) (width / 2 - ((width / 2 - cx5e)) / 50 * delta - litterRadius * 1.1);
            dst5.top = (float) (width / 2 - ((width / 2 - cy5e) / 50) * delta - litterRadius * 1.1);
            dst5.right = (float) (dst5.left + litterRadius * 1.1 * 2);
            dst5.bottom = (float) (dst5.top + litterRadius * 1.1 * 2);
            if (isBig1[4] && page == 1) {
                canvas.drawBitmap(bitmaps[4], src5, dst5, paint5);
            } else if (isBig1[10] && page == 2) {
                canvas.drawBitmap(bitmaps[10], src11, dst5, paint5);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx5e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy5e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 5 || index == 11)) {
            dst5.left = cx5e - curRadius;
            dst5.top = cy5e - curRadius;
            dst5.right = cx5e + curRadius;
            dst5.bottom = cy5e + curRadius;
            if (index == 5) {
                canvas.drawBitmap(bitmaps[4], src5, dst5, paint5);
            }
            if (index == 11) {
                canvas.drawBitmap(bitmaps[10], src11, dst5, paint5);
            }
        } else {
            dst5.left = (float) (cx5e - litterRadius * 1.1);
            dst5.top = (float) (cy5e - litterRadius * 1.1);
            dst5.right = (float) (cx5e + litterRadius * 1.1);
            dst5.bottom = (float) (cy5e + litterRadius * 1.1);
            if (isBig1[4] && page == 1) {
                canvas.drawBitmap(bitmaps[4], src5, dst5, paint5);
            } else if (isBig1[10] && page == 2) {
                canvas.drawBitmap(bitmaps[10], src11, dst5, paint5);
            } else {
                canvas.drawCircle(cx5e, cy5e, litterRadius, circlePaint);
            }
        }

        if (isRadiationAnmation) {
            dst6.left = (float) (width / 2 - ((width / 2 - cx6e)) / 50 * delta - litterRadius * 1.1);
            dst6.top = (float) (width / 2 - ((width / 2 - cy6e) / 50) * delta - litterRadius * 1.1);
            dst6.right = (float) (dst5.left + litterRadius * 1.1 * 2);
            dst6.bottom = (float) (dst5.top + litterRadius * 1.1 * 2);
            if (isBig1[5] && page == 1) {
                canvas.drawBitmap(bitmaps[5], src6, dst6, paint6);
            } else if (isBig1[11] && page == 2) {
                canvas.drawBitmap(bitmaps[11], src12, dst6, paint6);
            } else {
                canvas.drawCircle(
                        width / 2 - ((width / 2 - cx6e)) / 50 * delta, width
                                / 2 - ((width / 2 - cy6e) / 50) * delta,
                        litterRadius, circlePaint);
            }
        } else if (isStartAnimation && (index == 6 || index == 12)) {
            dst6.left = cx6e - curRadius;
            dst6.top = cy6e - curRadius;
            dst6.right = cx6e + curRadius;
            dst6.bottom = cy6e + curRadius;
            if (index == 6) {
                canvas.drawBitmap(bitmaps[5], src6, dst6, paint6);
            }
            if (index == 12) {
                canvas.drawBitmap(bitmaps[11], src12, dst6, paint6);
            }
        } else {
            dst6.left = (float) (cx6e - litterRadius * 1.1);
            dst6.top = (float) (cy6e - litterRadius * 1.1);
            dst6.right = (float) (cx6e + litterRadius * 1.1);
            dst6.bottom = (float) (cy6e + litterRadius * 1.1);
            if (isBig1[5] && page == 1) {
                canvas.drawBitmap(bitmaps[5], src6, dst6, paint6);
            } else if (isBig1[11] && page == 2) {
                canvas.drawBitmap(bitmaps[11], src12, dst6, paint6);
            } else {
                canvas.drawCircle(cx6e, cy6e, litterRadius, circlePaint);
            }
        }

        if (isBig1[0])
            paint1.setColor(whiteColor);
        else
            paint1.setColor(colors[0]);

        if (isBig1[1])
            paint2.setColor(whiteColor);
        else
            paint2.setColor(colors[1]);

        if (isBig1[2])
            paint3.setColor(whiteColor);
        else
            paint3.setColor(colors[2]);

        if (isBig1[3])
            paint4.setColor(whiteColor);
        else
            paint4.setColor(colors[3]);

        if (isBig1[4])
            paint5.setColor(whiteColor);
        else
            paint5.setColor(colors[4]);

        if (isBig1[5])
            paint6.setColor(whiteColor);
        else
            paint6.setColor(colors[5]);

        if (isBig1[6])
            paint7.setColor(whiteColor);
        else
            paint7.setColor(colors[6]);

        if (isBig1[7])
            paint8.setColor(whiteColor);
        else
            paint8.setColor(colors[7]);

        if (isBig1[8])
            paint9.setColor(whiteColor);
        else
            paint9.setColor(colors[8]);

        if (isBig1[9])
            paint10.setColor(whiteColor);
        else
            paint10.setColor(colors[9]);

        if (isBig1[10])
            paint11.setColor(whiteColor);
        else
            paint11.setColor(colors[10]);

        if (isBig1[11])
            paint12.setColor(whiteColor);
        else
            paint12.setColor(colors[11]);

        if (isRadiationAnmation) {
            if (page == 1) {
                canvas.drawText(strs[0], width / 2 - ((width / 2 - cx1e)) / 50
                        * delta - rect1.width() / 2, width / 2
                        - ((width / 2 - cy1e) / 50) * delta + rect1.height()
                        / 2, paint1);
                canvas.drawText(strs[1], width / 2 - ((width / 2 - cx2e)) / 50
                        * delta - rect2.width() / 2, width / 2
                        - ((width / 2 - cy2e) / 50) * delta + rect2.height()
                        / 2, paint2);
                canvas.drawText(strs[2], width / 2 - ((width / 2 - cx3e)) / 50
                        * delta - rect3.width() / 2, width / 2
                        - ((width / 2 - cy3e) / 50) * delta + rect3.height()
                        / 2, paint3);
                canvas.drawText(strs[3], width / 2 - ((width / 2 - cx4e)) / 50
                        * delta - rect4.width() / 2, width / 2
                        - ((width / 2 - cy4e) / 50) * delta + rect4.height()
                        / 2, paint4);
                canvas.drawText(strs[4], width / 2 - ((width / 2 - cx5e)) / 50
                        * delta - rect5.width() / 2, width / 2
                        - ((width / 2 - cy5e) / 50) * delta + rect5.height()
                        / 2, paint5);
                canvas.drawText(strs[5], width / 2 - ((width / 2 - cx6e)) / 50
                        * delta - rect6.width() / 2, width / 2
                        - ((width / 2 - cy6e) / 50) * delta + rect6.height()
                        / 2, paint6);
            } else {
                canvas.drawText(strs[6], width / 2 - ((width / 2 - cx1e)) / 50
                        * delta - rect7.width() / 2, width / 2
                        - ((width / 2 - cy1e) / 50) * delta + rect7.height()
                        / 2, paint7);
                canvas.drawText(strs[7], width / 2 - ((width / 2 - cx2e)) / 50
                        * delta - rect8.width() / 2, width / 2
                        - ((width / 2 - cy2e) / 50) * delta + rect8.height()
                        / 2, paint8);
                canvas.drawText(strs[8], width / 2 - ((width / 2 - cx3e)) / 50
                        * delta - rect9.width() / 2, width / 2
                        - ((width / 2 - cy3e) / 50) * delta + rect9.height()
                        / 2, paint9);
                canvas.drawText(strs[9], width / 2 - ((width / 2 - cx4e)) / 50
                        * delta - rect10.width() / 2, width / 2
                        - ((width / 2 - cy4e) / 50) * delta + rect10.height()
                        / 2, paint10);
                canvas.drawText(strs[10], width / 2 - ((width / 2 - cx5e)) / 50
                        * delta - rect11.width() / 2, width / 2
                        - ((width / 2 - cy5e) / 50) * delta + rect11.height()
                        / 2, paint11);
                canvas.drawText(strs[11], width / 2 - ((width / 2 - cx6e)) / 50
                        * delta - rect12.width() / 2, width / 2
                        - ((width / 2 - cy6e) / 50) * delta + rect12.height()
                        / 2, paint12);
            }
        } else {
            if (page == 1) {
                canvas.drawText(strs[0], cx1e - rect1.width() / 2,
                        cy1e + rect1.height() / 2, paint1);
                canvas.drawText(strs[1], cx2e - rect2.width() / 2,
                        cy2e + rect2.height() / 2, paint2);
                canvas.drawText(strs[2], cx3e - rect3.width() / 2,
                        cy3e + rect3.height() / 2, paint3);
                canvas.drawText(strs[3], cx4e - rect4.width() / 2,
                        cy4e + rect4.height() / 2, paint4);
                canvas.drawText(strs[4], cx5e - rect5.width() / 2,
                        cy5e + rect5.height() / 2, paint5);
                canvas.drawText(strs[5], cx6e - rect6.width() / 2,
                        cy6e + rect6.height() / 2, paint6);
            } else {
                canvas.drawText(strs[6], cx1e - rect7.width() / 2,
                        cy1e + rect7.height() / 2, paint7);
                canvas.drawText(strs[7], cx2e - rect8.width() / 2,
                        cy2e + rect8.height() / 2, paint8);
                canvas.drawText(strs[8], cx3e - rect9.width() / 2,
                        cy3e + rect9.height() / 2, paint9);
                canvas.drawText(strs[9], cx4e - rect10.width() / 2, cy4e
                        + rect10.height() / 2, paint10);
                canvas.drawText(strs[10], cx5e - rect11.width() / 2, cy5e
                        + rect11.height() / 2, paint11);
                canvas.drawText(strs[11], cx6e - rect12.width() / 2, cy6e
                        + rect12.height() / 2, paint12);
            }
        }
    }

    class MyTimerTask extends TimerTask {

        private Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    private void initPoint() {
        points.clear();
        waterOffset = 0;
        Point point1 = new Point();
        point1.setY(waterLine - innerRadius * 2);
        point1.setX(startX);
        points.add(point1);
        Point point2 = new Point();
        point2.setY(waterLine - innerRadius * 2 - weaveHeight / 2);
        point2.setX(startX + weaveWidth / 4);
        points.add(point2);
        Point point3 = new Point();
        point3.setY(waterLine - innerRadius * 2);
        point3.setX(startX + weaveWidth / 2);
        points.add(point3);
        Point point4 = new Point();
        point4.setY(waterLine - innerRadius * 2 + weaveHeight / 2);
        point4.setX(startX + (weaveWidth / 4) * 3);
        points.add(point4);
        Point point5 = new Point();
        point5.setY(waterLine - innerRadius * 2);
        point5.setX(startX + weaveWidth);
        points.add(point5);
        Point point6 = new Point();
        point6.setY(waterLine - innerRadius * 2 - weaveHeight / 2);
        point6.setX(startX + (weaveWidth / 4) * 5);
        points.add(point6);
        Point point7 = new Point();
        point7.setY(waterLine - innerRadius * 2);
        point7.setX(startX + (weaveWidth / 4) * 6);
        points.add(point7);
        Point point8 = new Point();
        point8.setY(waterLine - innerRadius * 2 + weaveHeight / 2);
        point8.setX(startX + (weaveWidth / 4) * 7);
        points.add(point8);
        Point point9 = new Point();
        point9.setY(waterLine - innerRadius * 2);
        point9.setX(startX + weaveWidth * 2);
        points.add(point9);
    }

    class Point {
        float x;
        float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int position = getTouchPosition(event.getX(), event.getY());
            doClickEvent(position);
        }
        return super.onTouchEvent(event);
    }

    private void doClickEvent(int position) {
        waterH = 0;
        int n = 0;
        for (int i = 0; i < isBig1.length; i++) {
            if (isBig1[i]) {
                waterH += (innerRadius * 2) / 16;
                n++ ;
            }
        }
        if(n >= 6){
            centerPaint.setColor(Color.WHITE);
        }else if(n>=0){
            centerPaint.setColor(Color.GRAY);
        }
    }

    private int getTouchPosition(float x, float y) {
        double distance1 = (x - width / 2) * (x - width / 2) + (y - height / 2)
                * (y - height / 2);
        if (distance1 < innerRadius * innerRadius) {
            page = page == 1 ? 2 : 1;
            startRadiation();
            return 0;
        }
        double distance2 = (x - cx1e) * (x - cx1e) + (y - cy1e) * (y - cy1e);
        if (distance2 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[0] = !isBig1[0];
                if (isBig1[0]) {
                    selectColor = colors[0];
                    lastColor = colors[0];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(1);
            } else {
                isBig1[6] = !isBig1[6];
                if (isBig1[6]) {
                    selectColor = colors[6];
                    lastColor = colors[6];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(7);
            }
            return 1;
        }
        double distance3 = (x - cx2e) * (x - cx2e) + (y - cy2e) * (y - cy2e);
        if (distance3 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[1] = !isBig1[1];
                if (isBig1[1]) {
                    selectColor = colors[1];
                    lastColor = colors[1];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(2);
            } else {
                isBig1[7] = !isBig1[7];
                if (isBig1[7]) {
                    selectColor = colors[7];
                    lastColor = colors[7];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(8);
            }
            return 2;
        }
        double distance4 = (x - cx3e) * (x - cx3e) + (y - cy3e) * (y - cy3e);
        if (distance4 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[2] = !isBig1[2];
                if (isBig1[2]) {
                    selectColor = colors[2];
                    lastColor = colors[2];
                    startAnimation(3);
                } else {
                    selectColor = lastColor;
                }
            } else {
                isBig1[8] = !isBig1[8];
                if (isBig1[8]) {
                    selectColor = colors[8];
                    lastColor = colors[8];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(9);
            }
            return 3;
        }
        double distance5 = (x - cx4e) * (x - cx4e) + (y - cy4e) * (y - cy4e);
        if (distance5 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[3] = !isBig1[3];
                if (isBig1[3]) {
                    selectColor = colors[3];
                    lastColor = colors[3];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(4);
            } else {
                isBig1[9] = !isBig1[9];
                if (isBig1[9]) {
                    selectColor = colors[9];
                    lastColor = colors[9];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(10);
            }
            startAnimation(4);
            return 4;
        }
        double distance6 = (x - cx5e) * (x - cx5e) + (y - cy5e) * (y - cy5e);
        if (distance6 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[4] = !isBig1[4];
                if (isBig1[4]) {
                    selectColor = colors[4];
                    lastColor = colors[4];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(5);
            } else {
                isBig1[10] = !isBig1[10];
                if (isBig1[10]) {
                    selectColor = colors[10];
                    lastColor = colors[10];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(11);
            }
            return 5;
        }
        double distance7 = (x - cx6e) * (x - cx6e) + (y - cy6e) * (y - cy6e);
        if (distance7 < bigRadius * bigRadius) {
            if (page == 1) {
                isBig1[5] = !isBig1[5];
                if (isBig1[5]) {
                    selectColor = colors[5];
                    lastColor = colors[5];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(6);
            } else {
                isBig1[11] = !isBig1[11];
                if (isBig1[11]) {
                    selectColor = colors[11];
                    lastColor = colors[11];
                } else {
                    selectColor = lastColor;
                }
                startAnimation(12);
            }
            return 6;
        }
        return -1;
    }

    private int index;
    private int curRadius;
    private boolean isStartAnimation;

    private void startAnimation(int index) {
        this.index = index;
        this.isStartAnimation = true;
    }

    private boolean isRadiationAnmation;

    private void startRadiation() {
        this.isRadiationAnmation = true;
    }

    public String getSelect() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < isBig1.length; i++) {
            if (isBig1[i]) {
                sb.append("{");
                sb.append("\"");
                sb.append("tagId");
                sb.append("\"");
                sb.append(":");
                sb.append("\"");
                sb.append(i+1);
                sb.append("\"");
                sb.append(",");
                sb.append("\"");
                sb.append("tagName");
                sb.append("\"");
                sb.append(":");
                sb.append("\"");
                sb.append(strs[i]);
                sb.append("\"");
                sb.append("}");
                sb.append(",");
            }
        }
        String result = sb.toString();
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        }
        //SharedPreferencesUtil.getsInstance().put("prefencesview", result);
        return result;
    }

    public void stop(){
        if(timerTask!=null)
            timerTask.cancel();
    }
}
