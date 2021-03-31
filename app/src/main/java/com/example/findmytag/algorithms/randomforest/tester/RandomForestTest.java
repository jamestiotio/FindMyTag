package com.example.findmytag.algorithms.randomforest.tester;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.findmytag.algorithms.randomforest.Serialize;
import com.example.findmytag.algorithms.randomforest.WifiValues;

import org.junit.After;
import org.testng.annotations.AfterClass;
import org.junit.Before;
import org.testng.annotations.BeforeClass;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import smile.base.cart.SplitRule;
import smile.classification.RandomForest;
import smile.math.MathEx;
import smile.validation.*;

import static org.junit.Assert.*;

public class RandomForestTest {
    long[] seeds = {
            342317953, 521642753, 72070657, 577451521, 266953217, 179976193,
            374603777, 527788033, 303395329, 185759582, 261518209, 461300737,
            483646580, 532528741, 159827201, 284796929, 655932697, 26390017,
            454330473, 867526205, 824623361, 719082324, 334008833, 699933293,
            823964929, 155216641, 150210071, 249486337, 713508520, 558398977,
            886227770, 74062428, 670528514, 701250241, 363339915, 319216345,
            757017601, 459643789, 170213767, 434634241, 414707201, 153100613,
            753882113, 546490145, 412517763, 888761089, 628632833, 565587585,
            175885057, 594903553, 78450978, 212995578, 710952449, 835852289,
            415422977, 832538705, 624345857, 839826433, 260963602, 386066438,
            530942946, 261866663, 269735895, 798436064, 379576194, 251582977,
            349161809, 179653121, 218870401, 415292417, 86861523, 570214657,
            701581299, 805955890, 358025785, 231452966, 584239408, 297276298,
            371814913, 159451160, 284126095, 896291329, 496278529, 556314113,
            31607297, 726761729, 217004033, 390410146, 70173193, 661580775,
            633589889, 389049037, 112099159, 54041089, 80388281, 492196097,
            912179201, 699398161, 482080769, 363844609, 286008078, 398098433,
            339855361, 189583553, 697670495, 709568513, 98494337, 99107427,
            433350529, 266601473, 888120086, 243906049, 414781441, 154685953,
            601194298, 292273153, 212413697, 568007473, 666386113, 712261633,
            802026964, 783034790, 188095005, 742646355, 550352897, 209421313,
            175672961, 242531185, 157584001, 201363231, 760741889, 852924929,
            60158977, 774572033, 311159809, 407214966, 804474160, 304456514,
            54251009, 504009638, 902115329, 870383757, 487243777, 635554282,
            564918017, 636074753, 870308031, 817515521, 494471884, 562424321,
            81710593, 476321537, 595107841, 418699893, 315560449, 773617153,
            163266399, 274201241, 290857537, 879955457, 801949697, 669025793,
            753107969, 424060977, 661877468, 433391617, 222716929, 334154852,
            878528257, 253742849, 480885528, 99773953, 913761493, 700407809,
            483418083, 487870398, 58433153, 608046337, 475342337, 506376199,
            378726401, 306604033, 724646374, 895195218, 523634541, 766543466,
            190068097, 718704641, 254519245, 393943681, 796689751, 379497473,
            50014340, 489234689, 129556481, 178766593, 142540536, 213594113,
            870440184, 277912577};

    public RandomForestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testWeather() throws Exception {
        System.out.println("Weather");

        MathEx.setSeed(19650218); // to get repeatable results for cross validation.
        RandomForest model = RandomForest.fit(WeatherNominal.formula, WeatherNominal.data, 20, 2, SplitRule.GINI, 8, 10, 1, 1.0, null, Arrays.stream(seeds));

        double[] importance = model.importance();
        for (int i = 0; i < importance.length; i++) {
            System.out.format("%-15s %.4f%n", model.schema().name(), importance[i]);
        }

        ClassificationMetrics metrics = LOOCV.classification(WeatherNominal.formula, WeatherNominal.data,
                (f, x) -> RandomForest.fit(f, x, 20, 2, SplitRule.GINI, 8, 10, 1, 1.0, null, Arrays.stream(seeds)));

        System.out.println(metrics);
        assertEquals(0.5714, metrics.accuracy, 1E-4);

        Path temp = Serialize.write(model);
        Serialize.read(temp);
    }


}