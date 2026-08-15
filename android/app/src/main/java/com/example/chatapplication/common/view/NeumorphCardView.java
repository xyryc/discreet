package com.example.chatapplication.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.chatapplication.R;

public class NeumorphCardView extends FrameLayout {

    public static final int SHAPE_FLAT = 0;
    public static final int SHAPE_PRESSED = 1;

    private float cornerRadius;
    private float shadowDistance;
    private float shadowBlur;
    private int darkShadowColor;
    private int lightShadowColor;
    private int cardBackgroundColor;
    private int strokeColor;
    private float strokeWidth;
    private int shapeType = SHAPE_FLAT;

    private Paint darkShadowPaint;
    private Paint lightShadowPaint;
    private Paint surfacePaint;
    private Paint strokePaint;

    private final RectF surfaceRect = new RectF();
    private final RectF darkShadowRect = new RectF();
    private final RectF lightShadowRect = new RectF();
    private final Path surfacePath = new Path();

    private boolean isPressedState = false;

    public NeumorphCardView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public NeumorphCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NeumorphCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_SOFTWARE, null); // Required for BlurMaskFilter

        // Default dimensions
        float density = context.getResources().getDisplayMetrics().density;
        cornerRadius = 24 * density;
        shadowDistance = 5 * density;
        shadowBlur = 9 * density;
        strokeWidth = 0.8f * density;

        // Default colors
        darkShadowColor = ContextCompat.getColor(context, R.color.neu_dark_shadow);
        lightShadowColor = ContextCompat.getColor(context, R.color.neu_light_shadow);
        cardBackgroundColor = ContextCompat.getColor(context, R.color.neu_surface);
        strokeColor = Color.parseColor("#FFFFFF");

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NeumorphCardView);
            cornerRadius = a.getDimension(R.styleable.NeumorphCardView_neu_corner_radius, cornerRadius);
            shadowDistance = a.getDimension(R.styleable.NeumorphCardView_neu_shadow_distance, shadowDistance);
            shadowBlur = a.getDimension(R.styleable.NeumorphCardView_neu_shadow_blur, shadowBlur);
            darkShadowColor = a.getColor(R.styleable.NeumorphCardView_neu_dark_shadow_color, darkShadowColor);
            lightShadowColor = a.getColor(R.styleable.NeumorphCardView_neu_light_shadow_color, lightShadowColor);
            cardBackgroundColor = a.getColor(R.styleable.NeumorphCardView_neu_background_color, cardBackgroundColor);
            strokeColor = a.getColor(R.styleable.NeumorphCardView_neu_stroke_color, strokeColor);
            strokeWidth = a.getDimension(R.styleable.NeumorphCardView_neu_stroke_width, strokeWidth);
            shapeType = a.getInt(R.styleable.NeumorphCardView_neu_shape_type, SHAPE_FLAT);
            a.recycle();
        }

        setupPaints();
        updatePadding();
    }

    private void setupPaints() {
        // Dark Bottom-Right Shadow (Gaussian Blur)
        darkShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        darkShadowPaint.setColor(darkShadowColor);
        darkShadowPaint.setStyle(Paint.Style.FILL);
        if (shadowBlur > 0) {
            darkShadowPaint.setMaskFilter(new BlurMaskFilter(shadowBlur, BlurMaskFilter.Blur.NORMAL));
        }

        // Light Top-Left Shadow (Gaussian Blur)
        lightShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lightShadowPaint.setColor(lightShadowColor);
        lightShadowPaint.setStyle(Paint.Style.FILL);
        if (shadowBlur > 0) {
            lightShadowPaint.setMaskFilter(new BlurMaskFilter(shadowBlur, BlurMaskFilter.Blur.NORMAL));
        }

        // Main Surface Body
        surfacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        surfacePaint.setColor(cardBackgroundColor);
        surfacePaint.setStyle(Paint.Style.FILL);

        // Highlight Perimeter Stroke
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
    }

    private void updatePadding() {
        int pad = (int) (shadowBlur + shadowDistance);
        // Ensure child content has safe space inside the shadow blur margin
        int currentLeft = getPaddingLeft();
        int currentTop = getPaddingTop();
        int currentRight = getPaddingRight();
        int currentBottom = getPaddingBottom();

        if (currentLeft < pad && currentTop < pad && currentRight < pad && currentBottom < pad) {
            super.setPadding(pad, pad, pad, pad);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float pad = shadowBlur + shadowDistance;
        surfaceRect.set(pad, pad, w - pad, h - pad);

        // Dark Shadow Rect (Offset to Bottom-Right)
        darkShadowRect.set(
                surfaceRect.left + shadowDistance,
                surfaceRect.top + shadowDistance,
                surfaceRect.right + shadowDistance,
                surfaceRect.bottom + shadowDistance
        );

        // Light Shadow Rect (Offset to Top-Left)
        lightShadowRect.set(
                surfaceRect.left - shadowDistance,
                surfaceRect.top - shadowDistance,
                surfaceRect.right - shadowDistance,
                surfaceRect.bottom - shadowDistance
        );

        surfacePath.reset();
        surfacePath.addRoundRect(surfaceRect, cornerRadius, cornerRadius, Path.Direction.CW);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (shapeType == SHAPE_PRESSED) {
            drawPressedState(canvas);
        } else {
            drawFlatState(canvas);
        }

        super.dispatchDraw(canvas);
    }

    private void drawFlatState(Canvas canvas) {
        float currentDistance = isPressedState ? (shadowDistance * 0.4f) : shadowDistance;

        // Compute dynamic shadow rects based on touch state
        lightShadowRect.set(
                surfaceRect.left - currentDistance,
                surfaceRect.top - currentDistance,
                surfaceRect.right - currentDistance,
                surfaceRect.bottom - currentDistance
        );

        darkShadowRect.set(
                surfaceRect.left + currentDistance,
                surfaceRect.top + currentDistance,
                surfaceRect.right + currentDistance,
                surfaceRect.bottom + currentDistance
        );

        // 1. Draw Top-Left Light Glow Shadow (Gaussian Blur)
        canvas.drawRoundRect(lightShadowRect, cornerRadius, cornerRadius, lightShadowPaint);

        // 2. Draw Bottom-Right Dark Shadow (Gaussian Blur)
        canvas.drawRoundRect(darkShadowRect, cornerRadius, cornerRadius, darkShadowPaint);

        // 3. Draw Main Extruded Surface
        canvas.drawRoundRect(surfaceRect, cornerRadius, cornerRadius, surfacePaint);

        // 4. Draw Crisp Highlight Stroke
        if (strokeWidth > 0) {
            canvas.drawRoundRect(surfaceRect, cornerRadius, cornerRadius, strokePaint);
        }
    }

    private void drawPressedState(Canvas canvas) {
        // Draw Inset Depressed Surface
        canvas.drawRoundRect(surfaceRect, cornerRadius, cornerRadius, surfacePaint);

        canvas.save();
        canvas.clipPath(surfacePath);

        // Inset Dark Shadow at Top-Left
        darkShadowRect.set(
                surfaceRect.left - shadowDistance,
                surfaceRect.top - shadowDistance,
                surfaceRect.right,
                surfaceRect.bottom
        );
        canvas.drawRoundRect(darkShadowRect, cornerRadius, cornerRadius, darkShadowPaint);

        // Inset Light Highlight at Bottom-Right
        lightShadowRect.set(
                surfaceRect.left,
                surfaceRect.top,
                surfaceRect.right + shadowDistance,
                surfaceRect.bottom + shadowDistance
        );
        canvas.drawRoundRect(lightShadowRect, cornerRadius, cornerRadius, lightShadowPaint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isPressedState = true;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isPressedState = false;
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public void setShadowDistance(float shadowDistance) {
        this.shadowDistance = shadowDistance;
        setupPaints();
        invalidate();
    }

    public void setShadowBlur(float shadowBlur) {
        this.shadowBlur = shadowBlur;
        setupPaints();
        invalidate();
    }

    public void setCardBackgroundColor(int cardBackgroundColor) {
        this.cardBackgroundColor = cardBackgroundColor;
        surfacePaint.setColor(cardBackgroundColor);
        invalidate();
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
        invalidate();
    }
}
