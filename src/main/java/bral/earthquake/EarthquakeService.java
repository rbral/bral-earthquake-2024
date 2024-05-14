package bral.earthquake;

import bral.earthquake.json.FeatureCollection;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface EarthquakeService
{
    // telling retrofit where to go- put in "" the file you're requesting
    @GET("/earthquakes/feed/v1.0/summary/1.0_hour.geojson")
    Single<FeatureCollection> oneHour(); // inside<> = name of class you're trying to download

    @GET("/earthquakes/feed/v1.0/summary/significant_month.geojson")
    Single<FeatureCollection> significantMonth();
}
