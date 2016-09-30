package svp.com.dontmissstation.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.svp.infrastructure.common.view.BaseCursorAdapter;

public class RecyclerCursorAdapter extends RecyclerView.Adapter<RecyclerCursorAdapter.ViewHolder> {
    private final Context context;
    private final BaseCursorAdapter cursorAdapter;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public RecyclerCursorAdapter(Context context, Cursor c, BaseCursorAdapter cursorAdapter) {
        this.context = context;
        this.cursorAdapter = cursorAdapter;
        //cursorAdapter = new SavedPlacesCursorAdapter(context, c);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());

    }
    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
