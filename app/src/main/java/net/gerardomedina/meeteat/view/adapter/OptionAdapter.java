package net.gerardomedina.meeteat.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.model.Option;

import java.util.List;

public class OptionAdapter extends ArrayAdapter<Option> {

    AppCommon appCommon = AppCommon.getInstance();

    public OptionAdapter(Context context, List<Option> options) {
        super(context, 0, options);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Option option = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_with_icon, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.text1)).setText(option.getName());
        if (option.isAdmin()) {
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_star);
        } else {
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_star_border);
        }
        convertView.setOnClickListener(option.getAction());
        return convertView;
    }
}