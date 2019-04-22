package com.teamand.bookstore.adapter;

import android.content.Context;
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

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.Holder> {
    private Context context;
    private int type;
    private List<Book> books;

    public BookAdapter(List<Book> books,int type){
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
        Book book = books.get(i);
        holder.tvBookName.setText(book.getBookInfo().getName());
        holder.tvBookPrice.setText(book.getBookInfo().getPrice() + " VNĐ");
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
