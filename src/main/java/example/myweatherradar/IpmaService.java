package example.myweatherradar;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * IPMA API service mapping
 */
public interface IpmaService {
    //entra o id no url
    @GET("forecast/meteorology/cities/daily/{city_id}.json")
    Call<IpmaCityForecast> getForecastForACity(@Path("city_id") int cityId);

    /**
     * http://api.ipma.pt/open-data/distrits-islands.json
     * base_url:http://api.ipma.pt/open-data/
     * endpoin:distrits-islands.json
    */

    //
    @GET("distrits-islands.json")
    Call<IpmaCityForecast> getCitiesIds();
    

}