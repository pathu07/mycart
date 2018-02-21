package com.cart.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cart.app.dagger.AppComponentProvider;
import com.cart.app.utils.AppUtils;
import com.cart.app.view.CartFragment;
import com.cart.app.view.FavFragment;
import com.cart.app.view.MenuFragment;


/**
 * Created by padmanabhan on 2/12/18.
 */

public class CartMainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart_main);
        AppComponentProvider.getInstance().getAppComponent().inject(this);

        if (savedInstanceState == null) {
            MenuFragment firstFragment = new MenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.flContainer, firstFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.red_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                CartFragment cartFragment = new CartFragment();
                openFragment(cartFragment, getString(R.string.my_cart));
                return true;
            case R.id.action_fav:
                FavFragment favFragment = new FavFragment();
                openFragment(favFragment, getString(R.string.my_fav));
                return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment, String title){
        Bundle args = new Bundle();
        args.putString("openedFrom", "activity");
        fragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.trans_left_in, R.anim.trans_left_out, R.anim.trans_right_in, R.anim.trans_right_out)
                .add(R.id.flContainer2, fragment)
                .addToBackStack(title)
                .commit();
        AppUtils.updateActionBarTitle(this, title, true);

    }
}
