package com.ocicatteam.afpj.algo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SiriusWangs on 2017/4/20.
 */
public class Matcher {

    private static final int magicNum = 16384;
    private int hitLimit;  //小于等于0，不限制

    public Matcher (int hitLimit) {
        this.hitLimit = hitLimit;
    }

    public int[] match(FpMap foundFp, FpMap testFp) {

        HashMap<Integer, Counter> counters = new HashMap<>();
        int i, j, key, dTime, count;
                            // id, dTime, hit
        int[] result = new int[]{-1, -1, -1};
        boolean found = false;

        TimeIdPair[] findTip;
        TimeIdPair[] testTip;

        // TODO: 待优化
        for (Map.Entry<Integer, TimeIdStack> entry : foundFp.map.entrySet()) {
            findTip = entry.getValue().get();
            testTip = testFp.get(entry.getKey()); if (testTip == null) continue;
            for (i = 0; i < testTip.length; i++) {
                for (j = 0; j < findTip.length; j++) {
                    dTime = findTip[j].time - testTip[i].time;
                    key = idAndDTimeToKey(findTip[j].id, dTime);
                    //感谢上帝有这么方便的方法
                    count = counters.computeIfAbsent(key, ns -> new Counter()).add();
                    if (count > result[2]) {
                        result[0] = findTip[j].id;
                        result[1] = dTime;
                        result[2] = count;
                        if (hitLimit > 0 && count >= hitLimit) {
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
            }
            if (found) break;
        }
        return result;
    }

    //TODO: 寻找更可靠的算法
    //dTime 不能大于16384
    private static int idAndDTimeToKey(int id, int dTime) {
        //dTime取绝对值
        if (dTime < 0) {
            //时间差如果为负数，把key标记为-负数，
            return -(id * magicNum + -dTime);
        } else {
            return id * magicNum + dTime;
        }
    }


}
