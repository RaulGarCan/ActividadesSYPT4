import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5555);
            while(true){
                Socket conexion = server.accept();
                System.out.println("conectado");
                InputStream in = conexion.getInputStream();
                ObjectInputStream leer = new ObjectInputStream(in);
                try {
                    Operando op = (Operando) leer.readObject();
                    System.out.println(op.getOp1()+" "+ op.getOp2());
                    Operador operador = (Operador) leer.readObject();
                    System.out.println(operador.name());
                    String resultado;
                    switch (operador.name().toLowerCase()){
                        case "suma":
                            resultado = String.valueOf(sumar(op.getOp1(),op.getOp2()));
                            break;
                        case "resta":
                            resultado = String.valueOf(restar(op.getOp1(),op.getOp2()));
                            break;
                        case "multiplicacion":
                            resultado = String.valueOf(multiplicar(op.getOp1(),op.getOp2()));
                            break;
                        case "division":
                            resultado = String.valueOf(dividir(op.getOp1(), op.getOp2()));
                            break;
                        default:
                            resultado = null;
                            break;
                    }
                    OutputStream out = conexion.getOutputStream();
                    BufferedWriter escribir = new BufferedWriter(new OutputStreamWriter(out));
                    escribir.write(resultado);
                    escribir.flush();
                    escribir.close();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int sumar(int n1, int n2){
        return n1+n2;
    }
    public static int restar(int n1, int n2){
        return n1-n2;
    }
    public static int multiplicar(int n1, int n2){
        return n1*n2;
    }
    public static float dividir(int n1, int n2){
        if(n2==0){
            return 0;
        }
        return (float) n1 /n2;
    }
}
