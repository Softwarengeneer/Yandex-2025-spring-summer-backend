import java.util.*;

public class Main {
    // Массив для минимального T9-представления для каждой буквы A-Z.
    // Например, 'A' -> "2", 'B' -> "22", 'C' -> "222",
    // 'D' -> "3", 'E' -> "33", 'F' -> "333", и т.д.
    private static final String[] MIN_MAPPING = new String[26];
    static {
        // Определяем соответствия согласно стандартной телефонной раскладке:
        // 2: ABC, 3: DEF, 4: GHI, 5: JKL, 6: MNO, 7: PQRS, 8: TUV, 9: WXYZ.
        String[] groups = {
                "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"
        };
        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];
            char digit = (char) ('2' + i);
            for (int j = 0; j < group.length(); j++) {
                // j+1 нажатие даёт соответствующую букву
                MIN_MAPPING[group.charAt(j) - 'A'] = String.valueOf(digit).repeat(j + 1);
            }
        }
    }

    // Функция, которая вычисляет T9-представление для слова
    private static String wordToT9(String word) {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            // Предполагаем, что слово состоит из заглавных латинских букв A-Z
            sb.append(MIN_MAPPING[c - 'A']);
        }
        return sb.toString();
    }

    // Узел Trie для T9-представлений
    static class TrieNode {
        // Так как ключи — цифры от '2' до '9', можно использовать массив длины 10
        TrieNode[] children = new TrieNode[10];
        // Если узел завершает слово, то слово хранится здесь (если несколько — можно сохранить любое)
        String word = null;
    }

    // Корень trie
    private static TrieNode root = new TrieNode();

    // Вставка слова (по его T9-представлению) в trie
    private static void insertT9(String t9, String word) {
        TrieNode cur = root;
        for (char ch : t9.toCharArray()) {
            int idx = ch - '0';
            if (cur.children[idx] == null) {
                cur.children[idx] = new TrieNode();
            }
            cur = cur.children[idx];
        }
        // Если в узле уже есть слово, оставляем его или заменяем — нам достаточно одного варианта
        cur.word = word;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Читаем входную цифровую строку
        String input = sc.nextLine().trim();
        int n = input.length();

        // Читаем количество слов в словаре
        int dictSize = Integer.parseInt(sc.nextLine().trim());
        // Строим trie для T9-представлений
        for (int i = 0; i < dictSize; i++) {
            String word = sc.nextLine().trim();
            String t9 = wordToT9(word);
            insertT9(t9, word);
        }

        // dp[i] будет хранить индекс предыдущего разбиения, а также слово, которое завершилось в позиции i.
        // Если dp[i] != null, значит, есть корректное разбиение input[0...i-1].
        class State {
            int prev;
            String word;
            State(int prev, String word) {
                this.prev = prev;
                this.word = word;
            }
        }
        State[] dp = new State[n + 1];
        dp[0] = new State(-1, ""); // начало строки

        // Итеративное DP: для каждой позиции i, если разбивка существует, пытаемся найти слово, начинающееся с i.
        for (int i = 0; i < n; i++) {
            if (dp[i] == null) continue;
            TrieNode cur = root;
            // Проходим по подстроке input, начиная с i, одновременно идём по trie.
            for (int j = i; j < n; j++) {
                int digit = input.charAt(j) - '0';
                // Если нет ребенка по этому символу, то дальше по этой ветке двигаться не имеет смысла.
                if (digit < 0 || digit > 9 || cur.children[digit] == null) break;
                cur = cur.children[digit];
                // Если достигли конца T9-представления какого-либо слова, то обновляем dp.
                if (cur.word != null) {
                    if (dp[j + 1] == null) {
                        dp[j + 1] = new State(i, cur.word);
                    }
                }
            }
        }

        if (dp[n] == null) {
            System.out.println("No solution");
            return;
        }

        // Восстанавливаем ответ по dp
        LinkedList<String> ansWords = new LinkedList<>();
        int curPos = n;
        while (curPos > 0) {
            State st = dp[curPos];
            ansWords.addFirst(st.word);
            curPos = st.prev;
        }
        System.out.println(String.join(" ", ansWords));
    }
}
