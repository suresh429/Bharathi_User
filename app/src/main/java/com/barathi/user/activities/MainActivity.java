package com.barathi.user.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.barathi.user.R;
import com.barathi.user.dao.AppDatabase;
import com.barathi.user.databinding.ActivityMainBinding;
import com.barathi.user.SessionManager;
import com.barathi.user.fragments.CartFragment;
import com.barathi.user.fragments.ComplainFragment;
import com.barathi.user.fragments.FAQsFragment;
import com.barathi.user.fragments.HomeFragment;
import com.barathi.user.fragments.NotificationFragment;
import com.barathi.user.fragments.ProfileFragment;
import com.barathi.user.fragments.RatingFragment;
import com.barathi.user.fragments.WishlistFragment;
import com.barathi.user.model.User;
import com.barathi.user.retrofit.Const;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public ObservableInt pos = new ObservableInt(0);
    ObservableInt lastPos = new ObservableInt(0);
    private String title;
    SessionManager sessionManager;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(FLAG_TRANSLUCENT_STATUS);
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();


        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra("work");
            Log.d("TAG", "onCreate:intent " + str);
            if (str != null) {
                Log.d("TAG", "onCreate:notnull " + str);
                if (str.equals("gotocart")) {
                    Log.d("TAG", "onCreate:yes " + str);
                    loadFragment(new CartFragment());
                    binding.tvNavtitle.setText("Cart");
                    pos.set(2);
                }
            }
        }


        initListener();
        initUser();
        binding.drawerLayout.setViewScale(GravityCompat.START, 0.9f); //set height scale for main view (0f to 1f)
        binding.drawerLayout.setViewElevation(GravityCompat.START, 30); //set main view elevation when drawer open (dimension)
        binding.drawerLayout.setViewScrimColor(GravityCompat.START, ContextCompat.getColor(this, R.color.colorPrimary)); //set drawer overlay coloe (color)
        binding.drawerLayout.setDrawerElevation(30); //set drawer elevation (dimension)
        //  binding.drawerLayout.setContrastThreshold(0); //set maximum of contrast ratio between white text and background color.
        binding.drawerLayout.setRadius(GravityCompat.START, 25);
        binding.drawerLayout.setViewRotation(GravityCompat.START, 0);
    }

    private void initUser() {
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            User user = sessionManager.getUser();
            binding.navToolbar.drawerTvUname.setText(user.getData().getFirstName().concat(" " + user.getData().getLastName()));
            binding.navToolbar.drawerTvUmobile.setText(user.getData().getEmail());
            Log.d("TAG", "initUser: " + user.getData().getProfileImage());


            Bitmap placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
            circularBitmapDrawable.setCircular(true);


           /* Glide.with(this)
                    .load(Const.BASE_IMG_URL + user.getData().getProfileImage())
                    .circleCrop()
                    .placeholder(circularBitmapDrawable)
                    .into(binding.navToolbar.drawerImgProfile);*/

        }

        db = Room.databaseBuilder(this, AppDatabase.class, Const.DB_NAME).allowMainThreadQueries().build();
        cartCount(db.cartDao().getall().size());
    }

    private void initView() {
        sessionManager = new SessionManager(this);
        HomeFragment fragment = new HomeFragment();
        binding.tvNavtitle.setText("Home");
        pos.set(1);
        closeDrawer();
        loadFragment(fragment);
    }

    private void initListener() {

        binding.appbarImgsearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        binding.appbarImgcart.setOnClickListener(v -> {
            pos.set(2);
            closeDrawer();
            loadFragment(new CartFragment());
            setTitleText("Cart");

        });


        binding.navToolbar.navHome.setOnClickListener(view -> {
            pos.set(1);
            closeDrawer();

        });
        binding.navToolbar.navCart.setOnClickListener(view -> {
            pos.set(2);
            closeDrawer();
        });
        binding.navToolbar.navProfile.setOnClickListener(view -> {
            pos.set(3);
            closeDrawer();
        });
        binding.navToolbar.navNotification.setOnClickListener(view -> {
            pos.set(4);
            closeDrawer();
        });
        binding.navToolbar.navRating.setOnClickListener(view -> {
            pos.set(5);
            closeDrawer();
        });
        binding.navToolbar.navWishlist.setOnClickListener(view -> {
            pos.set(6);
            closeDrawer();
        });
        binding.navToolbar.navComplaint.setOnClickListener(view -> {
            pos.set(7);
            closeDrawer();
        });
        binding.navToolbar.navFaqs.setOnClickListener(view -> {
            pos.set(8);
            closeDrawer();
        });

        binding.appbarImgmenu.setOnClickListener(v -> binding.drawerLayout.openDrawer(Gravity.LEFT, true));
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//ll
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                binding.appbarImgmenu.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_sharp_arrow_back_24));

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                binding.appbarImgmenu.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_sharp_menu_24));

                setDefultUI();
                Fragment fragment = null;
                switch (pos.get()) {
                    case 1:
                        fragment = new HomeFragment();
                        title = "Home";
                        binding.appbarImgsearch.setVisibility(View.VISIBLE);
                        binding.appbarImgcart.setVisibility(View.VISIBLE);
                        binding.txtCount.setVisibility(View.VISIBLE);


                        break;
                    case 2:
                        fragment = new CartFragment();
                        title = "Cart";
                        binding.appbarImgcart.setVisibility(View.GONE);
                        binding.txtCount.setVisibility(View.GONE);


                        break;
                    case 3:
                        fragment = new ProfileFragment();
                        title = "Profile";
                        binding.txtCount.setVisibility(View.GONE);

                        break;
                    case 4:
                        fragment = new NotificationFragment();
                        title = "Notification";
                        binding.txtCount.setVisibility(View.GONE);

                        break;
                    case 5:
                        fragment = new RatingFragment();
                        title = "Rating & Reviews";
                        binding.txtCount.setVisibility(View.GONE);

                        break;
                    case 6:
                        fragment = new WishlistFragment();
                        binding.appbarImgcart.setVisibility(View.VISIBLE);
                        binding.txtCount.setVisibility(View.VISIBLE);

                        title = "My Wishlist";
                        break;
                    case 7:
                        fragment = new ComplainFragment();
                        title = "Complains";
                        binding.txtCount.setVisibility(View.GONE);

                        break;
                    case 8:
                        fragment = new FAQsFragment();
                        title = "FAQs";
                        binding.txtCount.setVisibility(View.GONE);

                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + pos.get());
                }
                if (pos.get() != lastPos.get()) {
                    loadFragment(fragment);
                    binding.tvNavtitle.setText(title);
                }
                binding.appbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            }

            @Override
            public void onDrawerStateChanged(int newState) {
//ll
            }
        });
    }

    public void setTitleText(String title) {
        this.title = title;
        binding.tvNavtitle.setText(title);
    }

    private void setDefultUI() {
        binding.appbarImgsearch.setVisibility(View.GONE);
        binding.appbarImgcart.setVisibility(View.GONE);
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frame, fragment).commit();

        lastPos.set(pos.get());
    }

    public void closeDrawer() {
        binding.drawerLayout.closeDrawer(Gravity.LEFT);
        if (pos.get() != lastPos.get()) {
            boldText();
            unBoldText();
        }
    }

    private void boldText() {
        Typeface face = ResourcesCompat.getFont(this, R.font.gilroyextrabold);

        switch (pos.get()) {
            case 1:
                binding.navToolbar.tvHome.setTypeface(face);
                break;
            case 2:
                binding.navToolbar.tvCart.setTypeface(face);
                break;
            case 3:
                binding.navToolbar.tvProfile.setTypeface(face);
                break;
            case 4:
                binding.navToolbar.tvNotofication.setTypeface(face);
                break;
            case 5:
                binding.navToolbar.tvRating.setTypeface(face);
                break;
            case 6:
                binding.navToolbar.tvWishlist.setTypeface(face);
                break;
            case 7:
                binding.navToolbar.tvComplain.setTypeface(face);
                break;
            case 8:
                binding.navToolbar.tvFaqs.setTypeface(face);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + pos.get());
        }

    }

    private void unBoldText() {
        Typeface face = ResourcesCompat.getFont(this, R.font.gilroymedium);

        switch (lastPos.get()) {
            case 1:
                binding.navToolbar.tvHome.setTypeface(face);
                break;
            case 2:
                binding.navToolbar.tvCart.setTypeface(face);
                break;
            case 3:
                binding.navToolbar.tvProfile.setTypeface(face);
                break;
            case 4:
                binding.navToolbar.tvNotofication.setTypeface(face);
                break;
            case 5:
                binding.navToolbar.tvRating.setTypeface(face);
                break;
            case 6:
                binding.navToolbar.tvWishlist.setTypeface(face);
                break;
            case 7:
                binding.navToolbar.tvComplain.setTypeface(face);
                break;
            case 8:
                binding.navToolbar.tvFaqs.setTypeface(face);
                break;


            default:

        }

    }

    @Override
    public void onBackPressed() {
        if (pos.get() == 1) {
            super.onBackPressed();
            finish();
        } else {
            initView();
            initListener();
            binding.appbarImgsearch.setVisibility(View.VISIBLE);
            binding.appbarImgcart.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("SetTextI18n")
    public void cartCount(int size) {
        Log.d("TAG", "methodName: " + size);
        TextView txt = findViewById(R.id.txtCount);

        if (size != 0) {
            txt.setText("" + size);
            txt.setVisibility(View.VISIBLE);
        }else {
            txt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "onRestart: ");
        cartCount(db.cartDao().getall().size());
    }
}