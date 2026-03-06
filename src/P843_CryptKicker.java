import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P843_CryptKicker {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 843, CASOS = 0, ARCHIVO = 1;
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
        }

        // Imprimimos la salida y cerramos el flujo de entrada:
        System.out.print(output);
        sc.cerrar();
    }

    public static void solve() throws IOException {
        // Leemos las palabras de entrada y las transformamos en patrones:
        int num_palabras = sc.nextInt();
        HashMap<String, HashSet<String>> descifradas = new HashMap<>();

        while (num_palabras-- > 0) {
            String palabra = sc.nextLine();
            descifradas.computeIfAbsent(procesar(palabra), clave -> new HashSet<>()).add(palabra);
        }

        boolean linea_blanco = false;
        // Hacemos lo mismo con las de cada frase:
        while (true) {
            if (linea_blanco)
                output.append('\n');

            linea_blanco = true;

            String frase = sc.nextLine();

            if (frase == null)
                break;

            StringTokenizer st = new StringTokenizer(frase);
            HashSet<String> palabras_frase = new HashSet<>();
            while (st.hasMoreTokens())
                palabras_frase.add(st.nextToken());

            HashMap<String, String> cifradas = new HashMap<>();
            HashMap<String, Integer> frec_patron = new HashMap<>();
            boolean imposible = false;

            for (String palabra : palabras_frase) {
                String patron = procesar(palabra);

                // CONDICIÓN 1: El patrón debe existir dentro de las palabras descifradas:
                if (!descifradas.containsKey(patron)) {
                    imposible = true;
                    break;
                }

                frec_patron.merge(patron, 1, Integer::sum);
                cifradas.put(palabra, patron);

                // CONDICIÓN 2: No puede haber más ocurrencias de un patrón en las palabras
                // cifradas que en las descifradas:
                if (frec_patron.get(patron) > descifradas.get(patron).size()) {
                    imposible = true;
                    break;
                }
            }

            // Si 'imposible' está a 'false', significa que aún es viable que podamos
            // traducir la frase.
            // Para ello, haremos backtracking con cada ocurrencia de cada patrón,
            // al mismo tiempo que llenamos un diccionario con las equivalencias de cada
            // letra:
            char[] diccionario = imposible ? null
                    : backtracking(0, new ArrayList<>(palabras_frase), cifradas, descifradas,
                            new HashSet<>(), new char[26], new BitSet(26));

            // Si hemos sido capaces de corresponder todas las palabras cifradas con alguna
            // de las descifradas, traducimos la frase resultante. Si no, imprimimos
            // asteriscos:
            traducir(frase, diccionario);
        }

    }

    // MÉTODO AUXILIAR: Realizamos backtracking para corresponder las palabras
    // cifradas con las descifradas:
    public static char[] backtracking(int ind_elem, ArrayList<String> palabras_frase, HashMap<String, String> patrones,
            HashMap<String, HashSet<String>> descifradas, HashSet<String> usadas, char[] diccionario,
            BitSet letras_usadas) {

        if (ind_elem == palabras_frase.size())
            return diccionario;

        String cifrada = palabras_frase.get(ind_elem);
        String patron = patrones.get(cifrada);
        int[] cambios = new int[26];

        for (String descifrada : descifradas.get(patron)) {
            if (!usadas.contains(descifrada)) {
                int contador_cambios = corresponder(cifrada, descifrada, diccionario, letras_usadas, cambios);

                // Como 'corresponder' devuelve un número negativo si no ha conseguido terminar
                // la correspondencia, solo haremos backtracking si lo que ha devuelto es mayor
                // o igual que 0:
                if (contador_cambios >= 0) {
                    usadas.add(descifrada);
                    char[] res = backtracking(ind_elem + 1, palabras_frase, patrones, descifradas, usadas, diccionario,
                            letras_usadas);
                    if (res != null)
                        return res;
                    usadas.remove(descifrada);
                } else
                    contador_cambios = Math.abs(contador_cambios + 1);

                // Restauramos los cambios:
                for (int i = 0; i < contador_cambios; i++) {
                    int cambio = cambios[i];
                    letras_usadas.clear(diccionario[cambio] - 'a');
                    diccionario[cambio] = 0;
                }
            }
        }

        return null;
    }

    // MÉTODO AUXILIAR: Procesar dos palabras y añadir la correspondencia de sus
    // letras a un diccionario:
    public static int corresponder(String p1, String p2, char[] diccionario, BitSet letras_usadas, int[] cambios) {
        int contador_cambios = 0;

        for (int i = 0; i < p1.length(); i++) {
            int letra1 = p1.charAt(i) - 'a';
            char letra2 = p2.charAt(i);

            if (diccionario[letra1] == 0) {
                if (letras_usadas.get(letra2 - 'a'))
                    return -contador_cambios - 1;

                diccionario[letra1] = letra2;
                letras_usadas.set(letra2 - 'a');
                cambios[contador_cambios++] = letra1;
            } else if (diccionario[letra1] != letra2)
                return -contador_cambios - 1;
        }

        return contador_cambios;
    }

    // MÉTODO AUXILIAR: Extraemos el patrón de una palabra (por ejemplo, para
    // "alta", el patrón es "1231", pues identificaremos cada letra con un número,
    // asignándoles los números de izquierda a derecha):
    public static String procesar(String palabra) {
        char[] letras = palabra.toCharArray();
        byte orden[] = new byte[26], contador = 0;
        StringBuilder res = new StringBuilder();

        for (char c : letras) {
            if (orden[c - 'a'] == 0)
                orden[c - 'a'] = ++contador;

            res.append(orden[c - 'a']).append('.');
        }

        return res.toString();
    }

    // MÉTODO AUXILIAR: Imprimimos la frase de entrada descifrándola según
    // 'diccionario':
    public static void traducir(String frase, char[] diccionario) {
        if (diccionario == null) {
            asteriscos(frase);
            return;
        }

        char[] letras = frase.toCharArray();

        for (int i = 0; i < letras.length; i++)
            if (letras[i] != ' ')
                letras[i] = diccionario[letras[i] - 'a'];

        output.append(letras);
    }

    // MÉTODO AUXILIAR: Imprimimos todas las palabras de una frase como asteriscos:
    public static void asteriscos(String frase) {
        char[] letras = frase.toCharArray();

        for (int i = 0; i < letras.length; i++)
            if (letras[i] != ' ')
                letras[i] = '*';

        output.append(letras);
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
                return resto.toString().trim();
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
}