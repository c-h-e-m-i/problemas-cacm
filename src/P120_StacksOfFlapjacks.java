import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P120_StacksOfFlapjacks {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 120, CASOS = 0, ARCHIVO = 1;
    static int escenario = 1;

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
        // Vamos procesando cada stack de tortitas:
        while (sc.hasNext()) {
            String[] aux = sc.nextLine().split(" ");
            int len = aux.length;
            int[] tortitas = new int[len];

            for (int i = 0; i < len; i++) {
                tortitas[i] = Integer.parseInt(aux[i]);
                output.append(tortitas[i]).append(" ");
            }
            output.append("\n");

            /*
             * La lógica del problema es la siguiente:
             * > Vamos a ir colocando abajo del todo cada vez la tortita más grande de las
             * que nos queden por ordenar.
             * > Como solo podemos invertir desde una tortita hacia arriba, lo primero que
             * haremos será colocar dicha
             * tortita más grande arriba del todo, y luego invertiremos todo el substack de
             * tortitas que nos queden
             * por ordenar:
             */
            int desordenadas = len - 1;

            // Mientras queden tortitas por ordenar...
            while (desordenadas > 0) {
                // Calculamos cuál es la tortita más grande y nos guardamos su índice:
                int max = -1, maxInd = -1;
                for (int i = 0; i <= desordenadas; i++) {
                    if (tortitas[i] > max) {
                        max = tortitas[i];
                        maxInd = i;
                    }
                }

                // Si la tortita más grande ya está abajo del todo, pasamos a la siguiente:
                if (maxInd == desordenadas) {
                    desordenadas--;
                    continue;
                }

                // Invertimos primero desde 'maxInd' para colocar la tortita más grande arriba
                // del todo, y
                // luego invertimos todo el substack desordenado:

                // > Si la tortita más grande ya estaba arriba del todo, no invertimos:
                if (maxInd > 0) {
                    invertir(0, maxInd, tortitas);
                    output.append(len - maxInd).append(" ");
                }
                invertir(0, desordenadas, tortitas);
                output.append(len - desordenadas).append(" ");
                desordenadas--;
            }
            output.append(0).append("\n");
        }
    }

    // MÉTODO AUXILIAR: Invertimos las posiciones entre 'ini' y 'fin' de 'array':
    public static void invertir(int ini, int fin, int[] array) {
        while (ini < fin) {
            intercambiar(ini, fin, array);
            ini++;
            fin--;
        }
    }

    // MÉTODO AUXILIAR: Intercambiamos los elementos en las posiciones 'p1' y 'p2'
    // de 'array':
    public static void intercambiar(int p1, int p2, int[] array) {
        int aux = array[p1];
        array[p1] = array[p2];
        array[p2] = aux;
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

    @SuppressWarnings("unused")
    private static class Pair {
        @SuppressWarnings("unused")
        String autor;
        @SuppressWarnings("unused")
        int nivel;

        @SuppressWarnings("unused")
        Pair(String autor, int nivel) {
            this.autor = autor;
            this.nivel = nivel;
        }
    }
}