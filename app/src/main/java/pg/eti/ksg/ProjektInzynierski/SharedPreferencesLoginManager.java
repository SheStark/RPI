package pg.eti.ksg.ProjektInzynierski;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesLoginManager {

    private String SHARED_PREFERENCES_KEY="UsersData";
    private String SHARED_PREFERENCES_FILE="LoginPreferences";
    private String SHARED_PREFERENCES_LOGIN="Logged";
    private String SHARED_PREFERENCES_DATA_SYNCHRONIZED = "DataSynchronized";

    private SharedPreferences preferences;
    private Gson gson;

    private ArrayList<SharedPreferencesLoginData> users;

    public SharedPreferencesLoginManager(Context context){
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE,Context.MODE_PRIVATE);
    }

    public ArrayList<SharedPreferencesLoginData> getPreferences()
    {
        gson = new Gson();
        String json=preferences.getString(SHARED_PREFERENCES_KEY,"");
        if(json.isEmpty()) {
            users=new ArrayList<>();
            return users;
        }
        Type type = new TypeToken<List<SharedPreferencesLoginData>>(){}.getType();
        users = gson.fromJson(json,type);
        return users;
    }

    private void addToSharedPreferences()
    {
        String newJson=gson.toJson(users);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(SHARED_PREFERENCES_KEY,newJson);
        editor.apply();
    }

    public void addData(SharedPreferencesLoginData data)
    {
        if(users==null)
            getPreferences();
        users.add(data);
        addToSharedPreferences();
    }

    public void deleteData(int position)
    {
        if(users==null)
            getPreferences();
        users.remove(position);
        addToSharedPreferences();
    }

    public String logged()
    {
        return preferences.getString(SHARED_PREFERENCES_LOGIN,"");
    }

    public boolean logIn(String login)
    {
        try {
            if (login == null || login.isEmpty())
                return false;
            else if (!preferences.getString(SHARED_PREFERENCES_LOGIN, "").isEmpty())
                return false;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(SHARED_PREFERENCES_LOGIN, login);
            editor.apply();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean logout()
    {
        try {
            if(preferences.getString(SHARED_PREFERENCES_LOGIN, "").isEmpty())
                return false;
            preferences.edit().remove(SHARED_PREFERENCES_LOGIN).apply();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean isDataSynchronized()
    {
        String value = preferences.getString(SHARED_PREFERENCES_DATA_SYNCHRONIZED,"");
        if(value == null)
            return false;
        else if(value.isEmpty())
            return false;
        else return value.equals("true");

    }

    public void setToSynchronized()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREFERENCES_DATA_SYNCHRONIZED,"true");
        editor.apply();
    }

    public void setToNonSynchronized()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREFERENCES_DATA_SYNCHRONIZED,"false");
        editor.apply();
    }

}
