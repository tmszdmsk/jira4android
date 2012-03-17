package jira.For.Android.TaskDetails.Comments;

import java.util.List;

import jira.For.Android.R;
import jira.For.Android.DataTypes.Comment;
import android.content.Context;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Comment> comments;
	private InputMethodManager imm;

	public CommentsAdapter(Context context, List<Comment> comments) {
		this.inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.comments = comments;
		this.imm = (InputMethodManager) context
		        .getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public String getItem(int position) {
		return comments.get(position).toString();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_comment_layout, null);
		}
		TextView textViewUsername = (TextView) convertView
		        .findViewById(R.id.commentListUserTextView);

		textViewUsername.setText(comments.get(position).getAuthorFullName());

		TextView textViewDate = (TextView) convertView
		        .findViewById(R.id.commentListDateTextView);
		textViewDate.setText(comments.get(position).getCreated());
		TextView textViewCommentMsg = (TextView) convertView
		        .findViewById(R.id.commentListCommentTextView);
		textViewCommentMsg.setText(comments.get(position).getBody());
		final IBinder ib = convertView.getApplicationWindowToken();
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				imm.hideSoftInputFromWindow(ib, 0);
				return false;
			}
		});

		return convertView;
	}
}
