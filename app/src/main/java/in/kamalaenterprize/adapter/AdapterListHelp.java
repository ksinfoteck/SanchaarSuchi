package in.kamalaenterprize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.Tools;
import in.kamalaenterprize.commonutility.ViewAnimation;
import in.kamalaenterprize.model.HelpModel;
import in.kamalaenterprize.sncharsuchi.R;

public class AdapterListHelp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HelpModel> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, HelpModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListHelp(Context context, List<HelpModel> items, int animation_type) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView ques;
        public TextView ans;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            ques = (TextView) v.findViewById(R.id.txt_ques);
            ans = (TextView) v.findViewById(R.id.txt_ans);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_help, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final HelpModel p = items.get(position);
            view.ques.setText(p.getSteQues());
            view.ans.setText(p.getStrAns());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

//            if (p.getIsVerified().equalsIgnoreCase("1")){
//                view.lyt_parent.setBackgroundColor(ctx.getResources().getColor(R.color.gray_light));
//            }else {
//                view.lyt_parent.setBackgroundColor(ctx.getResources().getColor(R.color.gray_light));
//            }

            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!p.expanded, v, view.lyt_expand);
                    items.get(position).expanded = show;
                }
            });


            // void recycling view
            if(p.expanded){
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
            Tools.toggleArrow(p.expanded, view.bt_expand, false);

            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;
    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

    private String DivideYoddhaId(String str) {
        String[] equalStr = new String[5];

//        String str = "RJ123456789012";
        equalStr[0] = str.substring(0, 2);
        equalStr[1] = str.substring(2, 5);
        equalStr[2] = str.substring(5, 8);
        equalStr[3] = str.substring(8, 11);
        equalStr[4] = str.substring(11, str.length());


        String finalYoddhaId = "";
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                finalYoddhaId = finalYoddhaId + equalStr[i];
            } else if (1 == 1) {
                finalYoddhaId = finalYoddhaId + " " + equalStr[i];
            } else {
                finalYoddhaId = finalYoddhaId + " " + equalStr[i];
            }
        }
        return finalYoddhaId;
    }

}