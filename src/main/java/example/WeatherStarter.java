package example;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import example.myweatherradar.*;
//import example.ipma_client.IpmaService;

import java.util.logging.Logger;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    private static final int CITY_ID_AVEIRO = 1010500;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static final Logger logger = Logger.getLogger(WeatherStarter.class.getName());

    public static void  main(String[] args ) {
        int id = CITY_ID_AVEIRO;

        if (args.length > 0) {
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception ex) {}
        }

        /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpmaService service = retrofit.create(IpmaService.class);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(id);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                CityForecast today = forecast.getData().listIterator().next();
                String info = "\n--------- CITY ID: " + id + 
                "\n--> precepitation probabilty: " + today.getPrecipitaProb() +
                "\n--> minimum temperature: " + today.getTMin() +
                "\n--> maximum temperature: " + today.getTMax() +
                "\n--> forecast date: " + today.getForecastDate() +
                "\n\nobrigada e volte sempre";

                logger.info(info);
                

                //logger.info( "max temp for today: " + today.getTMax());




            } else {
                logger.info( "No results!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
