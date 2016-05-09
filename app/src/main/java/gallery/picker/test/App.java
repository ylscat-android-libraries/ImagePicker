package gallery.picker.test;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created at 2016/5/3.
 *
 * @author YinLanShan
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(800, 800) // max width, max height
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(15 * 1024 * 1024)
                .discCacheSize(100 * 1024 * 1024)
//                .defaultDisplayImageOptions(options)
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }
}
