package com.app.animalsshelter.content.get_list_lost_animals;

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
import com.app.animalsshelter.content.get_list_animals.RoundedTransformation;
import com.app.animalsshelter.model.Animal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterGridviewLostAnimals extends BaseAdapter implements Filterable {
    private Context mContext;
    private int resource;
    private ArrayList<Animal> listAnimal = null;
    private ArrayList<Animal> listAnimalTemp = null;


    public CustomAdapterGridviewLostAnimals(Context context, int resource, ArrayList<Animal> listAnimal) {
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
//            check to see if we have a view
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.image2);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textViewLostAnimal);
            holder.progressBar2 = (ProgressBar) convertView.findViewById(R.id.progressBarInCustomGridView2);
            holder.progressBar2.setVisibility(View.VISIBLE);


            convertView.setTag(holder);
        } else {
//                use the recycled view object
            holder = (Holder) convertView.getTag();
        }
        //Animal animal = listAnimal.get(position);
        holder.textView2.setText(listAnimal.get(position).getSpecies() + "");
        final Holder finalHolder = holder;
        Picasso.with(mContext)
                .load(listAnimal.get(position).getImageURLthumbnail())
                .placeholder(R.drawable.placeholder)
                .noFade()
                .resize(130, 130).transform(new RoundedTransformation(10, 10))
                .centerInside()
                .error(R.drawable.missing).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.imageView2, new ImageLoadedCallback(finalHolder.progressBar2) {
                    @Override
                    public void onSuccess() {
                        if (finalHolder.progressBar2 != null) {
                            finalHolder.progressBar2.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        if (finalHolder.progressBar2 != null) {
                            finalHolder.progressBar2.setVisibility(View.GONE);
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

                ArrayList<Animal> filteredAnimals = new ArrayList<Animal>();

                filterResults.count = filteredAnimals.size();
                filterResults.values = filteredAnimals;
                Log.e("VALUES", filterResults.values.toString());


                if (GetListLostAnimalsFragment.searchOn) {
                    charSequence = charSequence.toString().toLowerCase();
                    if (charSequence == null || charSequence.length() == 0) {
                        filterResults.values = listAnimalTemp;
                        filterResults.count = listAnimalTemp.size();

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
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    static class Holder {
        ImageView imageView2;
        TextView textView2;
        ProgressBar progressBar2;
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