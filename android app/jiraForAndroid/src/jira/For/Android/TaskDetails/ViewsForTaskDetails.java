package jira.For.Android.TaskDetails;

import java.util.Calendar;
import java.util.List;

import jira.For.Android.DLog;
import jira.For.Android.R;
import jira.For.Android.Connector.Connector;
import jira.For.Android.Connector.ConnectorComments;
import jira.For.Android.Connector.ConnectorWorkLog;
import jira.For.Android.DataTypes.Comment;
import jira.For.Android.DataTypes.DataTypesMethods;
import jira.For.Android.DataTypes.Issue;
import jira.For.Android.ImagesCacher.ImagesCacher;
import jira.For.Android.PagerView.ViewForPagerInterface;
import jira.For.Android.TaskDetails.TaskDetailsActivity.Tabs;
import jira.For.Android.TaskDetails.Comments.AddCommentThread;
import jira.For.Android.TaskDetails.Comments.LoadCommentsThread;
import jira.For.Android.TaskDetails.WorkLog.LoadWorkLogThread;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

// Klasa zajmuje się zwracaniem napompowanych layoutów i tytułów

class ViewsForTaskDetails implements ViewForPagerInterface {

	TaskDetailsActivity activity;
	Issue task;
	List<Comment> comments;
	InputMethodManager imm;
	private final Connector connector;
	private final ConnectorComments connectorComments;
	private final ConnectorWorkLog connectorWorkLog;

	public ViewsForTaskDetails(Activity activity, Issue task, Connector connector, ConnectorComments connectorComments, ConnectorWorkLog connectorWorkLog) {
		super();
		this.connector = connector;
		this.connectorComments = connectorComments;
		this.connectorWorkLog = connectorWorkLog;
		if (activity instanceof TaskDetailsActivity) this.activity = (TaskDetailsActivity) activity;
		this.task = task;
		imm = (InputMethodManager) activity
		        .getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	// Tablica tytułów
	final private String[] titles = {"Comments", "Basic Info", "Work Log"};// ,"Attachment"};

	/**
	 * Returns title for page
	 */
	@Override
	public String getTitle(int a) {

		return titles[a];
	}

	/**
	 * Loads layout
	 * 
	 * @return
	 */
	@Override
	public View loadView(LayoutInflater inflater, int pos) {
		int resId = 0;
		Tabs a = Tabs.fromInt(pos);
		// setting layout for chosen tab
		switch (a) {
			case comments:
				resId = R.layout.comments_view;
				DLog.i("myViewPagerAdapter", "loadView 0");
			break;
			case basicInfo:// Basic Info
				resId = R.layout.task_details_view;

				DLog.i("myViewPagerAdapter", "loadView 1");
			break;
			case worklog:
				resId = R.layout.worklog_view;
				DLog.i("myViewPagerAdapter", "loadView 2");
			break;
		/*
		 * case attachments: resId = R.layout.task_details_view;
		 * DLog.i("myViewPagerAdapter", "loadView 3"); break;
		 */
		}

		// viewsForPager[a.ordinal()] =
		View view = inflater.inflate(resId, null);
		if (view == null) Log.w("loadView", "view==null");
		switch (a) {
			case comments:
				Log.d("myViewPagerAdapter", "loadView 0");
				setCommentsInfo(view);
			break;
			case basicInfo:
				setTaskDetailsInfo(view);
				Log.d("myViewPagerAdapter", "loadView 1");
			break;
			case worklog:
				Log.d("myViewPagerAdapter", "loadView 2");
				setWorkLogInfo(view);
			// TODO Zrobić napompować layout
			break;
		/*
		 * case attachments: Log.d("myViewPagerAdapter", "loadView 3");
		 * setAttachmentsInfo(view); // TODO Zrobić napompować layout break;
		 */
		}
		return view;
	}

	/**
	 * Returns number of layouts
	 */
	@Override
	public int getLength() {

		return titles.length;
	}

	/**
	 * Setting setting elements in Basic info tab
	 * 
	 * @param view
	 */
	private void setTaskDetailsInfo(View view) {

		// setting all textViews in current layout
		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDescriptionValueTextView))
		        .setText(task.getDescription());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivitySummaryValueTextView))
		        .setText(task.getSummary());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDetailsTypeValueTextView))
		        .setText(connector.getIssueType(task.getType()).getName());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDetailsPriorityValueTextView))
		        .setText(connector.getPriority(task.getPriority()).getName());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDetailsStatusValueTextView))
		        .setText(connector.getStatus(task.getStatus()).getName());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDetailsResolutionValueTextView))
		        .setText(task.getResolution());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityPeopleAssigneeValueTextView))
		        .setText(task.getAssigneeFullName());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityPeopleReporterValueTextView))
		        .setText(task.getReporterFullName());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDatesCreatedValueTextView))
		        .setText(task.getCreated());

		((TextView) view
		        .findViewById(R.id.taskdetailsActivityDatesUpdatedValueTextView))
		        .setText(task.getUpdated());

		ImageView type = (ImageView) view.findViewById(R.id.taskType);
		type.setImageBitmap(ImagesCacher.getInstance().issuesTypesBitmaps
		        .get(task.getType()));
		
		ImageView priority = (ImageView) view.findViewById(R.id.taskPriority);
		priority.setImageBitmap(ImagesCacher.getInstance().issuesPrioritesBitmaps
		        .get(task.getPriority()));
		
		ImageView status = (ImageView) view.findViewById(R.id.taskStatus);
		status.setImageBitmap(ImagesCacher.getInstance().issuesStatusesBitmaps
				.get(task.getStatus()));

	}

	/**
	 * Setting setting elements in History tab
	 * 
	 * @param view
	 */
	private void setWorkLogInfo(View view) {
		// TODO Auto-generated method stub
		new LoadWorkLogThread(view, activity, task, connectorWorkLog).execute();
	}

	/**
	 * Setting setting elements in Comments tab
	 * 
	 * @param view
	 */
	private void setCommentsInfo(View view) {
		// Setting comments layout

		new LoadCommentsThread(view, activity, task, connectorComments).execute();

		final Button buttonSendMessage = (Button) view
		        .findViewById(R.id.button_send_comment);
		final EditText messageOfComment = (EditText) view
		        .findViewById(R.id.edit_text_comment);
		messageOfComment.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
			        int count) {
				if (s.length() == 0) buttonSendMessage.setEnabled(false);
				else buttonSendMessage.setEnabled(true);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
			        int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
		});

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				imm.hideSoftInputFromWindow(messageOfComment.getWindowToken(),
				        0);
				return false;
			}
		});

		buttonSendMessage.setOnClickListener(new OnClickListener() {

			Calendar date = Calendar.getInstance();

			@Override
			public void onClick(View v) {
				EditText messageOfComment = (EditText) activity
				        .findViewById(R.id.edit_text_comment);

				String msg = messageOfComment.getText().toString();

				System.out.println("Msg: " + msg);

				Comment comment = new Comment(connector
				        .getThisUser(), msg, "null", "12345678", "null",
				        "null",
				        DataTypesMethods.dateToGMTString(date.getTime()),
				        DataTypesMethods.dateToGMTString(date.getTime()));

				new AddCommentThread(activity, comment, task.getKey(), connectorComments)
				        .execute();
			}
		});
	}
}
