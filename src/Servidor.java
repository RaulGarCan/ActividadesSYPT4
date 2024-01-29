import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {
    Socket conexion;
    public Servidor(Socket conexion){
        this.conexion = conexion;
    }
    public static void main(String[] args) {
        ServerSocket server;
        try {
            server = new ServerSocket(5555);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                Socket socket = server.accept();
                Thread hilo = new Thread(new Servidor(socket));
                hilo.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    @Override
    public void run() {
        boolean ejecucion = true;
        System.out.println("conectado");
        while (ejecucion) {
            InputStream in;
            try {
                in = conexion.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObjectInputStream leer;
            try {
                leer = new ObjectInputStream(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Operando op;
                try {
                    op = (Operando) leer.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(op.getOp1() + " " + op.getOp2());
                Operador operador;
                try {
                    operador = (Operador) leer.readObject();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(operador.name());
                String resultado;
                switch (operador.name().toLowerCase()) {
                    case "suma":
                        resultado = String.valueOf(sumar(op.getOp1(), op.getOp2()));
                        break;
                    case "resta":
                        resultado = String.valueOf(restar(op.getOp1(), op.getOp2()));
                        break;
                    case "multiplicacion":
                        resultado = String.valueOf(multiplicar(op.getOp1(), op.getOp2()));
                        break;
                    case "division":
                        resultado = String.valueOf(dividir(op.getOp1(), op.getOp2()));
                        break;
                    default:
                        ejecucion = false;
                        resultado = null;
                        break;
                }
                OutputStream out;
                try {
                    out = conexion.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ObjectOutputStream escribir = new ObjectOutputStream(out);
                try {
                    if(ejecucion){
                        escribir.writeObject(resultado);
                        escribir.flush();
                    } else {
                        escribir.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            conexion.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
