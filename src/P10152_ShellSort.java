import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10152_ShellSort {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10152, CASOS = 1, ARCHIVO = 1;

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
            output.append('\n');
        }

        // Imprimimos la salida y cerramos el flujo de entrada:
        System.out.print(output);
        sc.cerrar();
    }

    public static void solve() throws IOException {
        // Leemos el número de tortugas en cada torre:
        int n = sc.nextInt();

        // Nos guardamos cada torre como un array de enteros:
        // > La torre original siempre será un array implícito con los elementos {0,
        // ..., n-1}
        // > La torre ordenada será un array con los índices de cada nombre obtenidos de
        // un hash map:
        HashMap<String, Integer> tortugas = new HashMap<>();
        String[] nombres = new String[n];
        for (int i = n - 1; i >= 0; --i) {
            String nombre = sc.nextLine();
            tortugas.put(nombre, i);
            nombres[i] = nombre;
        }

        int[] torreOrdenada = new int[n];
        int i = n;
        while (i > 0)
            torreOrdenada[--i] = tortugas.get(sc.nextLine());

        // Definimos un puntero para cada torre, avanzando ambos punteros si el valor
        // correspondiente coincide, o solo el de la torre original en caso contrario
        // (añadiendo consecuentemente la tortuga correspondiente a ese puntero en
        // 'eliminados'):
        boolean[] eliminados = new boolean[n];
        int p1 = 0, p2 = 0;
        while (p1 < n) {
            if (torreOrdenada[p2] == p1) {
                p1++;
                p2++;
            } else {
                eliminados[p1] = true;
                p1++;
            }
        }

        // Recorremos la torre ordenada de abajo arriba y printeamos en ese orden los
        // nombres de las tortugas eliminadas:
        for (int tortuga : torreOrdenada)
            if (eliminados[tortuga])
                output.append(nombres[tortuga]).append('\n');
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