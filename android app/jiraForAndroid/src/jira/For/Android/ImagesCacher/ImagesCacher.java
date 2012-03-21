package jira.For.Android.ImagesCacher;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

import jira.For.Android.IconUrlProvider;
import jira.For.Android.DataTypes.IssueType;
import jira.For.Android.DataTypes.Priority;
import jira.For.Android.DataTypes.Status;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImagesCacher {

	private static ImagesCacher INSTANCE = null;
	private Context applicationContext;

	public HashMap<String, Bitmap> issuesPrioritesBitmaps = new HashMap<String, Bitmap>();
	public HashMap<String, Bitmap> issuesStatusesBitmaps = new HashMap<String, Bitmap>();
	public HashMap<String, Bitmap> issuesTypesBitmaps = new HashMap<String, Bitmap>();

	private HashMap<String, IssueType> issueTypes;
	private HashMap<String, Priority> priorities;
	private HashMap<String, Status> statuses;

	private ImagesCacher(HashMap<String, IssueType> issueTypes,
	                     HashMap<String, Priority> priorities,
	                     HashMap<String, Status> statuses, Context context) {
		this.issueTypes = issueTypes;
		this.priorities = priorities;
		this.statuses = statuses;
		applicationContext = context;
	}

	public static synchronized ImagesCacher getInstance() {
		return INSTANCE;
	}

	public static synchronized ImagesCacher getInstance2(
	        HashMap<String, IssueType> issueTypes,
	        HashMap<String, Priority> priorities,
	        HashMap<String, Status> statuses, Context context) {
		if (INSTANCE == null) {
			INSTANCE = new ImagesCacher(issueTypes, priorities, statuses,
			        context);
		}
		return INSTANCE;
	}

	public void clearMemory() {
		issuesPrioritesBitmaps.clear();
		issuesStatusesBitmaps.clear();
		issuesTypesBitmaps.clear();

		// TODO Jeszcze wyczyścić pamięć stałą!
	}

	public void downloadAllNeededData() {

		downloadIconTypes();

		downloadIconPriorites();

		downloadIconStatuses();
	}

	public void flushToMemory() {
		flushHashMap(issuesPrioritesBitmaps);
		flushHashMap(issuesStatusesBitmaps);
		flushHashMap(issuesTypesBitmaps);
	}

	private void flushHashMap(HashMap<String, Bitmap> input) {

		Set<String> keySet = input.keySet();
		for (String key : keySet) {
			try {
				FileOutputStream fileOutputStream = applicationContext
				        .openFileOutput(key, Context.MODE_WORLD_WRITEABLE);
				input.get(key).compress(Bitmap.CompressFormat.PNG, 90,
				        fileOutputStream);
				fileOutputStream.close();
			} catch (Exception e) {
				Log.e(this.toString(), "Writing file to memory error!!!");
				e.printStackTrace();
			}
		}
	}

	private boolean downloadIcon(HashMap<String, ?> mapaIN,
	        HashMap<String, Bitmap> mapaOUT) throws IOException {
		System.out.println("POBIERAM!!!!!!!!!!!!");
		Bitmap bmp;

		String urlOfIcon;
		URL url;
		URLConnection connection;
		InputStream inputStream;
		for (String id : mapaIN.keySet()) {

			if (mapaOUT.containsKey(id) == false) {
				System.out.println("Nie mam id:" + id);
				urlOfIcon = ((IconUrlProvider) mapaIN
				        .get(id)).getIcon();
				url = new URL(urlOfIcon);
				connection = url.openConnection();

				inputStream = connection.getInputStream();

				BufferedInputStream bufferedInputStream = new BufferedInputStream(
				        inputStream);

				ByteArrayBuffer baf = new ByteArrayBuffer(256);
				int current = 0;
				while ((current = bufferedInputStream.read()) != -1) {
					baf.append((byte) current);
				}

				bmp = BitmapFactory.decodeByteArray(baf.toByteArray(), 0,
				        baf.length());

				if (bmp == null) Log.e("IssueType",
				        "Nie udało się odkodowac pliku: " + urlOfIcon);

				mapaOUT.put(id, bmp);
			}
		}

		return true;
	}

	private void downloadIconTypes() {
		try {
			downloadIcon(issueTypes, issuesTypesBitmaps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void downloadIconPriorites() {
		try {
			downloadIcon(priorities, issuesPrioritesBitmaps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void downloadIconStatuses() {
		try {
			downloadIcon(statuses, issuesStatusesBitmaps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
