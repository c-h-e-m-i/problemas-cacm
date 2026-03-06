import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10139_Factovisors {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10139, CASOS = 0, ARCHIVO = 1;

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
        }

        // Imprimimos la salida y cerramos el flujo de entrada:
        System.out.print(output);
        sc.cerrar();
    }

    public static void solve() throws IOException {
        while (sc.hasNext()) {
            int n = sc.nextInt(), m = sc.nextInt();

            // Si tenemos 0!, el resultado es 1 (por simplicidad, cambiaremos la n a 1):
            int nAux = n;
            if (n == 0)
                nAux = 1;

            // El problema está mal implementado en el juez, y cuando 'm' = 0 acepta
            // siempre:
            boolean valido = m == 0;

            if (!valido) {
                valido = true;

                // Factorizamos 'm':
                ArrayList<Pair> factores = factorizar(m);

                // Comprobamos si podemos "agotar" todas las potencias de la factorización
                // dentro de n!:
                for (Pair factor : factores) {
                    if (!agotarExp(factor, nAux)) {
                        valido = false;
                        break;
                    }
                }
            }

            String op = valido ? " divides " : " does not divide ";
            output.append(m).append(op).append(n).append("!\n");
        }
    }

    static ArrayList<Pair> factorizar(int x) {
        ArrayList<Pair> factores = new ArrayList<>();

        // Empezamos sacando la potencia de 2 dentro de la factorización:
        Pair factor = new Pair(2, 0);
        while ((x & 1) == 0) {
            factor.e++;
            x /= 2;
        }
        if (factor.e > 0)
            factores.add(factor);

        // Ahora, probamos con todos los impares hasta la raíz de 'x' (piensa que los
        // números compuestos nunca los añadiremos como factores porque ya habremos
        // dividido 'x' previamente por todos sus factores. Por ejemplo, nunca tendremos
        // el 15 como factor, porque ya habremos agotado todos los 3s y 5s contenidos en
        // la factorización de 'x'):
        for (int i = 3; i * i <= x; i += 2) {
            factor = new Pair(i, 0);
            while (x % i == 0) {
                factor.e++;
                x /= i;
            }
            if (factor.e > 0)
                factores.add(factor);
        }

        // Si llegados a ese punto 'x' es mayor que 1, significa que el último factor es
        // primo y aparece elevado a 1:
        if (x > 1)
            factores.add(new Pair(x, 1));

        return factores;
    }

    // MÉTODO AUXILIAR: Probamos si la potencia p^e está contenida dentro de los
    // factores de n!:
    static boolean agotarExp(Pair potencia, int n) {
        int p = potencia.p, e = potencia.e;
        for (int i = p; i <= n && e > 0; i += p) {
            int j = i;
            while (e > 0 && j % p == 0) {
                e--;
                j /= p;
            }
        }

        return e == 0;
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

    private static class Pair {
        int p; // Base
        int e; // Exponente

        Pair(int p, int e) {
            this.p = p;
            this.e = e;
        }
    }
}