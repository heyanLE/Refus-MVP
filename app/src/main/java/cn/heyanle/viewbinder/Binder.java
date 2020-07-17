package cn.heyanle.viewbinder;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by HeYanLe on 2020/5/4 0004.
 * https://github.com/heyanLE
 */
public class Binder {

    /**
     * 利用注解来绑定 View
     * @param activity  activity
     */
    public static void bind(Activity activity){
        try{
            Class clazz = activity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(BindView.class)){
                    int viewId = Objects.requireNonNull(field.getAnnotation(BindView.class)).value();
                    Method findView = clazz.getMethod("findViewById", int.class);
                    View view = (View) findView.invoke(activity ,viewId);
                    field.setAccessible(true);
                    field.set(activity ,view);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 利用注解来绑定 View
     * @param dialog dialog
     */
    public static void bind(Dialog dialog){
        try{
            Class clazz = dialog.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(BindView.class)){
                    int viewId = Objects.requireNonNull(field.getAnnotation(BindView.class)).value();
                    Method findView = clazz.getMethod("findViewById", int.class);
                    View view = (View) findView.invoke(dialog ,viewId);
                    field.setAccessible(true);
                    field.set(dialog ,view);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void bind(Object object ,View view){
        try{
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                if(field.isAnnotationPresent(BindView.class)){
                    int viewId = Objects.requireNonNull(field.getAnnotation(BindView.class)).value();
                    View viewW = view.findViewById(viewId);
                    field.setAccessible(true);
                    field.set(object ,viewW);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
