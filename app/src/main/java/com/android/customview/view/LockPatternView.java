package com.android.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.os.Build;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.android.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays and detects the user's unlock attempt, which is a drag of a finger
 * across 9 regions of the screen.
 * <p/>
 * Is also capable of displaying a static pattern in "in progress", "wrong" or
 * "correct" states.
 */
public class LockPatternView extends View {

    /**
     * Aspect to use when rendering this view. View will be the minimum of
     * width/height.
     */
    private static final int ASPECT_SQUARE = 0;
    /**
     * Fixed width; height will be minimum of (w,h)
     */
    private static final int ASPECT_LOCK_WIDTH = 1;
    /**
     * Fixed height; width will be minimum of (w,h)
     */
    private static final int ASPECT_LOCK_HEIGHT = 2;

    /**
     * This is the width of the matrix (the number of dots per row and column).
     * Change this value to change the dimension of the pattern's matrix.
     *
     * @since v2.7 beta
     * @author Thomas Breitbach
     */
    public static final int MATRIX_WIDTH = 3;

    /**
     * The size of the pattern's matrix.
     */
    public static final int MATRIX_SIZE = MATRIX_WIDTH * MATRIX_WIDTH;

    private static final boolean PROFILE_DRAWING = false;
    private boolean mDrawingProfilingStarted = false;

    private Paint mPaint = new Paint();
    private Paint mPathPaint = new Paint();

    /**
     * This can be used to avoid updating the display for very small motions or
     * noisy panels. It didn't seem to have much impact on the devices tested,
     * so currently set to 0.
     */
    private static final float DRAG_THRESHHOLD = 0.0f;

    private OnPatternListener mOnPatternListener;
    private ArrayList<Cell> mPattern = new ArrayList<Cell>(MATRIX_SIZE);

    /**
     * Lookup table for the circles of the pattern we are currently drawing.
     * This will be the cells of the complete pattern unless we are animating,
     * in which case we use this to hold the cells we are drawing for the in
     * progress animation.
     */
    private boolean[][] mPatternDrawLookup = new boolean[MATRIX_WIDTH][MATRIX_WIDTH];

    /**
     * the in progress point: - during interaction: where the user's finger is -
     * during animation: the current tip of the animating line
     */
    private float mInProgressX = -1;
    private float mInProgressY = -1;

    private DisplayMode mPatternDisplayMode = DisplayMode.Correct;
    private boolean mInStealthMode = false;
    private boolean mEnableHapticFeedback = true;
    private boolean mPatternInProgress = false;

    private float mDiameterFactor = 0.10f;
    private final int mStrokeAlpha = 128;
    private float mHitFactor = 0.6f;

    private float mSquareWidth;
    private float mSquareHeight;

    private float mPathWidth = -1;
    private int mPathAlpha = 255;
    private int mPathColor = Color.rgb(175, 175, 175);
    private int mPathWrongColor = Color.rgb(220, 37, 59);

    private PathDashPathEffect mPathDashPathEffect;

    private Bitmap mBitmapCircleDefault;
    private Bitmap mBitmapCircleOK;
    private Bitmap mBitmapCircleError;
    private boolean mBRequestParrentViewTouch = false;
    private boolean mBAllRequestParrent = false;

    private final Path mCurrentPath = new Path();
    private final Rect mInvalidate = new Rect();
    private final Rect mTmpInvalidateRect = new Rect();

    private int mBitmapWidth;
    private int mBitmapHeight;

    private int mAspect;
    private final Matrix mCircleMatrix = new Matrix();

    private final int mPadding = 0;
    private final int mPaddingLeft = mPadding;
    private final int mPaddingRight = mPadding;
    private final int mPaddingTop = mPadding;
    private final int mPaddingBottom = mPadding;

    private boolean mIsUsing = false; // ͼ����ͼ�Ƿ񱻴�����
    private boolean mShowOnLockScreen = false;

    /**
     * Represents a cell in the MATRIX_WIDTH x MATRIX_WIDTH matrix of the unlock
     * pattern view.
     */
    public static class Cell{

        public int mRow;
        public int mColumn;

        /*
         * keep # objects limited to MATRIX_SIZE
         */
        static Cell[][] sCells = new Cell[MATRIX_WIDTH][MATRIX_WIDTH];
        static {
            for (int i = 0; i < MATRIX_WIDTH; i++) {
                for (int j = 0; j < MATRIX_WIDTH; j++) {
                    sCells[i][j] = new Cell(i, j);
                }
            }
        }

