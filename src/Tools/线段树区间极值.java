package Tools;


class SegmentTreeMinMax { // 线段树，单点更新，区间求极值

    private final int n;
    public int[] maxTree, minTree;

    public SegmentTreeMinMax(int[] nums) {
        n = nums.length;
        maxTree = new int[1 << (33 - Integer.numberOfLeadingZeros(n))];
        minTree = new int[1 << (33 - Integer.numberOfLeadingZeros(n))];
        build(nums, 0, n - 1, 1);
    }

    private void build(int[] nums, int left, int right, int i) {
        if (left == right) {
            maxTree[i] = minTree[i] = nums[left];
            return;
        }
        int mid = left + (right - left) / 2;
        build(nums, left, mid, 2 * i);
        build(nums, mid + 1, right, 2 * i + 1);
        maxTree[i] = Math.max(maxTree[2 * i], maxTree[2 * i + 1]);
        minTree[i] = Math.min(minTree[2 * i], minTree[2 * i + 1]);
    }

    public void update(int index, int val) { // 单点更新
        update(index, val, 0, n - 1, 1);
    }

    private void update(int index, int val, int left, int right, int i) {
        if (left == right) {
            maxTree[i] = minTree[i] = val;
            return;
        }
        int mid = left + (right - left) / 2;
        if (index <= mid) {
            update(index, val, left, mid, 2 * i);
        } else {
            update(index, val, mid + 1, right, 2 * i + 1);
        }
        maxTree[i] = Math.max(maxTree[2 * i], maxTree[2 * i + 1]);
        minTree[i] = Math.min(minTree[2 * i], minTree[2 * i + 1]);
    }

    public int queryMax(int start, int end) { // 求区间极值
        if (start > end) return 0;
        return queryMax(start, end, 0, n - 1, 1);
    }

    private int queryMax(int start, int end, int left, int right, int i) {
        if (start <= left && right <= end) {
            return maxTree[i];
        }
        int mid = left + (right - left) / 2;
        int res = Integer.MIN_VALUE;
        if (start <= mid) {
            res = Math.max(res, queryMax(start, end, left, mid, 2 * i));
        }
        if (end >= mid + 1) {
            res = Math.max(res, queryMax(start, end, mid + 1, right, 2 * i + 1));
        }
        return res;
    }

    public int queryMin(int start, int end) { // 求区间极值
        if (start > end) return 0;
        return queryMin(start, end, 0, n - 1, 1);
    }

    private int queryMin(int start, int end, int left, int right, int i) {
        if (start <= left && right <= end) {
            return minTree[i];
        }
        int mid = left + (right - left) / 2;
        int res = Integer.MAX_VALUE;
        if (start <= mid) {
            res = Math.min(res, queryMin(start, end, left, mid, 2 * i));
        }
        if (end >= mid + 1) {
            res = Math.min(res, queryMin(start, end, mid + 1, right, 2 * i + 1));
        }
        return res;
    }
}