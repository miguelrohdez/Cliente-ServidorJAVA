/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Miguel Alejandro Rojas Hernandez
 */
import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Cliente
{
   public static void main(String a[]) throws Exception
   {
      Socket socket = null;
      String peticion = "";
      String respuesta = "";

      byte arreglo[] = new byte[16];
      byte[] textoPlano;
      byte[] textoCifrado;

      ObjectInputStream ois = new ObjectInputStream(new FileInputStream("llave.ser"));
      SecretKey clave = (SecretKey)ois.readObject();
      ois.close();

      String stringKey=clave.toString();
      String parts[] = stringKey.split("@");
      System.out.println( "\tLa clave es = " + parts[1] );
  
      //Conectando al servidor
      try{
        System.out.println("Me conecto al puerto "+ a[1] +" del servidor");
        socket = new Socket(a[0],Integer.parseInt(a[1]));
        System.out.println("Conexion exitosa!");
        System.out.println( "\n\tMENU\nCONSULTAR\nDEPOSITAR(espacio)Cantidad\nRETIRAR(espacio)Cantidad\nTERMINAR");
        DataOutputStream dos = new DataOutputStream( socket.getOutputStream());
        DataInputStream dis = new DataInputStream( socket.getInputStream() );
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Cipher cifrar = Cipher.getInstance("DES/ECB/PKCS5Padding");
        while( !peticion.equals( "terminar" ) ){
          
          System.out.print(">_ ");
          //AQUI ES PARA ENVIAR
          peticion = br.readLine();
          textoPlano = peticion.getBytes("UTF8");
          cifrar.init(Cipher.ENCRYPT_MODE, clave);
          textoCifrado = cifrar.doFinal(textoPlano);
         // System.out.println( new String(textoCifrado, "UTF8") );
          dos.write(textoCifrado);
          //TERMINA ENVIARR
          //dos.writeUTF(peticion);

          //AQUI ES PARA RECIBIR
          int numeroBytesLeidos = dis.read(arreglo);
          //System.out.println("numeroBytesLeidos: " + numeroBytesLeidos);
          cifrar.init(Cipher.DECRYPT_MODE, clave);
          textoPlano = cifrar.doFinal(arreglo);
          //System.out.println( new String(textoPlano, "UTF8") );
          respuesta = new String(textoPlano, "UTF8");
          System.out.println("Servidor>_ " + respuesta);
          //Termina LEEER

          //respuesta = dis.readUTF();
        }
        dos.close();
        dis.close();
        socket.close();

      }catch(IOException e){
       // System.out.println("java.io.IOException generada");
        e.printStackTrace();
      }
  }//Main

} //Cliente

