package in.kamalaenterprize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.Tools;
import in.kamalaenterprize.commonutility.ViewAnimation;
import in.kamalaenterprize.model.NotificationModel;
import in.kamalaenterprize.model.TravelHistoryModel;
import in.kamalaenterprize.sncharsuchi.ActivityNotification;
import in.kamalaenterprize.sncharsuchi.R;

public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationModel> items = new ArrayList<>();
    private Context ctx;
    ActivityNotification act;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, TravelHistoryModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterNotification(Context context, List<NotificationModel> items, int animation_type, ActivityNotification act) {
        this.items = items;
        ctx = context;
        this.act = act;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView type;
        public TextView name;
//        public ImageButton bt_expand;
        public Button btn_accept, btn_reject;
        public View lyt_expand;
        public View lyt_parent, layAlert;

        public OriginalViewHolder(View v) {
            super(v);
            type = (TextView) v.findViewById(R.id.notification_type);
            name = (TextView) v.findViewById(R.id.notification_title);
//            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            btn_accept = (Button) v.findViewById(R.id.btn_accept);
            btn_reject = (Button) v.findViewById(R.id.btn_reject);
            lyt_expand = (View) v.findViewById(R.id.lyt_expand);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            layAlert = (LinearLayout) v.findViewById(R.id.lay_alert);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final NotificationModel p = items.get(position);
            view.name.setText(p.getNotification_title());
            view.type.setText(p.getType());

            if (p.getType().equalsIgnoreCase("history")) {
                if (p.getStatus().equals("0")) {
                    if (p.getMeeter_remark().length() == 0) {
                        view.btn_accept.setVisibility(View.VISIBLE);
                        view.btn_reject.setVisibility(View.VISIBLE);
                    }else {
                        view.btn_accept.setVisibility(View.GONE);
                        view.btn_reject.setVisibility(View.GONE);
                    }
                }else {
                    view.btn_accept.setVisibility(View.GONE);
                    view.btn_reject.setVisibility(View.GONE);
                }
            }else if(p.getType().equalsIgnoreCase("alert")){
                view.btn_accept.setVisibility(View.GONE);
                view.btn_reject.setVisibility(View.GONE);

                view.layAlert.setBackgroundColor(ctx.getResources().getColor(R.color.red));
            }else {
                view.btn_accept.setVisibility(View.GONE);
                view.btn_reject.setVisibility(View.GONE);
            }

            view.btn_accept.setId(position);
            view.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AcceptReject("1", items.get(v.getId()));

                    notiRemark = "";
                    act.RejectAccept("1", items.get(v.getId()), notiRemark);
                }
            });

            view.btn_reject.setTag(position);
            view.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(v.getTag().toString());
//                    AcceptReject("0", items.get(id));

                    notiRemark = "";
                    act.RejectAccept("0", items.get(id), notiRemark);
                }
            });

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

//            view.bt_expand.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    boolean show = toggleLayoutExpand(!p.expanded, v, view.lyt_expand);
//                    items.get(position).expanded = show;
//                }
//            });


            // void recycling view
            if (p.expanded) {
                view.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                view.lyt_expand.setVisibility(View.GONE);
            }
//            Tools.toggleArrow(p.expanded, view.bt_expand, false);

            setAnimation(view.itemView, position);
        }
    }

    String notiRemark = "";
//    public void AcceptReject(String acceptReject, NotificationModel item) {
//        new MaterialDialog.Builder(ctx)
//                .input(notiRemark, notiRemark, false, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                        notiRemark = input.toString();
//
//                        act.RejectAccept(acceptReject, item, notiRemark);
//                    }
//                })
//                .title("Add Remark")
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .positiveText("Add")
//                .positiveColor(ctx.getResources().getColor(R.color.colorPrimaryDark))
//                .show();
//    }

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

}