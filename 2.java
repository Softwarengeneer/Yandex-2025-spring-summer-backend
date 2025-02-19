import java.util.*;

public class ScalarProduct {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем размер векторов
        int N = scanner.nextInt();

        // Читаем вектор Q
        int[] Q = new int[N];
        for (int i = 0; i < N; i++) {
            Q[i] = scanner.nextInt();
        }

        // Читаем сжатый вектор C
        int[] C = new int[N];
        for (int i = 0; i < N; i++) {
            C[i] = scanner.nextInt();
        }

        // Читаем A и B
        int A = scanner.nextInt();
        int B = scanner.nextInt();

        // Вычисляем исходный вектор D на основе C, A и B
        double scale = (B - A) / 255.0;

        long dotProduct = 0;
        for (int i = 0; i < N; i++) {
            int Di = (int) Math.round(A + C[i] * scale);
            dotProduct += (long) Q[i] * Di;
        }

        // Вывод результата
        System.out.println(dotProduct);
        scanner.close();
    }
}
