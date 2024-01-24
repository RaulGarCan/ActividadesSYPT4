import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("localhost",5555);
            OutputStream out = cliente.getOutputStream();
            ObjectOutputStream escribir = new ObjectOutputStream(out);
            Scanner userInput = new Scanner(System.in);
            System.out.print("Introduce el primer operando: ");
            int op1 = Integer.parseInt(userInput.nextLine());
            System.out.print("Introduce el primer operando: ");
            int op2 = Integer.parseInt(userInput.nextLine());
            escribir.writeObject(new Operando(op1, op2));
            escribir.flush();
            System.out.println("Sumar | Restar | Multiplicar | Dividir.");
            System.out.print("Introduce la operacion a realizar: ");
            String operacion = userInput.nextLine();
            Operador operador = null;
            switch (operacion.toLowerCase()){
                case "sumar":
                    operador = Operador.SUMA;
                    break;
                case "restar":
                    operador = Operador.RESTA;
                    break;
                case "multiplicar":
                    operador = Operador.MULTIPLICACION;
                    break;
                case "dividir":
                    operador = Operador.DIVISION;
                    break;
                default:
                    System.out.println("Error");
                    break;
            }
            escribir.writeObject(operador);
            escribir.flush();
            InputStream in = cliente.getInputStream();
            BufferedReader leer = new BufferedReader(new InputStreamReader(in));
            System.out.println(leer.readLine());
            escribir.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
