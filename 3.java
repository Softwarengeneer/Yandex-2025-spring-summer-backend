import java.util.*;

public class Main {
    // Длина круга и максимальная скорость
    static int L, S;

    // Храним для каждого taxi_id: последнее известное время и позицию
    // (если приходят несколько TAXI для одного id, берём тот, у которого timestamp больше или равен)
    static Map<Integer, Taxi> taxis = new HashMap<>();

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int N = in.nextInt(); // Кол-во событий
        L = in.nextInt();     // Длина круга
        S = in.nextInt();     // Макс. скорость
        in.nextLine();        // Переходим на следующую строку

        // Сколько ORDER-команд мы увидим — неизвестно заранее,
        // но выводим результат сразу, как только дойдём до ORDER.

        for (int i = 0; i < N; i++) {
            String line = in.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("TAXI")) {
                // Формат: TAXI timestamp taxi_id position
                int timestamp = Integer.parseInt(parts[1]);
                int taxiId    = Integer.parseInt(parts[2]);
                int position  = Integer.parseInt(parts[3]);

                // Если вдруг приходят события не по порядку времени,
                // сохраняем только самое свежее (или равное) время для данного такси.
                Taxi old = taxis.get(taxiId);
                if (old == null || timestamp >= old.timestamp) {
                    taxis.put(taxiId, new Taxi(timestamp, position));
                }

            } else if (parts[0].equals("ORDER")) {
                // Формат: ORDER timestamp order_id position order_time
                int timestamp     = Integer.parseInt(parts[1]);
                int orderId       = Integer.parseInt(parts[2]); // (не используется в расчётах, только идентификатор)
                int orderPosition = Integer.parseInt(parts[3]);
                int orderTime     = Integer.parseInt(parts[4]);

                // Собираем таксистов, которые "гарантированно" успевают
                List<Integer> available = new ArrayList<>();

                for (Map.Entry<Integer, Taxi> e : taxis.entrySet()) {
                    int taxiId = e.getKey();
                    Taxi info  = e.getValue();

                    // Сколько времени прошло с последнего известного положения
                    int dt = timestamp - info.timestamp;
                    if (dt < 0) {
                        // Теоретически может прийти ORDER "раньше", чем TAXI, 
                        // но задача обычно предполагает неубывающие времена.
                        // На всякий случай защитимся: если dt < 0, считаем, что таксист
                        // не мог ещё сдвинуться от info.position (dt=0).
                        dt = 0;
                    }

                    // d = макс. расстояние, которое таксист мог проехать по часовой стрелке
                    long d = 1L * dt * S;  // умножение в long, чтобы избежать переполнения int

                    // arcStart = p, arcEnd = (p + d) mod L
                    int p = info.position;
                    int arcStart = p;
                    int arcEnd   = (int)((p + d) % L); // опять в int, т.к. L <= 10^9 может быть?
                    // (по условию N<=5000, но L может быть и большим; всё равно mod L в int влезет, если L <= 2e9 примерно)

                    // Находим макс. расстояние по часовой стрелке до точки заказа
                    // из концов дуги [arcStart..arcEnd].
                    // distCW(a, b) = "сколько ехать от a до b по часовой"
                    long dist1 = distCW(arcStart, orderPosition);
                    long dist2 = distCW(arcEnd,   orderPosition);
                    long distArcMax = Math.max(dist1, dist2);

                    // Если это расстояние <= S*orderTime, значит таксист гарантированно успевает
                    long maxPossible = 1L * S * orderTime;
                    if (distArcMax <= maxPossible) {
                        available.add(taxiId);
                    }
                }

                if (available.isEmpty()) {
                    // Нет таксистов
                    System.out.println(-1);
                } else {
                    // Сортируем по возрастанию
                    Collections.sort(available);
                    // Берём не более 5
                    int limit = Math.min(5, available.size());
                    StringBuilder sb = new StringBuilder();
                    for (int k = 0; k < limit; k++) {
                        sb.append(available.get(k));
                        if (k + 1 < limit) {
                            sb.append(" ");
                        }
                    }
                    System.out.println(sb.toString());
                }
            }
        }
    }

    // Расстояние по часовой стрелке от a до b
    // (a,b лежат в [0..L-1])
    static long distCW(int a, int b) {
        if (b >= a) {
            return b - a;
        } else {
            return (long)L - (a - b);
        }
    }

    // Информация о таксисте
    static class Taxi {
        int timestamp;
        int position;

        Taxi(int timestamp, int position) {
            this.timestamp = timestamp;
            this.position  = position;
        }
    }
}
