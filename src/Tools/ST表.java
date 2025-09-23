package Tools;


class SparseTable { // ST 表，静态区间极值查询

    private final int[][] maxTable;
    private final int[][] minTable;
    private final int[] log;

    public SparseTable(int[] nums) {
        int n = nums.length;
        int logn = (int) (Math.log(n) / Math.log(2)) + 1;
        maxTable = new int[n][logn];
        minTable = new int[n][logn];
        log = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            log[i] = log[i / 2] + 1;
        }
        for (int i = 0; i < n; i++) {
            maxTable[i][0] = minTable[i][0] = nums[i];
        }
        for (int j = 1; j < logn; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                maxTable[i][j] = Math.max(maxTable[i][j - 1], maxTable[i + (1 << (j - 1))][j - 1]);
                minTable[i][j] = Math.min(minTable[i][j - 1], minTable[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    public int queryMin(int l, int r) {
        if (l > r) return 0;
        int k = log[r - l + 1];
        return Math.min(minTable[l][k], minTable[r - (1 << k) + 1][k]);
    }

    public int queryMax(int l, int r) {
        if (l > r) return 0;
        int k = log[r - l + 1];
        return Math.max(maxTable[l][k], maxTable[r - (1 << k) + 1][k]);
    }
}