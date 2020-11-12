package in.kamalaenterprize.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import in.kamalaenterprize.commonutility.ItemAnimation;
import in.kamalaenterprize.commonutility.Tools;
import in.kamalaenterprize.commonutility.ViewAnimation;
import in.kamalaenterprize.model.ApproveAlertModel;
import in.kamalaenterprize.model.TravelHistoryModel;
import in.kamalaenterprize.sncharsuchi.ActivityApproveAlert;
import in.kamalaenterprize.sncharsuchi.R;

public class AdapterApproveAlert extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ApproveAlertModel> items = new ArrayList<>();
    private Context ctx;
    ActivityApproveAlert act;
    private OnItemClickListener mOnItemClickListener;
    private int animation_type = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, TravelHistoryModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterApproveAlert(Context context, List<ApproveAlertModel> items, int animation_type, ActivityApproveAlert act) {
        this.items = items;
        ctx = context;
        this.act = act;
        this.animation_type = animation_type;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView title, sanchaarId, labname, reportnumber;
        public TextView contactnumber;
        public Button btn_accept, btn_reject;
        public View lyt_expand;
        public View lyt_parent, layAlert;

        public OriginalViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.alert_title);
            sanchaarId = (TextView) v.findViewById(R.id.alert_sanchaarid);
            labname = (TextView) v.findViewById(R.id.alert_labname);
            reportnumber = (TextView) v.findViewById(R.id.alert_reportnumber);
            contactnumber = (TextView) v.findViewById(R.id.alert_contactnumber);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approvealert, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final ApproveAlertModel p = items.get(position);
            view.title.setText(p.getFullname());
            view.sanchaarId.setText(p.getSender_yoddha());
            view.contactnumber.setText(p.getContactnumber());
            view.labname.setText(p.getLabname());
            view.reportnumber.setText(p.getReport_number());

            view.btn_accept.setId(position);
            view.btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AcceptReject("1", items.get(v.getId()));
                }
            });

            view.btn_reject.setTag(position);
            view.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.parseInt(v.getTag().toString());
                    AcceptReject("2", items.get(id));
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

    public void AcceptReject(String acceptReject, ApproveAlertModel item) {
        MaterialDialog dialog = new MaterialDialog.Builder(ctx)
                .input(notiRemark, notiRemark, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        notiRemark = input.toString();

                        act.RejectAccept(acceptReject, item, notiRemark);
                    }
                })
                .title("Add Remark")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("Add")
                .positiveColor(ctx.getResources().getColor(R.color.colorPrimaryDark))
                .show();
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

}