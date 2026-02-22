import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10077_SternBrocot {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10077, CASOS = 0, ARCHIVO = 1;

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
            // Leemos la fracción actual y comprobamos que no sea igual a 1 / 1:
            int num = sc.nextInt(), den = sc.nextInt();
            if (num == 1 && den == 1)
                break;
            Fraccion frac = new Fraccion(num, den);

            // Empezando en 1 / 1, nos desplazamos por el árbol de la siguiente manera:
            // - Si 'frac' es mayor que 'actual', nos movemos a la derecha. Si no, a la
            // izquierda.
            Fraccion padreIzq = new Fraccion(0, 1), padreDer = new Fraccion(1, 0), actual = new Fraccion(1, 1);

            while (true) {
                int comp = frac.compareTo(actual);

                if (comp == 0)
                    break;

                if (comp > 0) {
                    padreIzq = actual;
                    output.append('R');
                } else {
                    padreDer = actual;
                    output.append('L');
                }

                actual = padreIzq.mas(padreDer);
            }

            output.append('\n');
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

    private static class Fraccion implements Comparable<Fraccion> {
        int num, den;

        Fraccion(int num, int den) {
            this.num = num;
            this.den = den;
        }

        Fraccion mas(Fraccion f) {
            return new Fraccion(this.num + f.num, this.den + f.den);
        }

        @Override
        public int compareTo(Fraccion f) {
            if (this.num == f.num && this.den == f.den)
                return 0;

            if (this.num * f.den < f.num * this.den)
                return -1;
            else
                return 1;
        }
    }
}