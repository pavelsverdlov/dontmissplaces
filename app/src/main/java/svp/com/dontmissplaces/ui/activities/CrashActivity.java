package svp.com.dontmissplaces.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import svp.com.dontmissplaces.R;

public class CrashActivity extends AppCompatActivity {

    public static final String ERROR_KEY = "ERROR_KEY";
    @Bind(R.id.crash_activity_error_text) TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        ButterKnife.bind(this);

        text.setText(savedInstanceState == null ? "error" : savedInstanceState.getString(ERROR_KEY));
    }
}
