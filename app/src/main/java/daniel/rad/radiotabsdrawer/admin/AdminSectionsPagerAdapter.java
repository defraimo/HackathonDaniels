package daniel.rad.radiotabsdrawer.admin;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import daniel.rad.radiotabsdrawer.admin.programsManager.ProgramManagerFragment;

public class AdminSectionsPagerAdapter extends FragmentPagerAdapter {


        public AdminSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String[] tabNames = new String[]{"ניהול תוכניות","ניהול תגובות","סטטיסטיקות"};

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ProgramManagerFragment();
                case 1:
                    return new CommentsManagerFragment();
                case 2:
                    return new StatisticsFragment();
            }
            throw new IllegalArgumentException("There are only 3 screens");
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }