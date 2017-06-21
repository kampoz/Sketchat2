package com.kampoz.sketchat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.tab.SlidingTabLayout;
import java.util.List;

public class LoginAndRegisterActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private ViewPager viewPager;
  private SlidingTabLayout sTLTabs;
  //private List<Fragment> fragmentsList;
  private Fragment[] fragmentsList = new Fragment[2];
  private Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login_and_register);

    viewPager = (ViewPager) findViewById(R.id.VPviewPager);
    viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    sTLTabs = (SlidingTabLayout) findViewById(R.id.STLtabs);
    sTLTabs.setViewPager(viewPager);

    fragmentsList[0] = new LoginFragment();
    fragmentsList[1] = new RegisterFragment();

  }

  class MyPagerAdapter extends FragmentPagerAdapter {



    String[] tabs;

    public MyPagerAdapter(FragmentManager fm) {
      super(fm);
      tabs = getResources().getStringArray(R.array.tabs);
    }

    @Override
    public Fragment getItem(int position) {
      //MyFragment myFragment = MyFragment.getInstance(position);
      //return myFragment;
      Log.i("position: ", String.valueOf(position));
      //return fragmentsList[position];

      switch(position){
        case 0: return LoginFragment.getInstance(position);
        case 1: return RegisterFragment.getInstance(position);
        default: return LoginFragment.getInstance(position);
      }
      /*Fragment currentFragment;
      if(position == 0){
        currentFragment = new LoginFragment();
      } else if (position == 1){
        currentFragment = new RegisterFragment();
      } else {
        currentFragment = new LoginFragment();
      }
      //return myFragment;
      Log.i("position: ", String.valueOf(position));
      return currentFragment;*/

    }


    @Override
    public CharSequence getPageTitle(int position) {
      return tabs[position];
    }

    @Override
    public int getCount() {
      return 2;
    }
  }

  public static class MyFragment extends Fragment {
    private TextView textView;
    public static MyFragment getInstance(int position) {
      MyFragment myFragment = new MyFragment();
      Bundle args = new Bundle();
      args.putInt("position", position);
      myFragment.setArguments(args);
      return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      View layout = inflater.inflate(R.layout.fragment_login_register, container, false);
      textView = (TextView) layout.findViewById(R.id.tvPosition);
      Bundle bundle = getArguments();
      if (bundle != null) {
        textView.setText("The page selected is " + bundle.getInt("position"));
      }
      return layout;
    }
  }

  public static class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      View loginFragmentView = inflater.inflate(R.layout.fragment_login, container, false);
      return loginFragmentView;
    }
    public static LoginFragment getInstance(int position) {
      LoginFragment myFragment = new LoginFragment();
      Bundle args = new Bundle();
      args.putInt("position", position);
      myFragment.setArguments(args);
      return myFragment;
    }
  }

  public static class RegisterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      View loginFragmentView = inflater.inflate(R.layout.fragment_register, container, false);
      return loginFragmentView;
    }
    public static RegisterFragment getInstance(int position) {
      RegisterFragment myFragment = new RegisterFragment();
      Bundle args = new Bundle();
      args.putInt("position", position);
      myFragment.setArguments(args);
      return myFragment;
    }
  }

}
