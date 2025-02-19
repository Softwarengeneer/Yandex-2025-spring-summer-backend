import java.io.*;
import java.util.*;

public class Main {
    static int n, m, d;
    static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        // Быстрое чтение входных данных
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = br.readLine().split("\\s+");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        d = Integer.parseInt(parts[2]);

        int size = n * m;
        int[] dist = new int[size];
        Arrays.fill(dist, INF);

        // Будем использовать собственную очередь на базе массива
        int[] queue = new int[size];
        int front = 0, rear = 0;

        // Чтение строк и инициализация стартовых точек для BFS
        for (int i = 0; i < n; i++) {
            String line = br.readLine().trim().replaceAll("\\s+", "").toLowerCase();
            int rowBase = i * m;
            for (int j = 0; j < m; j++) {
                if (line.charAt(j) == 'x') {
                    int pos = rowBase + j;
                    dist[pos] = 0;
                    queue[rear++] = pos;
                }
            }
        }

        // Если стартовых точек нет, выводим min(n, m)
        if (front == rear) {
            System.out.println(Math.min(n, m));
            return;
        }

        // Собственный BFS без использования объекта Queue
        while (front < rear) {
            int pos = queue[front++];
            int i = pos / m;
            int j = pos % m;
            int nd = dist[pos] + 1;

            // Проверка сверху
            if (i > 0) {
                int nPos = pos - m;
                if (dist[nPos] == INF) {
                    dist[nPos] = nd;
                    queue[rear++] = nPos;
                }
            }
            // Проверка снизу
            if (i < n - 1) {
                int nPos = pos + m;
                if (dist[nPos] == INF) {
                    dist[nPos] = nd;
                    queue[rear++] = nPos;
                }
            }
            // Проверка слева
            if (j > 0) {
                int nPos = pos - 1;
                if (dist[nPos] == INF) {
                    dist[nPos] = nd;
                    queue[rear++] = nPos;
                }
            }
            // Проверка справа
            if (j < m - 1) {
                int nPos = pos + 1;
                if (dist[nPos] == INF) {
                    dist[nPos] = nd;
                    queue[rear++] = nPos;
                }
            }
        }

        // Оптимизированное динамическое программирование для поиска максимального квадрата.
        int maxSize = 0;
        int maxPossible = Math.min(n, m);
        int[] dp = new int[m]; // одномерный массив для текущей строки
        for (int i = 0; i < n; i++) {
            int prev = 0; // диагональное значение (dp[j-1] из предыдущей строки)
            int rowBase = i * m;
            for (int j = 0; j < m; j++) {
                int temp = dp[j]; // сохраним старое значение (значение сверху)
                if (dist[rowBase + j] >= d) {
                    if (i == 0 || j == 0) {
                        dp[j] = 1;
                    } else {
                        dp[j] = Math.min(Math.min(dp[j], dp[j - 1]), prev) + 1;
                    }
                    if (dp[j] > maxSize) {
                        maxSize = dp[j];
                        if (maxSize == maxPossible) {
                            System.out.println(maxSize);
                            return;
                        }
                    }
                } else {
                    dp[j] = 0;
                }
                prev = temp;
            }
        }

        System.out.println(maxSize);
    }
}
