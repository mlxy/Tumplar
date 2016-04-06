package mlxy.tumplar.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import mlxy.tumplar.entity.Photo;
import mlxy.tumplar.entity.PhotoPost;
import mlxy.tumplar.entity.event.PrefetchProgressEvent;
import mlxy.tumplar.global.App;
import mlxy.tumplar.internal.progress.ProgressListener;
import mlxy.tumplar.internal.progress.ProgressTarget;
import mlxy.tumplar.model.UserModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PrefetchService extends Service {
    @Inject
    UserModel model;
    @Inject
    EventBus eventBus;

    private final RequestManager glide;

    public PrefetchService() {
        App.graph.inject(this);
        glide = Glide.with(App.component.context());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void prefetch() {
        String url = "https://40.media.tumblr.com/08dba4ef02458447542912f25a0e7c56/tumblr_o4fd294APs1qix0dvo1_1280.jpg";
        Observable.just(url)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        glide.load(Uri.parse(url)).downloadOnly(new ProgressTarget(url, createListener()));
                    }
                });
    }

    @NonNull
    private ProgressListener createListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(String url, long bytesRead, long totalBytes) {
                eventBus.post(new PrefetchProgressEvent(url, bytesRead, totalBytes));
            }
        };
    }
}