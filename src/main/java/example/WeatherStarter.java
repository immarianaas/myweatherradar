package example;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import example.myweatherradar.*;
import java.util.Iterator;
//import example.ipma_client.IpmaService;

import org.apache.logging.log4j.*;

import java.util.logging.Logger;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

   // private static final int CITY_ID_AVEIRO = 1010500;
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    
    private static final Logger logger = Logger.getLogger(WeatherStarter.class.getName());
    private static final org.apache.logging.log4j.Logger logger2 = LogManager.getLogger(WeatherStarter.class.getName());


    public static void  main(String[] args ) {
        String city = "Aveiro";

        if (args.length > 0) {
            try {
                city = args[0];
                logger2.debug("city id read from command line argument (" + city + ")");
            } catch (Exception ex) {
                logger2.error("invalid argument passed (" + args[0] +"); the default value will be used: " + city);
            }
        }

        /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
       
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpmaService service = retrofit.create(IpmaService.class);
        
        //get cityID
        //ira verificar se a cidade existe, se existir obtem o ser id
        Call<CitiesInfo> callSyncCities = service.getCitiesIds();
        Integer cityID = null;
        Boolean flagCityID = false;
        try{
            Response<CitiesInfo> apiResponse = callSyncCities.execute();
            CitiesInfo citiesInfo = apiResponse.body();
            Iterator<CityInfo> iterator = citiesInfo.getData().listIterator();
           
            while(iterator.hasNext()) {
                CityInfo cityInfo = iterator.next();
                if(cityInfo.getLocal().equals(city)){ //verifica se existe um nome com essa cidade nos dados da Api
                    cityID = cityInfo.getGlobalIdLocal();
                    flagCityID = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(flagCityID){
            logger.info("\n\nCidade valida!");
        }
       
        //----------------


        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityID);
        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();
            //System.out.println(forecast);
            String allInfo ="";
            if (forecast != null) {
                Iterator<CityForecast> iterator = forecast.getData().listIterator();
                while (iterator.hasNext()){
                    CityForecast today = iterator.next();
                    /*
                    String info = "\n\nCity ID: " + cityID +
                    "\nCity Name:" +city + 
                    "\nforecast date:: " + infoCityForecast.getForecastDate() + "\n" +
                    "   Minimum temperature:  " + infoCityForecast.getTMin() + " ºC" +"\n" +
                    "   Maximum temperature:  "+  infoCityForecast.getTMax() + " ºC" + "\n" +
                    "   --> precepitation probabilty: " + infoCityForecast.getPrecipitaProb() + " %" + "\n";
                    allInfo = allInfo + "\n" +infoForecast;
                    */
                    String info = "\n------ CITY Name: " + city + "[ " + cityID + "]" +
                "\n--> precepitation probabilty: " + today.getPrecipitaProb() +
                "\n--> minimum temperature: " + today.getTMin() +
                "\n--> maximum temperature: " + today.getTMax() +
                "\n--> forecast date: " + today.getForecastDate();
                logger.info(info);
                }
                logger.info("\n\nobrigada e volte sempre");
                    
            
            } else {
                logger.info( "No results!");
                logger2.info("No results were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger2.error("An error occurred while looking for the information.");
        }


    }
}
