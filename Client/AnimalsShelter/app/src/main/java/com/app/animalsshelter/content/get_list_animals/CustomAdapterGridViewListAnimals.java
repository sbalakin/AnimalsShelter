package com.app.animalsshelter.content.get_list_animals;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.animalsshelter.R;
import com.app.animalsshelter.model.Animal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Adapter for ListView inside "Get List Animals" Fragment
 *
 */
public class CustomAdapterGridViewListAnimals extends BaseAdapter implements Filterable {
    private Context mContext;
    private int resource;
    private ArrayList<Animal> listAnimal = null;
    private ArrayList<Animal> listAnimalTemp = null;

    public CustomAdapterGridViewListAnimals(Context context, int resource, ArrayList<Animal> listAnimal) {
        this.resource = resource;
        this.mContext = context;
        this.listAnimal = listAnimal;
        this.listAnimalTemp = listAnimal;
    }

    @Override
    public int getCount() {
        if (listAnimal != null)
            return listAnimal.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return listAnimal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarInCustomGridView);
            holder.progressBar.setVisibility(View.VISIBLE);


            convertView.setTag(holder);
        } else {
//                use the recycled view object
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText(listAnimal.get(position).getName() + "");
        final Holder finalHolder = holder;
        Picasso.with(mContext)
                .load(listAnimal.get(position).getImageURLthumbnail())
                .placeholder(R.drawable.placeholder)
                .noFade()
                .resize(130, 130).transform(new RoundedTransformation(10, 10))
                .centerInside()
                .error(R.drawable.missing).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.imageView, new ImageLoadedCallback(finalHolder.progressBar) {
                    @Override
                    public void onSuccess() {
                        if (finalHolder.progressBar != null) {
                            finalHolder.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        if (finalHolder.progressBar != null) {
                            finalHolder.progressBar.setVisibility(View.GONE);
                        }
                    }
                });


        return convertView;
    }


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();
                String filterSpecies = GetListAnimalsFragment.filterSpecies;
                String filterGender = GetListAnimalsFragment.filterGender;
                String filterAge = GetListAnimalsFragment.filterAge;
                String spinnerSpecies = GetListAnimalsFragment.spinnerFilterSpecies.getSelectedItem().toString();
                String spinnerGender = GetListAnimalsFragment.spinnerFilterGender.getSelectedItem().toString();
                String spinnerAge = GetListAnimalsFragment.spinnerFilterAge.getSelectedItem().toString();
                String all = mContext.getResources().getString(R.string.get_list_animals_filter_all);
                ArrayList<Animal> filteredAnimals = new ArrayList<Animal>();
                if (spinnerSpecies.equals(all) && spinnerGender.equals(all) && spinnerAge.equals(all)) {
                    filteredAnimals = listAnimalTemp;

                } else if (!spinnerSpecies.equals(all) && spinnerGender.equals(all) && spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterSpecies.equals(listAnimalTemp.get(i).getSpecies())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (spinnerSpecies.equals(all) && !spinnerGender.equals(all) && spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterGender.equals(listAnimalTemp.get(i).getGender())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (spinnerSpecies.equals(all) && spinnerGender.equals(all) && !spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterAge.equals(listAnimalTemp.get(i).getAge())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (spinnerSpecies.equals(all) && !spinnerGender.equals(all) && !spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterGender.equals(listAnimalTemp.get(i).getGender()) && filterAge.equals(listAnimalTemp.get(i).getAge())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (!spinnerSpecies.equals(all) && spinnerGender.equals(all) && !spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterSpecies.equals(listAnimalTemp.get(i).getSpecies()) && filterAge.equals(listAnimalTemp.get(i).getAge())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (!spinnerSpecies.equals(all) && !spinnerGender.equals(all) && spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterSpecies.equals(listAnimalTemp.get(i).getSpecies()) && filterGender.equals(listAnimalTemp.get(i).getGender())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                } else if (!spinnerSpecies.equals(all) && !spinnerGender.equals(all) && !spinnerAge.equals(all)) {
                    for (int i = 0; i < listAnimalTemp.size(); i++) {
                        if (filterSpecies.equals(listAnimalTemp.get(i).getSpecies()) && filterGender.equals(listAnimalTemp.get(i).getGender()) && filterAge.equals(listAnimalTemp.get(i).getAge())) {
                            filteredAnimals.add(listAnimalTemp.get(i));
                        }
                    }
                }
                filterResults.count = filteredAnimals.size();
                filterResults.values = filteredAnimals;
                Log.e(mContext.getResources().getString(R.string.log_values), filterResults.values.toString());


                if (GetListAnimalsFragment.searchOn) {
                    charSequence = charSequence.toString().toLowerCase();
                    if (charSequence == null || charSequence.length() == 0) {
                        filterResults.values = listAnimalTemp;
                        filterResults.count = listAnimalTemp.size();
                        GetListAnimalsFragment.searchOn = false;

                    } else {
                        ArrayList<Animal> FilteredListAnimal = new ArrayList<Animal>();
                        for (int i = 0; i < listAnimalTemp.size(); i++) {
                            String animalName = listAnimalTemp.get(i).getName();
                            if (animalName.toLowerCase().startsWith(charSequence.toString())) {
                                FilteredListAnimal.add(listAnimalTemp.get(i));
                            }
                        }

                        filterResults.count = FilteredListAnimal.size();
                        filterResults.values = FilteredListAnimal;
                        Log.e(mContext.getResources().getString(R.string.log_values), filterResults.values.toString());
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listAnimal = (ArrayList<Animal>) filterResults.values;
                if (listAnimal != null) {
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    static class Holder {
        ImageView imageView;
        TextView textView;
        ProgressBar progressBar;
    }

    class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

}