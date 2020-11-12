package in.kamalaenterprize.commonutility;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.sncharsuchi.R;

public class SpinnerPopulateAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	Context mContext;
	List<String> lstSpinner = new ArrayList<String>();
	boolean isIDExists 		= false;
	public SpinnerPopulateAdapter(Context con, List<String> listSpinner, boolean isIDExists) {
		mContext 			= con;
		mInflater 			= LayoutInflater.from(con);
		this.lstSpinner 	= listSpinner;
		this.isIDExists 	= isIDExists;
	}

	@Override
	public int getCount() {
		return lstSpinner.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ListContent holder;
		View v = convertView;
		if (v == null) {
			v = mInflater.inflate(R.layout.item_spinner, parent, false);
			holder = new ListContent();
			holder.name = (TextView)v.findViewById(R.id.txtitem);
			v.setTag(holder);
		} else {
			holder = (ListContent) v.getTag();
		}

		if (isIDExists) {
			holder.name.setText(Html.fromHtml(lstSpinner.get(position).split("#:#")[1]));
		} else {
			holder.name.setText(Html.fromHtml(lstSpinner.get(position)));
		}
		return v;
	}

}

class ListContent {
	TextView name;
}
