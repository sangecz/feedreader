package cz.cvut.marekp11.feedreader.update.helpers;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {

    public static final String LIST_FRAGMENT_ID = "LIST_FRAGMENT_ID";

    private Map<String, WeakReference<Object>> data;
    private static DataHolder instance;

    public static DataHolder getInstance(){
        if(instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    private DataHolder(){
        data = new HashMap<String, WeakReference<Object>>();
    }

    public void save(String id, Object object) {
        data.put(id, new WeakReference<Object>(object));
    }

    public Object retrieve(String id) {
        WeakReference<Object> objectWeakReference = data.get(id);
        if (objectWeakReference != null)
            return objectWeakReference.get();
        return null;
    }
}
