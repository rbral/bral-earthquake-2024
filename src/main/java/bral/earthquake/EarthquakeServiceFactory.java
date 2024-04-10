package bral.earthquake;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class EarthquakeServiceFactory
{
    // will give us an EQ service
    public EarthquakeService getService() {
        // creating retrofit object:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://earthquake.usgs.gov/") // beginning part of the link
                // Configure Retrofit to use Gson to turn the Json into Objects
                .addConverterFactory(GsonConverterFactory.create())
                // Configure Retrofit to use Rx
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build(); // builds us a retrofit object
        return retrofit.create(EarthquakeService.class);
    }
}
