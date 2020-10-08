package example;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import example.myweatherradar.*;
//import example.ipma_client.IpmaService;

import java.util.Iterator;
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

        //int id = CITY_ID_AVEIRO;
        String city = "Aveiro";
        if (args.length > 0) {
            try {
                 city = args[0];
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
        //get city id if exists
        Call<CitiesInfo> callSyncCities = service.getCitiesIds();
        Integer cityID = null;
        Boolean flagCityID = false;
        try{
            Response<CitiesInfo> apiResponse = callSyncCities.execute();
            Iterator<CityForecast> iter = forecast.getData().listIterator();
            while (iter.hasNext()) {
                CityInfo cityInfo = iter.next();
                if(cityInfo.getIdLocal().equals(city)){
                    cityID = cityInfo.getGlobalIdLocal();
                    flagCityID = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(flagCityID){
            logger.info("Cidade valida!\n\n");
        }
       



        //---------------
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityID);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                //CityForecast today = forecast.getData().listIterator().next();
                Iterator<CityForecast> iter = forecast.getData().listIterator();
                while (iter.hasNext()) {
                    CityForecast infoCityForecast = iter.next();
                    String info = "\n--------- CITY ID: " + 
                    "\n--> date: " + infoCityForecast.getForecastDate() +
                    "\n--> precepitation probabilty: " + infoCityForecast.getPrecipitaProb() +
                    "\n--> minimum temperature: " + infoCityForecast.getTMin() +
                    "\n--> maximum temperature: " + infoCityForecast.getTMax() +
                    "\n--> forecast date: " + infoCityForecast.getForecastDate() + "\n\n";
                    

                    logger.info(info);
                }
            } else {
                logger.info( "No results!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*
        //exc2
        //IpmaService service = retrofit.create(IpmaService.class);
        Call<CitiesInfo> callCitiesSync = service.getCitiesIds();
        try{
            Response<CitiesInfo> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();
        } 
        */

        

    }
}