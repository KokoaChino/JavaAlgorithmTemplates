package Tools;

import java.util.*;



class Difference { // 密集差分

    public static void oneDifference(int[] nums, int[][] operations) { // 一维差分
        int n = nums.length;
        int[] dif = new int[n + 1];
        for (int[] a: operations) {
            int i = a[0], j = a[1], d = a[2];
            dif[i] += d;
            dif[j + 1] -= d;
        }
        for (int i = 0; i < n; i++) {
            dif[i + 1] += dif[i];
            nums[i] += dif[i];
        }
    }

    public static void twoDifference(int[][] mat, int[][] operations) { // 二维差分
        int m = mat.length, n = mat[0].length;
        int[][] dif = new int[m + 2][n + 2];
        for (int[] a: operations) {
            int x1 = a[0], y1 = a[1], x2 = a[2], y2 = a[3], d = a[4];
            dif[x2 + 2][y2 + 2] += d;
            dif[x1 + 1][y2 + 2] -= d;
            dif[x2 + 2][y1 + 1] -= d;
            dif[x1 + 1][y1 + 1] += d;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dif[i][j] += dif[i - 1][j] + dif[i][j - 1] - dif[i - 1][j - 1];
                mat[i - 1][j - 1] += dif[i][j];
            }
        }
    }
}



class OneSparseDifference { // 一维稀疏差分

    public final TreeMap<Integer, Long> prefixMap = new TreeMap<>();

    public OneSparseDifference(int[][] operations) {
        TreeMap<Integer, Long> dif = new TreeMap<>();
        for (int[] op : operations) {
            int l = op[0], r = op[1], d = op[2];
            dif.put(l, dif.getOrDefault(l, 0L) + d);
            dif.put(r + 1, dif.getOrDefault(r + 1, 0L) - d);
        }
        long sum = 0;
        for (Map.Entry<Integer, Long> entry : dif.entrySet()) {
            sum += entry.getValue();
            prefixMap.put(entry.getKey(), sum);
        }
    }

    public long get(int pos) {
        Map.Entry<Integer, Long> floor = prefixMap.floorEntry(pos);
        if (floor == null) {
            return 0;
        }
        return floor.getValue();
    }
}



class TwoSparseDifference { // 二维稀疏差分

    private final long[][] g;
    private final int[] xs, ys;

    public TwoSparseDifference(int[][] operations) {
        Set<Integer> xSet = new HashSet<>();
        Set<Integer> ySet = new HashSet<>();
        for (int[] op : operations) {
            int x1 = op[0], y1 = op[1], x2 = op[2], y2 = op[3];
            xSet.add(x1);
            xSet.add(x2 + 1);
            ySet.add(y1);
            ySet.add(y2 + 1);
        }
        xs = xSet.stream().sorted().mapToInt(i -> i).toArray();
        ys = ySet.stream().sorted().mapToInt(i -> i).toArray();
        Map<Integer, Integer> xToIdx = new HashMap<>();
        Map<Integer, Integer> yToIdx = new HashMap<>();
        for (int i = 0; i < xs.length; i++) xToIdx.put(xs[i], i);
        for (int i = 0; i < ys.length; i++) yToIdx.put(ys[i], i);
        int n = xs.length, m = ys.length;
        long[][] dif = new long[n + 2][m + 2];
        for (int[] op : operations) {
            int x1 = op[0], y1 = op[1], x2 = op[2], y2 = op[3], d = op[4];
            int i1 = xToIdx.get(x1) + 1, j1 = yToIdx.get(y1) + 1;
            int i2 = xToIdx.get(x2 + 1) + 1, j2 = yToIdx.get(y2 + 1) + 1;
            dif[i1][j1] += d;
            dif[i2][j1] -= d;
            dif[i1][j2] -= d;
            dif[i2][j2] += d;
        }
        this.g = new long[n][m];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dif[i][j] += dif[i - 1][j] + dif[i][j - 1] - dif[i - 1][j - 1];
                g[i - 1][j - 1] = dif[i][j];
            }
        }
    }

    public long get(int x, int y) {
        int i = upperBound(xs, x) - 1;
        if (i < 0) return 0;
        int j = upperBound(ys, y) - 1;
        if (j < 0) return 0;
        return g[i][j];
    }

    private static int upperBound(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + right >>> 1;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}