        /**
         * @param row
         *            The row of the cell.
         * @param column
         *            The column of the cell.
         */
        private Cell(int row, int column) {
            checkRange(row, column);
            this.mRow = row;
            this.mColumn = column;
        }

        /**
         * Gets the row index.
         *
         * @return the row index.
         */
        public int getRow() {
            return mRow;
        }// getRow()

        /**
         * Gets the column index.
         *
         * @return the column index.
         */
        public int getColumn() {
            return mColumn;
        }// getColumn()

        /**
         * Gets the ID.It is counted from left to right, top to bottom of the
         * matrix, starting by zero.
         *
         * @return the ID.
         */
        public int getId() {
            return mRow * MATRIX_WIDTH + mColumn;
        }// getId()

        /**
         * @param row
         *            The row of the cell.
         * @param column
         *            The column of the cell.
         */
        public static synchronized Cell of(int row, int column) {
            checkRange(row, column);
            return sCells[row][column];
        }

        /**
         * Gets a cell from its ID.
         *
         * @param id
         *            the cell ID.
         * @return the cell.
         * @since v2.7 beta
         * @author Hai Bison
         */
        public static synchronized Cell of(int id) {
            return of(id / MATRIX_WIDTH, id % MATRIX_WIDTH);
        }// of()

        private static void checkRange(int row, int column) {
            if (row < 0 || row > MATRIX_WIDTH - 1) {
                throw new IllegalArgumentException("row must be in range 0-"
                        + (MATRIX_WIDTH - 1));
            }
            if (column < 0 || column > MATRIX_WIDTH - 1) {
                throw new IllegalArgumentException("column must be in range 0-"
                        + (MATRIX_WIDTH - 1));
            }
        }

        @Override
        public String toString() {
            return "(ROW=" + getRow() + ",COL=" + getColumn() + ")";
        }// toString()

        @Override
        public boolean equals(Object object) {
            if (object instanceof Cell)
                return getColumn() == ((Cell) object).getColumn()
                        && getRow() == ((Cell) object).getRow();
            return super.equals(object);
        }// equals()
    }// Cell

    /**
     * How to display the current pattern.
     */
    public enum DisplayMode {

        /**
         * The pattern drawn is correct (i.e draw it in a friendly color)
         */
        Correct,

        /**
         * The pattern is wrong (i.e draw a foreboding color)
         */
        Wrong
    }

    /**
     * The call back interface for detecting patterns entered by the user.
     */
    public static interface OnPatternListener {

        /**
         * A new pattern has begun.
         */
        void onPatternStart();

        /**
         * The pattern was cleared.
         */
        void onPatternCleared();

        /**
         * The user extended the pattern currently being drawn by one cell.
         *
         * @param pattern
         *            The pattern with newly added cell.
         */
        void onPatternCellAdded(List<Cell> pattern);

        /**
         * A pattern was detected from the user.
         *
         * @param pattern
         *            The pattern.
         */
        void onPatternDetected(List<Cell> pattern);
    }

    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.LockPatternView);

        mShowOnLockScreen = array.getBoolean(R.styleable.LockPatternView_showOnLockScreen, false);

        array.recycle();

        final String aspect = "";// a.getString(R.styleable.LockPatternView_aspect);

        if ("square".equals(aspect)) {
            mAspect = ASPECT_SQUARE;
        } else if ("lock_width".equals(aspect)) {
            mAspect = ASPECT_LOCK_WIDTH;
        } else if ("lock_height".equals(aspect)) {
            mAspect = ASPECT_LOCK_HEIGHT;
        } else {
            mAspect = ASPECT_SQUARE;
        }

        setClickable(true);

        mPathPaint.setAntiAlias(true);
        mPathPaint.setDither(true);
