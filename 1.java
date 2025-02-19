import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // количество этажей
        int m = sc.nextInt(); // квартир на этаже
        int x = sc.nextInt(); // окон в высоту в каждой квартире
        int y = sc.nextInt(); // окон в ширину в каждой квартире
        sc.nextLine(); // переход на следующую строку после считывания чисел

        int totalRows = n * x;     // всего строк окон
        int totalCols = m * y;     // всего столбцов окон

        // Читаем состояние окон здания
        String[] building = new String[totalRows];
        for (int i = 0; i < totalRows; i++) {
            building[i] = sc.nextLine();
        }

        int awakeApartments = 0;
        int threshold = (x * y + 1) / 2; // минимум включённых окон для "бодрствующей" квартиры

        // Перебираем каждую квартиру
        for (int floor = 0; floor < n; floor++) {
            for (int apt = 0; apt < m; apt++) {
                int litCount = 0;
                // Обходим окна внутри квартиры
                for (int i = 0; i < x; i++) {
                    int row = floor * x + i;
                    for (int j = 0; j < y; j++) {
                        int col = apt * y + j;
                        if (building[row].charAt(col) == 'X') {
                            litCount++;
                        }
                    }
                }
                if (litCount >= threshold) {
                    awakeApartments++;
                }
            }
        }

        System.out.println(awakeApartments);
    }
}
