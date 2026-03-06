import java.io.*;
import java.util.*;

public class P10161_AntOnAChessboard {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10161, CASOS = 0, ARCHIVO = 1;

    @SuppressWarnings("unused")
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

        while (t-- > 0)
            solve();

        // Imprimimos la salida y cerramos el flujo de entrada:
        System.out.print(output);
        sc.cerrar();
    }

    public static void solve() throws IOException {
        while (true) {
            int t = sc.nextInt();
            if (t == 0)
                break;

            /*
             * La lógica de este problema es sencilla: la hormiga va moviéndose formando
             * cuadrados perfectos. Partiendo de esto, podemos calcular cuál es el último
             * cuadrado que ha formado truncando la raíz cuadrada de 't', y luego añadirle
             * al resultado tantos pasos como la diferencia entre 't' y su raíz cuadrada
             * truncada elevada de nuevo al cuadrado:
             * - Si la raíz cuadrada es impar, la hormiga habrá terminado su cuadrado en la
             * esquina superior izquierda, así que si la diferencia es menor que el lado del
             * cuadrado, nos moveremos por el eje horizontal. Si no, por el vertical.
             * - Si es par, lo habrá terminado en la esquina inferior derecha, así que ahora
             * empezaremos moviéndonos en vertical y luego en horizontal:
             */
            int lado = (int) Math.sqrt(t), diff = t - lado * lado;

            // Asumimos que 'lado' es impar:
            Pair pos = diff == 0 // Estamos justo en la esquina del cuadrado
                    ? new Pair(1, lado)
                    : (diff <= lado + 1 // Estamos en la línea horizontal encima del cuadrado
                            ? new Pair(diff, lado + 1)
                            : new Pair(lado + 1, 2 * (lado + 1) - diff)); // Estamos en la línea vertical a la
                                                                          // derecha del cuadrado

            // Pero si es par, basta con trasponer las cordenadas de 'pos':
            if ((lado & 1) == 0) {
                int aux = pos.x;
                pos.x = pos.y;
                pos.y = aux;
            }

            // Imprimimos 'pos':
            output.append(pos.x).append(' ').append(pos.y).append('\n');
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
            if (st.hasMoreTokens()) {
                StringBuilder resto = new StringBuilder();
                while (st.hasMoreTokens()) {
                    resto.append(st.nextToken()).append(" ");
                }
                return resto.toString();
            }
            return br.readLine();
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

    @SuppressWarnings("unused")
    private static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Pair mas(Pair p) {
            return new Pair(this.x + p.x, this.y + p.y);
        }
    }
}