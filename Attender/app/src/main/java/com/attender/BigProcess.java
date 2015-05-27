package com.attender;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class BigProcess  extends AsyncTask <Void, Integer, Void> {
    private Context mContext;
    ProgressDialog mProgress;
    private int mProgressDialog = 0;

    public BigProcess(Context context, int progressDialog) {
        this.mContext = context;
        this.mProgressDialog = progressDialog;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Loading Data...");
        if (mProgressDialog == ProgressDialog.STYLE_HORIZONTAL) {

            mProgress.setIndeterminate(false);
            mProgress.setMax(100);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setCancelable(true);
        }
        mProgress.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mProgressDialog == ProgressDialog.STYLE_HORIZONTAL) {
            mProgress.setProgress(values[0]);
        }
    }

    @Override
    protected Void doInBackground(Void... values) {
        try {
            //This is to simulate there is a heavy process is running
            //and we make it slow down by 500 ms for each number
            for (int i = 1; i <= 100; i++) {
                publishProgress(i * 10);
                Thread.sleep(400);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        mProgress.dismiss();
        Toast.makeText(mContext, "Done!!!", Toast.LENGTH_LONG).show();

    }
}
