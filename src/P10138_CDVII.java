import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P10138_CDVII {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 10138, CASOS = 1, ARCHIVO = 1;

    public static void main(String[] args) throws IOException {
        // Inicializamos entrada y salida:
        if (ARCHIVO > 0)
            inicializar(PROB);
        else
            inicializar();

        // Resolvemos el problema:
        int t = 1;
        if (CASOS > 0) {
            t = sc.nextInt();
            sc.nextLine();
        }

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
        // Leemos las tarifas para cada hora:
        int tarifas[] = new int[24], i = 0;
        while (i < 24)
            tarifas[i++] = sc.nextInt();

        // Creamos un hash map con las matrículas:
        Map<String, ArrayList<Info>> matriculas = new HashMap<>();

        String linea = sc.nextLine();
        while (linea != null && !linea.isEmpty()) {
            StringTokenizer st = new StringTokenizer(linea);
            String matricula = st.nextToken(), fechaString = st.nextToken();
            int hora = (fechaString.charAt(6) - '0') * 10 + (fechaString.charAt(7) - '0');
            int fecha = fechaAEntero(fechaString, hora);
            boolean accion = st.nextToken().charAt(1) == 'n';
            int km = Integer.parseInt(st.nextToken());

            // Metemos en el tree map la matrícula actual junto a la hora correspondiente y
            // la acción:
            matriculas.computeIfAbsent(matricula, clave -> new ArrayList<>()).add(new Info(fecha, hora, accion, km));

            // Leemos la siguiente línea:
            linea = sc.nextLine();
        }

        // Convertimos el mapa de matrículas en un tree map para que se ordenen por
        // clave:
        matriculas = new TreeMap<>(matriculas);

        /*
         * Ahora, recorreremos cada clave de la siguiente manera:
         * - Primero, ordenaremos su array list asociado según la fecha de los objetos
         * 'Info'.
         * - Luego, consultaremos sus elementos de 2 en 2.
         * - Si el primero es un "enter" y el segundo es un "exit", añadimos el precio
         * de ese trayecto al monto total y nos desplazamos al elemento siguiente al
         * "exit" (es decir, sumamos 2 a la variable 'i' que usemos para iterar).
         * - Si tenemos 2 enters seguidos, desplazaremos la 'i' al segundo para ver si
         * el elemento siguiente es un exit.
         * - Si tenemos 2 exits seguidos, desplazaremos la 'i' después del segundo exit,
         * pues sabremos que ninguno de los dos estará emparejado con un enter.
         * - Si tenemos primero un exit y luego un enter, desplazamos 1 la 'i'.
         */
        for (Map.Entry<String, ArrayList<Info>> entrada : matriculas.entrySet()) {
            String matricula = entrada.getKey();
            ArrayList<Info> registros = entrada.getValue();
            int suma = 200; // Incluimos los 2 euros de impuestos de antemano.

            // Poner el 'null' le indica al método .sort() que use el orden natural (es
            // decir, el 'compareTo()'):
            registros.sort(null);

            i = 0;
            while (i < registros.size() - 1) {
                Info i1 = registros.get(i), i2 = registros.get(i + 1);

                if (i1.entrada && !i2.entrada) {
                    suma += tarifas[i1.hora] * Math.abs(i2.km - i1.km) + 100; // Sumamos 1 euro por viaje
                    i += 2;
                } else if (!i1.entrada && !i2.entrada)
                    i += 2;
                else
                    i++;
            }

            // Imprimimos la información:
            if (suma > 300)
                output.append(matricula).append(" $").append(String.format(Locale.US, "%.2f", suma / 100.0))
                        .append('\n');
        }
    }

    // MÉTODO AUXILIAR: Convertimos una fecha en formato "MM:dd:HH:mm" a un entero
    // concatenando cada campo y eliminando los ':':
    public static int fechaAEntero(String fecha, int hora) {
        int mes = (fecha.charAt(0) - '0') * 10 + (fecha.charAt(1) - '0');
        int dia = (fecha.charAt(3) - '0') * 10 + (fecha.charAt(4) - '0');
        int min = (fecha.charAt(9) - '0') * 10 + (fecha.charAt(10) - '0');

        return (mes * 1000000) + (dia * 10000) + (hora * 100) + min;
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

    private static class Info implements Comparable<Info> {
        int fecha, hora, km;
        boolean entrada;

        Info(int fecha, int hora, boolean entrada, int km) {
            this.fecha = fecha;
            this.hora = hora;
            this.entrada = entrada;
            this.km = km;
        }

        @Override
        public int compareTo(Info p) {
            return Integer.compare(this.fecha, p.fecha);
        }
    }
}