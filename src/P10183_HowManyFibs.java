import java.io.*;
import java.math.BigInteger;
import java.util.*;

@SuppressWarnings("unused")
public class P10183_HowManyFibs {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10183, CASOS = 0, ARCHIVO = 1;

    // Precalculamos los números de Fibonacci hasta 10^100:
    static final BigInteger[] fibs;
    static {
        fibs = new BigInteger[479];
        fibs[0] = BigInteger.ONE;
        fibs[1] = BigInteger.valueOf(2);

        for (int i = 2; i < fibs.length; i++)
            fibs[i] = fibs[i - 1].add(fibs[i - 2]);
    }

    public static void main(String[] args) throws IOException {
        // Inicializamos entrada y salida:
        if (ARCHIVO > 0)
            inicializar(PROB);
        else
            inicializar();

        // Resolvemos el problema:
        int t = 1;
        if (CASOS > 0)
            t = sc.nextInt();

        while (t-- > 0) {
            solve();
            if (t > 0)
                output.append('\n');
        }

        // Imprimimos la salida y cerramos el flujo de entrada:
        System.out.print(output);
        sc.cerrar();
    }

    public static void solve() throws IOException {
        while (true) {
            // Leemos los valores 'a', 'b' y comprobamos si valen 0 los dos o no:
            String strA = sc.next(), strB = sc.next();
            if (strA.charAt(0) == '0' && strB.charAt(0) == '0')
                break;

            // Si no valen 0, los pasamos a BigInteger:
            BigInteger a = new BigInteger(strA), b = new BigInteger(strB);

            // Realizamos una búsqueda binaria con ambos para encontrar el inicio y el final
            // del intervalo que comprenden dentro de la lista de números de Fibonacci:
            int ini = busquedaBinaria(a, true), fin = busquedaBinaria(b, false);

            // Imprimimos la diferencia entre 'ini' y 'fin':
            output.append(fin - ini).append('\n');
        }
    }

    // MÉTODO AUXILIAR: Realiza búsqueda binaria sobre la lista precalculada de
    // números de Fibonacci. Devuelve el índice del primer número MAYOR O IGUAL
    // (lower bound) que 'n' si 'lower' está a true. Si no, devuelve el índice
    // del primer número ESTRICTAMENTE MAYOR que 'n' (upper bound):
    public static int busquedaBinaria(BigInteger n, boolean lower) {
        int izq = 0, der = fibs.length;

        while (izq < der) {
            int medio = izq + (der - izq) / 2; // Hacemos esto en vez de '(izq + der) / 2' para evitar desbordamiento.

            // Como el 1 se repite dos veces en la lista, no podemos directamente devolver
            // 'medio' si 'n' == 'fibs[medio]'.

            if (n.compareTo(fibs[medio]) > (lower ? 0 : -1))
                izq = medio + 1;
            else
                der = medio;
        }

        return izq;
    }

    // -------- CLASES Y MÉTODOS AUXILIARES --------
    public static void inicializar(int cap) throws IOException {
        File f = new File(String.format("inputs/%02d.txt", cap));
        sc = new Lector();
        sc.leerArchivo(f);
        output = new StringBuilder();
    }

    public static void inicializar() {
        sc = new Lector();
        sc.leerStd();
        output = new StringBuilder();
    }

    @SuppressWarnings("unused")
    private static class Lector {
        BufferedReader br;
        StringTokenizer st;

        Lector() {
            br = null;
            st = new StringTokenizer("");
        }

        void leerArchivo(File f) throws IOException {
            br = new BufferedReader(new FileReader(f));
        }

        void leerStd() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        void cerrar() throws IOException {
            br.close();
        }

        boolean hasNext() throws IOException {
            if (st.hasMoreTokens())
                return true;
            String aux = br.readLine();
            if (aux == null)
                return false;
            st = new StringTokenizer(aux);
            return true;
        }

        String next() throws IOException {
            if (!st.hasMoreTokens()) {
                st = new StringTokenizer(br.readLine());
            }
            return st.nextToken();
        }

        String nextLine() throws IOException {
            if (!st.hasMoreTokens()) {
                return br.readLine();
            } else {
                StringBuilder resto = new StringBuilder();
                while (st.hasMoreTokens()) {
                    resto.append(st.nextToken()).append(" ");
                }
                return resto.toString().trim();
            }
        }

        byte nextByte() throws IOException {
            return Byte.parseByte(next());
        }

        short nextShort() throws IOException {
            return Short.parseShort(next());
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        float nextFloat() throws IOException {
            return Float.parseFloat(next());
        }

        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        boolean nextBoolean() throws IOException {
            return Boolean.parseBoolean(next());
        }
    }
}