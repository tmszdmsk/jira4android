package jira.For.Android.ProjectList;

import jira.For.Android.R;
import jira.For.Android.DataTypes.Project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] values;
	private Project[] projects;

	public ProjectAdapter(Context context, String[] values, Project[] projects) {
		super(context, R.layout.project_list_view_row, values);
		this.context = context;
		this.values = values;
		this.projects = projects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.project_list_view_row, parent,
		        false);
		TextView textView = (TextView) rowView.findViewById(R.id.project_name);
		ImageView imageView = (ImageView) rowView
		        .findViewById(R.id.project_image);
		textView.setText(values[position]);

		// Change the icon
		imageView.setImageBitmap(projects[position].getAvatar());

		return rowView;
	}
}