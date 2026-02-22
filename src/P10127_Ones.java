import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10127_Ones {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10127, CASOS = 0, ARCHIVO = 1;
    static int[][] tablas = new int[10][10];

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

        // > Precalculamos las tablas de cada número:
        calcularTablas();

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
        while (sc.hasNext()) {
            int n = sc.nextInt(), restante = n, contador = 0;

            while (restante > 0) {
                /*
                 * Cogemos el último dígito de 'restante' y el último de 'n' y vemos qué
                 * múltiplo de 'n' debemos sumar a 'restante' para que su último dígito sea 1.
                 * Por ejemplo, si 'restante' = 876 y 'n' = 17, debemos sumarle a 'restante' un
                 * número acabado en 5 para que su último dígito sea 1. El primer múltiplo de
                 * 'n' que cumplirá esto será el primer múltiplo de 7 (el último dígito de 'n')
                 * acabado en 5, que es 35 (7 * 5). Por tanto, tenemos que sumarle 'n' * 5 a
                 * 'restante' para que su último dígito valga 1: 876 + 17 * 5 = 961. El
                 * siguiente valor de 'restante' será, por tanto, 96.
                 */
                int ultN = n % 10, ultRestante = restante % 10;

                int dif = 11 - ultRestante;
                if (dif >= 10)
                    dif -= 10;

                // Consulto por qué número debo multiplicar 'n' para que, sumando el resultado a
                // 'restante', el último dígito de este sea 1:
                int mult = tablas[ultN][dif];

                // Sumo 'n' * 'mult' a 'restante' y quito todos los 1s que le queden al final:
                restante += n * mult;

                while (restante % 10 == 1) {
                    restante /= 10;
                    contador++;
                }
            }
            output.append(contador).append('\n');
        }
    }

    // MÉTODO AUXILIAR: Precalculamos el valor por el que hay que multiplicar 'i'
    // tal que el último dígito del resultado sea 'j' y lo guardamos en
    // 'tablas[i][j]'.
    // Por ejemplo, 'tablas[3][1]' = 7 porque 3 * 7 = 21 <-- El último dígito es 1:
    public static void calcularTablas() {
        for (int i = 0; i <= 9; i++) {
            for (int mult = 0; mult <= 9; mult++) {
                int j = i * mult % 10;
                tablas[i][j] = mult;
            }
        }
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
        String autor;
        int nivel;

        Pair(String autor, int nivel) {
            this.autor = autor;
            this.nivel = nivel;
        }
    }
}