package com.example.cosc341project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchActivity extends AppCompatActivity {

    //Initiating variables
    ListView listView; //import ListView class
    SearchView searchView; //import SearchView class
    ArrayAdapter<String> arrayAdapter;//import ArrayAdapter class

    //creating string array to hold contents for myListView
    String[] WinerySearchList = {"Adega on 45th Estate Winery", "Ancient Hill Estate Winery", "Arrowleaf Cellars", "Bartier Bros. Vineyard & Winery", "Bella Wines", "Bench 1775 Winery", "Black Hill Estate Winery", "Black Widow Winery", "Blast Church Vineyards", "Blue Mountain Vineyard and Cellars",
            "Bordertown Winery", "Burrowing Owl Vineyards", "Calona Vineyards", "Castoro de Oro Estate Winery", "CedarCreek Estate Winery", "Chain Reaction Winery", "Corcelettes Estate Winery", "Covert Farms Family Estate", "Crowsnest Vineyards", "D’angelo Estate Winery", "Da Silva Vineyards",
            "Daydreamer Wines", "Deep Roots Winery", "Desert Hill Estate Winery", "Dirty Laundry Vineyards", "Eau Viver Winery", "Elephant Island Winery", "Fairview Cellars", "Forbidden Fruit Winery", "French Door Estate Winery", "Gehringer Brothers Estate Winery", "Grey Monk estate Winery",
            "Hester Creek Estate Winery", "Hillside Winery", "Howling Bluff Estate Winery", "Intersection Estate Winery", "JoieFarm Winery", "Kettle Vally Winery", "Kitsch Wines", "La Stella Winery", "Lake Breeze Vineyards", "Lang Vineyards", "Laughing Stock Vineyards", "Le Vieux Pin",
            "Liber Farms & Winery", "Little Farm Winery", "Lock & Worth Winery", "Lunessence Winery & Vineyards", "Marichel Vineard", "Maverick Estate Winery", "Meyer Family Vineyards", "Mission Hill Family Estate Winery", "Moon Curser Vineyards", "Moraine estate Winery", "Mt. Boucherie estate Winery",
            "Nichol Vinyards", "Nk’mip Cellars", "Noble Ridge Vineyard & Winery", "Nostalgia Wines", "Origin Wines", "Orofino Winery", "Painted Rock Estate Winery", "Pentâge Winery", "Phantom Creek Estates", "Poplar Grove Winery", "Quail’s Gate Winery", "Ramification Cellars", "Red Rooster Winery",
            "Riverstone Estate Winery", "Road 13 Vineyards", "Robin Ridge Winery", "Roche Wines", "Rollingdale Winery", "Ruby Blues Winery", "Rust Wine Co.", "Sandhill Wines", "See Ya Later Ranch", "Seven Stones Winery", "Silver Sage Winery", "Solvero Wines", "SpearHead Winery", "Sperling Vineyards",
            "St. Hubertus & Oak Bay Estate Winery", "Stag’s Hollow Winery", "Stoneboat Vineyards", "Sumac Ridge Estate Winery", "Summerhill Pyramid Winery", "Synchromesh Wines", "Tantalus Vineyards", "Terravista Vineyards", "The Vibrant Vine Winery", "Therapy Vineyards", "Thornhaven Estates Winery",
            "Three Sisters Winery", "TIME Family of Wines", "Tinhorn Creek Vineyards", "Township 7 Vineyards", "Van Western Vineyards", "Wild Goose Vineyards & Winery", "Winemaker’s Cut"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //SETTING UP SEARCHVIEW AND LISTVIEW

        //getting refernce to the WinerySearchListView and WineryNameSearchView
        listView = findViewById(R.id.WinerySearchListView);
        searchView = findViewById(R.id.WineryNameSearchView);

        //setting up ArrayAdapter
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, WinerySearchList);

        //setting the ListView contents to the ArrayAdapter
        listView.setAdapter(arrayAdapter);

        //setting up a listener to deal with listView visibility (i.e. make list visible once the searchView is activated)
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if (hasFocus){
                    //searchView is active so show listView
                    listView.setVisibility(View.VISIBLE);
                }else {
                    //searchView is not active so do not show listView
                    listView.setVisibility(View.GONE);
                }
            }//end onFocusChange
        });

        //implementing search features
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //method for when the user submits their query
                arrayAdapter.getFilter().filter(query);
                return false;
            }//end onQueryTextSubmit

            @Override
            public boolean onQueryTextChange(String newText) {
                //method that changes the list filter as the user changes their query
                arrayAdapter.getFilter().filter(newText);
                return false;
            }//end onQueryTextChange
        });

        //DONE SETTING UP SEARCHVIEW AND LISTVIEW

    }//end onCreate

    /* Layout IDs
     * SearchView to search Wineries by name: WineryNameSearchView
     * ListView: WinerySearchListView
     * Button to access Compare Tours Page: CompareButton
     * Button to book a tour: BookButton
     * Button to access the home page: SearchHomeButton
     * Button for user to check their upcoming and previous tours: SearchMyToursButton
     * Button to access profile page: ProfileButton
     */
}//end SearchActivity class