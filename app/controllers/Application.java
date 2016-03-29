package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Location;
import play.libs.Json;
import play.mvc.Result;
import views.html.*;

/**
 * Created by gabriel.perez on 27/01/16.
 * Made as a template by leonel.atencio on 13/03/16
 * This class Extends HecticusController, which contains all custom made methods for REST services.
 * This is the place to write your application-specific methods.
 */

public class Application extends HecticusController {

    public static final String USER_ROLE = "user";
    public static final String ADMIN_ROLE = "admin";

    public static Result alive(){
        return ok("alive");
    }

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /**
     * Metodo de prueba. Acceder desde http://localhost:9000/localizacion
     * JSON para probar: {"start":{"lat":8.9753626,"lng":-79.50758479999999},"end":{"lat":8.9753626,"lng":-79.50758479999999},"current":{"lat":8.9753626,"lng":-79.50758479999999,"status_location":0}}
     *
     * @return	data para insertar recibida por post
     */
    //
    public static Result checkLocalization(){

        //Obtener JSON
        ObjectNode jsonInfo = getJson();
        if (jsonInfo != null && jsonInfo.size() == 3){

            /* status:  0:start 1:transit 2:end  */
            int statusLocation = jsonInfo.get("current").get("status_location").asInt();
            switch (statusLocation){
                case 0:
                    Location location = generateInitialLocation(jsonInfo);
                    location.save();
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            //Regresar validación de JSON Correcto
            return ok(buildBasicResponse(0,"OK"));
        }else{
            //Regresar validación de JSON Invalido
            return badRequest(buildBasicResponse(3,"El Json no cumple con el formato establecido"));
        }
    }

    /**
     * Metodo de prueba. Genera un JSON con punto de inicio, fin y ruta a seguir.
     *
     * @return	String con los pares de latitud y logitud
     */
    //
    public static Result follow(int id){
        Location location = Location.getById(id);
        if(location != null){
            ObjectNode jsonObj = Json.newObject();
            jsonObj.put("id",id);
            jsonObj.put("start",buildJson(location.getStartLat(),location.getStartLong()));
            jsonObj.put("end",buildJson(location.getEndLat(),location.getEndLong()));
            jsonObj.put("coordinates",location.getFollow());
            return ok(buildBasicResponse(0,"OK", jsonObj));
        } else {
            return badRequest(buildBasicResponse(1, "No existe el registro"));
        }
    }

    /**
     * Metodo de prueba. Genera un JSON con latitud y longitud.
     *
     * @return	String con los pares de latitud y logitud
     */
    //
    private static ObjectNode buildJson (float lat, float lng){
        ObjectNode jsonObj = Json.newObject();
        jsonObj.put("lat",String.valueOf(lat));
        jsonObj.put("lng",String.valueOf(lng));

        return jsonObj;
    }

    /**
     * Metodo de prueba. Genera la locación inicial.
     *
     * @return	String con los pares de latitud y logitud
     */
    //
    private static Location generateInitialLocation(ObjectNode json){
        float startLat = json.get("start").get("lat").floatValue();
        float startLong = json.get("start").get("lng").floatValue();
        short status = json.get("current").get("status_location").shortValue();
        Location location = new Location(
                startLat,
                startLong,
                json.get("end").get("lat").floatValue(),
                json.get("end").get("lng").floatValue(),
                json.get("current").get("lat").floatValue(),
                json.get("current").get("lng").floatValue(),
                status,
                generateFollowCoordinates(startLat,startLong,status)
        );
        return location;
    }

    /**
     * Metodo de prueba. Genera las coordenadas de la ruta a seguir.
     *
     * @return	String con los pares de latitud y logitud
     */
    //
    private static String generateFollowCoordinates(float lat, float lng, short status){
        String fallow =  String.valueOf(lat).concat(",").concat(String.valueOf(lng));
        return (status == 1) ? fallow.concat("|") : fallow;
    }

}