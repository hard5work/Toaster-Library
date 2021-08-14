package com.motion.toasterlibrary.productive;

import com.motion.toasterlibrary.interfaces.RepoGet;
import com.motion.toasterlibrary.interfaces.RepoGetter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflect {
    public static Object invokeStaticVerify(String clazzName, String annotationName, String verifyName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName) && ((RepoGetter) annotation).name().equals(verifyName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticeNoVerify(String clazzName, String annotationName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object repoGet(String clazzName, String reqName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains("RepoGet") && ((RepoGet) annotation).value().equals(reqName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object getAPIContractVar(String clazz,String varName){
        Class o = null;
        try{
            o = Class.forName(clazz);
            for(Annotation annotation : o.getAnnotations()){
                if (annotation.toString().contains("IBaseAPIContract")){
                    return  o.getDeclaredField(varName).get(null);
                }
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> classOnly(String uri){
        Class c = null;
        try{
            c = Class.forName(uri);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return c;
    }

    public static Object createObj(String clazz) {
        Class o = null;
        try {
            o = Class.forName(clazz);
            return o.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
