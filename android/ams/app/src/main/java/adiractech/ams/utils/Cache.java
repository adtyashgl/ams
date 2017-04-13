package adiractech.ams.utils;

import android.content.Context;
import android.content.SharedPreferences;

import adiractech.ams.utils.Constants;

/**
 * Created by aditya on 07/05/16.
 */
public class Cache {

    static public void setInCache(Context context, String key, Object value,
                                  int type){
        SharedPreferences.Editor cache = context.
                getSharedPreferences(Constants.CACHE_NAME, 0).edit();

        switch(type){
            case Constants.TYPE_INT:
            {
                cache.putInt(key,(Integer)value);
            }break;

            case Constants.TYPE_BOOL:
            {
                cache.putBoolean(key, (Boolean)value);
            }break;

            case Constants.TYPE_STRING:
            {
                cache.putString(key, (String)value);
            }break;

            case Constants.TYPE_DOUBLE:
            {
                String valueStr = Double.toString((Double)value);
                cache.putString(key,valueStr);
            }
            break;

            case Constants.TYPE_LONG:
            {
                cache.putLong(key, (Long)value);
            }
            break;

        }

        cache.commit();

    }

    static public Object getFromCache(Context context, String key,
                                      int type){
        SharedPreferences cache = context.getSharedPreferences(Constants.CACHE_NAME, 0);
        switch(type){
            case Constants.TYPE_INT:
            {
                return(cache.getInt(key,0));
            }

            case Constants.TYPE_BOOL:
            {
                return(cache.getBoolean(key,false));
            }

            case Constants.TYPE_STRING:
            {
                return(cache.getString(key,""));
            }

            case Constants.TYPE_DOUBLE:
            {
                String value = cache.getString(key,"");

                return Double.parseDouble(value);

            }

            case Constants.TYPE_LONG:
            {
                return(cache.getLong(key,0));
            }
        }
        return new Object(); //TODO: need a way to signal error
    }


    /* Public Interface */
    static public void resetCache(Context context){

    }


}
