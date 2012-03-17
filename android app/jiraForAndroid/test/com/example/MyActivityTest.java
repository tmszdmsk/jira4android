package com.example;

import jira.For.Android.LoginActivity;
import jira.For.Android.R;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MyActivityTest {

    @Test
    public void shouldHaveHappySmiles() throws Exception {
        String app = new LoginActivity().getResources().getString(R.string.app_name);
        assertThat(app, equalTo("Jira For Android"));
    }
}
