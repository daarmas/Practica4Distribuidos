package Ej1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Descargador implements Runnable{
    private URL laURL;
    private String file;
    private long init, fin;

    public Descargador(URL laURL, long init, long fin){
        super();
        this.file = laURL.getFile();
        this.laURL=laURL;
        this.init = init;
        this.fin=fin;
    }

    @Override
    public void run() {
        HttpURLConnection con = null;
        RandomAccessFile raf = null;
        DataInputStream dis = null;
        byte [] buff = new byte[128];
        int leidos;
        try {
            con = (HttpURLConnection) this.laURL.openConnection();
            con.setRequestProperty("Range", "bytes="+this.init+"-"+this.fin);
            dis=new DataInputStream(con.getInputStream());
            raf = new RandomAccessFile(this.file, "rw");
            raf.seek(this.init);
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
    }
}
