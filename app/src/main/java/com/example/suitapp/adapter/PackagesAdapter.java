package com.example.suitapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.suitapp.R;
import com.example.suitapp.model.Package;
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
    ShoppingViewModel shoppingViewModel;

    public PackagesAdapter(List<Package> packages, ShoppingViewModel shoppingViewModel) {
        this.packages = packages;
        this.shoppingViewModel = shoppingViewModel;
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

//        holder.tvId.setText(String.valueOf(articleGroups.get(position).getId()));
        holder.tvPackage.setText("Paquete " + String.valueOf(position +1));

        ThemedButton btnS1 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);
        ThemedButton btnS2 = (ThemedButton) inflater.inflate(R.layout.component_size_toggle, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, pxWidth);
        params.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);

        btnS1.setTag("En Persona - $ 0");
        btnS1.setText("En Persona - $ 0");
        holder.tgShipment.addView(btnS1, params);

        btnS2.setTag("Por Correo - $ 300");
        btnS2.setText("Por Correo - $ 300");
        holder.tgShipment.addView(btnS2, params);

        holder.tgShipment.setOnSelectListener((ThemedButton btn) -> {
            if (((ThemedToggleButtonGroup) btn.getParent()).getSelectedButtons().size() > 0) {
                shoppingViewModel.getPackages().getValue().get(position).setSelectedShiping(btn.getTag().toString());
            } else {
                shoppingViewModel.getPackages().getValue().get(position).setSelectedShiping(null);
            }
            return kotlin.Unit.INSTANCE;
        });

        holder.listArticles.setAdapter(new ArticleAdapter(packages.get(position).getArticleList(), this, R.layout.card_cart_package));
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
