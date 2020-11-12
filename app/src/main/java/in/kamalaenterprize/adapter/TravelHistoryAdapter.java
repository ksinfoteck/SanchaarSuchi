package in.kamalaenterprize.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.kamalaenterprize.commonutility.GlobalData;
import in.kamalaenterprize.commonutility.GlobalVariables;
import in.kamalaenterprize.database.DatabaseHandler;
import in.kamalaenterprize.model.TravelHistoryModel;
import in.kamalaenterprize.sncharsuchi.FragDashboard;
import in.kamalaenterprize.sncharsuchi.R;

public class TravelHistoryAdapter extends RecyclerView.Adapter<TravelHistoryAdapter.MyViewHolder> {
	private List<TravelHistoryModel> list;
	private FragDashboard frag1;
	private Context con;
	private DatabaseHandler db;

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView tvName, tvDate;

		public MyViewHolder(View view) {
			super(view);
            tvDate 	    = (TextView)view.findViewById(R.id.date);
			tvName		= (TextView)view.findViewById(R.id.name);

			if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
				Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), GlobalVariables.CUSTOMFONTNAME);
				ViewGroup root = (ViewGroup) view.findViewById(R.id.mylayout);
				GlobalData.setFont(root, font);
			}
		}
	}

	public TravelHistoryAdapter(List<TravelHistoryModel> lstData, Context con, FragDashboard frag) {
		this.list = lstData;
		this.frag1 = frag;
		this.con = con;
		db = new DatabaseHandler(con);
	}

	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_traveldata, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {
		TravelHistoryModel item 	= list.get(position);

		holder.tvName.setText(item.getName());
		holder.tvDate.setText(item.getDate());

//		holder.tvAddUpload.setTag(position+"");
//		holder.tvAddUpload.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				int pos = Integer.parseInt(view.getTag().toString());
//
//				frag1.UploadData(list.get(position));
//			}
//		});

		holder.itemView.setTag(position+"");
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
		private GestureDetector gestureDetector;
		private ClickListener clickListener;

		public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
			this.clickListener = clickListener;
			gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					return true;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
					if (child != null && clickListener != null) {
						clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
					}
				}
			});
		}

		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
			View child = rv.findChildViewUnder(e.getX(), e.getY());
			if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
				clickListener.onClick(child, rv.getChildPosition(child));
			}
			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

		@Override
		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
	}

	public interface ClickListener {
		void onClick(View view, int position);
		void onLongClick(View view, int position);
	}
}