import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10202_PairsumoniousNumbers {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10202, CASOS = 0, ARCHIVO = 1;

    // Datos del problema:
    static int[] nums;

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
            // Leemos los datos de entrada:
            int n = sc.nextInt(), pares = n * (n - 1) / 2, valores[] = new int[pares];

            for (int i = 0; i < pares; i++)
                valores[i] = sc.nextInt();

            Arrays.sort(valores);

            // Creamos un TreeMap que guarde las sumas pendientes por cubrir y sus
            // frecuencias:
            TreeMap<Integer, Integer> original = new TreeMap<>();
            for (int val : valores)
                original.merge(val, 1, Integer::sum);

            // Quitamos las 2 primeras sumas del mapa (pues ya sabemos qué sumandos tienen):
            quitar(original, valores[0]);
            quitar(original, valores[1]);

            // Creamos un array para los números que debemos descubrir:
            nums = new int[n];

            // Declaramos un booleano que recogerá si hemos sido capaces de encontrar o no
            // un conjunto de números válidos:
            boolean acabado = false;

            /*
             * ESTRATEGIA DE RESOLUCIÓN:
             *
             * - Vamos a emplear backtracking para resolver el problema.
             *
             * - ¿Qué sabemos de antemano?:
             * > La suma más pequeña se ha obtenido con los 2 sumandos más pequeños (nums[0]
             * y nums[1]).
             * > La segunda suma más pequeña se ha obtenido con 'nums[0]' y 'nums[2]'.
             * > Lo mismo aplica para las dos sumas más grandes, pero estas no las vamos a
             * considerar.
             * 
             * - El problema reside en la siguiente cuestión: ¿la tercera suma más pequeña
             * es 'nums[0] + nums[i]' o 'nums[1] + nums[2]'?
             * > Aquí es donde vamos a aplicar backtracking. Iteraremos sobre todas las
             * sumas desde la tercera más pequeña (valores[2]) a la más grande, y asumiremos
             * que esa suma es 'nums[1] + nums[2]'.
             * > Si realmente lo es, podremos calcular 'nums[0]' como:
             * nums[0] = [SUMA(0) + SUMA(1) - SUMA(I)] / 2
             * = [(nums[0] + nums[1]) + (nums[0] + nums[2]) - (nums[1] + nums[2])] / 2
             * = (2 * nums[0]) / 2
             * > Partiendo de esto, podremos descartar algunas posibilidades si vemos que
             * SUMA(0) + SUMA(1) + SUMA(I) no es par.
             * 
             * - Una vez tengamos 'nums[0]', podemos obtener 'nums[1]' y 'nums[2]'
             * restándolo a SUMA(0) y SUMA(1), respectivamente.
             * 
             * - Para este punto, ya habremos cubierto las sumas de los pares <0, 1>, <0, 2>
             * y <1, 2>, así que la suma más pequeña que no hayamos cubierto aún tendrá que
             * ser SÍ o SÍ la del par <0, 3> (usaremos un TreeMap precisamente para poder
             * obtener esa suma más pequeña con el método .firstKey()).
             * 
             * - NOTA: Cada vez que "cubramos" una suma nos referiremos a restar 1 a la
             * frecuencia de dicha suma en el TreeMap 'sumas'. Si la frecuencia alcanza 0,
             * borraremos la clave. Y si la clave no existe, no podremos cubrir la suma y
             * por tanto realizaremos backtracking (explicaré esto más abajo).
             * 
             * - Teniendo ya la SUMA(<0, 3>), podemos obtener 'nums[3]' restándole
             * 'nums[0]'.
             * 
             * - Para este punto, ya tendremos 'nums[0]', 'nums[1]', 'nums[2]' y 'nums[3]'.
             * Por tanto, aparte de los pares que ya hemos cubierto ({<0, 1>, <0, 2>,
             * <0, 3>, <1, 2>}), ahora podremos cubrir también los pares <1, 3> y <2, 3>.
             * Es decir, todos aquellos pares del tipo <i, 3>, donde i ∈ {1, 2}.
             * 
             * - Una vez cubiertos esos pares, se vuelve a cumplir que la suma más pequeña
             * que nos queda por cubrir debe corresponder SÍ o SÍ al par <0, 4>, así que
             * aplicamos el método .firstKey() sobre nuestro TreeMap para obtener
             * SUMA(<0, 4>) y luego calculamos 'nums[4]' como SUMA(<0, 4>) - 'nums[0]'.
             * 
             * - Teniendo ya 'nums[4]', podemos cubrir las sumas de los pares <i, 4>, donde
             * i ∈ {1, 2, 3}.
             * 
             * - Fíjate que ya hemos entrado en un bucle donde siempre debemos seguir los
             * mismos pasos. Bastará con aplicarlos sucesivamente hasta tener todos los
             * números.
             * 
             * - Si la suma de algún par <i, j> no se pudiese cubrir, significa que la suma
             * del par <1, 2> no es la que hemos asumido, por lo que restablecemos el
             * TreeMap original y probamos con la siguiente candidata.
             * 
             * - Si todas las candidatas fallan, significará que es IMPOSIBLE obtener un
             * conjunto de números válidos para la entrada proporcionada.
             * 
             * - IMPORTANTE: Tras obtener el último número (nums[n - 1]) debes comprobar que
             * puedes cubrir las sumas de los pares <i, n-1>. No te vale con haber
             * completado el array, pues puede que alguno de esos pares no tenga suma
             * asociada. La condición necesaria para considerar válidos los valores de
             * 'nums' debe ser que el TreeMap se quede VACÍO, pues eso significará que hemos
             * probado con todos los pares posibles y cada uno nos ha dado un valor distinto
             * del map.
             */
            for (int i = 2; i < pares; i++) {
                TreeMap<Integer, Integer> sumas = new TreeMap<>(original);
                int res = valores[0] + valores[1] - valores[i];
                if ((res & 1) == 1)
                    continue;

                int contador = 2;
                nums[0] = res / 2;
                nums[1] = valores[0] - nums[0];
                nums[2] = valores[1] - nums[0];

                while (borrarSumas(sumas, contador) && !sumas.isEmpty()) {
                    contador++;
                    int sumaMin = sumas.firstKey();
                    nums[contador] = sumaMin - nums[0];
                    quitar(sumas, sumaMin);
                }

                if (sumas.isEmpty()) {
                    acabado = true;
                    break;
                }
            }

            // Si hemos salido del bucle porque hemos encontrado un conjunto de números
            // válidos, lo imprimimos. Si no, mostramos "Impossible":
            if (acabado)
                printArray(nums);
            else
                output.append("Impossible").append('\n');
        }
    }

    // MÉTODO AUXILIAR: Reducimos en 1 la frecuencia de la clave 'clave' del map
    // 'mapa':
    public static boolean quitar(Map<Integer, Integer> mapa, int clave) {
        if (!mapa.containsKey(clave))
            return false;

        int frec = mapa.get(clave);
        if (frec == 1)
            mapa.remove(clave);
        else
            mapa.put(clave, frec - 1);

        return true;
    }

    // MÉTODO AUXILIAR: Borramos todas las sumas asociadas a los pares del tipo
    // <i, n>, donde 'i' es un valor en {1, ..., n-1}:
    public static boolean borrarSumas(Map<Integer, Integer> mapa, int n) {
        for (int i = 1; i < n; i++)
            if (!quitar(mapa, nums[i] + nums[n]))
                return false;

        return true;
    }

    // MÉTODO AUXILIAR: Imprimimos el array de entrada:
    public static void printArray(int[] array) {
        int i;
        for (i = 0; i < array.length - 1; i++)
            output.append(array[i]).append(' ');

        output.append(array[i]).append('\n');
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