//package in.ksinfoteck.adapter;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.support.v7.widget.RecyclerView;
//import android.view.GestureDetector;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import in.ksinfoteck.commonutility.GlobalData;
//import in.ksinfoteck.commonutility.GlobalVariables;
//import in.ksinfoteck.database.DatabaseHandler;
//import in.ksinfoteck.yoddha.ActivityViewSchool;
//import in.ksinfoteck.yoddha.R;
//
//public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {
//	private List<String[]> list;
//	private ActivityViewSchool frag1;
//	private Context con;
//	private DatabaseHandler db;
//	String strRemark = "";
//
//	public class MyViewHolder extends RecyclerView.ViewHolder {
//		public TextView tvCount, tvName, tvDesc, tvViewShow, tvAddUpload, txtDelete;
//
//		public MyViewHolder(View view) {
//			super(view);
//			tvCount 	= (TextView)view.findViewById(R.id.text_count);
//			tvName		= (TextView)view.findViewById(R.id.text_name);
//			tvDesc		= (TextView)view.findViewById(R.id.text_address);
//			tvViewShow	= (TextView)view.findViewById(R.id.text_show);
//			tvAddUpload		= (TextView)view.findViewById(R.id.text_upload);
//			txtDelete	= (TextView)view.findViewById(R.id.text_delete);
//
//			if (!GlobalVariables.CUSTOMFONTNAME.equals("")) {
//				Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), GlobalVariables.CUSTOMFONTNAME);
//				ViewGroup root = (ViewGroup) view.findViewById(R.id.mylayout);
//				GlobalData.setFont(root, font);
//			}
//		}
//	}
//
//	public ViewAdapter(List<String[]> list, Context con, ActivityViewSchool frag) {
//		this.list = list;
//		this.frag1 = frag;
//		this.con = con;
//		db = new DatabaseHandler(con);
//	}
//
//	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_traveldata, parent, false);
//
//		return new MyViewHolder(itemView);
//	}
//
//	@Override
//	public void onBindViewHolder(final MyViewHolder holder, int position) {
//		final String[] item 	= list.get(position);
//
//		holder.tvCount.setText(item[0]);
//		holder.tvName.setText(item[1]);
//		holder.tvDesc.setText(item[2]);
//
//		holder.tvAddUpload.setTag(position+"");
//		holder.tvAddUpload.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				int pos = Integer.parseInt(view.getTag().toString());
//
//				frag1.UploadData(list.get(position));
//			}
//		});
//
//		holder.txtDelete.setTag(position+1000 + "");
//		holder.txtDelete.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				int pos = Integer.parseInt(view.getTag().toString()) - 1000;
//
//				frag1.DeleteData(list.get(pos));
//			}
//		});
//
////		holder.tvAddRemark.setTag(position+"");
////		holder.tvAddRemark.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View view) {
////				int pos = Integer.parseInt(view.getTag().toString());
////
////				new MaterialDialog.Builder(con)
////						.input(strRemark, strRemark, false, new MaterialDialog.InputCallback() {
////							@Override
////							public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
////								strRemark = input.toString();
////
////								frag1.AddRemark(list.get(position).getStr_school_id(), strRemark);
////							}
////						})
////						.title("Add Remark")
////						.inputType(InputType.TYPE_CLASS_TEXT)
////						.positiveText("Add")
////						.positiveColor(con.getResources().getColor(R.color.colorPrimaryDark))
////						.show();
////			}
////		});
//
//		holder.itemView.setTag(position+"");
//		holder.itemView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//
//			}
//		});
//	}
//
//	@Override
//	public int getItemCount() {
//		return list.size();
//	}
//
//	public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
//		private GestureDetector gestureDetector;
//		private ClickListener clickListener;
//
//		public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
//			this.clickListener = clickListener;
//			gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//				@Override
//				public boolean onSingleTapUp(MotionEvent e) {
//					return true;
//				}
//
//				@Override
//				public void onLongPress(MotionEvent e) {
//					View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//					if (child != null && clickListener != null) {
//						clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
//					}
//				}
//			});
//		}
//
//		@Override
//		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//			View child = rv.findChildViewUnder(e.getX(), e.getY());
//			if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//				clickListener.onClick(child, rv.getChildPosition(child));
//			}
//			return false;
//		}
//
//		@Override
//		public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
//
//		@Override
//		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
//	}
//
//	public interface ClickListener {
//		void onClick(View view, int position);
//		void onLongClick(View view, int position);
//	}
//}