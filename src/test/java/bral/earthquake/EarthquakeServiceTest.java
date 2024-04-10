package bral.earthquake;

import bral.earthquake.json.FeatureCollection;
import bral.earthquake.json.Properties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EarthquakeServiceTest
{
    @Test
    void oneHour() {
        // given
        EarthquakeService service = new EarthquakeServiceFactory().getService();

        // when
        FeatureCollection collection = service.oneHour().blockingGet();

        // then
        Properties properties = collection.features[0].properties;
        assertNotNull(properties.place);
        // time and mag can never be 0:
        assertNotEquals(0, properties.mag);
        assertNotEquals(0, properties.time);

        /*
        every time you make this request it
        gets the updated data from the website
         */
    }

}