package Ej2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Ej2 {
    private static final String HOMEDIR = "C:\\home2";

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket socket = new ServerSocket(8080)) {
            while (true) {
                try{
                    Socket conexion = socket.accept();
                    Runnable r = new Runnable() {
                        public void run() {
                            byte [] buff = new byte[1024*1024];
                            int leidos;
                            DataInputStream dis = null;
                            try {
                                dis = new DataInputStream(conexion.getInputStream());
                                String s;
                                s= dis.readLine();
                                File f = buscaFichero(s);
                                try (FileInputStream fis = new FileInputStream(f);
                                     OutputStream os = conexion.getOutputStream()
                                ){
                                    sendMIMEHeading(os,200, URLConnection.guessContentTypeFromName(f.getName()),f.length());
                                    while((leidos=fis.read(buff))!=-1){
                                        os.write(buff,0, leidos);
                                    }
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }finally {
                                try {
                                    dis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    pool.execute(r);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            pool.shutdown();
        }
    }

    private static File buscaFichero(String m) {
        String fileName="";
        if (m.startsWith("GET ")){
            // A partir de una cadena de mensaje (m) correcta (comienza por GET)
            fileName = m.substring(4, m.indexOf(" ", 5));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        if (m.startsWith("HEAD ")){
            // A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
            fileName = m.substring(6, m.indexOf(" ", 7));
            if (fileName.equals("/")) {
                fileName += "index.html";
            }
        }
        return new File(HOMEDIR, fileName);
    }

    private static void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
        PrintStream dos = new PrintStream(os);
        dos.print("HTTP/1.1 " + code + " ");
        if (code == 200) {
            dos.print("OK\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + cType + "\r\n");
            dos.print("\r\n");
        } else if (code == 404) {
            dos.print("File Not Found\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        } else if (code == 501) {
            dos.print("Not Implemented\r\n");
            dos.print("Date: " + new Date() + "\r\n");
            dos.print("Server: Cutre http Server ver. -6.0\r\n");
            dos.print("Connection: close\r\n");
            dos.print("Content-length: " + fSize + "\r\n");
            dos.print("Content-type: " + "text/html" + "\r\n");
            dos.print("\r\n");
        }
        dos.flush();
    }

    private static String makeHTMLErrorText(int code, String txt) {
        StringBuffer msg = new StringBuffer("<HTML>\r\n");
        msg.append(" <HEAD>\r\n");
        msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
        msg.append(" </HEAD>\r\n");
        msg.append(" <BODY>\r\n");
        msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
        msg.append(" </BODY>\r\n");
        msg.append("</HTML>\r\n");
        return msg.toString();
    }
}
