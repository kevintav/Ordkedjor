import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        //region file reading
        //---------------------------------------------------------------------------------------
        BufferedReader r =
                new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
        ArrayList<String> words = new ArrayList<String>();
        while (true) {
            String word = r.readLine();
            if (word == null) { break; }
            assert word.length() == 5; // assert that the words follow the expected size
            words.add(word);
        }

        int n = words.size();
        Map<String, Integer> id = new HashMap<>(); // HashMap used for storing word-ids
        for (int i = 0; i < n; i++) id.put(words.get(i), i); // Assigning unique ids for each word
        //---------------------------------------------------------------------------------------
        //endregion


        //region graph building with adjacency list
        //---------------------------------------------------------------------------------------
        ArrayList<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();

        Map<String, ArrayList<Integer>> buckets = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String w = words.get(i);

            char[] arr = w.substring(1, 5).toCharArray();
            Arrays.sort(arr);
            String key = new String(arr);

            if (!buckets.containsKey(key)) {
                buckets.put(key, new ArrayList<>());
            }
            buckets.get(key).add(i);
        }

        for (int y = 0; y < n; y++) {
            String word = words.get(y);

            // Generera alla fyrabokstavsdelmängder
            for (int k = 0; k < 5; k++) {
                char[] arr = new char[4];
                int p = 0;

                for (int j = 0; j < 5; j++) {
                    if (j != k) arr[p++] = word.charAt(j);
                }

                Arrays.sort(arr);
                String key = new String(arr);

                for (int x : buckets.getOrDefault(key, new ArrayList<>())) {
                    if (x != y) graph[x].add(y);
                }
            }
        }
        //---------------------------------------------------------------------------------------
        //endregion


        //region reading testfile and executing breadth-first search
        //---------------------------------------------------------------------------------------
        BufferedReader b =
                new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));

        while (true) {
            String line = b.readLine();
            if (line == null) break;

            assert line.length() == 11;
            String start = line.substring(0, 5);
            String stop = line.substring(6, 11);

            // Getting the word indexes for start and goal-words
            int st = id.get(start);
            int sp = id.get(stop);

            int res = bfs(st, sp, graph);
            System.out.println(res);
        }
        //---------------------------------------------------------------------------------------
        //endregion
    }

    static int bfs(int start, int goal, ArrayList<Integer>[] graph) {
        if (start == goal) return 0;

        int n = graph.length;
        boolean[] visited = new boolean[n];
        int[] dist = new int[n];

        Queue<Integer> queue = new ArrayDeque<>();
        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();

            for (int w : graph[v]) {
                if (!visited[w]) {
                    visited[w] = true;
                    dist[w]++;

                    if (w == goal) {
                        return dist[w];
                    }

                    queue.add(w);
                }
            }
        }
        return -1;
    }
}