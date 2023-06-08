package com.yzy.lib_common.glide;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by yzy on 2020/8/3.
 */
public class OkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

    private final Call.Factory client;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public OkHttpUrlLoader(@NonNull Call.Factory client) {
        this.client = client;
    }

    @Override
    public boolean handles(@NonNull GlideUrl url) {
        return true;
    }

    @Override
    public LoadData<InputStream> buildLoadData(
            @NonNull GlideUrl model, int width, int height, @NonNull Options options) {
        return new LoadData<>(model, new OkHttpStreamFetcher(client, model));
    }

    /** The default factory for {@link OkHttpUrlLoader}s. */
    // Public API.
    @SuppressWarnings("WeakerAccess")
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile Call.Factory internalClient;
        private final Call.Factory client;

        private static Call.Factory getInternalClient() {
            if (internalClient == null) {
                synchronized (Factory.class) {
                    if (internalClient == null) {
                        OkHttpClient.Builder builder=new OkHttpClient.Builder();
                      /*  if (BuildConfig.IS_DEBUG){
                            builder.addInterceptor(new LogInterceptor());
                        }*/
                        //超时时间 连接、读、写
                        builder.connectTimeout(20, TimeUnit.SECONDS);
                        builder.readTimeout(20, TimeUnit.SECONDS);
                        builder.writeTimeout(20, TimeUnit.SECONDS);

                        internalClient = (Call.Factory) builder.build();
                    }
                }
            }
            return internalClient;
        }





        /** Constructor for a new Factory that runs requests using a static singleton client. */
        public Factory() {
            this(getInternalClient());
        }

        /**
         * Constructor for a new Factory that runs requests using given client.
         *
         * @param client this is typically an instance of {@code OkHttpClient}.
         */
        public Factory(@NonNull Call.Factory client) {
            this.client = client;


        }

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new OkHttpUrlLoader(client);
        }

        @Override
        public void teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }
}