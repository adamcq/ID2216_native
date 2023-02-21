package com.example.map_test1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.map_test1.view.YearFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.example.map_test1.databinding.ActivityMapsBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final static String mLogTag = "MapsActivity";
    private int currentYearIndex;
    private int currentDistrictIndex;
    private static int[] years;
    private static int currentYear;
    private static int[][] crimeCounts;
    private static String[] districts;
    private static String[] crimes; // [value]
    private static int[][][] data; // [year][district][value]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set data
        setYears();
        setDistricts();
        setCrimes();
        setData();
        setCurrentYear(2022);
        setCrimeCounts();
        Log.v("setValues", "all values set");

        // menu layout
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ImageView imgMenu = findViewById(R.id.imgMenu);
        NavigationView navView = findViewById(R.id.navDrawer);

        navView.setItemIconTintList(null);
        imgMenu.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        );

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(navView,navController);

        TextView textTitle = findViewById(R.id.title);
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {
                    Log.v("dstID","currentYear: " + getCurrentYear() + " mapsId: " + Integer.toString(R.id.maps) +  " dst ID: " + Integer.toString(destination.getId()) + " end");
                    if (destination.getId() == R.id.maps) {
                        destination.setLabel(Integer.toString(getCurrentYear()));
                    }
                    textTitle.setText(destination.getLabel());
                });
    }

    private void setYears() {
        years = new int[]{2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022};
    }

    private void setDistricts() {
        districts = new String[]{"Kungsholmen", "Östermalm", "Bromma", "Skärholmen", "Södermalm", "Hägersten-Älvsjö", "Norrmalm", "Farsta", "Rinkeby-Kista", "Skarpnäck", "Spånga - Tensta", "Hägersten-Älvsjö (2)", "Enskede - Årsta - Vantör", "Hässelby - Vällingby"};
    }
    private void setCrimes() {
        crimes = new String[]{"Attempted murder or manslaughter (1, 2§)", "Abuse (5, 6 §)", "Causing the death of others (7, 10 §)", "Causing bodily injury, unrelated with a traffic accident (8, 10 §)", "Rape (1 a §)", "Sexual assault (2 §)  (Fr. o. m. 2018-07)", "Theft, roberry, etc. (8 kap.)", "Crimes against the public (13-15 kap.)", "Generally dangerous crimes (13 kap.)", "Violation of the narcotics penal code", "Violation of the law regarding guns (9 kap. 1-2 §)", "Violation of the law for carrying knives (1, 2, 4 §)"};
    }
    private void setData() {
        data = new int[][][]{
                {
                        {9, 696, 4, 28, 0, 0, 4741, 166, 23, 723, 55, 226},
                        {6, 585, 2, 15, 0, 0, 4342, 158, 13, 642, 41, 210},
                        {10, 613, 2, 12, 0, 0, 4228, 184, 16, 780, 41, 213},
                        {10, 530, 3, 6, 0, 0, 3540, 147, 25, 652, 31, 164},
                        {7, 525, 5, 26, 2, 3, 3092, 107, 18, 630, 43, 152},
                        {5, 531, 1, 11, 1, 1, 3513, 160, 17, 850, 45, 150},
                        {4, 488, 2, 27, 1, 3, 3325, 153, 17, 1040, 59, 181},
                        {12, 470, 5, 25, 1, 4, 2765, 109, 20, 1155, 53, 161},
                },

                {
                        {1, 783, 1, 37, 0, 0, 7245, 167, 35, 654, 29, 65},
                        {1, 660, 0, 27, 0, 0, 7660, 151, 26, 604, 31, 62},
                        {3, 613, 2, 32, 0, 0, 6878, 123, 28, 493, 32, 57},
                        {6, 540, 4, 41, 0, 2, 5898, 117, 32, 623, 30, 62},
                        {4, 565, 1, 34, 1, 0, 5895, 101, 30, 699, 38, 55},
                        {7, 481, 2, 23, 4, 3, 5305, 96, 16, 538, 50, 54},
                        {1, 521, 2, 30, 3, 2, 4393, 97, 26, 671, 54, 61},
                        {4, 569, 2, 28, 2, 3, 4195, 96, 28, 995, 44, 66},
                },

                {
                        {5, 451, 1, 27, 0, 0, 4258, 107, 20, 532, 31, 49},
                        {9, 412, 0, 15, 0, 0, 3957, 75, 21, 377, 34, 50},
                        {4, 405, 4, 22, 0, 0, 4287, 115, 30, 336, 41, 42},
                        {1, 478, 0, 19, 0, 0, 3754, 106, 25, 333, 43, 56},
                        {3, 442, 1, 20, 2, 1, 3095, 115, 35, 356, 37, 46},
                        {2, 468, 0, 29, 0, 0, 3821, 116, 25, 411, 57, 80},
                        {6, 472, 0, 29, 1, 1, 3723, 103, 26, 486, 67, 70},
                        {2, 456, 0, 12, 1, 4, 3347, 100, 28, 565, 81, 80},
                },

                {
                        {9, 526, 0, 11, 0, 0, 2865, 114, 30, 472, 35, 47},
                        {7, 658, 0, 9, 0, 0, 2679, 100, 24, 391, 33, 61},
                        {2, 536, 0, 19, 0, 0, 2583, 94, 22, 611, 47, 50},
                        {5, 519, 1, 8, 0, 2, 2094, 77, 13, 749, 59, 68},
                        {12, 481, 0, 9, 0, 0, 1723, 79, 12, 717, 53, 52},
                        {15, 593, 0, 9, 0, 0, 1806, 106, 23, 584, 47, 56},
                        {13, 544, 0, 13, 0, 0, 1838, 66, 14, 651, 56, 47},
                        {13, 460, 0, 10, 2, 2, 1373, 73, 19, 597, 59, 52},
                },

                {
                        {15, 1808, 1, 49, 0, 0, 14301, 268, 43, 2192, 69, 436},
                        {10, 1897, 0, 41, 0, 0, 12804, 237, 48, 2181, 85, 397},
                        {21, 1643, 1, 49, 0, 0, 11543, 242, 58, 2719, 81, 484},
                        {17, 1687, 4, 57, 0, 1, 13413, 284, 50, 2746, 104, 467},
                        {12, 1655, 1, 47, 2, 1, 13688, 196, 33, 2501, 114, 453},
                        {17, 1669, 3, 49, 5, 7, 11519, 267, 49, 3004, 134, 492},
                        {21, 1636, 5, 46, 1, 4, 9625, 230, 56, 3279, 129, 616},
                        {11, 1694, 3, 38, 2, 6, 7595, 163, 52, 3060, 99, 446},
                },

                {
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {12, 936, 5, 26, 5, 3, 5730, 169, 35, 928, 89, 152},
                        {13, 781, 2, 23, 1, 2, 5033, 138, 42, 862, 83, 111},
                },

                {
                        {11, 2022, 2, 40, 0, 0, 17959, 458, 28, 3030, 79, 549},
                        {16, 1875, 3, 57, 0, 0, 16185, 334, 26, 2494, 77, 496},
                        {19, 1628, 8, 50, 0, 0, 15823, 314, 31, 2636, 67, 472},
                        {13, 1401, 4, 58, 0, 1, 12814, 245, 22, 2748, 68, 389},
                        {12, 1427, 2, 54, 2, 3, 11178, 252, 24, 1976, 63, 356},
                        {8, 1015, 5, 33, 4, 4, 9322, 334, 23, 2486, 91, 455},
                        {12, 1090, 0, 37, 8, 6, 8024, 326, 28, 2651, 81, 384},
                        {6, 1326, 2, 36, 9, 3, 8038, 250, 26, 2670, 78, 305},
                },

                {
                        {6, 638, 0, 23, 0, 0, 3698, 114, 32, 720, 61, 90},
                        {8, 608, 1, 16, 0, 0, 3224, 124, 49, 927, 52, 81},
                        {9, 577, 0, 11, 0, 0, 2710, 111, 30, 607, 64, 81},
                        {2, 654, 2, 18, 0, 1, 2719, 85, 24, 528, 54, 70},
                        {8, 622, 0, 22, 3, 1, 2658, 107, 34, 569, 39, 81},
                        {9, 633, 1, 12, 1, 2, 2863, 124, 32, 561, 62, 95},
                        {14, 595, 1, 20, 0, 2, 2701, 139, 50, 589, 60, 86},
                        {16, 579, 0, 13, 1, 1, 2606, 96, 31, 667, 86, 91},
                },

                {
                        {11, 818, 1, 20, 0, 0, 3585, 162, 27, 845, 71, 44},
                        {18, 831, 1, 21, 0, 0, 3175, 145, 37, 560, 80, 43},
                        {14, 839, 4, 11, 0, 0, 3002, 201, 34, 722, 89, 45},
                        {10, 702, 4, 18, 0, 2, 2424, 143, 31, 884, 78, 43},
                        {12, 754, 0, 17, 1, 2, 1882, 145, 48, 963, 91, 30},
                        {28, 726, 0, 12, 0, 2, 1891, 184, 55, 1161, 102, 81},
                        {7, 746, 0, 20, 0, 1, 2099, 152, 55, 1040, 107, 96},
                        {13, 673, 2, 13, 1, 0, 1518, 132, 40, 1174, 102, 55},
                },

                {
                        {6, 445, 0, 6, 0, 0, 2278, 78, 30, 414, 38, 55},
                        {6, 397, 2, 2, 0, 0, 2055, 51, 23, 300, 24, 40},
                        {12, 305, 0, 9, 0, 0, 1759, 49, 21, 200, 30, 26},
                        {7, 340, 0, 9, 0, 0, 1730, 49, 17, 165, 32, 21},
                        {5, 346, 0, 12, 1, 0, 1445, 36, 18, 272, 44, 29},
                        {9, 366, 0, 11, 1, 0, 1719, 43, 23, 414, 48, 46},
                        {7, 391, 0, 5, 3, 4, 1624, 64, 21, 361, 60, 37},
                        {5, 329, 0, 9, 1, 2, 1483, 61, 28, 459, 71, 33},
                },

                {
                        {8, 532, 0, 23, 0, 0, 2355, 92, 27, 535, 45, 35},
                        {8, 447, 2, 6, 0, 0, 2093, 64, 10, 497, 50, 29},
                        {5, 381, 3, 14, 0, 0, 1884, 77, 24, 531, 55, 22},
                        {15, 368, 1, 9, 0, 0, 1605, 88, 29, 649, 54, 31},
                        {9, 372, 0, 10, 1, 0, 1207, 60, 19, 762, 59, 34},
                        {14, 368, 1, 20, 0, 1, 1196, 91, 26, 585, 77, 35},
                        {8, 406, 0, 15, 2, 1, 1150, 57, 14, 609, 101, 29},
                        {5, 346, 0, 7, 0, 0, 1007, 57, 20, 590, 45, 34},
                },

                {
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {12, 936, 5, 26, 5, 3, 5730, 169, 35, 928, 89, 152},
                        {13, 781, 2, 23, 1, 2, 5033, 138, 42, 862, 83, 111},
                },

                {
                        {9, 1208, 0, 42, 0, 0, 6293, 238, 63, 1662, 107, 163},
                        {16, 1240, 0, 38, 0, 0, 6078, 193, 51, 1348, 93, 136},
                        {21, 1200, 2, 31, 0, 0, 5862, 174, 45, 1058, 102, 134},
                        {11, 1186, 1, 36, 0, 0, 4910, 167, 45, 1018, 76, 101},
                        {18, 1068, 0, 34, 2, 1, 4301, 138, 32, 1153, 135, 140},
                        {15, 1098, 3, 21, 1, 2, 4729, 237, 46, 1129, 99, 159},
                        {13, 1109, 1, 26, 9, 5, 4471, 214, 60, 1252, 116, 139},
                        {16, 1055, 0, 35, 3, 4, 3928, 152, 53, 1351, 111, 128},
                },

                {
                        {7, 737, 2, 22, 0, 0, 3847, 109, 32, 676, 38, 74},
                        {3, 787, 0, 21, 0, 0, 3386, 133, 41, 437, 47, 57},
                        {6, 833, 0, 11, 0, 0, 3740, 120, 34, 326, 58, 37},
                        {6, 784, 1, 19, 0, 1, 3346, 100, 31, 440, 49, 42},
                        {9, 807, 2, 21, 2, 5, 2852, 112, 29, 626, 74, 57},
                        {10, 842, 2, 25, 1, 1, 3166, 168, 41, 784, 96, 71},
                        {10, 836, 1, 17, 1, 3, 3106, 142, 45, 698, 100, 96},
                        {10, 756, 2, 21, 0, 3, 2845, 107, 32, 955, 91, 74}
                }
        };
    }

    public static void setCurrentYear(int year) {
        currentYear = year;
    }

    public static int[] getYears() {
        return years;
    }
    public static int getCurrentYear(){
        return currentYear;
    }

    public static String[] getDistricts() {
        return districts;
    }
    public static String[] getCrimes(){
        return crimes;
    }

    public static int[][][] getData(){
        return data;
    }

    public static void setCrimeCounts(){
        crimeCounts = new int[years.length][districts.length];
        for (int i = 0; i < years.length; i++) {
            for (int j = 0; j < years.length; j++) {
                crimeCounts[i][j] = getCrimeCount(i,j);
            }
        }
    }

    /**
     * @param y year index
     * @param d district index
     * @return crime count for given year-district pair based on data
     */
    private static int getCrimeCount(int y, int d) {
        int crimeCount = 0;
        for (int c = 0; c < MapsActivity.getCrimes().length; c++) {
            crimeCount += MapsActivity.getData()[d][y][c];
        }
        return crimeCount;
    }

    public static int[][] getCrimeCounts(){
        return crimeCounts;
    }

}