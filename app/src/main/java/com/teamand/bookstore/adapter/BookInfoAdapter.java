package com.teamand.bookstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamand.bookstore.R;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.ui.activity.BookDetailActivity;

import java.util.List;

public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.Holder> {
    private Context context;
    private int type;
    private List<BookInfo> books;

    public BookInfoAdapter(List<BookInfo> books, int type){
        this.books = books;
        this.type = type;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = null;
        if (type == Constants.ITEM_BOOK_TYPE_VER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_book_type_ver, viewGroup, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_book_type_hor,viewGroup,false);
        }
        return new Holder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        final BookInfo book = books.get(i);
        holder.tvBookName.setText(book.getName());
        holder.tvBookPrice.setText(book.getPrice() + " VNĐ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("bookId",book.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(books == null)
            return 0;
        else
            return books.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imvThumbnail;
        private TextView tvBookName, tvBookPrice;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imvThumbnail = itemView.findViewById(R.id.imv_thumbnail);
            tvBookName = itemView.findViewById(R.id.tv_book_name);
            tvBookPrice = itemView.findViewById(R.id.tv_book_price);
        }
    }
}
