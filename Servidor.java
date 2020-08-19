import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class ServidorCifrado
{
 public static void main(String a[]) throws Exception
 {
  ServerSocket serverSocket = null;
  Socket socket = null;
  // Peticion es lo que envia el Cliente
  String peticion = "";
  byte arreglo[] = new byte[8];

  System.out.println( "Generando la llave..." );
  KeyGenerator keyGen = KeyGenerator.getInstance("DES");
  keyGen.init(56);
  Key clave = keyGen.generateKey();
  System.out.println( "la clave del servidor es=" + clave );
  System.out.println( "Llave generada!" );

  ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("llave.ser"));
  oos.writeObject(clave);
  oos.close();

  System.out.println("Escuchando por el puerto 8000");
  serverSocket = new ServerSocket(8000); 
  System.out.println("Esperando a que los clientes se conecten...");
  socket = serverSocket.accept();
  System.out.println("Se conecto un cliente: " + socket.getInetAddress().getHostName());
  DataInputStream dis = new DataInputStream( socket.getInputStream() );
  int numeroBytesLeidos = dis.read(arreglo);
  System.out.println("numeroBytesLeidos: " + numeroBytesLeidos);
  System.out.println( "El argumento ENCRIPTADO pasado a bytes y a bits es:" );
  bytesToBits( arreglo );
  System.out.println( "----------------------------------------------------" );
  Cipher cifrar = Cipher.getInstance("DES/ECB/PKCS5Padding");
  cifrar.init(Cipher.DECRYPT_MODE, clave);
  byte[] textoPlano = cifrar.doFinal(arreglo);
  System.out.println( "El argumento DESENCRIPTADO es:" );
  System.out.println( new String(textoPlano, "UTF8") );
  System.out.println( "El argumento DESENCRIPTADO pasado a bytes y a bits es:" );
  bytesToBits( textoPlano );
  peticion = new String(textoPlano, "UTF8");
  System.out.println("El mensaje que me envio el cliente es: " + peticion);
 }

 public static void bytesToBits( byte[] texto )
 {
  StringBuilder stringToBits = new StringBuilder();
  for( int i=0; i < texto.length; i++ )
  {
   StringBuilder binary = new StringBuilder();
   byte b = texto[i];
   int val = b;
   for( int j = 0; j < 8; j++ )
   {
    binary.append( (val & 128) == 0 ? 0 : 1 );
    val <<= 1;
   }
   System.out.println( (char)b + " \t " + b + " \t " + binary );
   stringToBits.append( binary );
  }
  System.out.println( "El mensaje completo en bits es:" + stringToBits );
 }
}

