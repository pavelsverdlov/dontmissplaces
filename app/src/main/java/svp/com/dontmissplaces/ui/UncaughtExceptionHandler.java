package svp.com.dontmissplaces.ui;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;

import svp.com.dontmissplaces.ui.activities.CrashActivity;

public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    private final String LINE_SEPARATOR = "\n";
    private final Application application;

    public UncaughtExceptionHandler(Application application) {
        this.application = application;
    }
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        Context context = application.getApplicationContext();
        String error = errorReport.toString();

        Intent intent = new Intent();//"svp.com.dontmissplaces.ui.activities.CrashActivity");//context, CrashActivity.class);//
        intent.setClass(context, CrashActivity.class);
        intent.setAction(CrashActivity.class.getName());

        PendingIntent pendingIntent = PendingIntent.getActivity(application.getBaseContext(), 0, intent, 0);

        AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pendingIntent);
        System.exit(2);


        /*

        Intent intent = new Intent();//"svp.com.dontmissplaces.ui.activities.CrashActivity");//context, CrashActivity.class);//
        intent.setClass(context, CrashActivity.class);
        intent.setAction(CrashActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        intent.putExtra(CrashActivity.ERROR_KEY, error);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 22, intent, 0);
        try {
//            pendingIntent.send();
//            application.startActivity(Intent.createChooser(intent,"An error has occurred! Send an error report?"));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(10);
    }

}
