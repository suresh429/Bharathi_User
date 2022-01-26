package com.barathi.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.barathi.user.R;
import com.barathi.user.databinding.FragmentImageBinding;
import com.barathi.user.model.ProductMain;
import com.barathi.user.adapters.ProductImageViewPager;


public class ImageFragment extends Fragment {
    FragmentImageBinding binding;
    private ProductMain.Datum product;

    public ImageFragment(ProductMain.Datum product) {

        this.product = product;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);

        ProductImageViewPager productImageadapter = new ProductImageViewPager(product.getProductImage(), () -> {
            //ll
        });
        binding.imageSlider.setSliderAdapter(productImageadapter, false);
        return binding.getRoot();
    }
}