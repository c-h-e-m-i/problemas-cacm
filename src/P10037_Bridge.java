import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10037_Bridge {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10037, CASOS = 1, ARCHIVO = 1;
    static int[] tiempos;
    static StringBuilder sb;

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
        sc.nextLine();

        // Leemos el número de personas:
        int n = sc.nextInt();

        // Leemos lo que tarda cada persona en cruzar el puente y lo guardamos en un
        // array con las frecuencias para cada número:
        int segundos[] = new int[101], i = n;

        while (i-- > 0) {
            segundos[sc.nextInt()]++;
        }

        // Ordenamos las velocidades de menor a mayor (con un Counting Sort INESTABLE
        // -no mantiene el orden relativo de los elementos, pero para primitivas enteras
        // no lo necesitamos-):
        tiempos = new int[n];
        int seg = 1;
        i = 0;
        while (i < n) {
            while (segundos[seg] <= 0) {
                seg++;
            }

            tiempos[i++] = seg;
            segundos[seg]--;
        }

        sb = new StringBuilder();

        // Comprobamos los casos triviales:
        if (n <= 3) {
            output.append(casosTriviales(n)).append('\n');
        }

        // Comprobamos el resto de casos:
        else {
            int suma = 0;
            i = n - 1;
            while (i >= 3) {
                int opcionA = tiempos[0] + 2 * tiempos[1] + tiempos[i],
                        opcionB = 2 * tiempos[0] + tiempos[i - 1] + tiempos[i];

                if (opcionA <= opcionB) {
                    cruza(0, 1);
                    cruza(0);
                    cruza(i - 1, i);
                    cruza(1);

                    suma += opcionA;
                } else {
                    cruza(0, i);
                    cruza(0);
                    cruza(0, i - 1);
                    cruza(0);

                    suma += opcionB;
                }

                i -= 2;
            }
            suma += casosTriviales(i + 1);
            output.append(suma).append('\n');
        }

        output.append(sb);
    }

    public static void cruza(int a) {
        sb.append(tiempos[a]).append('\n');
    }

    public static void cruza(int a, int b) {
        sb.append(tiempos[a]).append(' ').append(tiempos[b]).append('\n');
    }

    public static int casosTriviales(int n) {
        int ret = 0;
        switch (n) {
            case 1 -> {
                ret = tiempos[0];
                cruza(0);
            }
            case 2 -> {
                ret = tiempos[1];
                cruza(0, 1);
            }
            case 3 -> {
                ret = tiempos[0] + tiempos[1] + tiempos[2];
                cruza(0, 1);
                cruza(0);
                cruza(0, 2);
            }
        }

        return ret;
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