package com.govan.listfragments;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

class Container {

	String Text = null;
	Drawable Icon;

	public Container() {
		super();
		Text = new String();

	}

	public Container(String Itext, Drawable iIcon) {
		Text = Itext;
		Icon = iIcon;

	}

	public String getName() {
		return Text;

	}
}

class ImageAdapter extends ArrayAdapter<Container> {
	Context context;
	int layoutRid;
	boolean notifyData = false;
	Holder holder = null;

	ArrayList<Container> data = null;
	ArrayList<Container> org = null;

	public ImageAdapter(Context iContext, int iResourceId,
			ArrayList<Container> data2) {
		super(iContext, iResourceId, data2);

		context = iContext;
		layoutRid = iResourceId;
		data = data2;
		holder = new Holder();
	}

	static class Holder {
		ImageView imageIcon;
		TextView text;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutRid, parent, false);

		holder.imageIcon = (ImageView) row.findViewById(R.id.icon);
		holder.text = (TextView) row.findViewById(R.id.product_name);

		Container temp = data.get(position);
		holder.imageIcon.setImageDrawable(temp.Icon);
		holder.text.setText(temp.Text);

		return row;

	}

	public Filter getFilter() {

		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				// System.out.println("\n"+constraint);
				final FilterResults results = new FilterResults();
				ArrayList<Container> newContainer = new ArrayList<Container>();

				if (org == null) {
					org = new ArrayList<Container>();
					org.addAll(0, data);

				}

				if (constraint.length() != 0) {
					if (org != null && org.size() != 0) {
						for (final Container loop : org) {

							boolean temp = loop.getName().toLowerCase()
									.contains(constraint.toString());

							if (temp) {

								Container tempContainer = new Container(
										loop.Text, loop.Icon);
								newContainer.add(tempContainer);
							}

						}
					}

					results.values = newContainer;
				} else {
					results.values = org;

				}
				return results;

			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Au1to-generated method stub
				if (results.values != null) {

					data.clear();
					data.addAll((Collection<? extends Container>) results.values);
					notifyDataSetChanged();
				}

			}

		};

	}

	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
		notifyData = true;
	}

}
