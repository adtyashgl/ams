package adiractech.ams.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import adiractech.ams.R;
import adiractech.ams.listelements.FranchiseElement;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by root on 13/04/17.
 */

public class CustomFranchiseListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<FranchiseElement> elements;

    public CustomFranchiseListAdapter(Activity context, ArrayList<FranchiseElement> elements){
        super(context, R.layout.franchise_list);
        this.context = context;
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.franchise_list, null, true);
        FancyButton icon  = (FancyButton)rowView.findViewById(R.id.icon);
        TextView heading = (TextView)rowView.findViewById(R.id.item);
        TextView space_address = (TextView)rowView.findViewById(R.id.space_address);

        FranchiseElement element = elements.get(position);

        heading.setText(element.getName());
        space_address.setText(element.getAddress());
        rowView.setTag(element.getFranchiseId());
        return rowView;
    }


}
