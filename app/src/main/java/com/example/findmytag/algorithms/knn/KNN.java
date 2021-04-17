import com.example.findmytag.wifi.WiFiDataManager;

import java.util.ArrayList;
import java.util.Arrays;

public class KNN {

    public static float localizationAlogrithm(float rss[], float radioMap[][], int K,
                                              int n_AP, float D_Threshold, float x0, float y0) {



        int M = radioMap.length;
        int N = rss.length;
        // Log.i("定位算法的N和M", "M = " + String.valueOf(M) + "N= " + N);
        // 选出前8个最大的rss及其index
        int index[] = new int[N];
        for (int i = 0; i < N; i++) {
            index[i] = i;
        }
        for (int i = 0; i < n_AP; i++) {
            for (int j = 0; j < N - 1 - i; j++) {
                if (rss[j] > rss[j + 1]) {
                    float tmp = rss[j];
                    rss[j] = rss[j + 1];
                    rss[j + 1] = (int) tmp;
                    int temp = index[j];
                    index[j] = index[j + 1];
                    index[j + 1] = temp;
                }
            }
        }// 这时rss数组的右端为最大的rss值，数组index的右端为相应的index

        // 设定搜索范围
        int searchRange[] = new int[M];
        int L_search;

        if (x0 == 0 && y0 == 0) {// 初始定位，没有先验信息，全局搜索
            L_search = M;
            for (int i = 0; i < M; i++) {
                searchRange[i] = i;
            }
        } else {
            // 缩减搜索范围
            L_search = 0;
            for (int i = 0; i < M; i++) {
                if (Math.abs(radioMap[i][N] - x0)
                        + Math.abs(radioMap[i][N + 1] - y0) <= D_Threshold) {
                    searchRange[L_search] = i;
                    L_search++;
                }
            }
            if (L_search < K) {
                L_search = M;
                for (int i = 0; i < M; i++) {
                    searchRange[i] = i;
                }
            }
        }

        //   Log.i("搜索范围L_search", "L_search = " + L_search);

        // 计算曼哈顿距离
        float mDistance[] = new float[L_search];
        for (int i = 0; i < L_search; i++) {
            mDistance[i] = 0;// 初始化为0
            for (int j = 0; j < n_AP; j++) {
                mDistance[i] = mDistance[i]
                        + Math.abs(rss[N - 1 - j]
                        - radioMap[searchRange[i]][index[N - 1 - j]]);
            }
            mDistance[i] = mDistance[i] / n_AP;
        }
        // 选出K个距离最小的指纹，对searchRange和mDistance一起排序
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < L_search - 1 - i; j++) {
                if (mDistance[j] < mDistance[j + 1]) {
                    float tmp = mDistance[j];
                    mDistance[j] = mDistance[j + 1];
                    mDistance[j + 1] = tmp;
                    int temp = searchRange[j];
                    searchRange[j] = searchRange[j + 1];
                    searchRange[j + 1] = temp;
                }
            }
        }// 现在数组mDistance的右端是最小的K个距离，searchRange数组的右端是相应的指纹在radiomap中的index

        // 计算位置
        float x = 0;
        float y = 0;

        for (int i = 0; i < K; i++) {
            x += radioMap[searchRange[L_search - 1 - i]][N]; // radiomap的长度一定是N+2的，多一个x和一个y
            y += radioMap[searchRange[L_search - 1 - i]][N + 1];
        }

        x = x / K;
        y = y / K;

        x = 0.7f * x + 0.3f * x0;
        y = 0.7f * y + 0.3f * y0;
        return (x+y);

    }
}