//        mPathPaint.setColor(getContext().getResources().getColor(
//                UI.resolveAttribute(getContext(),
//                        R.attr.alp_42447968_color_pattern_path)));
        mPathPaint.setColor(mPathColor);
        mPathPaint.setAlpha(mStrokeAlpha);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

        loadDefaultRes();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2)
            mBAllRequestParrent = true;
    }// LockPatternView()

    public void resetRes(){
        final Bitmap bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        mBitmapCircleDefault = mBitmapCircleOK = mBitmapCircleError = bm;
        mBitmapWidth = mBitmapHeight = 1;
    }

    public void loadDefaultRes(){
        mBitmapCircleDefault = getBitmapFor(
                mShowOnLockScreen ? R.mipmap.password_pattern_circle_normal_white : R.mipmap.password_pattern_circle_normal_gray);
        mBitmapCircleOK = getBitmapFor(R.mipmap.password_pattern_circle_ok);
        mBitmapCircleError = getBitmapFor(R.mipmap.password_pattern_circle_error);

        /*
         * bitmaps have the size of the largest bitmap in this group
         */
        final Bitmap bitmaps[] = { mBitmapCircleDefault, mBitmapCircleOK, mBitmapCircleError };

        for (Bitmap bitmap : bitmaps) {
            mBitmapWidth = Math.max(mBitmapWidth, bitmap.getWidth());
            mBitmapHeight = Math.max(mBitmapHeight, bitmap.getHeight());
        }

        mPathWidth = 3;
    }

    public boolean isUsing(){
        return mIsUsing;
    }

    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }

    /**
     * @return Whether the view is in stealth mode.
     */
    public boolean isInStealthMode() {
        return mInStealthMode;
    }

    /**
     * @return Whether the view has tactile feedback enabled.
     */
    public boolean isTactileFeedbackEnabled() {
        return mEnableHapticFeedback;
    }

    /**
     * Set whether the view is in stealth mode. If true, there will be no
     * visible feedback as the user enters the pattern.
     *
     * @param inStealthMode
     *            Whether in stealth mode.
     */
    public void setInStealthMode(boolean inStealthMode) {
        mInStealthMode = inStealthMode;
    }

    /**
     * Set whether the view will use tactile feedback. If true, there will be
     * tactile feedback as the user enters the pattern.
     *
     * @param tactileFeedbackEnabled
     *            Whether tactile feedback is enabled
     */
    public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
        mEnableHapticFeedback = tactileFeedbackEnabled;
    }

    /**
     * Set the call back for pattern detection.
     *
     * @param onPatternListener
     *            The call back.
     */
    public void setOnPatternListener(OnPatternListener onPatternListener) {
        mOnPatternListener = onPatternListener;
    }

    /**
     * Set the pattern explicitely (rather than waiting for the user to input a
     * pattern).
     *
     * @param displayMode
     *            How to display the pattern.
     * @param pattern
     *            The pattern.
     */
    public void setPattern(DisplayMode displayMode, List<Cell> pattern) {
        mPattern.clear();
        mPattern.addAll(pattern);
        clearPatternDrawLookup();
        for (Cell cell : pattern) {
            mPatternDrawLookup[cell.getRow()][cell.getColumn()] = true;
        }

        setDisplayMode(displayMode);
    }

    /**
     * Set the display mode of the current pattern. This can be useful, for
     * instance, after detecting a pattern to tell this view whether change the
     * in progress result to correct or wrong.
     *
     * @param displayMode
     *            The display mode.
     */
    public void setDisplayMode(DisplayMode displayMode) {
        mPatternDisplayMode = displayMode;
        invalidate();
    }

    /**
     * Retrieves last display mode. This method is useful in case of storing
     * states and restoring them after screen orientation changed.
     *
     * @return {@link DisplayMode}
     * @since v1.5.3 beta
     */
    public DisplayMode getDisplayMode() {
        return mPatternDisplayMode;
    }

    /**
     * Retrieves current displaying pattern. This method is useful in case of
     * storing states and restoring them after screen orientation changed.
     *
     * @return current displaying pattern. <b>Note:</b> This is an independent
     *         list with the view's pattern itself.
     * @since v1.5.3 beta
     */
    @SuppressWarnings("unchecked")
    public List<Cell> getPattern() {
        return (List<Cell>) mPattern.clone();
    }

    private void notifyCellAdded() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternCellAdded(mPattern);
        }
    }

    private void notifyPatternStarted() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternStart();
        }
    }

    private void notifyPatternDetected() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternDetected(mPattern);
        }
    }

    private void notifyPatternCleared() {
        if (mOnPatternListener != null) {
            mOnPatternListener.onPatternCleared();
        }
    }

    /**
     * Clear the pattern.
     */
    public void clearPattern() {
        resetPattern();
    }

    /**
     * Reset all pattern state.
     */
    private void resetPattern() {
        mPattern.clear();
        clearPatternDrawLookup();
        mPatternDisplayMode = DisplayMode.Correct;
        invalidate();
    }

    /**
     * Clear the pattern lookup table.
     */
    private void clearPatternDrawLookup() {
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                mPatternDrawLookup[i][j] = false;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int width = w - mPaddingLeft - mPaddingRight;
        mSquareWidth = width / (float) MATRIX_WIDTH;

        final int height = h - mPaddingTop - mPaddingBottom;
        mSquareHeight = height / (float) MATRIX_WIDTH;
    }

    @SuppressWarnings("unused")
    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        /*
         * View should be large enough to contain MATRIX_WIDTH side-by-side
         * target bitmaps
         */
        int w = MATRIX_WIDTH * mBitmapWidth;
        int screenW = getResources().getDisplayMetrics().widthPixels;
        return Math.min(screenW, w);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        /*
         * View should be large enough to contain MATRIX_WIDTH side-by-side
         * target bitmaps
         */
        int h = MATRIX_WIDTH * mBitmapWidth;
        int screenH = getResources().getDisplayMetrics().heightPixels;
        return Math.min(screenH, h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();

        //int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        //int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        int viewWidth = getDefaultSize(minimumWidth, widthMeasureSpec);
        int viewHeight = getDefaultSize(minimumHeight, heightMeasureSpec);

        switch (mAspect) {
            case ASPECT_SQUARE:
                viewWidth = viewHeight = Math.min(viewWidth, viewHeight);
                break;
            case ASPECT_LOCK_WIDTH:
                viewHeight = Math.min(viewWidth, viewHeight);
                break;
            case ASPECT_LOCK_HEIGHT:
                viewWidth = Math.min(viewWidth, viewHeight);
                break;
        }

        setMeasuredDimension(viewWidth, viewHeight);
    }

    /**
     * Determines whether the point x, y will add a new point to the current
     * pattern (in addition to finding the cell, also makes heuristic choices
     * such as filling in gaps based on current pattern).
     *
     * @param x
     *            The x coordinate.
     * @param y
     *            The y coordinate.
     */
    private Cell detectAndAddHit(float x, float y) {
        final Cell cell = checkForNewHit(x, y);
        if (cell != null) {

            /*
             * check for gaps in existing pattern
             */
            Cell fillInGapCell = null;
            final ArrayList<Cell> pattern = mPattern;
            if (!pattern.isEmpty()) {
                final Cell lastCell = pattern.get(pattern.size() - 1);
                int dRow = cell.mRow - lastCell.mRow;
                int dColumn = cell.mColumn - lastCell.mColumn;

                int fillInRow = lastCell.mRow;
                int fillInColumn = lastCell.mColumn;

                if (Math.abs(dRow) == 2 && Math.abs(dColumn) != 1) {
                    fillInRow = lastCell.mRow + ((dRow > 0) ? 1 : -1);
                }

                if (Math.abs(dColumn) == 2 && Math.abs(dRow) != 1) {
                    fillInColumn = lastCell.mColumn + ((dColumn > 0) ? 1 : -1);
                }

                fillInGapCell = Cell.of(fillInRow, fillInColumn);
            }

            if (fillInGapCell != null
                    && !mPatternDrawLookup[fillInGapCell.mRow][fillInGapCell.mColumn]) {
                addCellToPattern(fillInGapCell);
            }
            addCellToPattern(cell);
            if (mEnableHapticFeedback) {
                performHapticFeedback(
                        HapticFeedbackConstants.VIRTUAL_KEY,
                        HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                                | HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
            return cell;
        }
        return null;
    }

    private void addCellToPattern(Cell newCell) {
        mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
        mPattern.add(newCell);
        notifyCellAdded();
    }

    /**
     * Helper method to find which cell a point maps to.
     *
     * @param x
     * @param y
     * @return
     */
    private Cell checkForNewHit(float x, float y) {

        final int rowHit = getRowHit(y);
        if (rowHit < 0) {
            return null;
        }
        final int columnHit = getColumnHit(x);
        if (columnHit < 0) {
            return null;
        }

        if (mPatternDrawLookup[rowHit][columnHit]) {
            return null;
        }
        return Cell.of(rowHit, columnHit);
    }

    /**
     * Helper method to find the row that y falls into.
     *
     * @param y
     *            The y coordinate
     * @return The row that y falls in, or -1 if it falls in no row.
     */
    private int getRowHit(float y) {

        final float squareHeight = mSquareHeight;
        float hitSize = squareHeight * mHitFactor;

        float offset = mPaddingTop + (squareHeight - hitSize) / 2f;
        for (int i = 0; i < MATRIX_WIDTH; i++) {

            final float hitTop = offset + squareHeight * i;
            if (y >= hitTop && y <= hitTop + hitSize) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Helper method to find the column x fallis into.
     *
     * @param x
     *            The x coordinate.
     * @return The column that x falls in, or -1 if it falls in no column.
     */
    private int getColumnHit(float x) {
        final float squareWidth = mSquareWidth;
        float hitSize = squareWidth * mHitFactor;

        float offset = mPaddingLeft + (squareWidth - hitSize) / 2f;
        for (int i = 0; i < MATRIX_WIDTH; i++) {

            final float hitLeft = offset + squareWidth * i;
            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        // TODO Auto-generated method stub
        //if (mBAllRequestParrent || mBRequestParrentViewTouch) {
        ViewParent vp = getParent();
        do
        {
            vp.requestDisallowInterceptTouchEvent(true); //
            vp = vp.getParent();
        }while(vp != null);
        //}

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsUsing = true;
                handleActionDown(event);
                return true;
            case MotionEvent.ACTION_UP:
                mBRequestParrentViewTouch = false;
                mIsUsing = false;
                handleActionUp(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                mIsUsing = true;
                handleActionMove(event);
                return true;
            case MotionEvent.ACTION_CANCEL:
                mBRequestParrentViewTouch = false;
            /*
             * Original source check for mPatternInProgress == true first before
             * calling next three lines. But if we do that, there will be
             * nothing happened when the user taps at empty area and releases
             * the finger. We want the pattern to be reset and the message will
             * be updated after the user did that.
             */
                mIsUsing = false;
                mPatternInProgress = false;
                resetPattern();
                notifyPatternCleared();

                if (PROFILE_DRAWING) {
                    if (mDrawingProfilingStarted) {
                        Debug.stopMethodTracing();
                        mDrawingProfilingStarted = false;
                    }
                }
                return true;
        }
        return false;
    }

    private void handleActionMove(MotionEvent event) {
        /*
         * Handle all recent motion events so we don't skip any cells even when
         * the device is busy...
         */
        final float radius = (mSquareWidth * mDiameterFactor * 0.5f);
        final int historySize = event.getHistorySize();
        mTmpInvalidateRect.setEmpty();
        boolean invalidateNow = false;
        for (int i = 0; i < historySize + 1; i++) {
            final float x = i < historySize ? event.getHistoricalX(i) : event.getX();
            final float y = i < historySize ? event.getHistoricalY(i) : event.getY();
            Cell hitCell = detectAndAddHit(x, y);
            final int patternSize = mPattern.size();
            if (hitCell != null && patternSize == 1) {
                mPatternInProgress = true;
                notifyPatternStarted();
            }
            /*
             * note current x and y for rubber banding of in progress patterns
             */
            final float dx = Math.abs(x - mInProgressX);
            final float dy = Math.abs(y - mInProgressY);
            if (dx > DRAG_THRESHHOLD || dy > DRAG_THRESHHOLD) {
                invalidateNow = true;
            }

            if (mPatternInProgress && patternSize > 0) {
                final ArrayList<Cell> pattern = mPattern;
                final Cell lastCell = pattern.get(patternSize - 1);
                float lastCellCenterX = getCenterXForColumn(lastCell.mColumn);
                float lastCellCenterY = getCenterYForRow(lastCell.mRow);

                /*
                 * Adjust for drawn segment from last cell to (x,y). Radius
                 * accounts for line width.
                 */
                float left = Math.min(lastCellCenterX, x) - radius;
                float right = Math.max(lastCellCenterX, x) + radius;
                float top = Math.min(lastCellCenterY, y) - radius;
                float bottom = Math.max(lastCellCenterY, y) + radius;

                /*
                 * Invalidate between the pattern's new cell and the pattern's
                 * previous cell
                 */
                if (hitCell != null) {
                    final float width = mSquareWidth * 0.5f;
                    final float height = mSquareHeight * 0.5f;
                    final float hitCellCenterX = getCenterXForColumn(hitCell.mColumn);
                    final float hitCellCenterY = getCenterYForRow(hitCell.mRow);

                    left = Math.min(hitCellCenterX - width, left);
                    right = Math.max(hitCellCenterX + width, right);
                    top = Math.min(hitCellCenterY - height, top);
                    bottom = Math.max(hitCellCenterY + height, bottom);
                }

                /*
                 * Invalidate between the pattern's last cell and the previous
                 * location
                 */
                mTmpInvalidateRect.union(Math.round(left), Math.round(top),
                        Math.round(right), Math.round(bottom));
            }
        }
        mInProgressX = event.getX();
        mInProgressY = event.getY();

        /*
         * To save updates, we only invalidate if the user moved beyond a
         * certain amount.
         */
        if (invalidateNow) {
            mInvalidate.union(mTmpInvalidateRect);
            invalidate(mInvalidate);
            mInvalidate.set(mTmpInvalidateRect);
            invalidate(mTmpInvalidateRect);
        }
    }

    private void handleActionUp(MotionEvent event) {
        /*
         * report pattern detected
         */
        if (!mPattern.isEmpty()) {
            mPatternInProgress = false;
            notifyPatternDetected();
            invalidate();
        }
        if (PROFILE_DRAWING) {
            if (mDrawingProfilingStarted) {
                Debug.stopMethodTracing();
                mDrawingProfilingStarted = false;
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        resetPattern();
        final float x = event.getX();
        final float y = event.getY();
        final Cell hitCell = detectAndAddHit(x, y);
        if (hitCell != null) {
            mBRequestParrentViewTouch = true;
            mPatternInProgress = true;
            mPatternDisplayMode = DisplayMode.Correct;
            notifyPatternStarted();
        } else {
            /*
             * Original source check for mPatternInProgress == true first before
             * calling this block. But if we do that, there will be nothing
             * happened when the user taps at empty area and releases the
             * finger. We want the pattern to be reset and the message will be
             * updated after the user did that.
             */
            mPatternInProgress = false;
            notifyPatternCleared();
        }
        if (hitCell != null) {
            final float startX = getCenterXForColumn(hitCell.mColumn);
            final float startY = getCenterYForRow(hitCell.mRow);

            final float widthOffset = mSquareWidth / 2f;
            final float heightOffset = mSquareHeight / 2f;

            invalidate((int) (startX - widthOffset),
                    (int) (startY - heightOffset),
                    (int) (startX + widthOffset), (int) (startY + heightOffset));
        }
        mInProgressX = x;
        mInProgressY = y;
        if (PROFILE_DRAWING) {
            if (!mDrawingProfilingStarted) {
                Debug.startMethodTracing("LockPatternDrawing");
                mDrawingProfilingStarted = true;
            }
        }
    }

    private float getCenterXForColumn(int column) {
        return mPaddingLeft + column * mSquareWidth + mSquareWidth / 2f;
    }

    private float getCenterYForRow(int row) {
        return mPaddingTop + row * mSquareHeight + mSquareHeight / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final ArrayList<Cell> pattern = mPattern;
        final int count = pattern.size();
        final boolean[][] drawLookup = mPatternDrawLookup;

        final float squareWidth = mSquareWidth;
        final float squareHeight = mSquareHeight;

        mPathPaint.setColor(mPathColor);

        final Path currentPath = mCurrentPath;
        currentPath.rewind();

        /*
         * TODO: the path should be created and cached every time we hit-detect
         * a cell only the last segment of the path should be computed here draw
         * the path of the pattern (unless the user is in progress, and we are
         * in stealth mode)
         */
        final boolean drawPath = (!mInStealthMode || mPatternDisplayMode == DisplayMode.Wrong);

        /*
         * draw the arrows associated with the path (unless the user is in
         * progress, and we are in stealth mode)
         */
        boolean oldFlag = (mPaint.getFlags() & Paint.FILTER_BITMAP_FLAG) != 0;
        /*
         * draw with higher quality since we render with transforms
         */
        mPaint.setFilterBitmap(true);

        if (drawPath) {
            boolean anyCircles = false;
            for (int i = 0; i < count; i++) {
                Cell cell = pattern.get(i);

                /*
                 * only draw the part of the pattern stored in the lookup table
                 * (this is only different in the case of animation).
                 */
                if (!drawLookup[cell.mRow][cell.mColumn]) {
                    break;
                }
                anyCircles = true;

                float centerX = getCenterXForColumn(cell.mColumn);
                float centerY = getCenterYForRow(cell.mRow);
                if (i == 0) {
                    currentPath.moveTo(centerX, centerY);
                } else {
                    currentPath.lineTo(centerX, centerY);
                }
            }

            /*
             * add last in progress section
             */
            if (mPatternInProgress && anyCircles && count > 0) {
                currentPath.lineTo(mInProgressX, mInProgressY);
            }

            if (mPatternDisplayMode == DisplayMode.Wrong){
                mPathPaint.setColor(mPathWrongColor);
            }else{
                mPathPaint.setColor(mPathColor);
            }

            // ���·��С��0���ߴ��ڿ�ȵ�1/8���Ͳ�ȥ���ÿ��
            if (mPathWidth >= 0 && mPathWidth < (getWidth() >> 3)) {
                mPathPaint.setStrokeWidth(mPathWidth);
            } else {
                float radius = (squareWidth * mDiameterFactor * 0.5f);
                mPathPaint.setStrokeWidth(radius);
            }
            mPathPaint.setAlpha(mPathAlpha);

            mPathPaint.setPathEffect(mPathDashPathEffect);

            canvas.drawPath(currentPath, mPathPaint);
        }

        /*
         * draw the circles
         */
        final int paddingTop = mPaddingTop;
        final int paddingLeft = mPaddingLeft;

        for (int i = 0; i < MATRIX_WIDTH; i++) {
            float topY = paddingTop + i * squareHeight;
            /*
             * float centerY = mPaddingTop + i * mSquareHeight + (mSquareHeight
             * / 2);
             */
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                float leftX = paddingLeft + j * squareWidth;
                drawCircle(canvas, (int) leftX, (int) topY, drawLookup[i][j]);
            }
        }


        /*
         * restore default flag
         */
        mPaint.setFilterBitmap(oldFlag);
    }

    /**
     * @param canvas
     * @param leftX
     * @param topY
     * @param partOfPattern
     *            Whether this circle is part of the pattern.
     */
    private void drawCircle(Canvas canvas, int leftX, int topY,
                            boolean partOfPattern) {
        Bitmap outerCircle;

        if (!partOfPattern
                || (mInStealthMode && mPatternDisplayMode != DisplayMode.Wrong)) {
            /*
             * unselected circle
             */
            outerCircle = mBitmapCircleDefault;
        } else if (mPatternInProgress) {
            /*
             * user is in middle of drawing a pattern
             */
            outerCircle = mBitmapCircleOK;
        } else if (mPatternDisplayMode == DisplayMode.Wrong) {
            /*
             * the pattern is wrong
             */
            outerCircle = mBitmapCircleError;
        } else if (mPatternDisplayMode == DisplayMode.Correct) {
            /*
             * the pattern is correct
             */
            outerCircle = mBitmapCircleOK;
        } else {
            throw new IllegalStateException("unknown display mode "
                    + mPatternDisplayMode);
        }

        final int width = mBitmapWidth;
        final int height = mBitmapHeight;

        final float squareWidth = mSquareWidth;
        final float squareHeight = mSquareHeight;

        int offsetX = (int) ((squareWidth - width) / 2f);
        int offsetY = (int) ((squareHeight - height) / 2f);

        /*
         * Allow circles to shrink if the view is too small to hold them.
         */
        float sx = Math.min(mSquareWidth / mBitmapWidth, 0.8f);
        float sy = Math.min(mSquareHeight / mBitmapHeight, 0.8f);

        mCircleMatrix.setTranslate(leftX + offsetX, topY + offsetY);
        mCircleMatrix.preTranslate(mBitmapWidth / 2, mBitmapHeight / 2);
        mCircleMatrix.preScale(sx, sy);
        mCircleMatrix.preTranslate(-mBitmapWidth / 2, -mBitmapHeight / 2);

        canvas.drawBitmap(outerCircle, mCircleMatrix, mPaint);
//        canvas.drawBitmap(innerCircle, mCircleMatrix, mPaint);
    }
}

