package dearfriend.abhirams.example.com.dearfriend.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dearfriend.abhirams.example.com.dearfriend.Model.CommonSearchDO;
import dearfriend.abhirams.example.com.dearfriend.R;

public class AutoCompleteCommonAdapter extends ArrayAdapter<CommonSearchDO>
{
    private List<CommonSearchDO> commonSearchDOListFull;

    public AutoCompleteCommonAdapter(@NonNull Context context, @NonNull List<CommonSearchDO> CommonSearchDOList)
    {
        super(context, 0, CommonSearchDOList);
        commonSearchDOListFull = new ArrayList<>(CommonSearchDOList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return commonSearchDOFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.common_autocomplete_row, parent, false
            );
        }

        TextView textViewLeft = convertView.findViewById(R.id.textViewLeft);
        TextView textViewRight = convertView.findViewById(R.id.textViewRight);

        CommonSearchDO commonSearchDO = getItem(position);

        if(commonSearchDO!=null)
        {
            textViewLeft.setText(commonSearchDO.getLeftValue());
            textViewLeft.setTag(commonSearchDO.getHiddenValue());

            textViewRight.setText(commonSearchDO.getRightValue());
            textViewRight.setTag(commonSearchDO.getRightValue());
        }
        return convertView;
    }

    private Filter commonSearchDOFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<CommonSearchDO> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(commonSearchDOListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(CommonSearchDO item : commonSearchDOListFull)
                {
                    if (item.getLeftValue().toLowerCase().contains(filterPattern) || item.getRightValue().toLowerCase().contains(filterPattern))
                    {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((CommonSearchDO) resultValue).getLeftValue();
        }
    };
}
