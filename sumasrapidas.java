import java.util.*;
import java.time.*;


class Jugador {
    String nombre;
    int puntaje;

    public Jugador(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }
}

// Nodo de la lista doblemente enlazada
class Nodo {
    Jugador jugador;
    Nodo anterior, siguiente;

    public Nodo(Jugador jugador) {
        this.jugador = jugador;
    }
}

// Lista doblemente enlazada para manejar el ranking
class ListaDoblementeEnlazada {
    private Nodo cabeza;


    public void agregarOrdenado(Jugador jugador) {
        Nodo nuevo = new Nodo(jugador);

        if (cabeza == null) {
            cabeza = nuevo;
            return;
        }

        Nodo actual = cabeza;
        Nodo anterior = null;

        while (actual != null && actual.jugador.puntaje >= jugador.puntaje) {
            anterior = actual;
            actual = actual.siguiente;
        }

        if (anterior == null) { // insertar al inicio
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        } else {
            nuevo.siguiente = actual;
            nuevo.anterior = anterior;
            anterior.siguiente = nuevo;
            if (actual != null) actual.anterior = nuevo;
        }

        // Mantener solo los 5 mejores
        int contador = 1;
        actual = cabeza;
        while (actual != null && contador < 5) {
            actual = actual.siguiente;
            contador++;
        }
        if (actual != null && actual.siguiente != null)
            actual.siguiente = null;
    }

    // Muestra el ranking actual
    public void mostrarRanking() {
        System.out.println("\n RANKING DE LOS 5 MEJORES JUGADORES ");
        Nodo actual = cabeza;
        int pos = 1;
        while (actual != null) {
            System.out.println(pos + ". " + actual.jugador.nombre + " - " + actual.jugador.puntaje + " pts");
            actual = actual.siguiente;
            pos++;
        }
        System.out.println();
    }
}

public class SumasRapidas {
    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();
    static ListaDoblementeEnlazada ranking = new ListaDoblementeEnlazada();

    public static void main(String[] args) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("🎮 BIENVENIDO AL JUEGO DE SUMAS RÁPIDAS 🎮");
            System.out.print("Ingresa tu nombre de usuario: ");
            String nombre = sc.nextLine();

            int puntaje = jugar(nombre);

            ranking.agregarOrdenado(new Jugador(nombre, puntaje));
            ranking.mostrarRanking();

            System.out.print("¿Deseas jugar otra vez? (s/n): ");
            String opcion = sc.nextLine().toLowerCase();
            if (!opcion.equals("s")) continuar = false;
        }

        System.out.println("\nGracias por jugar ");
    }

    public static int jugar(String nombre) {
        int nivel = 1;
        int tiempoLimite = 10; // segundos por suma
        int puntaje = 0;
        boolean jugando = true;

        while (jugando) {
            System.out.println("\n Nivel " + nivel + " (Tiempo: " + tiempoLimite + "s por suma)");

            for (int i = 1; i <= 5; i++) {
                int a = rand.nextInt(50) + 1;
                int b = rand.nextInt(50) + 1;
                int resultado = a + b;

                System.out.print("Suma #" + i + ": ¿Cuánto es " + a + " + " + b + "? ");

                Instant inicio = Instant.now();
                String respuestaUsuario;

                try {
                    respuestaUsuario = sc.nextLine();
                } catch (Exception e) {
                    System.out.println(" Entrada inválida. Fin del juego.");
                    return puntaje;
                }

                Instant fin = Instant.now();
                long duracion = Duration.between(inicio, fin).toSeconds();

                // Validar tiempo
                if (duracion > tiempoLimite) {
                    System.out.println("Tiempo agotado. Fin del juego.");
                    jugando = false;
                    break;
                }

                // Validar número
                int respuesta;
                try {
                    respuesta = Integer.parseInt(respuestaUsuario);
                } catch (NumberFormatException e) {
                    System.out.println(" Valor no válido. Fin del juego.");
                    jugando = false;
                    break;
                }

                // Validar resultado
                if (respuesta == resultado) {
                    puntaje += 100;
                    System.out.println(" Correcto! +100 puntos (Tiempo usado: " + duracion + "s)");
                } else {
                    System.out.println(" Incorrecto. El resultado era " + resultado);
                    jugando = false;
                    break;
                }
            }

            if (jugando) {
                nivel++;
                tiempoLimite = Math.max(2, tiempoLimite - 2);
                System.out.println("⭐ Nivel superado! Puntuación actual: " + puntaje);
            }
        }

        System.out.println("\n " + nombre + ", tu puntaje final es: " + puntaje + " puntos.\n");
        return puntaje;
    }
}
