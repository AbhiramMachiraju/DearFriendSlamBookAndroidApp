package dearfriend.abhirams.example.com.dearfriend.Util;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import dearfriend.abhirams.example.com.dearfriend.R;
import com.nex3z.notificationbadge.NotificationBadge;

import dearfriend.abhirams.example.com.dearfriend.Model.DataModel;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<DataModel> mValues;
    Context mContext;
    protected  ItemListener mListener;
    public  static Integer approvalCount;

    public  RecyclerViewAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        DataModel item;
        NotificationBadge notificationBadge;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = v.findViewById(R.id.textView);
            imageView = v.findViewById(R.id.imageView);
            relativeLayout = v.findViewById(R.id.relativeLayout);
            notificationBadge= v.findViewById(R.id.badge);
        }

        public void setData(DataModel item) {
            this.item = item;

            textView.setText(item.text);
            textView.setTag(item.hidenValue);
            imageView.setImageResource(item.drawable);
            //relativeLayout.setBackgroundColor(Color.parseColor(item.color));
            if(item.hidenValue.equals("APPROVE"))
            {
                notificationBadge.setVisibility(View.VISIBLE);
                notificationBadge.setNumber(approvalCount);
                notificationBadge.setTextColor(R.color.ColorPrimaryDark);
            }
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}