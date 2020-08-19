import java.net.*;
import java.io.*;
import java.security.*;
import javax.crypto.*;

public class ClienteCifrado
{
 public static void main(String a[]) throws Exception
 {
  Socket socket = null;
  String peticion = null;
  String respuesta = null;

/*		        
  System.out.println( "Generando la llave..." );
  KeyGenerator keyGen = KeyGenerator.getInstance("DES");
  keyGen.init(56);
  Key clave = keyGen.generateKey();
  System.out.println( "La llave del cliente es:" + clave );
  System.out.println( "Llave generada!" );
*/			

  ObjectInputStream ois = new ObjectInputStream(new FileInputStream("llave.ser"));
  Key clave = (Key)ois.readObject();
  ois.close();
  System.out.println( "La llave del cliente es:" + clave );

  System.out.println("Me conecto al puerto 8000 del servidor");
  socket = new Socket(a[0],8000);
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  peticion = br.readLine();
  System.out.println("Teclado: " + peticion);
  byte[] textoPlano = peticion.getBytes("UTF8");
  System.out.println( "El mensaje pasado a bytes y a bits es:" );
  bytesToBits( textoPlano );
  System.out.println( "----------------------------------------------------" );
  Cipher cifrar = Cipher.getInstance("DES/ECB/PKCS5Padding");
  cifrar.init(Cipher.ENCRYPT_MODE, clave);
  byte[] textoCifrado = cifrar.doFinal(textoPlano);
  System.out.println( "El argumento ENCRIPTADO es:" );
  System.out.println( new String(textoCifrado, "UTF8") );
  System.out.println( "El argumento ENCRIPTADO pasado a bytes y a bits es:" );
  bytesToBits( textoCifrado );
  System.out.println( "----------------------------------------------------" );
  DataOutputStream dos = new DataOutputStream( socket.getOutputStream());
  dos.write(textoCifrado);
  dos.close();
  socket.close();
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
