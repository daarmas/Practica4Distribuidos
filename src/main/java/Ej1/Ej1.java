package Ej1;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ej1 {
    public static void main(String[] args) {
//        String url = new Scanner(System.in).nextLine();
        String url= "https://dibujosycolores.com/letras/letra-a/letra-a-8.jpg";
        URL u = null;
        HttpURLConnection con = null;
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("HEAD");
            long tamaño = con.getContentLengthLong();
            long tamDiv=tamaño/3, max;
            final CyclicBarrier starter = new CyclicBarrier(4);
            ExecutorService pool = Executors.newFixedThreadPool(3);
            for(int i=0;i<3;i++){
                max=tamDiv*(i+1)-1;
                if(i==2) max=tamaño-1;
//                Descargador d = new Descargador(new URL(url), tamDiv*i, max);
                URL finalU = u;
                int finalI = i;
                long finalMax = max;
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection con = null;
                            RandomAccessFile raf = null;
                            DataInputStream dis = null;
                            byte [] buff = new byte[128];
                            int leidos;
                            try {
                                con = (HttpURLConnection) new URL(url).openConnection();
                                con.setRequestProperty("Range", "bytes="+tamDiv* finalI +"-"+finalMax);
                                dis=new DataInputStream(con.getInputStream());
                                String [] s = finalU.getFile().split("/");
                                raf = new RandomAccessFile(s[s.length-1], "rw");
                                raf.seek(tamDiv*finalI);
                                while((leidos=dis.read(buff))!=-1){
                                    raf.write(buff,0,leidos);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            finally {
                                try {
                                    assert raf != null;
                                    raf.close();
                                    dis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            starter.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            starter.await();
            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
