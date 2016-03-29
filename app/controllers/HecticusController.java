package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Config;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static play.libs.Jsonp.jsonp;


/**
 * This is the base controller template for Hecticus projects.
 * It xcon
 *
 * @author  Zara Ali
 * @version 1.0
 * @since   2014-03-31
 */
@MappedSuperclass
public class HecticusController extends Controller {

    /**
     * Metodo para minar la data que se recibe por post, el daemon manda la data en el body como un json y los admins la mandan como un
     * Form.
     *
     * @return data para insertar recibida por post
     */
    public static StringBuilder invoker;

    public static ObjectNode getJson() {
        ObjectNode jsonInfo = (ObjectNode) request().body().asJson();
        if (jsonInfo == null) {
            Map<String, String[]> b = request().body().asFormUrlEncoded();
            if (b == null) {
                return null;
            }
            jsonInfo = Json.newObject();
            Set<String> keys = b.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                jsonInfo.put(key, Json.toJson(b.get(key)[0]));
            }
        }
        if (jsonInfo != null) {
            if (jsonInfo.has("invoker")) {
                invoker = new StringBuilder(jsonInfo.get("invoker").asText());
            } else {
                invoker = null;
            }
        }
        if (invoker == null) setInvoker();
        return jsonInfo;
    }

    public static String[] getFromQueryString(String key) {
        return request().queryString().get(key);
    }

    /**
     * Metodo que construye la respuesta básica o estándar de HECTICUS con una excepción.
     *
     * @return JSON correctamente formado, con código y descripcion de la respuesta.
     */
    public static ObjectNode buildBasicResponse(int code, String responseMsg) {
        ObjectNode responseNode = Json.newObject();
        responseNode.put(Config.ERROR_KEY, code);
        responseNode.put(Config.DESCRIPTION_KEY, responseMsg);
        return responseNode;
    }


    /**
     * Metodo que construye la respuesta básica o estándar de HECTICUS con una excepción.
     *
     * @return JSON correctamente formado, con código, descripcion y mensaje de excepción para la respuesta.
     */
    public static ObjectNode buildBasicResponse(int code, String responseMsg, Exception e) {
        ObjectNode responseNode = Json.newObject();
        responseNode.put(Config.ERROR_KEY, code);
        responseNode.put(Config.DESCRIPTION_KEY, responseMsg);
        responseNode.put(Config.EXCEPTION_KEY, e.getMessage());
        return responseNode;
    }

    /**
     * Metodo para validar que el json tenga por lo menos un campo
     *
     * @return data para insertar recibida por post
     */
    public static boolean validateJson(ObjectNode jsonInfo, ArrayList<String> reqKeys) {
        boolean atLeastOne = false;
        if (jsonInfo != null) {
            for (String s : reqKeys) {
                if (jsonInfo.has(s)) {
                    atLeastOne = true;
                }
            }
        }
        return atLeastOne;
    }

    /**
     * Metodo para validar (parse) un String.
     *
     * @return El String validado (parsed).
     */
    public static long validate(String var) {
        return Long.parseLong(var);
    }


    /**
     * Metodo para validar (parse) un Int.
     *
     * @return El Int validado (parsed).
     */
    public static int validateInt(String var) {
        return Integer.parseInt(var);
    }

    public static void setInvoker() {
        if (request().queryString().containsKey("invoker")) {
            invoker = new StringBuilder(request().queryString().get("invoker")[0]);
        }
    }

    /**
     * Metodo que construye la respuesta completa de HECTICUS con una excepción.
     *
     * @gets Int code: Codigo de la respuesta a colocar como respuesta en el JSON final.
     * String description: Descripcion de la respuesta a colocar como respuesta en el JSON final.
     * String parentObj: Nombre del nodo padre a colocar como respuesta en el JSON final.
     * ArrayList data: La data a a colocar como respuesta en el JSON final.
     * @return JSON correctamente formado, con código, descripcion y el contenido final (BODY) para la respuesta.
     */
    public static ObjectNode hecticusResponse(int code, String description, String parentObj, ArrayList data) {
        ObjectNode tr = Json.newObject();
        tr.put("error", code);
        tr.put("description", description);
        ObjectNode innerObj = Json.newObject();
        innerObj.put(parentObj, Json.toJson((data)));
        tr.put("response", innerObj);
        return tr;
    }

    /**
     * Metodo que construye la respuesta completa de HECTICUS con una excepción.
     *
     * @gets Int code: Codigo de la respuesta a colocar como respuesta en el JSON final.
     * String description: Descripcion de la respuesta a colocar como respuesta en el JSON final.
     * ObjectNode data: La data a a colocar como respuesta en el JSON final directamente en el nodo "response".
     * @return JSON correctamente formado, con código, descripcion y el contenido final (BODY) para la respuesta.
     */
    public static ObjectNode hecticusResponse(int code, String description, ObjectNode data) {
        ObjectNode tr = Json.newObject();
        tr.put("error", code);
        tr.put("description", description);
        tr.put("response", data);
        return tr;
    }

    public static play.libs.Jsonp buildBasicJSONPResponse(int code, String responseMsg, ObjectNode obj) {
        String callback = getJSONPCallback();
        ObjectNode responseNode = buildBasicResponse(code, responseMsg, obj);
        return jsonp(callback, responseNode);
    }

    public static ObjectNode buildBasicResponse(int code, String responseMsg, ObjectNode obj) {
        ObjectNode responseNode = Json.newObject();
        responseNode.put(Config.ERROR_KEY, code);
        responseNode.put(Config.DESCRIPTION_KEY, responseMsg);
        responseNode.put(Config.RESPONSE_KEY,obj);
        return responseNode;
    }

    public static play.libs.Jsonp buildBasicJSONPResponse(int code, String responseMsg, JsonNode obj) {
        String callback = getJSONPCallback();
        ObjectNode responseNode = buildBasicResponse(code, responseMsg, obj);
        return jsonp(callback, responseNode);
    }

    public static ObjectNode buildBasicResponse(int code, String responseMsg, JsonNode obj) {
        ObjectNode responseNode = Json.newObject();
        responseNode.put(Config.ERROR_KEY, code);
        responseNode.put(Config.DESCRIPTION_KEY, responseMsg);
        responseNode.put(Config.RESPONSE_KEY,obj);
        return responseNode;
    }

    public static String getJSONPCallback(){
        String callback = request().queryString().get("callback")[0];
        return callback;
    }

    public static play.libs.Jsonp buildBasicJSONPResponse(int code, String responseMsg) {
        String callback = getJSONPCallback();
        ObjectNode responseNode = buildBasicResponse(code, responseMsg);
        return jsonp(callback, responseNode);
    }

    public static play.libs.Jsonp buildBasicJSONPResponse(int code, String responseMsg, Exception e) {
        String callback = getJSONPCallback();
        ObjectNode responseNode = buildBasicResponse(code, responseMsg, e);
        return jsonp(callback, responseNode);
    }

    public static Http.MultipartFormData getMultiformData(){
        return request().body().asMultipartFormData();
    }

}
