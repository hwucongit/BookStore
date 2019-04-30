package com.teamand.bookstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamand.bookstore.R;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.model.BookInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BookInCartAdapter extends RecyclerView.Adapter<BookInCartAdapter.Holder> {
    private Context context;
    private List<BookInfo> bookInfoList;
    private HashMap<Integer, Integer> hashMap;
    private CartManager cartManager;
    private IBook iBook;

    public BookInCartAdapter(Context context, List<BookInfo> bookInfoList, HashMap<Integer, Integer> hashMap) {
        this.context = context;
        this.bookInfoList = bookInfoList;
        this.hashMap = hashMap;
        this.cartManager = CartManager.getInstance(context);
    }

    public void setIbook(IBook ibook){
        this.iBook = ibook;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_in_cart, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final BookInfo bookInfo = bookInfoList.get(i);
        final int index = i;
        holder.tvName.setText(bookInfo.getName());
        if(bookInfo.getDiscount() > 0){
            holder.tvOriginPrice.setText(bookInfo.getPrice() + "");
            holder.tvPrice.setText(bookInfo.getPrice() - bookInfo.getPrice()*bookInfo.getDiscount()/100 +"");
        }else {
            holder.tvPrice.setText(bookInfo.getPrice() + "");
            holder.tvOriginPrice.setVisibility(View.GONE);
        }
        if (bookInfo.getImgUrl() != null) {
            String url = Constants.urlImage + bookInfo.getImgUrl();
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.logo_book_default)
                    .into(holder.imvThumbnail);
        }
        holder.tvQuantity.setText(getQuantity(bookInfo.getId()) + "");

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.removeItem(bookInfo.getId());
                bookInfoList.remove(index);
                notifyDataSetChanged();
                iBook.onChangeTotalPrice(getTotalPrice());
            }
        });
        holder.btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvQuantity.setText(Integer.parseInt(holder.tvQuantity.getText().toString())+ 1 + "");
                cartManager.updateQuantityItem(bookInfo.getId(),Integer.parseInt(holder.tvQuantity.getText().toString()));
                iBook.onChangeTotalPrice(getTotalPrice());
            }
        });
        holder.btnSubQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(holder.tvQuantity.getText().toString()) > 1){
                    holder.tvQuantity.setText(Integer.parseInt(holder.tvQuantity.getText().toString()) - 1 + "");
                    cartManager.updateQuantityItem(bookInfo.getId(),Integer.parseInt(holder.tvQuantity.getText().toString()));
                    iBook.onChangeTotalPrice(getTotalPrice());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (bookInfoList == null)
            return 0;
        else return bookInfoList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private ImageView imvThumbnail;
        private ImageButton btnDelete;
        private Button btnAddQuantity, btnSubQuantity;
        private TextView tvName, tvPrice, tvQuantity, tvOriginPrice;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imvThumbnail = itemView.findViewById(R.id.imv_thumbnail);
            btnDelete = itemView.findViewById(R.id.imb_delete);
            tvName = itemView.findViewById(R.id.tv_book_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvPrice = itemView.findViewById(R.id.tv_book_price);
            btnAddQuantity = itemView.findViewById(R.id.btn_add_quantity);
            btnSubQuantity = itemView.findViewById(R.id.btn_sub_quantity);
            tvOriginPrice = itemView.findViewById(R.id.tv_book_price_origin);
        }
    }

    private int getQuantity(int bookId) {
        return hashMap.get(bookId);
    }

    public HashMap<Integer, Integer> getHashMap() {
        return hashMap;
    }

    public List<BookInfo> getBookInfoList() {
        return bookInfoList;
    }
    public interface IBook{
        void onChangeTotalPrice(int price);
    }
    public int getTotalPrice(){
        int total = 0;
        for (int i = 0; i < cartManager.getCartInfo().size(); i++) {
            BookInfo bookInfo = bookInfoList.get(i);
            if(bookInfo.getDiscount() > 0){
                total += (bookInfo.getPrice() - bookInfo.getPrice()*bookInfo.getDiscount()/100)
                        * cartManager.getCartInfo().get(bookInfo.getId());
            }else {
                total += bookInfoList.get(i).getPrice() * cartManager.getCartInfo().get(bookInfoList.get(i).getId());
            }
        }
        return total;
    }
}
