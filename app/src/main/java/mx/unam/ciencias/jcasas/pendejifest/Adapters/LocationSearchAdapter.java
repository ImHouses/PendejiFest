package mx.unam.ciencias.jcasas.pendejifest.Adapters;

import android.content.Context;
import android.location.Address;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.unam.ciencias.jcasas.pendejifest.R;

/**
 * Created by juanh on 5/12/2017.
 */

public class LocationSearchAdapter extends BaseAdapter {

    private Context context;
    private List<Address> list;

    public LocationSearchAdapter(Context context, List<Address> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.location_result_item, null);
        TextView nombre = (TextView) v.findViewById(R.id.textItemLocationResult);
        Address a = list.get(position);
        int i = 0;
        String s = "";
        while (a.getAddressLine(i) != null) {
            if (i == 0)
                s += a.getAddressLine(i) + ", ";
            else s += a.getAddressLine(i);
            i++;
        }
        nombre.setText(s);
        return v;
    }
}
