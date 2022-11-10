package Ej1;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Ej1 {
    public static void main(String[] args) {
        String url = new Scanner(System.in).nextLine();
        URL u = null;
        HttpURLConnection con = null;
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("HEAD");
            long tamaño = con.getContentLengthLong();
            Descargador d1, d2, d3;
            d1 = new Descargador(new URL(url), 0, tamaño/3-1);
            d2 = new Descargador(new URL(url), tamaño/3, tamaño/3*2-1);
            d3 = new Descargador(new URL(url), tamaño/3*2, tamaño-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main (String [] args){
//        try{
//            URL url= new URL("https://lh3.googleusercontent.com/jUoaTIlBn5ibfQcND2n5OMD6Z7xoqNj-ShHlFR6QuLffLXD5pS8V2eNg1rGlrsRrnDkoQ28O8UHzqzBQKAGY4l1CS2NQSq2SkRScK6FOjl82jppyohK-");
//            DataInputStream dis=new DataInputStream(url.openStream());
//            FileOutputStream fos = new FileOutputStream(url.getFile().split("/")[1]);
//            byte [] buff = new byte[256];
//            int leidos;
//            while(( leidos = dis.read(buff)) !=-1)
//                fos.write(buff,0,leidos);
//            dis.close(); // Ya sabéis que está mal cerrado así, debería hacerse un finally
//        }
//        catch(IOException e) {e.printStackTrace();}
//    }
}
