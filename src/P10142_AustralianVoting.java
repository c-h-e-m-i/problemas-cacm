import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10142_AustralianVoting {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10142, CASOS = 1, ARCHIVO = 1;

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

        sc.nextLine();
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
        int cand = sc.nextInt();
        String[] candidatos = new String[cand + 1];

        // Rellenamos un array de strings con los candidatos:
        for (int i = 1; i <= cand; i++) {
            candidatos[i] = sc.nextLine();
        }

        // Creamos una lista de colas con las papeletas:
        ArrayList<Queue<Integer>> papeletas = new ArrayList<>();
        String linea = sc.nextLine();

        while (linea != null && !linea.isEmpty()) {
            Queue<Integer> papeleta = new LinkedList<>();
            StringTokenizer votos = new StringTokenizer(linea);

            while (votos.hasMoreTokens()) {
                papeleta.offer(Integer.valueOf(votos.nextToken()));
            }

            papeletas.add(papeleta);
            linea = sc.nextLine();
        }

        // Creamos un array para guardarnos los candidatos eliminados:
        boolean[] eliminado = new boolean[cand + 1];

        while (true) {
            // Creamos un array para contabilizar los votos para cada cantidado:
            int votos[] = new int[cand + 1];

            // Contabilizamos los votos:
            for (Queue<Integer> papeleta : papeletas) {
                while (eliminado[papeleta.peek()]) {
                    papeleta.poll();
                }

                votos[papeleta.peek()]++;
            }

            // Comprobamos el ganador o si hay que hacer otra ronda:
            boolean empate = true;
            int max = -1, min = Integer.MAX_VALUE, maxInd = 0, votantes = papeletas.size(), ultVoto = -1;
            ArrayList<Integer> minInds = new ArrayList<>();
            for (int i = 1; i < votos.length; i++) {
                if (eliminado[i])
                    continue;

                // Comprobamos si hay empate entre todos los candidatos:
                if (votos[i] != ultVoto && ultVoto != -1)
                    empate = false;

                ultVoto = votos[i];

                // Si un candidato consigue el mayor número de votos hasta ahora, nos lo
                // guardamos:
                if (votos[i] > max) {
                    max = votos[i];
                    maxInd = i;
                }

                // Si el candidato actual tiene el mismo número de votos que el mínimo, lo
                // añadimos a la lista de candidatos a eliminar:
                if (votos[i] == min) {
                    minInds.add(i);
                }

                // Si actualizamos el número mínimo de votos, borramos la lista de candidatos a
                // eliminar y añadimos exclusivamente al candidato actual:
                if (votos[i] < min) {
                    min = votos[i];
                    minInds = new ArrayList<>();
                    minInds.add(i);
                }
            }

            // Si hay empate, imprimimos todos los candidatos no eliminados:
            if (empate) {
                for (int i = 1; i < votos.length; i++) {
                    if (!eliminado[i]) {
                        output.append(candidatos[i]).append('\n');
                    }
                }
                break;
            }

            // Vemos si el candidato con más votos supera el 50 %, en cuyo caso lo
            // imprimimos:
            if (max * 2 > votantes) {
                output.append(candidatos[maxInd]).append('\n');
                break;
            }

            // Si no se da uno de los dos casos anteriores, eliminamos los candidatos con
            // menos votos (marcándolos a true en 'eliminado') y hacemos un reconteo:
            for (int ind : minInds)
                eliminado[ind] = true;
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