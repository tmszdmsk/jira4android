package jira.For.Android.TaskDetails.WorkLog;

import java.util.List;

import jira.For.Android.R;
import jira.For.Android.DataTypes.WorkLog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class WorkLogAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<WorkLog> worklogs;
	
	public WorkLogAdapter(Context context, List<WorkLog> worklogs) {
		this.inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.worklogs = worklogs;
	}
	
	public void addWorkLog(WorkLog worklog) {
		worklogs.add(worklog);
	}
	
	@Override
	public int getCount() {
		return worklogs.size();
	}
	
	@Override
	public String getItem(int position) {
		return worklogs.get(position).toString();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_worklog, null);
		}
		
		TextView textViewUsername = (TextView) convertView
		        .findViewById(R.id.worklogListUserTextView);
		textViewUsername.setText(worklogs.get(position).getAuthorFullName());
		
		TextView textViewDate = (TextView) convertView
		        .findViewById(R.id.worklogListDateTextView);
		textViewDate.setText(worklogs.get(position).getCreated()
		        .toLocaleString());
		
		TextView textViewTimeSpent = (TextView) convertView
				.findViewById(R.id.worklogListTimeSpentTextView);
		textViewTimeSpent.setText(worklogs.get(position).getTimeSpent());
		
		TextView textViewComment = (TextView) convertView
				.findViewById(R.id.worklogListCommentTextView);
		textViewComment.setText(worklogs.get(position).getComment());
		
		return convertView;
	}
	
}
