package svp.com.dontmissstation.ui.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.SubMenu;
import android.widget.TextView;

import com.svp.infrastructure.common.DrawableEx;
import com.svp.infrastructure.common.view.NavigationViewDecorator;

import svp.com.dontmissstation.R;

public class MainNavigationView{
    private final Activity activity;

    public MainNavigationView(Activity activity) {
        this.activity = activity;
    }

    private NavigationView getNavigationView(){
        return (NavigationView) activity.findViewById(R.id.activity_main_nav_view);
    }

    public void setItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener){
        getNavigationView().setNavigationItemSelectedListener(listener);
    }

    public void initGeneralMenu(){
        NavigationViewDecorator nd = new NavigationViewDecorator(getNavigationView());
        nd.setMenu(R.menu.activity_main_drawer_menu_general);

    }
    public void initSubwayMenu(SubwayView subway){
        NavigationViewDecorator nd = new NavigationViewDecorator(getNavigationView());
        nd.setMenu(R.menu.activity_main_drawer_menu_selected_subway);

        Menu menu = nd.getMenu();
        SubMenu submenu = menu.addSubMenu(R.id.main_activity_nave_menu_lines_group,Menu.NONE,0,"Lines");
        for (SubwayLineView line : subway.getLines()) {
            MyRoundCornerDrawable testD = new MyRoundCornerDrawable(
                    DrawableEx.getBitmapFromDrawableRes(activity,R.drawable.ic_subway_black_24dp));

            DrawableEx.changeColor(testD,line.getColor());
            submenu.add(line.getName())
                    .setIcon(testD)
                    .setCheckable(true);
        }
        setSubwayHeader(subway);
    }

    private void setSubwayHeader(SubwayView subway) {
        ((TextView)getNavigationView().findViewById(R.id.activity_main_nav_header_subway_name))
                .setText(subway.getCountry() + " " + subway.getCity());
    }

    public class MyRoundCornerDrawable extends Drawable {

        private Paint paint;

        public MyRoundCornerDrawable(Bitmap bitmap) {
            BitmapShader shader;
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);
        }

        @Override
        public void draw(Canvas canvas) {
            int height = getBounds().height();
            int width = getBounds().width();
            RectF rect = new RectF(0.0f, 0.0f, width, height);
            canvas.drawRoundRect(rect, 30, 30, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

    }
}
