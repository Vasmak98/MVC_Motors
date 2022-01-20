package com.example.mvc_motors.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvc_motors.R;
import com.google.android.gms.maps.MapFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_places_to_visit, container, false);

        int[] capegrecoimage = new int[] {
                R.drawable.cavogrego1, R.drawable.capegreco2, R.drawable.cavogrego2};
        int[] bluelagoonimage = new int[]{
                R.drawable.bluelagoonpafos, R.drawable.blue2, R.drawable.bluelagoonpafos3jpg
        };
        int [] konnosimage = new int[] {
                R.drawable.konnosbay, R.drawable.konnos2, R.drawable.konnos3a
        };
        int [] figtreeimage = new int[] {
                R.drawable.fig1, R.drawable.figbtreebay2, R.drawable.fig2
        };
        int[] bridgeofloveimage = new int[]{
                R.drawable.bridgeoflove, R.drawable.love2, R.drawable.love3
        };
        CarouselView carouselView = root.findViewById(R.id.carouselView);
        TextView textView = root.findViewById(R.id.textView22);
        View fraggreco = root.findViewById(R.id.fragment2);
        View fragkonnos = root.findViewById(R.id.fragment3);
        View fragfigtree = root.findViewById(R.id.fragment4);
        View fragbluelagoon = root.findViewById(R.id.fragment5);
        View fraglovebridge = root.findViewById(R.id.fragment6);
        TextView tv = root.findViewById(R.id.textView10);


        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            tv.setText("Location of Cape Greco: ");
            carouselView.setPageCount(capegrecoimage.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(capegrecoimage[position]);
                }
            });
            fragkonnos.setVisibility(View.INVISIBLE);
            fragfigtree.setVisibility(View.INVISIBLE);
            fragbluelagoon.setVisibility(View.INVISIBLE);
            fraglovebridge.setVisibility(View.INVISIBLE);
            textView.setText("Cape Greco is a headland in the southeastern part of the island of Cyprus. \n" +
             "It is visited by tourists for its natural environment, and is a protected nature park. \n" +
                    "The Cape has a lighthouse built by the British and is the second one built in Cyprus after the castle of Pafos.\n"+
                    "According to local legend, it is also the home of the 'Ayia Napa sea monster'.");
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            tv.setText("Location of Fig Tree Bay: ");

            carouselView.setPageCount(figtreeimage.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(figtreeimage[position]);
                }
            });
            fraggreco.setVisibility(View.INVISIBLE);
            fragkonnos.setVisibility(View.INVISIBLE);
            fragbluelagoon.setVisibility(View.INVISIBLE);
            fraglovebridge.setVisibility(View.INVISIBLE);
            textView.setText("Fig Tree Bay is a sandy beach in the resort of Protaras.\n"+
                    "As with all beaches in Cyprus, access to the public is free, whilst bed and umbrella hire is chargeable. A municipal car park provides parking within a short walk.\n"+
            "In 2010, during structural improvements to the beach, an ancient Greek tomb was unearthed.\n" +
                    "The beach was mentioned in a song titled \"Fig Tree Bay\" - the first song on the 1972 Wind of Change album by Peter Frampton.\n" );
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
            tv.setText("Location of Konnos Bay: ");
            carouselView.setPageCount(konnosimage.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(konnosimage[position]);
                }
            });
            fraggreco.setVisibility(View.INVISIBLE);
            fragfigtree.setVisibility(View.INVISIBLE);
            fragbluelagoon.setVisibility(View.INVISIBLE);
            fraglovebridge.setVisibility(View.INVISIBLE);
            textView.setText("Konnos bay is considered to be one of the most scenic beaches on Cyprus.\n" +
                    "The beach stretches with fine golden sand for about 200 metres.\n"+
            "Konnos Bay also has a snack bar and a great selection of watersports available and it's ingenious sun loungers available for hire actually float in the water. ");
        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4){
            tv.setText("Location of Blue Lagoon: ");
            carouselView.setPageCount(bluelagoonimage.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(bluelagoonimage[position]);
                }
            });
            fraggreco.setVisibility(View.INVISIBLE);
            fragfigtree.setVisibility(View.INVISIBLE);
            fragkonnos.setVisibility(View.INVISIBLE);
            fraglovebridge.setVisibility(View.INVISIBLE);
            textView.setText("The Blue Lagoon is one of the most isolated yet beautiful beaches in Cyprus. " +
                    "The blue lagoon beach could have easily taken the name of heaven.\n"+
                    "It is a beach with golden sand and crystal clear waters where any visitor can enjoy swimming and the beautiful sun of our Cyprus.\nIt is an area of unique natural beauty. ");

        }
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 5){
            tv.setText("Location of Bridge of Love: ");
            carouselView.setPageCount(bridgeofloveimage.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(bridgeofloveimage[position]);
                }
            });
            fraggreco.setVisibility(View.INVISIBLE);
            fragfigtree.setVisibility(View.INVISIBLE);
            fragkonnos.setVisibility(View.INVISIBLE);
            fragbluelagoon.setVisibility(View.INVISIBLE);
            textView.setText("The bridge of the lovers or the bridge of love is very beautiful and one of the most romantic places in all of Cyprus. \n"+
                    "It is a magical spot where the sea meets the land in a magnificent way, a natural bridge that has been standing there for centuries .\n"+
                    "It is believed that if you kiss your loved one while you are standing in the middle of the bridge arch and make a wish, it will surely become a reality.");
        }

            return root;
    }
}