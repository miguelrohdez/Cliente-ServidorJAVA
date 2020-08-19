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

public class Servidor
{
 public static void main(String a[]) throws Exception
 {
    ServerSocket serverSocket = null; //Crea socket servidor
    Socket socket = null; //Crear socket
    String peticion = ""; //CadenaCliente
    String respuesta = ""; //CadenaServidor
    byte[] textoPlano;
    byte[] textoCifrado;
    byte arreglo[] = new byte[16];

    String saldo = "";
    String saldoTotal = "";
    Double actualiza;
    int opcion;



    
    //Para generar la llave
    System.out.println( "Generando la llave..." );
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");
    keyGen.init(56);
    SecretKey clave = keyGen.generateKey();
    //Para mostrar solamente la clave
    String stringKey=clave.toString();
    String parts[] = stringKey.split("@");
    System.out.println( "\tLa clave es = " + parts[1] );
    System.out.println( "Llave generada!" );
    //Escribe la llave a un archivo
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("llave.ser"));
    oos.writeObject(clave);
    oos.close();
    //Iniciando conexion con el socket
    try{
      System.out.println("Escuchando por el puerto 8000");
      serverSocket = new ServerSocket(8000);
    }catch(IOException e){
      System.out.println("No se recibieron peticiones");
      e.printStackTrace();
    }

    System.out.println("Esperando a que los clientes se conecten...");
    while(true){
      try{
        socket = serverSocket.accept();
        System.out.println("Se conecto: " + socket.getInetAddress().getHostName());
        DataInputStream dis = new DataInputStream( socket.getInputStream() );
        DataOutputStream dos = new DataOutputStream( socket.getOutputStream());
        Cipher cifrar = Cipher.getInstance("DES/ECB/PKCS5Padding");

        while( !respuesta.equals( "Terminar" ) ){
          int numeroBytesLeidos = dis.read(arreglo);
          System.out.println("numeroBytesLeidos: " + numeroBytesLeidos);
          cifrar.init(Cipher.DECRYPT_MODE, clave);
          textoPlano = cifrar.doFinal(arreglo);
          //System.out.println( new String(textoPlano, "UTF8") );
          peticion = new String(textoPlano, "UTF8");
          //peticion = dis.readUTF();

          System.out.println("Cliente>_" + peticion);
          String comando[] = peticion.split(" ");

          switch(comando[0].toLowerCase()){
            case "consultar":
               
              try{
                BufferedReader arch = new BufferedReader(new InputStreamReader(new FileInputStream("Saldo.txt")));
                saldo = arch.readLine();
                System.out.println(saldo);
                arch.close();
              }catch(IOException e){
                System.out.println("Error al mostrar el saldo");
                e.printStackTrace();
              }
              respuesta= "Saldo: "+ saldo;
              textoPlano = respuesta.getBytes("UTF8");
              cifrar.init(Cipher.ENCRYPT_MODE, clave);
              textoCifrado = cifrar.doFinal(textoPlano);
              //System.out.println( new String(textoCifrado, "UTF8") );
              dos.write(textoCifrado);

              

             // dos.writeUTF(respuesta);
                
              break;
            case "depositar":
                try{
                  BufferedReader arch = new BufferedReader(new InputStreamReader(new FileInputStream("Saldo.txt")));
                  saldo = arch.readLine();
                  System.out.println(saldo);
                  arch.close();
                }catch(IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }
                actualiza =  Double.parseDouble(saldo)+Double.parseDouble(comando[1]);
                saldoTotal = String.valueOf(actualiza);                          

                  File fichero = new File("Saldo.txt");
                  File fichero2= new File ("prueba2.txt");
                try{
                  BufferedReader arch = new BufferedReader(new InputStreamReader(new FileInputStream("Saldo.txt")));
                  PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("prueba2.txt")));
                  pw.write(saldoTotal);
                  arch.close();
                  pw.close();
                }catch(IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }

                if (fichero.delete())
                     System.out.println("El fichero ha sido borrado satisfactoriamente");
                else
                     System.out.println("El fichero no puede ser borrado");
                
                if (fichero2.renameTo(fichero))
                  System.out.println("Se renombro correctamente");

                respuesta ="Dep OK: "+comando[1];
                textoPlano = respuesta.getBytes("UTF8");
                cifrar.init(Cipher.ENCRYPT_MODE, clave);
                textoCifrado = cifrar.doFinal(textoPlano);
                //System.out.println( new String(textoCifrado, "UTF8") );
                dos.write(textoCifrado);

                //dos.writeUTF(respuesta);

              break;
            case "retirar":
              try{
                  BufferedReader arch = new BufferedReader(new InputStreamReader(new FileInputStream("Saldo.txt")));
                  saldo = arch.readLine();
                  System.out.println(saldo);
                  arch.close();
                }catch(IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }
                actualiza =  Double.parseDouble(saldo)-Double.parseDouble(comando[1]);
                saldoTotal = String.valueOf(actualiza);                          

                  File fichero3 = new File("Saldo.txt");
                  File fichero4= new File ("prueba2.txt");
                try{
                  BufferedReader arch = new BufferedReader(new InputStreamReader(new FileInputStream("Saldo.txt")));
                  PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("prueba2.txt")));
                  pw.write(saldoTotal);
                  arch.close();
                  pw.close();
                }catch(IOException e){
                    System.out.println("Error");
                    e.printStackTrace();
                }

                if (fichero3.delete())
                     System.out.println("El fichero ha sido borrado satisfactoriamente");
                else
                     System.out.println("El fichero no puede ser borrado");
                
                if (fichero4.renameTo(fichero3))
                  System.out.println("Se renombro correctamente");

                
                respuesta ="Ret OK: "+comando[1];
                textoPlano = respuesta.getBytes("UTF8");
                cifrar.init(Cipher.ENCRYPT_MODE, clave);
                textoCifrado = cifrar.doFinal(textoPlano);
                //System.out.println( new String(textoCifrado, "UTF8") );
                dos.write(textoCifrado);

//                dos.writeUTF(respuesta);
               
              break;
            case "terminar":
              respuesta ="Terminar";
              textoPlano = respuesta.getBytes("UTF8");
              cifrar.init(Cipher.ENCRYPT_MODE, clave);
              textoCifrado = cifrar.doFinal(textoPlano);
              System.out.println( new String(textoCifrado, "UTF8") );
              dos.write(textoCifrado);

             // dos.writeUTF(respuesta);

              break;
            default:
              respuesta ="Op Invalida";
              textoPlano = respuesta.getBytes("UTF8");
              cifrar.init(Cipher.ENCRYPT_MODE, clave);
              textoCifrado = cifrar.doFinal(textoPlano);
              System.out.println( new String(textoCifrado, "UTF8") );
              dos.write(textoCifrado);

              break;
          }
        }

 dos.close();
 dis.close();
 socket = null;
 System.out.println("Conexion Terminada");
 System.exit(0);
      }catch(IOException e){
        System.out.println("Aqui esta el erro");
        e.printStackTrace();
      }
    }//WHILE TRUE

  }//Main

}//Clase Servidor

