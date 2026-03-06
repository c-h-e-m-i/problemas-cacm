import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class P847_MultiplicationGame {
    static Lector sc;
    static StringBuilder output;
    static final int PROB = 847, CASOS = 0, ARCHIVO = 1;
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
        while (sc.hasNext()) {
            long n = sc.nextLong();
            boolean par = true;

            /*
             * La lógica de este problema es bastante simple y se entiende intuitivamente
             * con un ejemplo:
             *
             * - Imagina que somos Stan y queremos jugar de manera óptima. Si 'n' = 2226,
             * queremos obtener en nuestro último turno un valor mayor o igual que él.
             * 
             * - Para ello, necesitaremos que Ollie haya sacado un valor mayor o igual que
             * ⌈2226 / 9⌉ = 248, de modo que podamos multiplicarlo por 9 y superar el 2226.
             * 
             * - Para que Ollie obtenga sí o sí ese valor, nosotros tendremos que haberle
             * pasado previamente un valor mayor o igual que ⌈248 / 2⌉ = 144, ya que, como
             * tiene que multiplicar como mínimo por 2, haga lo que haga el resultado de su
             * multiplicación superará o igualará el 248.
             * 
             * - Si queremos obtener un valor >= que 144, Ollie deberá haber obtenido
             * previamente un valor mayor o igual que ⌈144 / 9⌉ = 16, por lo mismo que hemos
             * explicado antes.
             * 
             * - Repitiendo esto sucesivamente, obtendremos la siguiente traza:
             * 
             * STAN: 2226
             * OLLIE: 248
             * STAN: 144
             * OLLIE: 16
             * STAN: 8
             * OLLIE: 1
             * 
             * > Como Stan elige el primer número, si su primera aparición en la traza es
             * mayor que 1, tiene el control del juego (en este caso, le bastará con elegir
             * 8 al principio para ganar. Ollie necesitaría alcanzar el 144 de Stan para
             * darle la vuelta al juego, pero esto nunca lo conseguirá porque entre 8 y 144
             * hay una multiplicación por 2 y otra por 9 (es decir, por 18), y en un solo
             * turno Ollie solo puede multiplicar hasta 9).
             * 
             * - Sin embargo, en otro caso como el de 'n' = 322, obtendremos la siguiente
             * traza:
             * 
             * STAN: 322
             * OLLIE: 36
             * STAN: 18
             * OLLIE: 2
             * STAN: 1
             * 
             * > El primer movimiento que haga Stan le dará un resultado entre 2 y 9. Si
             * vemos la traza de Ollie:
             * 
             * OLLIE: 322
             * STAN: 36
             * OLLIE: 18
             * STAN: 2
             * OLLIE: 1
             * 
             * >> Para ganar solo necesita que Stan saque un número entre 2 y 17 en su
             * primer turno, cosa que ocurrirá.
             * >> Ahora bien, si hubiera necesitado que Stan sacara un número mayor o igual
             * que 3, ya no habría ganado, pues Stan podría haber sacado un 2 y darle la
             * vuelta al juego.
             * 
             * - La ESTRATEGIA DE RESOLUCIÓN, por tanto, será ver cuál de los dos se come el
             * 1 en la traza del equipo de Stan (es decir, aquella donde aparece STAN = 'n'
             * arriba del todo):
             * > Si en la traza vemos OLLIE = 1, significa que por encima estará STAN = i,
             * donde i ∈ [2, ..., 9], pues cuando pasamos de Stan a Ollie dividimos entre 9.
             * > Si en la traza vemos STAN = 1, significa que por encima estará OLLIE = 2,
             * pues cuando pasamos de Ollie a Stan dividimos entre 2.
             * > Por tanto, el primer nombre que aparezca seguido de un 1 en lo más bajo de
             * la traza será el perdedor.
             */

            /*
             * CÓDIGO ANTERIOR:
             * while (n > 1) {
             * int divisor = par ? 9 : 2;
             * n = (long) Math.ceil(n * 1.0 / divisor);
             * 
             * par = !par;
             * }
             * 
             * String ganador = par ? "Ollie" : "Stan";
             * 
             * SE PUEDE RESUMIR EN:
             */
            while (n > 18)
                n = (n - 1) / 18 + 1;

            String ganador = n <= 9 ? "Stan" : "Ollie";
            output.append(ganador).append(" wins.\n");

            /*
             * Esto es porque, si dividimos 'n - 1' en vez de 'n':
             * - Si 'n' era múltiplo del número, el resultado nos dará 1 unidad menos, la
             * cual compensaremos con el '+ 1'.
             * - Si 'n' no era múltiplo pero 'n - 1' sí, entonces el Math.ceil() deberá
             * devolver el resultado de dividir 'n - 1' y sumar 1.
             * - Y si ninguno de los dos era múltiplo del número, entonces el resultado de
             * la división truncará y el '+ 1' hará la función del Math.ceil().
             * - Nunca podrá pasar que 'n - 1' dé un valor menor que 'k' y 'n' un valor
             * mayor, pues restando de 1 en 1 tendremos que pasar por todos los múltiplos sí
             * o sí.
             * 
             * Es verdad que hacer el ceil() tras dividir entre 2 y entre 9 da distinto
             * resultado que tras dividir entre 18, pero al convertir el cociente a entero
             * no hay diferencia.
             * 
             * Ahora bien, como siempre dividimos en ciclos de <9, 2> (es decir, cada 18 por
             * el que dividimos implica dividir primero entre 9 y luego entre 2), sabemos
             * que al momento de salir del bucle, nos toca dividir entre 9. Partiendo de
             * esto, si n <= 9, al dividir entre 9 y aplicar el Math.ceil(), 'n' se hará 1,
             * por lo que Stan ganará. En cambio, si 'n' está en el intervalo (9, 18], al
             * dividir entre 9 siempre obtenedremos el valor 2, y por tanto al dividir luego
             * entre 2 el resultado será 1, por lo que Ollie ganará.
             */
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
}