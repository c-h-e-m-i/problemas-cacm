import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10044_ErdosNumbers {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10044, CASOS = 1, ARCHIVO = 1;
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
        int P = sc.nextInt(), N = sc.nextInt();

        // Creamos el grafo con los autores de cada paper:
        HashMap<String, HashSet<String>> grafo = crearGrafo(P);

        // Ejecutamos BFS sobre el grafo partiendo del nodo "Erdös".
        // Nos guardaremos todos los autores del grafo en un HashMap de entradas <autor,
        // número de Erdös>:
        HashMap<String, Integer> numeros = BFS(grafo, "Erdos, P.");

        // Añadimos a 'output' el nombre de cada autor junto a su número de Erdös:
        output.append("Scenario ").append(escenario).append('\n');

        while (N-- > 0) {
            String autor = sc.nextLine();

            output.append(autor).append(" ");
            output.append(numeros.containsKey(autor) ? numeros.get(autor) : "infinity");
            output.append("\n");
        }

        // Aumentamos el índice del escenario:
        escenario++;
    }

    public static HashMap<String, HashSet<String>> crearGrafo(int P) throws IOException {
        // Inicializamos el grafo:
        HashMap<String, HashSet<String>> grafo = new HashMap<>();

        // Vamos leyendo la información de cada paper:
        for (int i = 0; i < P; i++) {
            String paper = sc.nextLine();

            // Realizamos un split sobre la lista de autores y juntamos los strings
            // resultantes por pares:
            paper = paper.split(":", 2)[0];
            String[] aux = paper.split(", "), autores = new String[(int) Math.ceil(aux.length / 2.0)];
            for (int j = 0; j < aux.length; j += 2)
                autores[j / 2] = j < aux.length - 1 ? aux[j] + ", " + aux[j + 1] : aux[j];

            // Añadimos cada par de autores al grafo:
            for (String autor : autores) {
                grafo.computeIfAbsent(autor, k -> new HashSet<>()).addAll(Arrays.asList(autores));
                grafo.get(autor).remove(autor);
            }
        }

        return grafo;
    }

    public static HashMap<String, Integer> BFS(HashMap<String, HashSet<String>> grafo, String ini) {
        Queue<Pair> cola = new LinkedList<>();
        cola.offer(new Pair(ini, 0));

        HashSet<String> visitados = new HashSet<>();
        visitados.add(ini);

        // Creamos un HashMap para guardar el número de Erdös de cada autor:
        HashMap<String, Integer> numeros = new HashMap<>();

        while (!cola.isEmpty()) {
            Pair nodo = cola.poll();

            // Añadimos el autor de cada nodo junto a su número de Erdös a 'numeros':
            numeros.put(nodo.autor, nodo.nivel);

            for (String vecino : grafo.get(nodo.autor))
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(new Pair(vecino, nodo.nivel + 1));
                }
        }

        return numeros;
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