package com.example.suitapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Package;
import com.example.suitapp.model.ShippingPrice;
import com.example.suitapp.util.Util;
import com.example.suitapp.viewmodel.ShoppingViewModel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> implements ArticleAdapter.OnArticleListener {
    private List<Package> packages;
    LayoutInflater inflater;
    int pxWidth, pxMargin;
    Context context;
    ShoppingViewModel shoppingViewModel;

    public PackagesAdapter(List<Package> packages, ShoppingViewModel shoppingViewModel, Context context ) {
        this.packages = packages;
        this.shoppingViewModel = shoppingViewModel;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_package, parent, false);

        pxWidth = Util.getPxByDensity(parent.getContext().getResources(), 40);
        pxMargin = Util.getPxByDensity(parent.getContext().getResources(), 3);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int provinceId = shoppingViewModel.getProvinceId().getValue();
        int selectId = -1;
        holder.tvPackage.setText("Paquete " + String.valueOf(position + 1));

        ThemedButton btnS1 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
        ThemedButton btnS2 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, pxWidth);
        params.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);

        if (packages.get(position).getStore().isPhysical_store()) {
            btnS1.setId(0);
            btnS1.setTag(0);
            btnS1.setText("En Persona - $ 0");
            holder.tgShipment.addView(btnS1, params);
        }
        if (packages.get(position).getStore().isMailShipping()) {
            for (int i = 0; i < packages.get(position).getStore().getShippingPrice().size() ; i++) {
                if(provinceId == packages.get(position).getStore().getShippingPrice().get(i).getProvince().getId()) {
                    selectId = i;
                    break;
                }
            }
            if(selectId != -1) {
                int price = packages.get(position).getStore().getShippingPrice().get(selectId).getPrice();
                btnS2.setId(1);
                btnS2.setTag(price);
                btnS2.setText("Por Correo - $ " + price);
                holder.tgShipment.addView(btnS2, params);
            }
        }

        holder.tgShipment.setOnSelectListener((
                ThemedButton btn) ->
        {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                shoppingViewModel.getPackages().getValue().get(position).setPriceShipping(Integer.parseInt(btn.getTag().toString()));
                shoppingViewModel.getPackages().getValue().get(position).setSelectedShiping(btn.getText());
                shoppingViewModel.getPackages().getValue().get(position).setMailing((btn.getId()==1) ? true : false);
                shoppingViewModel.refreshShippingPrice();
            } else {
                shoppingViewModel.getPackages().getValue().get(position).setSelectedShiping(null);
            }
            return kotlin.Unit.INSTANCE;
        });

        holder.listArticles.setAdapter(new
                CartAdapter(packages.get(position).getArticleList(), context, null));
    }

    @Override
    public int getItemCount() {
        if (packages == null)
            return 0;
        else
            return packages.size();
    }

    public void setItems(List<Package> packages) {
        this.packages = packages;
    }

    @Override
    public void onArticleClick(int position) {

    }

    @Override
    public void onArticleEditClick(int position, View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView tvPackage;
        public final ThemedToggleButtonGroup tgShipment;
        public final RecyclerView listArticles;
        public Button btMore;
        OnGroupListener onGroupListener;

        public ViewHolder(View view) {
            super(view);

            this.tvPackage = (TextView) view.findViewById(R.id.tvPackage);
            this.tgShipment = view.findViewById(R.id.tgShipment);
            this.listArticles = view.findViewById(R.id.listArticles);
            this.btMore = view.findViewById(R.id.btMore);
            this.onGroupListener = onGroupListener;
            if (btMore != null)
                btMore.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onGroupListener.onGroupClick(getAdapterPosition());
        }
    }

    public interface OnGroupListener {
        void onGroupClick(int position);
    }
}
