import android.annotation.SuppressLint
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.suleymanovtat.gallery.BuildConfig
import ru.suleymanovtat.gallery.GalleryApp
import ru.suleymanovtat.gallery.core.RetrofitInterface
import java.util.concurrent.TimeUnit

object ApiCore {

    private var mHttpClient: OkHttpClient? = null
    @SuppressLint("StaticFieldLeak")
    var picasso: Picasso? = null


    val service: RetrofitInterface
        get() {
            mHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(AddTokenInterceptor())
                    .build()
            picasso = Picasso.Builder(GalleryApp.instance.applicationContext)
                    .downloader(OkHttp3Downloader(mHttpClient))
                    .build()
            return retrofit.create(RetrofitInterface::class.java)
        }

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mHttpClient!!)
                .build()


    class AddTokenInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header(BuildConfig.AUTH, BuildConfig.OAUTH + BuildConfig.TOKEN)
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}
