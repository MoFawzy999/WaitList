package com.fawzy.waitlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context ;
    private Cursor cursor ;

    public Adapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(!cursor.moveToPosition(position)){
             return;
        }
        String Name  = cursor.getString(cursor.getColumnIndex(WaitListContract.WaitListEntry.COULUMN_GUEST_NAME));
        String size = cursor.getString(cursor.getColumnIndex(WaitListContract.WaitListEntry.COULUMN_PARTY_SIZE));
        long id = cursor.getLong(cursor.getColumnIndex(WaitListContract.WaitListEntry._ID));

        holder.number.setText(size);
        holder.name.setText(Name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void SwapCursor(Cursor allGuests){
        if (cursor != null) {
            cursor.close();
        }
            cursor = allGuests ;
        if (allGuests != null){
            // force recycler view to refresh.
            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView number , name ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.num);
            name = itemView.findViewById(R.id.name);
        }
    }


}
