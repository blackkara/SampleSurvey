package eu.fiskur.simpleviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

//http://stackoverflow.com/a/22797619/3253665
public class BiViewPager extends ViewPager {

  private static final String TAG = "BiViewpager";
  private boolean vertical = false;

  public BiViewPager(Context context) {
    super(context);
    init(vertical);
  }

  public BiViewPager(Context context, boolean vertical) {
    super(context);
    this.vertical = vertical;
    init(vertical);
  }

  public BiViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    //todo - get styled attribute
   // this.vertical = vertical;
    init(false);
  }

  private void init(boolean vertical){
    if(vertical){
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
      setLayoutParams(params);
      setPageTransformer(true, new VerticalPageTransformer());
      // The easiest way to get rid of the overscroll drawing that happens on the left and right
      setOverScrollMode(OVER_SCROLL_NEVER);
    }
  }

  private class VerticalPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
      if (position <= 1) { // [-1,1]
        view.setAlpha(1);

        // Counteract the default slide transition
        view.setTranslationX(view.getWidth() * -position);

        //set Y position to swipe in from top
        float yPosition = position * view.getHeight();
        view.setTranslationY(yPosition);
      }
    }
  }

  /**
   * Swaps the X and Y coordinates of your touch event.
   */
  private MotionEvent swapXY(MotionEvent ev) {
    float width = getWidth();
    float height = getHeight();

    float newX = (ev.getY() / height) * width;
    float newY = (ev.getX() / width) * height;

    ev.setLocation(newX, newY);

    return ev;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if(vertical){
      boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
      swapXY(ev); // return touch coordinates to original reference frame for any child views
      return intercepted;
    }else {
      return super.onInterceptTouchEvent(ev);
    }
  }



  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if(vertical){
      return super.onTouchEvent(swapXY(ev));
    }else{
      return super.onTouchEvent(ev);
    }
  }


}
